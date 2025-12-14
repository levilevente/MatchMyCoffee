# ☕ MatchMyCoffee

![React](https://img.shields.io/badge/Frontend-React%20%7C%20TypeScript-61DAFB?style=for-the-badge&logo=react)
![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot)
![Python](https://img.shields.io/badge/AI%20Agent-LangGraph%20%7C%20FastAPI-3776AB?style=for-the-badge&logo=python)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-336791?style=for-the-badge&logo=postgresql)

**MatchMyCoffee** is an integrated webshop designed to help coffee lovers discover personalized roasts and flavors through a unique taste-profile recommendation system.

---

## 📖 Overview

The platform connects coffee enthusiasts with artisanal roasters, fostering a community where quality meets personal taste.
* **For Coffee Lovers:** Discover new flavors, order high-quality beans, and get personalized recommendations based on your taste profile.
* **For Roasters:** Small, local roasters can showcase their products and reach a wider audience.

## 🏗️ Architecture & Tech Stack

The application is built on a **microservice architecture**, ensuring modularity and scalability.

### 📂 Repository Structure

| Service | Technology | Description |
| :--- | :--- | :--- |
| **Frontend** | React + TypeScript | The user interface for the webshop and admin panels. |
| **Backend** | Java (Spring Boot) | Handles core business logic, user management, and order processing. |
| **Agent** | Python + LangGraph | AI-driven service for the recommendation system and intelligent features. |
| **Database** | PostgreSQL | Relational database configurations and scripts. |

**Deployment:** Designed for deployment on AWS.

---

## ✨ Key Features

### 👤 User Experience
* **Coffee Recommendation System:** Users fill out an interactive questionnaire to generate a taste profile, receiving tailored coffee suggestions.
* **Webshop & Ordering:** Browse detailed product info (roast, origin, notes), manage a cart, and securely place orders.
* **Reviews:** Users can rate coffees and write feedback to help the community.


### 🛠️ Administration & Marketing
* **Product Management:** Administrators can add, modify, or remove products and moderate user reviews.
* **Blog & Content:** The Marketing role can publish articles, brewing tips (e.g., "How to brew the perfect pour-over"), and updates on new trends.

---

## 🚀 Getting Started

### Prerequisites
* Node.js & npm/yarn
* Java JDK 17+
* Python 3.10+
* PostgreSQL
* Docker (optional, for running services together)

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/yourusername/matchmycoffee.git
    ```

2.  **Database Setup**
    Navigate to the `database` folder and run the initialization scripts to set up the PostgreSQL schema.

3.  **Backend (Spring Boot)**
    ```bash
    cd backend
    ./mvnw spring-boot:run
    ```

4.  **Agent (Python/LangGraph)**
    ```bash
    cd agent
    pip install -r requirements.txt
    uvicorn main:app --reload
    ```

5.  **Frontend (React)**
    ```bash
    cd frontend
    npm install
    npm start
    ```

---

## 👥 Authors

* **Demeter Beniamin**
* **Daroczi Levente**


---

*Note: Features like Dark Mode, Wishlists, and non-coffee products are excluded from this MVP version.*