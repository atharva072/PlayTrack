import { API_BASE } from "./config.js";

document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Session expired or unauthorized");
    window.location.href = "index.html";
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/api/admin/dashboard`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    const data = await res.json();

    const sidebar = document.getElementById("sidebar");
    const toggleBtn = document.getElementById("toggleBtn");
    const mainContent = document.getElementById("mainContent");

    toggleBtn.addEventListener("click", () => {
      sidebar.classList.toggle("collapsed");
    });

    const totalTeams = await fetch(`${API_BASE}/api/admin/dashboard/totalTeams`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    const numTeams = await totalTeams.json();

    const totalPlayers = await fetch(`${API_BASE}/api/admin/dashboard/totalPlayers`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    const numPlayers = await totalPlayers.json();

    // /dashboard/totalAttendances
    const totalAttendances = await fetch(`${API_BASE}/api/admin/dashboard/totalPlayers`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    const numAttendances = await totalAttendances.json();

    const renderHome = () => {
      mainContent.innerHTML = `
        <h1 class="page-title">Admin Dashboard</h1>

        <div class="stats-grid">
          <div class="stat-card">
            <h3>Total Teams</h3>
            <p class="stat-value">${numTeams}</p>
          </div>

          <div class="stat-card">
            <h3>Total Players</h3>
            <p class="stat-value">${numPlayers}</p>
          </div>

          <div class="stat-card">
            <h3>Attendances</h3>
            <p class="stat-value">${numAttendances}</p>
          </div>
        </div>
      `;
    };

    // Default page
    renderHome();

    // Sidebar navigation
    document.querySelectorAll(".nav-links li").forEach(item => {
      item.addEventListener("click", () => {
        const page = item.dataset.page;

        if (page === "home") {
          renderHome();
        } else {
          mainContent.innerHTML = `
            <h1 class="page-title">${page.toUpperCase()}</h1>
            <p>Coming soon 🚧</p>
          `;
        }
      });
    });

    const logoutBtn = document.getElementById("logoutBtn");
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem('token');
      window.location.href = "index.html";
    });

  } catch (err) {
    console.error(err);
    // localStorage.removeItem("token");
    // window.location.href = "index.html";
  }
});

