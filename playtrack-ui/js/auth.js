// import { jwtDecode } from "jwt-decode";
import { API_BASE } from "./config.js";

document.addEventListener("DOMContentLoaded", () => {
  
  const form = document.getElementById("loginForm");
  const errorEl = document.getElementById("error");
  const registerLink = document.getElementById("registerLink");

  if (!form) return;

  const loginBtn = document.getElementById("loginBtn");
  const feedback = document.getElementById("authFeedback");

  loginBtn.addEventListener("click", async (e) => {
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
      
      const data = await res.json();

      if (!res.ok || !data?.data) {
        throw new Error(data.message || "Invalid username or password");
      }

      const token = data.data;
      const decodedPayload = parseToken(token);
      const roles = decodedPayload.roles;

      // store the token
      localStorage.setItem("token", token);

      if (Object.values(roles).includes('ROLE_ADMIN')) {
        console.log("Welcome ADMIN")
        window.location.href = "adminDashboard.html";
      }

    } catch (err) {
      errorEl.textContent = err.message;
      registerLink.style.display = "block";
      feedback.classList.add("show");
    }
  });
});

function parseToken (token) {
  try {
      // Split the token into its parts (header.payload.signature)
      const parts = token.split('.');
      if (parts.length !== 3) {
          throw new Error('Invalid token format');
      }

      // The payload is the second part (index 1)
      const base64UrlPayload = parts[1];

      // Base64Url decode the payload. Note: standard atob() needs slight modification for base64url format
      const base64 = base64UrlPayload.replace(/-/g, '+').replace(/_/g, '/');
      const decodedPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      // Parse the JSON string into a JavaScript object
      return JSON.parse(decodedPayload);
  } catch (e) {
      console.error("Error decoding JWT:", e);
      return null;
  }
}