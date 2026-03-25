package com.eeman.smartreplymlkit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eeman.smartreplymlkit.databinding.ActivityMainBinding
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage

class MainActivity : AppCompatActivity() {

    private val eemanTag = "eemanSmartReplyML"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val conversation = ArrayList<TextMessage>().apply {
            add(
                TextMessage.createForRemoteUser(
                    "What’s the best thing about today? 🌞",
                    System.currentTimeMillis(),
                    "user"
                )
            )
            add(
                TextMessage.createForLocalUser(
                    "Probably this conversation! 😉✨",
                    System.currentTimeMillis()
                )
            )
            add(
                TextMessage.createForRemoteUser(
                    "You’re so easy to talk to! 🥰",
                    System.currentTimeMillis(),
                    "user"
                )
            )
            add(
                TextMessage.createForLocalUser(
                    "Aww, you’re the best! 😇💖",
                    System.currentTimeMillis()
                )
            )
        }

//        binding.edtInput.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//        })

        binding.btnSend.setOnClickListener {

            conversation.add(
                TextMessage.createForRemoteUser(
                    binding.edtInput.text.toString(), System.currentTimeMillis(), "user"
                )
            )

            binding.txtOutput.text =
                binding.txtOutput.text.toString() + "\n\nuser: " + binding.edtInput.text.toString()

            val smartReplyGenerator = SmartReply.getClient() // SmartReply.getClient()

            smartReplyGenerator.suggestReplies(conversation).addOnSuccessListener { result ->
                if (result.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {

                    Log.d(eemanTag, "STATUS_NOT_SUPPORTED_LANGUAGE")

                } else if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {

                    Log.d(
                        eemanTag,
                        "STATUS_SUCCESS. optional features: ${smartReplyGenerator.optionalFeatures}"
                    )

                    val lastUserMessage = conversation.lastOrNull()?.messageText ?: ""
                    val reply = getCustomReply(lastUserMessage) ?: run {
                        // Use ML replies, filter short ones, and append emojis
                        val mlReplies = result.suggestions.map { suggestion ->
                            if (isShortReply(suggestion.text)) {
                                fallbackReplies.random() // Replace short replies
                            } else {
                                "${suggestion.text} ${randomEmojis.random()}" // Append random emoji
                            }
                        }

                        // Join the top 3 ML replies (or fewer if less available)
                        mlReplies.take(3).joinToString(", ")
                    }

//                        getCustomReply(lastUserMessage) ?: result.suggestions.map { suggestion ->
//                            val a = if (isShortReply(suggestion.text)) {
//                                fallbackReplies.random() // Replace with a fallback reply
//                            } else {
//                                "${suggestion.text} ${randomEmojis.random()}" // Append random emoji. Use the ML-generated reply
//                            }
//                            a
//                        }.let {
//                            "${it.getOrNull(0)}, ${it.getOrNull(1)}, ${it.getOrNull(2)}"
//                        }


                    binding.txtOutput.text =
                        binding.txtOutput.text.toString() + "\n\nbot: $reply"

                    for (suggestion in result.suggestions) {
                        Log.d(eemanTag, "Suggestion: " + suggestion.text)
                    }
                }
            }
                .addOnFailureListener {
                    Log.d(eemanTag, "addOnFailureListener")
                }

            binding.edtInput.setText("")
        }
    }

    private val randomEmojis = listOf(
        "😊",
        "😂",
        "😉",
        "",
        "😍",
        "",
        "",
        "",
        "",
        "",
        "✨",
        "😋",
        "",
        "🐼",
        "💖",
        "",
        "🌞",
        "",
        "",
        "💘",
        "💖",
        "👀",
        "\uD83D\uDE09",
        "\uD83D\uDE0B"
    )

    private val customReplies = mapOf(
        "movie" to listOf(
            "I love sci-fi movies! Especially Interstellar. 🚀✨",
            "Romantic comedies are my jam. How about you? 🥰",
            "Anything with action and adventure! 🗡️🏝️"
        ),
        "place" to listOf(
            "I’d love to visit Paris someday! 🗼",
            "The Maldives is my dream destination. 🌴🌊",
            "I enjoy quiet places with lots of nature. 🌳🌺"
        ),
        "country" to listOf(
            "I’ve always wanted to explore Japan. 🇯🇵",
            "Italy has amazing food and culture! 🍝🇮🇹",
            "India is so vibrant and full of life! 🇮🇳"
        ),
        "whatsapp" to listOf(
            "I prefer chatting here! 😄",
            "Why not chat here instead? 😉",
            "This app is way more fun!, let's chat here ✨"
        ),
        "number" to listOf(
            "I prefer chatting here! 😄",
            "Why not chat here instead? 😉",
            "This app is way more fun!, let's chat here ✨"
        )
    )

    private val shortReplies = listOf("ok", "okay", "yes", "thanks", "no", "sure")

    private val fallbackReplies = listOf(
        "What are you doing? 😊",
        "Where are you from? 🌍",
        "Can we meet? 😉",
        "Can we be friends? 🥰",
        "I need someone to talk to. 💬"
    )

    private fun isShortReply(reply: String): Boolean {
        return reply.lowercase() in shortReplies
    }

    private fun getCustomReply(userMessage: String): String? {
        val lowercasedMessage = userMessage.lowercase()
        for ((keyword, replies) in customReplies) {
            if (lowercasedMessage.contains(keyword)) {
                return replies.random() // Return a random reply for the matched keyword
            }
        }
        return null // No keyword matched
    }

}