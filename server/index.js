var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
server.listen(8080, function(){
    console.log('server started');
});
io.on('connection', function(socket){
    console.log('player connected');
    socket.emit('socketID', {id :socket.id});
    socket.broadcast.emit('newPlayer', {id : socket.id});
    //Disconnection event
    socket.on('disconnect', function(){
        console.log('player disconnected');
    });
});
//