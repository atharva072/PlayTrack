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
    sidebarNavigation({
      renderHome: () => renderHome(mainContent, numTeams, numPlayers, numAttendances),
      renderTeams
    });

    document.getElementById("logoutBtn");
      logoutBtn.addEventListener("click", () => {
        localStorage.removeItem('token');
        window.location.href = "index.html";
      });

  } catch (err) {
    console.error(err);
  }
});

function sidebarNavigation ({renderHome, renderTeams}) {
  const mainContent = document.getElementById("mainContent");
  document.querySelectorAll(".nav-links li").forEach(item => {
    item.addEventListener("click", () => {
      const page = item.dataset.page;

      if (page === "home") renderHome();
      if (page == 'teams') renderTeams();
      
      if (page == 'players') {
        mainContent.innerHTML = `
          <h1 class="page-title">${page.toUpperCase()}</h1>
          <p>Coming soon 🚧</p>
        `;
      }
      
      if (page == 'settings') {
        mainContent.innerHTML = `
          <h1 class="page-title">${page.toUpperCase()}</h1>
          <p>Coming soon 🚧</p>
        `;
      }
    });
  });
}

async function renderTeams () {
  const mainContent = document.getElementById("mainContent");
  mainContent.innerHTML = `<p>Loading teams...</p>`;

  try {
    const token = localStorage.getItem("token");
    const teams = await getTeams(token);

    if (!teams.length) {
      `<h1 class="page-title">Teams</h1>
        <p>No teams created yet</p>
        <button class="primary-btn">➕ Add Team</button>
      `;
      return;
    }

    mainContent.innerHTML = `
      <h1 class="page-title">Teams</h1>
      <button class="primary-btn">➕ Add Team</button>

      <div class="table">
        <div class="table-header">
          <span>Name</span>
          <span>Coach</span>
          <span>Players</span>
        </div>

        ${teams.map(team => `
          <div class="table-row">
            <span>${team.name}</span>
            <span>${team.coach}</span>
            <span>${team.playersCount}</span>
          </div>
        `).join("")}
      </div>
    `;
  } catch (err) {
    console.error(err);
    mainContent.innerHTML = `<p>Error loading teams ❌</p>`;
  }
}

function renderHome (mainContent, numTeams, numPlayers, numAttendances) {
  mainContent = document.getElementById("mainContent");
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
  const totalAttendances = await fetch(`${API_BASE}/api/admin/dashboard/totalAttendances`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    return totalAttendances.json();    
}

async function getTeams (token) {
  const res = await fetch(`${API_BASE}/api/admin/teams`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
  const json = await res.json();

  console.log(json.data);
  return json.data;
}