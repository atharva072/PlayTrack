const API_BASE = "http://localhost:9090";

document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");
  console.log("Token from storage:", token);

  if (!token) {
    alert("Session expired or unauthorized");
    window.location.href = "index.html";
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/api/admin/dashboard`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (res.status === 401) {
      throw new Error("Unauthorized");
    }

    const data = await res.json();
    console.log("Dashboard data:", data);

    // render dashboard here

  } catch (err) {
    console.error(err);
    localStorage.removeItem("token");
    alert("Session expired or unauthorized");
    window.location.href = "index.html";
  }
});