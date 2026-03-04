// import { jwtDecode } from "jwt-decode";
import { API_BASE } from "./config.js";

document.addEventListener("DOMContentLoaded", () => {
  
  document.getElementById("tagline").textContent = returnQuote();
  const form = document.getElementById("loginForm");
  const errorEl = document.getElementById("error");
  const registerLink = document.getElementById("registerLink");
  
  const registerKeyWord = document.getElementById("registerKeyWord");
  registerKeyWord.addEventListener("click", function (event) {
    event.preventDefault();

    const container1 = document.getElementById("container1");
    const container2 = document.getElementById("container2");
    container1.classList.add("slide-left");

    container2.classList.remove("offscreen-right");
    container2.classList.add("slide-in");
  });

  // logic to move back and forth
  const backButton = document.getElementById("backToLogin");
  document.getElementById("backToLogin").addEventListener("click", function (event) {
    event.preventDefault();

    const container1 = document.getElementById("container1");
    const container2 = document.getElementById("container2");

    // Remove forward animation
    container1.classList.remove("slide-left");

    // Slide register out to right
    container2.classList.remove("slide-in");
    container2.classList.add("offscreen-right");
  });

  // logic to check if the content in confirm password field matches the password field
  const registerPassword1 = document.getElementById("registerPassword1");
  const registerPassword2 = document.getElementById("registerPassword2");
  const validation = document.getElementById("validation");
  registerPassword2.addEventListener('input', () => {
    if (registerPassword1.value != registerPassword2.value) {
      validation.textContent = "Passwords don't match";
    } else {
      validation.textContent = "";
    }
  });

  // logic to check if the username already exists while registering a new user
  const registerBtn = document.getElementById("registerBtn");
  registerBtn.addEventListener("click", async (e) => {
    const registerUsername = document.getElementById("registerUsername").value;
    try {
      const checkUsername = await fetch(`${API_BASE}/api/auth/checkUsername/${registerUsername}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });

      const data = await checkUsername.json();
      const value = data.data;
      if (value) throw new Exception(data.message);
    } catch (err) {
      const usernameFeedback = document.getElementById("usernameFeedback");
      usernameFeedback.textContent = err.message;
      usernameFeedback.style.display = 'inline';
    }
  });

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

function returnQuote () {
  const quotes = [
    "\"I've missed more than 9000 shots in my career. That is why I succeed.\" — Michael Jordan",
    "\"Hard work beats talent when talent doesn't work hard.\" — Kevin Durant",
    "\"I learned all about life with a ball at my feet.\" - Ronaldinho",
    "\"Your love makes me strong, your hate makes me unstoppable!\" - Cristiano Ronaldo"
    /*"\"I start early and I stay late, day after day, year after year. It took me 17 years and 114 days to become an overnight success\" - Lionel Messi"*/
  ];

  const randomIndex = Math.floor(Math.random() * quotes.length);
  return quotes[randomIndex]
}