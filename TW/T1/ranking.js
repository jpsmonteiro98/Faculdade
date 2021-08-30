var fs = require('fs');

module.exports.ranking = function ranking(body,response){
  var table = "./ranking/" + "admin.txt";
  
  fs.readFile(table,function (err,data){
    if(err == null){
      let rank = data.toString();

      response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
      response.write(rank);
      response.end();
    }

    else {
      console.log('Create ranking file');
      
      let rank = {"ranking":[]};
      var ranking = JSON.stringify(rank);
      
      fs.writeFile(table, ranking, function (err, file){
        if(err)
          throw err;
        
        response.writeHead(200, {"content-type": "application/javascript", "cache-control":"no-cache", "Access-Control-Allow-Origin": "*"});
        response.write(JSON.stringify(rank));
        response.end();
      });
    }
  });
}
