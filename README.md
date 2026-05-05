# 🧠 AI Memory Hook Teacher

AI Memory Hook Teacher is a local AI-powered learning assistant that explains topics in simple language and helps users remember them using short memory tricks.

It is designed for students who want:
- simple explanations
- real-world examples
- fast revision
- easy recall using memory hooks

---

## ✨ Features

- 💬 Chat-style learning interface
- 🧠 Simple topic explanations
- 🎯 Smart memory hooks for recall
- 🔊 Text-to-Speech (browser voice)
- 🎤 Speech-to-Text (mic input)
- 🗂️ Short conversational memory
- ⚡ Fast Spring Boot backend
- 🧩 Context-aware prompting

---

## 🏗️ Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring AI
- Ollama (local LLM)
- REST APIs

### Frontend
- HTML
- CSS
- JavaScript
- Web Speech API

### AI Models
- `llama3` → explanations
- `nomic-embed-text` → embeddings

---
## 🚀 How It Works

User enters a topic →  
AI explains it in simple words →  
AI generates a memory trick →  
User can listen to response or ask by voice

---

⚙️ Run Locally
1. Clone repository
git clone https://github.com/your-username/spring-ai-explore.gitcd spring-ai-explore/explore-openai
2. Start Ollama
ollama serve
3. Pull required models
ollama pull llama3ollama pull nomic-embed-text
4. Run project
./gradlew bootRun
5. Open browser
http://localhost:8080

⚠️ Requirements
Make sure these are installed before running:


Java 17


Gradle


Ollama



📌 Notes


This project runs fully local using Ollama


No paid API key required


Mic input works best in Chrome


Voice output uses browser speech synthesis


First response may be slightly slow depending on local model speed



🎯 Future Improvements


Dark mode


Save chat history


Better long-term memory


Multi-language support


Export notes


User login



👩‍💻 Author
**Pragya Dwivedi**

⭐ Support
If you found this useful, star the repo.




