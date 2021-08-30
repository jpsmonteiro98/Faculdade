const port = 8124;
const http = require('http');
const url = require('url');

let register = require('./register');
let ranking = require('./ranking');
let join = require('./join');
let update = require('./update');
let leave = require('./leave');

function send404(response){
  response.writeHead(404, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
  response.write("Error 404: Page not found");
  response.end();
}

function doPost(pathname, body, response){
  switch(pathname) {
    case '/register':
      register.register(body,response);
      break;

    case '/ranking':
      ranking.ranking(body,response);
      break;

    case '/join':
      join.join(body,response);
      break;

    case '/leave':
      leave.leave(body, response);
      break;

    default:
      send404(response);
      break;
  }
}

function doGet(pathname, search, request, response){
  switch (pathname) {
    case '/update':
      update.remember(response);
      request.on('close', () => update.forget(response));
      setImmediate(() => update.updater(update.update(search.name)), response);
      break;

    default:
      send404(response);
      break;
  }
}

function onRequest(request, response){
  const preq = url.parse(request.url,true);
  const pathname = preq.pathname;
  const search = preq.query;

  let answer = {};

  switch (request.method){
    case 'POST':
      var body = '';
      var jsonString = '';

      request.on('data', function (data) {
          jsonString += data;
      });

      request.on('end', function () {
        try {
          body = JSON.parse(jsonString);
        }
        
        catch(e){
          answer.error = "Error Parsing JSON";
          response.writeHead(400, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
          response.write(JSON.stringify(answer));
          response.end();
          return;
        }

        answer = doPost(pathname, body, response);
      });
      break;

    case 'GET':
      answer = doGet(pathname,search,request,response);
      break;

    default:
      send404();
      break;
  }
}

http.createServer(onRequest).listen(port);
console.log("Server running");
