# YorkStore | Full-Stack E-Commerce Platform

YorkStore is a specialized e-commerce web application designed exclusively for the York University community, providing a digital marketplace for official faculty merchandise (Glendon, Lassonde, Osgoode, and Schulich). 

Developed by a 2-person engineering team as a modern, decoupled single-page application (SPA).

🔗 **Live Frontend Demo:** [https://mellow-unicorn-811456.netlify.app](https://mellow-unicorn-811456.netlify.app)

---

## 🚀 Key Features
- **Modern SPA Frontend:** Built with React, Vite, Redux Toolkit, Tailwind CSS, and Material-UI for a lightning-fast, responsive user experience.
- **Advanced State & Cart Persistence:** Uses Redux Toolkit for global state management; user shopping carts persist across sessions via backend synchronization.
- **Dynamic UX & Alerts:** Real-time "Hurry Up!" low-stock indicators on product cards and multi-criteria filtering (Faculty & Clothing Type).
- **Secure RESTful Backend:** Powered by Java Spring Boot, Hibernate/JPA, and Spring Security with stateless JSON Web Tokens (JWT).
- **Cloud Media Management:** Integrated Cloudinary API for optimized product image hosting, keeping database queries lightweight.
- **Admin Analytics Dashboard:** Provides real-time metrics on total revenue, order volume, and inventory status for store administrators.

---

## 🛠️ Tech Stack
* **Frontend:** React.js, Vite, Redux Toolkit, Tailwind CSS, Material-UI, Axios
* **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA (MVC Architecture)
* **Database & Storage:** PostgreSQL (Neon Serverless), Cloudinary API
* **Deployment:** Netlify (Frontend), Railway (Backend)

---

## 👥 Team & Individual Contributions
* **Syed (Backend Lead):** Architected the Spring Boot REST API, implemented JWT security, configured database relationships in PostgreSQL, and managed database seeding scripts.
* **Maya (Frontend Lead & Author):** Designed and developed the entire client-side architecture using React, Vite, and Redux Toolkit. Built responsive views (Home, Catalog, Cart, Checkout, Admin Dashboard), managed Axios API interception, and produced system architecture artifacts (UML & Sequence diagrams).
