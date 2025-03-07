var x = document.getElementById("login1");
var y = document.getElementById("registro");
var z = document.getElementById("btn");

function registro(){
    x.style.left = "-400px";
    y.style.left = "50px";
    z.style.left = "110px";
}
function login(){
    x.style.left = "50px";
    y.style.left = "450px";
    z.style.left = "0";
}

document.getElementById('register').addEventListener('click', function() {
    registro();
});

document.getElementById('login').addEventListener('click', function() {
    login();
});