import { API_BASE } from "./config.js";
console.log(API_BASE);

document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");
  console.log("Token from storage:", token);

  if (!token) {
    alert("Session expired or unauthorized");
    window.location.href = "index.html";
    return;
  }

  try {
    const res = await fetch("http://localhost:9090/api/admin/dashboard", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
      
    console.log('res : ' + res);

    const data = await res.json();
    console.log("Dashboard data:", data);

  } catch (err) {
    console.error(err);
    // localStorage.removeItem("token");
    // alert("Session expired or unauthorized");
    // window.location.href = "index.html";
  }
});