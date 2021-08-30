let responses = [];
var answer = {};

module.exports.remember = function(response) {
    responses.push(response);
}

module.exports.forget = function(response) {
    let pos = responses.findIndex((resp) => resp === response);
    if(pos > -1)
      responses.splice(pos,1);
}

module.exports.updater = function(message, response) {
    for(let response of responses) {
        response.write('data: '+ message +'\n\n');
    }
}

module.exports.update = function update(name) {
  return answer;
}
