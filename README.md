# Cyber Risk

A full-stack web application that automates cyber insurance risk assessment. Companies submit their security profile, receive a scored risk rating from 0–100, and generate a professional AI-written underwriting memo.

Built this to explore how AI can automate real workflows in the insurance industry. The scoring model is based on common cybersecurity frameworks (firewall presence, encryption, incident response planning, breach history, etc).

<img width="1454" height="807" alt="image" src="https://github.com/user-attachments/assets/8e909d86-9c0d-43c6-94dc-80c07af4c688" />


---

## Features

- **Risk Scoring Engine**: scores a company's security posture from 0 to 100 using weighted deductions across 6 security factors
- **AI Memo Generation**: uses LLaMA 3.3 70B via Groq to generate a 3-paragraph professional underwriting memo
- **REST API**: full CRUD endpoints built with Spring Boot and Spring Data JPA
- **React Dashboard**: form-based UI with an animated SVG risk gauge and security breakdown
- **Dockerized**: entire app (backend + database) spins up with one `docker-compose up` command
- **CI/CD**: GitHub Actions pipeline builds and tests on every push to main

<img width="1453" height="801" alt="image" src="https://github.com/user-attachments/assets/c75cf6fc-7098-4428-bc22-731fec0116c0" />


---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18, Vite, Axios |
| Backend | Java 21, Spring Boot 3.5 |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| AI | Groq API (LLaMA 3.3 70B) |
| DevOps | Docker, Docker Compose, GitHub Actions |

---

## Risk Scoring Model

```
Base Score: 100

Deductions:
  -20   No firewall
  -15   No antivirus software
  -20   No data encryption
  -15   No incident response plan
  -10   No security training program
  -10   Per previous breach (×n)

Minimum score: 0
```

| Score Range | Risk Level |
|-------------|------------|
| 80 - 100 | LOW |
| 60 - 79 | MEDIUM |
| 40 - 59 | HIGH |
| 0 - 39 | CRITICAL |

---

## Getting Started

**Prerequisites:** Docker Desktop, Groq API key (free at [console.groq.com](https://console.groq.com))

```bash
git clone https://github.com/sabrinahaniff/cyber-risk-platform.git
cd cyber-risk-platform

# Run backend + database
export GROQ_API_KEY=your_key_here
docker-compose up --build

# Run frontend (new terminal)
cd frontend
npm install
npm run dev
```

- API: `http://localhost:8080`  
- UI: `http://localhost:5173`

---

## API Endpoints

```
POST   /api/companies                       Create company assessment
GET    /api/companies                       Get all assessments
GET    /api/companies/:id                   Get by ID
DELETE /api/companies/:id                   Delete assessment
POST   /api/companies/:id/generate-memo     Generate AI underwriting memo
```

**Example request:**
```bash
curl -X POST http://localhost:8080/api/companies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Acme Corp",
    "industry": "Finance",
    "employeeCount": 500,
    "country": "Canada",
    "hasFirewall": true,
    "hasAntiVirus": true,
    "encryptsData": false,
    "hasIncidentResponsePlan": false,
    "hasSecurityTraining": true,
    "previousBreaches": 2
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Acme Corp",
  "industry": "Finance",
  "employeeCount": 500,
  "country": "Canada",
  "hasFirewall": true,
  "hasAntiVirus": true,
  "encryptsData": false,
  "hasIncidentResponsePlan": false,
  "hasSecurityTraining": true,
  "previousBreaches": 2,
  "riskScore": 45
}
```

---

## Project Structure

```
cyber-risk-platform/
├── src/main/java/com/cyberrisk/
│   ├── controller/          # REST endpoints
│   ├── service/             # Risk scoring + AI memo logic
│   ├── repository/          # JPA repositories
│   ├── model/               # Company entity
│   └── config/              # CORS config
├── frontend/
│   └── src/
│       ├── App.jsx          # Main UI + risk gauge component
│       └── App.css
├── Dockerfile
├── docker-compose.yml
└── .github/workflows/
    └── ci.yml
```

---

## CI/CD

GitHub Actions runs on every push to `main`:

1. Spins up a PostgreSQL service container
2. Builds the project with Maven
3. Runs the test suite

[![CI Pipeline](https://github.com/sabrinahaniff/cyber-risk-platform/actions/workflows/ci.yml/badge.svg)](https://github.com/sabrinahaniff/cyber-risk-platform/actions/workflows/ci.yml)
