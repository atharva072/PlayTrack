console.log('auth.js loaded');
const API_BASE = "http://localhost:9090";

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");
  const errorEl = document.getElementById("error");
  const registerLink = document.getElementById("registerLink");

  if (!form) return;

  const loginBtn = document.getElementById("loginBtn");
  const feedback = document.getElementById("authFeedback");

  loginBtn.addEventListener("click", async (e) => {

    console.log("LOGIN CLICKED");

    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    errorEl.textContent = "";
    registerLink.style.display = "none";
    feedback.classList.remove("show");

    try {
      const res = await fetch(`${API_BASE}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: `{\"userName\": \"${username}\", \"password\": \"${password}\"}`
      });

      console.log("FETCH DONE");
      
      const data = await res.json();
      console.log("FULL RESPONSE:", data);

      if (!res.ok || !data?.data) {
        throw new Error(data.message || "Invalid username or password");
      }

      // store the token
      localStorage.setItem("token", data.data);
      console.log("TOKEN STORED");

      window.location.href = "dashboard.html";

    } catch (err) {
      errorEl.textContent = err.message;
      registerLink.style.display = "block";
      feedback.classList.add("show");
    }
  });
});