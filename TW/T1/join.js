const crypto = require('crypto');
var fs = require('fs');
var peças = [];
var val1;
var val2;you

module.exports.join = function join(body, response){
  var answer = {};
  var gameid;
  var mao = [];
  let flag = true;

  if(typeof body.group != "number" || typeof body.nick != "string" || typeof body.pass != "string"){
    answer.error = "Type Error";
    response.writeHead(400, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
    response.write(JSON.stringify(answer));
    response.end();
    return;
  }

  var join = "./join/" + body.group + ".txt";

  fs.readFile(join, function (err, data){
    if(err == null){
      gameid = data.toString('utf-8');

      fs.unlink(join, function (err){
        if (err)
        throw err;

        console.log('File deleted');
      });

      baralhar();
      answer = {"game": gameid, "hand": mao};

      response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
      response.write(JSON.stringify(answer));
      response.end();
      return;
    }

    else{
      console.log('Create Game file');

      gameid = crypto
      .randomBytes(15)
      .toString('hex');

      baralhar();
      answer= {"game": gameid, "hand": mao};

      fs.writeFile(join, gameid, function (err, file){
        flag = false;
        if(err)
            throw err;

        response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
        response.write(JSON.stringify(answer));
        response.end();
        return;
      })
    }
  })


  function baralhar() {
    for(let i = 0; i < 7; i++) {
      val1 = Math.floor(Math.random() * 7);
      val2 = Math.floor(Math.random() * 7);

      if(!peça_usada(val1, val2)) {
        var random = [val1, val2];
        mao[i] = random;
        peças.push(random);
      }

      else
        i--;
    }
  }

  function peça_usada(valor1, valor2){
    for(let i = 0; i < peças.length; i++){
      var analisar = peças[i];
      if((analisar[0] == valor1 && analisar[1] == valor2) || (analisar[1] == valor1 && analisar[0] == valor2)){
        return true;
      }
    }
    return false;
  }
}
