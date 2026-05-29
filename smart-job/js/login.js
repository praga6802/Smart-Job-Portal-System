const form = document.getElementById("loginform");
form.addEventListener("submit", handleLogin);
const errorMsg = document.getElementById("error");

async function handleLogin(e) {
  e.preventDefault();

  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();

  const loginData = { email, password };

  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
      credentials: "include",
    });
    const data = await response.json();
    if (response.ok) {
      console.log(data);
      console.log(`Token: ${data.token}`);
      errorMsg.innerText = "";
      alert(data.message);
      localStorage.setItem("token", data.token);
    } else {
      errorMsg.innerText = data.error;
      console.error("Backend Error", data);
    }
  } catch (err) {
    errorMsg.innerText = "Network Error.. Please try again";
    console.error(err);
  }

  form.addEventListener("reset", () => {
    errorMsg.innerText = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";
  });
}
