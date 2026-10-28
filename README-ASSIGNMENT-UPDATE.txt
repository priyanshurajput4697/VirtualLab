VirtualLab Assignment Database Update

What changed:
- Added a MySQL/JPA Assignment entity and repository.
- Added authenticated GET /api/assignments and teacher-only POST /api/assignments endpoints.
- Connected the React assignments view to the backend API.
- Added CORS support for localhost:5174 as well as 5173.
- Removed the auth controller's conflicting single-origin @CrossOrigin annotation.

Apply safely:
1. Make a backup copy of your existing virtuallab-next-batch folder.
2. Extract this ZIP into the existing virtuallab-next-batch folder and allow files to be replaced. Keep the folder structure intact.
3. In the backend terminal, stop the old backend with Ctrl+C, then run:
   .\mvnw.cmd clean compile
   .\mvnw.cmd spring-boot:run
4. In the frontend terminal, run (if not already running):
   npm install
   npm run dev -- --port 5174
5. Open http://localhost:5174/ and sign in as a TEACHER to create an assignment. Then sign in as a STUDENT and open Assignments. The assignment should be visible after refresh because it is stored in MySQL.

Important:
- This ZIP includes source files only, not node_modules or Maven target files.
- Do not replace your application.properties / database credentials with anything from another project. This ZIP keeps the existing properties file unchanged.
- If an already-open browser has an old session without a token, log out and log in again before testing.
