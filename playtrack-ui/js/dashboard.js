import { API_BASE } from "./config.js";

document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Session expired or unauthorized");
    window.location.href = "index.html";
    return;
  }

  try {
    const sidebar = document.getElementById("sidebar");
    const toggleBtn = document.getElementById("toggleBtn");
    const mainContent = document.getElementById("mainContent");

    toggleBtn.addEventListener("click", () => {
      sidebar.classList.toggle("collapsed");
    });

    const numTeams = await getTotalTeams(token);
    const numPlayers = await getTotalPlayers(token);
    const numAttendances = await getTotalAttendances(token);

    renderHome(mainContent, numTeams, numPlayers, numAttendances);
    sidebarNavigation(renderHome);

    document.getElementById("logoutBtn");
      logoutBtn.addEventListener("click", () => {
        localStorage.removeItem('token');
        window.location.href = "index.html";
      });

  } catch (err) {
    console.error(err);
  }
});

function renderHome (mainContent, numTeams, numPlayers, numAttendances) {
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
            <h3>Total Attendances</h3>
            <p class="stat-value">${numAttendances}</p>
          </div>
        </div>
      `;
}

function sidebarNavigation (renderHome) {
  document.querySelectorAll(".nav-links li").forEach(item => {
    item.addEventListener("click", () => {
      const page = item.dataset.page;

      if (page === "home") {
        renderHome();
      } else if (page == 'teams') {
        mainContent.innerHTML = `
          <h1 class="page-title">${page.toUpperCase()}</h1>
          <p>Coming soon 🚧</p>
        `;
      } else if (page == 'players') {
        mainContent.innerHTML = `
          <h1 class="page-title">${page.toUpperCase()}</h1>
          <p>Coming soon 🚧</p>
        `;
      } else if (page == 'settings') {
        mainContent.innerHTML = `
          <h1 class="page-title">${page.toUpperCase()}</h1>
          <p>Coming soon 🚧</p>
        `;
      }
    });
  });
}

async function getTotalTeams (token) {
  const totalTeams = await fetch(`${API_BASE}/api/admin/dashboard/totalTeams`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    return totalTeams.json();
}

async function getTotalPlayers (token) {
  const totalPlayers = await fetch(`${API_BASE}/api/admin/dashboard/totalPlayers`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    return totalPlayers.json();
}

async function getTotalAttendances (token) {
  const totalAttendances = await fetch(`${API_BASE}/api/admin/dashboard/totalPlayers`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    return totalAttendances.json();    
}
