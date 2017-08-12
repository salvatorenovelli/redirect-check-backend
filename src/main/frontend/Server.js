let app = require('express')();
// let httpServer = require("http").createServer(app);
// let ws = require('ws');
var expressWs = require('express-ws')(app);

let httpProxy = require('http-proxy');

let apiProxy = httpProxy.createProxyServer();
let frontend = 'http://localhost:3000',
    backend = 'http://localhost:8080';


console.log("Proxy started!");


app.all("/api/*", function (req, res) {
    console.log('redirecting to backend');
    apiProxy.web(req, res, {target: backend});
});

app.all("*", function (req, res) {
    console.log('redirecting to frontend');
    apiProxy.web(req, res, {target: frontend});
});

app.ws('/', function(ws, req) {
    ws.on('*', function(msg) {
        console.log(msg);
    });
    console.log('socket', req.testing);
});

app.listen(3001);