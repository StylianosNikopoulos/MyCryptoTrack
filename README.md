# MyCryptoTrack – Real-Time Cryptocurrency Tracking Platform

**MyCryptoTrack** is a full-stack microservices application for **real-time cryptocurrency monitoring and alerting**, built with **Spring Boot**, **React**, **Kafka**, **PostgreSQL**, and **Docker Compose**.  
It continuously streams live market data, lets users create personalized buy/sell alerts, and notifies them instantly via email and in-app notifications.

---

## 🚀 Features

### 🧩 Backend (Spring Boot Microservices)
- **Market Service:**  
  Fetches and streams live cryptocurrency data from public APIs.  
  Publishes updates to Kafka and persists market data in PostgreSQL.
- **Alert Service:**  
  Handles alert creation, updates, and deletions.  
  Consumes market data from Kafka to trigger alerts when target prices are reached.  
  Sends email + in-app notifications via notification service.
- **Auth Service:**  
  Secure user registration and JWT-based authentication.
- **Notification Service:**  
  Stores and serves triggered alerts for users.

### 💻 Frontend (React)
- Real-time live ticker of cryptocurrency prices using Server-Sent Events.
- Create, update, and delete price alerts visually.
- View triggered notifications in a responsive UI.
- OAuth2 / JWT authentication integrated with backend.

### ⚙️ Infrastructure
- **Kafka + Zookeeper:** Event streaming for real-time updates.
- **PostgreSQL:** Central relational database shared across services.
- **Docker Compose:** Orchestrates multi-service environment.
- **CI:** GitHub Actions pipeline automatically builds, tests, and validates the project on every commit.

---

![Screenshot 2025-11-06 at 17 36 24 (2)](https://github.com/user-attachments/assets/5be86fcd-9451-4b1c-baae-73de73a427f7)


![Screenshot 2025-11-06 at 17 34 03 (2)](https://github.com/user-attachments/assets/f030cf91-a416-470d-bb68-dc8a113c588c)


![Screenshot 2025-11-06 at 17 34 11 (2)](https://github.com/user-attachments/assets/c117529b-b725-4250-9514-32428ee5ff8f)


![Screenshot 2025-11-06 at 17 34 36 (2)](https://github.com/user-attachments/assets/de2d80e3-780d-4395-94a8-feafd714c595)


![Screenshot 2025-11-06 at 17 34 37 (2)](https://github.com/user-attachments/assets/1cbfdb1e-56e8-46ec-b990-80d219d48457)


![Screenshot 2025-11-06 at 17 34 46 (2)](https://github.com/user-attachments/assets/58e501e4-0c51-4290-8188-a621169f5915)


![Screenshot 2025-11-06 at 17 34 57 (2)](https://github.com/user-attachments/assets/64eb45bf-f9f3-456c-9752-12e0431d7667)


![Screenshot 2025-11-06 at 17 35 12 (2)](https://github.com/user-attachments/assets/45479a13-bed8-49eb-ae4c-1ca7e1909445)


![Screenshot 2025-11-06 at 17 35 17 (2)](https://github.com/user-attachments/assets/03512c56-cf02-44c4-bfc2-046f12565e41)


![Screenshot 2025-11-06 at 17 47 04](https://github.com/user-attachments/assets/ebc00ecb-f9a3-4adc-8dde-ed162b5481f5)


![Screenshot 2025-11-06 at 17 35 52 (2)](https://github.com/user-attachments/assets/b70bd262-df35-4a79-b666-8a5738d08bdd)
