package com.boxcubed.node_server;

import com.badlogic.gdx.Gdx;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

/**
 * Created by Tej Sidhu on 1/04/2017.
 */
public class server {
    private Socket clientSocket;
    public  server(){
        connectSocket();
        configureSocketEvents();
    }
    public void connectSocket(){
        try {
            clientSocket = IO.socket("http://localhost:8080");
            clientSocket.connect();
            Gdx.app.log("[SocketIO]", "Connection to server established");
        }catch (Exception e){
            Gdx.app.log("[SocketIO]", "Error connecting to server: "+ e);
        }
        System.out.println("I work");
    }
    public void configureSocketEvents(){
        clientSocket.on(clientSocket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("[SocketIO]", "Connection success");
            }
        });
    }
    public void updateServer(float delta){

    }
}
