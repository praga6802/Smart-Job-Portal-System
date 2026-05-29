const form = document.getElementById("signup");
form.addEventListener("submit", handleSignUp);

const errorMsg = document.getElementById("error");

async function handleSignUp(e) {
  e.preventDefault();

  const username = document.getElementById("username").value.trim();
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();
  const role = document.getElementById("role").value.trim();
  const mobNumber = document.getElementById("mobNumber").value.trim();

  const signUp = {
    username,
    email,
    password,
    role,
    mobNumber,
  };

  try {
    const response = await fetch("http://localhost:8080/auth/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(signUp),
      credentials: "include",
    });

    const data = await response.json();

    if (response.ok) {
      alert(data.details);
      console.log(data);
      errorMsg.innerText = "";
      form.reset();
    } else {
      errorMsg.innerText = data.details;
      console.error("Backend Error:", data);
    }
  } catch (error) {
    errorMsg.innerText = "Network Error.. Please try again";
    console.error(error);
  }

  form.addEventListener("reset", () => {
    errorMsg.innerText = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";
  });
}
