package com.boxcubed.node_server;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Vector2;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import me.boxcubed.main.States.GameState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tej Sidhu on 1/04/2017.
 */
public class server {
    private Socket clientSocket;
    public String clientID;
    public  server(){
        connectSocket();
        configureSocketEvents();
    }
    public void connectSocket(){
        try {
            clientSocket = IO.socket("http://localhost:8080");
            clientSocket.connect();
            Gdx.app.log("[SocketIO]", "Connection to socket established");
        }catch (Exception e){
            Gdx.app.log("[SocketIO]", "Error connecting: "+ e);
        }
    }
    public void configureSocketEvents(){
        clientSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("[SocketIO]", "Connection success");
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args [0];
                try{
                    clientID = data.getString("id");
                    Gdx.app.log("[SocketIO]", "Client Id: " + clientID);
                }catch (JSONException e){
                    Gdx.app.log("[SocketIO]", "Error getting client ID " + e );
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args [0];

                try{
                    String clientID = data.getString("id");
                    GameState.instance.createNewPlayer(clientID);
                    Gdx.app.log("[SocketIO]", "New Player connected");
                }catch (JSONException e){
                    Gdx.app.log("[SocketIO]", "Error connecting new player: " + e);
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for (int i = 0; i < objects.length(); i++){
                        GameState.instance.createNewPlayer(objects.getJSONObject(i).getString("id"));
                        Gdx.app.log("SocketIO", "Players connected");
                    }
                }catch (JSONException e){
                        Gdx.app.log("[SocketIO]", "Error getting players");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String player_to_remove = data.toString();
                GameState.instance.removePlayer(player_to_remove);

            }
        });
        //TODO "playerDisconnected" event
    }
    public void updateServer(float delta){

    }
}
