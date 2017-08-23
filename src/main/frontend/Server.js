let app = require('express')();
// let httpServer = require("http").createServer(app);
// let ws = require('ws');
var expressWs = require('express-ws')(app);
let httpProxy = require('http-proxy');

let apiProxy = httpProxy.createProxyServer();
let frontend = 'http://localhost:3000',
    backend = 'http://localhost:8080';


console.log("Proxy started!");

process.on('uncaughtException', function (err) {
    console.log("Uncaught exception: " + err.message);
});

app.all("/api/*", function (req, res) {
    console.log('redirecting ' + req.originalUrl + ' to backend');
    apiProxy.web(req, res, {target: backend});
});

app.ws('*', function(ws, req) {
    ws.on('*', function(msg) {
        console.log(msg);
    });
    console.log('Websocket request intercepted');
});

app.all("*", function (req, res) {
    console.log('redirecting ' + req.originalUrl + ' to frontend');
    apiProxy.web(req, res, {target: frontend});
});

app.listen(3001);