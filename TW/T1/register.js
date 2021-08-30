const crypto = require('crypto');
var fs = require('fs');


module.exports.register = function register(body,response){
  var answer = {};

  if(body.nick == undefined){
    answer.error = "User not defined";
    response.writeHead(400, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
    response.write(JSON.stringify(answer));
    response.end();
    
    return;
  }

  else if(body.pass == undefined){
    answer.error = "Password not defined";
    response.writeHead(400, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
    response.write(JSON.stringify(answer));
    response.end();

    return;
  }

  if(typeof body.nick != "string" || typeof body.pass!= "string"){
    answer.error = "values are not strings";
    response.writeHead(400, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
    response.write(JSON.stringify(answer));
    response.end();
    
    return;
  }

  var user = crypto
              .createHash('md5')
              .update(body.nick)
              .digest('hex');

  var pass = crypto
              .createHash('md5')
              .update(body.pass)
              .digest('hex');

  var nick = "./register/"+ user + ".txt";
    
  fs.readFile(nick,function (err,data){
    if(err == null){
      password = data.toString('utf8');
      
      if(password == pass){
        response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
        response.write(JSON.stringify(answer));
        response.end();
      }

      else{
        answer.error = "Wrong Password";
        response.writeHead(401, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
        response.write(JSON.stringify(answer));
        response.end();
      }
    }

    else{
      fs.writeFile(nick, pass, function (err, file){
        if(err) 
          throw err;
          
        response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
        response.write(JSON.stringify(answer));
        response.end();
      })
    }
  })
}
