VIRTUALLAB COMPLETE REPLACEMENT PACKAGE

The archive contains the complete frontend and backend source plus database.sql. Extract it into a NEW folder first. Do not merge files into your old folder until you have a backup.

QUICK START
1. Open database.sql in MySQL Workbench and run it. It creates the virtuallab database.
2. Start backend in PowerShell:
   cd virtuallab-backend
   $env:DB_PASSWORD="YOUR_ACTUAL_MYSQL_PASSWORD"
   .\mvnw.cmd spring-boot:run
3. In another terminal start frontend:
   cd virtuallab-frontend
   npm install
   npm run dev -- --port 5174
4. Use the Vite URL printed in terminal. Register a Teacher account and a Student account.
5. Teacher creates an assignment. Student signs in, opens Assignments, starts the assignment, runs code, then presses Submit Code. Teacher opens Submissions to see student, problem, source code, language, output, status, and time.

FEATURES INCLUDED
- Register/login for student and teacher; logout; remembered session.
- Database-backed assignments and submissions.
- Assignment title and statement shown in the editor.
- Search results across assignments, practice problems, and submissions.
- Run Code and Submit Code separated; submit stores source, input, output, language, and status.
- Teacher submissions list and students list.
- Streak calculated from recorded submission dates.
- CORS includes localhost:5173 and localhost:5174.

IMPORTANT
- Keep backend and frontend terminals open. If old Java is already using port 8080, stop it before starting this backend; otherwise the old server will continue serving old endpoints.
- Configure DB_PASSWORD with your own MySQL password in the backend terminal. Do not publish it to GitHub.
- Code execution calls the existing public Judge0 service, so internet access is required.
- I cannot test against your computer's local MySQL/Judge0 environment from here; check the Spring Boot terminal for startup errors.
