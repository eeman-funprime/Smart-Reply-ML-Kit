# 🤖 ML Kit Smart Reply Android App

An Android application that demonstrates **Smart Reply functionality** using **Google ML Kit**.  
It generates contextual reply suggestions based on conversation history, similar to modern messaging apps.

---

## 🚀 Features

- 💬 Real-time smart reply suggestions  
- 🧠 On-device machine learning (no internet required)  
- ⚡ Fast and lightweight  
- 📱 Clean and simple UI  
- 🔄 Dynamic conversation handling  

---

## 🛠️ Tech Stack

- Kotlin  
- Android SDK  
- Google ML Kit (Smart Reply API)  
- Jetpack Components (optional)  

---

## 📦 Dependency

Add the following dependency in your `build.gradle`:

```gradle
dependencies {
    implementation "com.google.mlkit:smart-reply:17.0.2"
}
```

## 🧠 How It Works

- Create a conversation using TextMessage
- Send the conversation to ML Kit
- Receive smart reply suggestions
- Display suggestions in the UI

💡 Example Code:

```
val conversation = listOf(
    TextMessage.createForRemoteUser(
        "Hey, are you coming?",
        System.currentTimeMillis(),
        "user1"
    ),
    TextMessage.createForLocalUser(
        "Yes, on my way!",
        System.currentTimeMillis()
    )
)

val smartReply = SmartReply.getClient()

smartReply.suggestReplies(conversation)
    .addOnSuccessListener { result ->
        if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
            result.suggestions.forEach { suggestion ->
                println(suggestion.text)
            }
        }
    }
    .addOnFailureListener {
        it.printStackTrace()
    }
```
