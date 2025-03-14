var loginForm = document.getElementById("login1");
var registerForm = document.getElementById("registro");

function registro(){
    loginForm.style.display = "none";
    registerForm.style.display = "block";
}

function login(){
    loginForm.style.display = "block";
    registerForm.style.display = "none";
}

document.getElementById('register').addEventListener('click', function() {
    registro();
});

document.getElementById('login').addEventListener('click', function() {
    login();
});
