var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
server.listen(8080, function(){
    console.log('server started');
});
io.on('connection', function(socket){
    console.log('player connected');
    socket.emit('socketID', {id :socket.id});
    socket.emit('getPlayers', players);
    socket.broadcast.emit('newPlayer', {id : socket.id});
    //Disconnection event
    socket.on('disconnect', function(){
        console.log('player disconnected');
        for(var i = 0; i < players.length; i++){
            if(players[i].id = socket.id){
                console.log('Player removed from array');
                players.splice(i, 1);
                socket.broadcast.emit('playerDisconnected', {id: socket.id});
            }
        }
    });
    players.push(new player(socket.id, 0, 0));
});
function player(id, x, y){
    this.id = id;
    this.x = x;
    this.y = y;
}