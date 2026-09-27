# Price Tracker — Java Full Stack Project

Track Amazon/Flipkart product prices, view history charts, and get alerted when a price drops to your target.

## Tech Stack
- **Frontend:** React.js + Vite, Bootstrap, Axios, Recharts, React Router
- **Backend:** Java 17, Spring Boot, Spring MVC, Spring Data JPA, Spring Security + JWT, Maven
- **Database:** PostgreSQL
- **Scraping:** Jsoup (reads price/name/image off the pasted product page HTML)
- **Deployment:** Docker + Docker Compose, NGINX (serves the built React app)

## ⚠️ Important: About the Price Fetching
Amazon and Flipkart don't provide a free public API for arbitrary product lookup by URL.
`PriceFetcherService` (backend) works by downloading and parsing the product page's HTML
with Jsoup. Two real limitations to know about, especially before a demo/viva:
1. Both sites change their page structure over time, so the CSS selectors in
   `PriceFetcherService.java` may need updating.
2. Both sites can rate-limit or block repeated automated requests.

Because of this, the service has a **mock fallback**: if a live fetch fails, it generates
a believable simulated price so the rest of the app (history, charts, alerts, scheduler)
still works and is demoable. You can see/toggle this in `PriceFetcherService.java`
(`MOCK_MODE_FALLBACK`).

---

## Option A: Run Everything with Docker (easiest)

Prerequisites: Docker Desktop installed and running.

```bash
cd price-tracker
docker-compose up --build
```

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- PostgreSQL: localhost:5432 (user: `postgres`, password: `postgres`, db: `price_tracker_db`)

First build takes a few minutes (Maven + npm downloading dependencies). Subsequent runs are fast.

To stop: `Ctrl+C`, then `docker-compose down` (add `-v` to also wipe the database volume).

---

## Option B: Run Manually (IntelliJ + VS Code) — recommended while learning

### 1. Install PostgreSQL locally
Install PostgreSQL, then create the database:
```sql
CREATE DATABASE price_tracker_db;
```
(Default assumed credentials: user `postgres`, password `postgres` — matches
`application.properties`. Change there if yours differ.)

### 2. Run the backend (IntelliJ IDEA)
1. Open the `backend/` folder in IntelliJ as a Maven project.
2. Let Maven download dependencies (pom.xml).
3. Run `PriceTrackerApplication.java`.
4. Backend starts on **http://localhost:8080**. Hibernate auto-creates all tables
   on first run (`spring.jpa.hibernate.ddl-auto=update`).
5. Test it's alive: `POST http://localhost:8080/api/auth/register` in Postman with:
   ```json
   { "username": "test", "email": "test@test.com", "password": "test123" }
   ```
   You should get back a JWT token.

### 3. Run the frontend (VS Code)
```bash
cd frontend
npm install
npm run dev
```
Frontend starts on **http://localhost:5173** and talks to the backend at
`http://localhost:8080/api` (see `frontend/src/services/api.js`).

---

## Testing the API with Postman
1. `POST /api/auth/register` or `/api/auth/login` → copy the `token` from the response.
2. For every other request, add header:
   `Authorization: Bearer <token>`
3. `POST /api/products` with body `{ "url": "https://www.amazon.in/dp/...", "targetPrice": 999 }`
4. `GET /api/products` → see your tracked products.
5. `GET /api/products/{id}/history` → see price history points.
6. `GET /api/alerts` → see triggered alerts.

## Project Structure
```
price-tracker/
├── backend/    (Spring Boot - Controller → Service → Repository → Hibernate → PostgreSQL)
├── frontend/   (React + Vite)
└── docker-compose.yml
```
See in-code comments throughout — every non-trivial class explains *why*, not just *what*,
since the goal is understanding the stack, not just having working code.

## Suggested next steps for you to extend (good for viva/demo talking points)
- Add pagination to `/api/products` (you already have `PriceHistoryRepository` using `Pageable` — extend the pattern).
- Add a refresh-token endpoint (concept is in your syllabus; current implementation uses a single 24h token for simplicity).
- Add email/push notifications when an alert triggers (currently in-app only).
- Add an admin endpoint (`/api/admin/users`) to actually back the Admin Panel screen.
