# Corporate Jargon Translator POC

A Java 21 / Spring Boot backend and React / Vite frontend for translating workplace jargon into plain language.

## What it does

- Classifies an input as a word, phrase, or sentence.
- Looks up short terms through Free Dictionary API, then Wiktionary's REST API.
- Uses a provider abstraction and sequential fallback.
- Produces a consistent plain meaning, professional explanation, and suggested reply.
- Caches results in memory (five-minute TTL).
- Runs without an AI key: the contextual translator is deliberately deterministic for a usable POC. Replace `ContextualTranslationService` with an LLM adapter when credentials are available.

## Run

Prerequisites: JDK 21+, Maven 3.9+, Node 20+.

```powershell
cd backend
mvn spring-boot:run
```

In another terminal:

```powershell
cd frontend
npm install
npm run dev
```

Open the Vite URL (normally `http://localhost:5173`). The frontend proxies `/api` to port 8080.

## API

`POST /api/v1/translations`

```json
{ "text": "Let's circle back on this tomorrow." }
```

`GET /actuator/health` exposes the service health check.
