const API_BASE = "http://localhost:9090";

async function apiFetch(url, options = {}) {
  const token = localStorage.getItem("token");

  if (!token) {
    window.location.href = "index.html";
    return;
  }

  const headers = {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`,
    ...options.headers
  };

  const res = await fetch(`${API_BASE}${url}`, {
    ...options,
    headers
  });

  if (res.status === 401 || res.status === 403) {
    alert("Session expired or unauthorized");
    localStorage.removeItem("token");
    window.location.href = "index.html";
    return;
  }

  return res.json();
}

function logout() {
  localStorage.removeItem("token");
  window.location.href = "index.html";
}