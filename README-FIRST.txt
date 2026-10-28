VIRTUALLAB CODING PLATFORM — FULL PROJECT SOURCE

Contents:
- virtuallab-frontend: React + Vite UI, login/register, student/teacher dashboards, Code Editor, assignments UI.
- virtuallab-backend: Spring Boot API, JWT authentication, MySQL/JPA assignments, code-run endpoint.
- Assignment creation endpoint is teacher-only; authenticated students/teachers can fetch assignments.
- Logout is made visible in the sidebar for desktop and mobile layouts.

WINDOWS RUN STEPS
1. Extract this ZIP.
2. Open the extracted folder in VS Code.
3. Backend terminal:
   cd virtuallab-backend
   .\mvnw.cmd clean compile
   .\mvnw.cmd spring-boot:run
4. Frontend terminal:
   cd virtuallab-frontend
   npm install
   npm run dev -- --port 5174
5. Open http://localhost:5174/

IMPORTANT CONFIGURATION
- Backend uses application.properties for the MySQL connection. It must match your local MySQL username/password and database. The ZIP keeps the configuration from the existing project; if those credentials are not valid on your computer, update them before running.
- Create/register a TEACHER account and a STUDENT account on the login page.
- Login token is stored in browser storage; use the visible Logout button to switch accounts.
- Code execution depends on the configured Judge0 endpoint/network availability.
- This source package is prepared from the project files provided in this chat. It was not possible to run an end-to-end test against your local computer or push to your GitHub account from here.
