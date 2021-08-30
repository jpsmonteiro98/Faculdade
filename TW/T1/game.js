var player = [];
var anonymous = [];

const player_2 = document.getElementById("player_2");
const anonymous_2 = document.getElementById("anonymous_2");

function codigoh(valor1,valor2){
  return 127075 + valor1 * 7 + valor2;
}

function codigo_left(valor1,valor2){
  return 127025 + valor1 * 7 + valor2;
}

function codigo_right(valor2,valor1){
  return 127025 + valor2 * 7 + valor1;
}

function distribuir_jogar() {
  document.getElementById("PXP").disabled = true;
  join();
}

function show(){
  document.getElementById('popup').style.display = "block";

  document.getElementById('loginbtn').onclick = function() {
    var username = document.getElementById('loguser').value;
    var password = document.getElementById('logpasswd').value;
    register(username, password);
  }
}