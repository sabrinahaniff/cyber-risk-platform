# Cyber Risk Platform

A full-stack cyber insurance underwriting platform that automates risk assessment for companies. Built with Spring Boot, PostgreSQL, and AI designed for the kind of workflow a cyber insurance underwriter would use daily.

## What it does

1. A company submits their security profile via REST API
2. The risk scoring engine evaluates their posture and generates a score from 0–100
3. AI automatically generates a professional underwriting memo with recommendations

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.5, Spring Data JPA
- **Database:** PostgreSQL
- **AI Integration:** Groq API (LLaMA 3.3 70B)
- **Containerization:** Docker, Docker Compose
- **CI/CD:** GitHub Actions

## Risk Scoring Model

The engine scores companies from 0 (critical risk) to 100 (low risk) based on weighted security factors:

| Factor | Penalty |
|--------|---------|
| No firewall | -20 pts |
| No antivirus | -15 pts |
| No data encryption | -20 pts |
| No incident response plan | -15 pts |
| No security training | -10 pts |
| Each previous breach | -10 pts |

| Score Range | Risk Level |
|-------------|------------|
| 80–100 | LOW |
| 60–79 | MEDIUM |
| 40–59 | HIGH |
| 0–39 | CRITICAL |

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/companies` | Submit a company for assessment |
| GET | `/api/companies` | Get all assessed companies |
| GET | `/api/companies/{id}` | Get a specific company |
| DELETE | `/api/companies/{id}` | Delete a company |
| POST | `/api/companies/{id}/generate-memo` | Generate AI underwriting memo |

## Running Locally

### Prerequisites
- Docker and Docker Compose
- Groq API key (free at console.groq.com)

### Start the app
```bash
export GROQ_API_KEY=your_key_here
docker-compose up --build
```

The API will be available at `http://localhost:8080`

### Example Request
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

### Example Response
```json
{
  "id": 1,
  "name": "Acme Corp",
  "industry": "Finance",
  "employeeCount": 500,
  "country": "Canada",
  "riskScore": 45
}
```

### Generate Underwriting Memo
```bash
curl -X POST http://localhost:8080/api/companies/1/generate-memo
```

## Architecture
REST API (Spring Boot)
↓
Risk Scoring Engine
↓
AI Memo Generator (Groq)
↓
PostgreSQL Database

## CI/CD

GitHub Actions automatically builds and tests the application on every push to main.
