var fs = require('fs');

module.exports.leave = function leave(body, response){
  var answer = {};

  if(typeof body.nick != "string" || typeof body.pass != "string" || typeof body.game != "string"){
    answer.error = "Type Error";
    response.writeHead(400, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
    response.write(JSON.stringify(answer));
    response.end();
    return;
  }

  var quit = "./join/" + body.group + ".txt";

  fs.readFile(quit, function (err, data){
    if(err == null){
        fs.unlink(quit, function (err){
            if(err) 
                throw err;
        });

      response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
      response.write(JSON.stringify(answer));
      response.end();
      return;
    }
    
    else{
      response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
      response.write(JSON.stringify(answer));
      response.end();
      return;
    }
  })
}
