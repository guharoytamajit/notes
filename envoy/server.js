var http = require('http');

//create a server object:
http.createServer(function (req, res) {
  res.write('Hello World! from 4444'); //write a response to the client
  res.end(); //end the response
}).listen(4444); //the server object listens on port 8080