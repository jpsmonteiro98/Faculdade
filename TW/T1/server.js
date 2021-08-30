var eventSource = null;
var updt = null;
var id = 24;
var game = null;
var usr = null;
var passwd = null;

function register(username, password) {
  var user = {
    nick: username,
    pass: password
  }

  usr = username;
  passwd = password;

  fetch("http://localhost:8124/register", {
    method: "POST",
    body: JSON.stringify(user)
  })

  .then(function(response) {
    return response.json();
  })

  .then(function(data) {
    if(data.error != null)
    alert(data.error);

    else
      document.getElementById("authentication").style.display = "none";

    console.log("Register", data);
  })

  .catch(function(error) {
    window.alert("Fetch failed", error);
  })
}

function ranking() {
  var table = document.getElementById("rkg");
  var row;
  var cell1;
  var cell2;
  var cell3;
  var cell4;

  fetch("http://localhost:8124/ranking", {
    method: "POST",
    body: JSON.stringify({})
  })

  .then(function(response) {
    return response.json();
  })

  .then(function(data) {
    if(data.error != null)
      alert(data.error);

    console.log("Ranking", data);

    if(data.ranking.length > 0) {
      row = table.insertRow(0);
      cell1 = row.insertCell(0);
      cell2 = row.insertCell(1);
      cell3 = row.insertCell(2);
      cell4 = row.insertCell(3);

      cell1.appendChild(document.createTextNode("#"));
      cell2.appendChild(document.createTextNode("User"));
      cell3.appendChild(document.createTextNode("Victories"));
      cell4.appendChild(document.createTextNode("Games"));

      for(let i = 1; i <= 10; i++) {
        row = table.insertRow(i);

        cell1 = row.insertCell(0);
        cell2 = row.insertCell(1);
        cell3 = row.insertCell(2);
        cell4 = row.insertCell(3);

        cell1.appendChild(document.createTextNode(i + "."));
        cell2.appendChild(document.createTextNode(data.ranking[i-1].nick));
        cell3.appendChild(document.createTextNode(data.ranking[i-1].victories));
        cell4.appendChild(document.createTextNode(data.ranking[i-1].games));
      }
    }
  })

  .catch(function(error) {
    console.log("Fetch failed", error);
  })
}

function join() {
  var user2 = {
    group: id,
    nick: usr,
    pass: passwd
  }

  fetch("http://localhost:8124/join", {
    method: "POST",
    body: JSON.stringify(user2)
  })

  .then(function(response) {
    return response.json();
  })

  .then(function(data) {
    if(data.error == null) {
      game = data.game;
      update();
    }

    console.log("Join", data);

    for(let i = 0; i < 7; i++) {
      var server = data.hand[i];
      player.push(String.fromCodePoint(codigoh(server[0], server[1])));
      anonymous.push("\uD83C\uDC62");
    }

    player_2.appendChild(document.createTextNode(player.join(" ")));
    anonymous_2.appendChild(document.createTextNode(anonymous.join(" ")));
  })

  .catch(function(error) {
    console.log("Fetch failed", error);
  })
}

function update() {
  eventSource = new EventSource("http://localhost:8124/update?nick=" + usr + "&game=" + game);
  eventSource.onmessage = function(event) {
    updt = JSON.parse(event.data);
    console.log("Update", updt);

    if(updt.winner == usr) {
      window.alert("Won the game!");
      eventSource.close();
      return;
    }

    if(updt.winner != usr && updt.winner != undefined) {
      window.alert("Lost the game!");
      eventSource.close();
      return;
    }

    if(updt.turn == usr) {
      window.alert("Your turn!\n");
      return;
    }

    if(updt.turn != usr) {
      window.alert("Opponent turn!\n");
      return;
    }
  }

  eventSource.onerror = function(e) {
    console.log("EventSource failed");
  }
}

function leave() {
  var user3 = {
    nick: usr,
    pass: passwd,
    game: game
  }
  fetch("http://localhost:8124/leave", {
    method: 'POST',
    body: JSON.stringify(user3)
  })

  .then(function(response) {
    return response.json();
  })

  .then(function(data) {
    console.log("Leave", data);

    if(!data.error)
      box_4.style.display = "block";

    if(data.error)
      window.alert("Invalid game! \n");
  })

  .catch(function(error) {
    console.log("Fetch failed", error);
  })
}