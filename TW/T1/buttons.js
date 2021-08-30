const box = document.getElementById("box");
const box_2 = document.getElementById("box_2");
const box_3 = document.getElementById("box_3");
const box_4 = document.getElementById("box_4");

const button = document.getElementById("btn");
const button_2 = document.getElementById("btn_2");
const button_3 = document.getElementById("btn_3");
const button_4 = document.getElementById("btn_4");

const close_button = document.getElementsByClassName("close")[0];
const close_button_2 = document.getElementsByClassName("close_2")[0];
const close_button_3 = document.getElementsByClassName("close_3")[0];

button.onclick = function() {
  box.style.display = "block";
}

close_button.onclick = function() {
  box.style.display = "none";
}

button_2.onclick = function() {
  box_2.style.display = "block";
  ranking();
}

close_button_2.onclick = function() {
  box_2.style.display = "none";
}

button_3.onclick = function() {
  box_3.style.display = "block";
}

close_button_3.onclick = function() {
  box_3.style.display = "none";
}

button_4.onclick = function() {
  leave();
}
