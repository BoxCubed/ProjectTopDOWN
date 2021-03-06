package com.boxcubed.node_server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.socket.client.IO;
import io.socket.client.Socket;
import me.boxcubed.main.States.GameState;

/**
 * Created by Tej Sidhu on 1/04/2017.
 */
// ---------------------------------------------------------------------------------------------\\
// ------------------------------------| Networking
// |-------------------------------------------\\
// ---------------------------------------------------------------------------------------------\\
public class server {
	private Socket clientSocket;
	public String clientID;

	public server() {
		connectSocket();
		configureSocketEvents();
	}

	public void connectSocket() {
		try {
			clientSocket = IO.socket("http://localhost:8080");
			clientSocket.connect();
			Gdx.app.log("[SocketIO]", "Initalised the socket");
		} catch (Exception e) {
			Gdx.app.log("[SocketIO]", "Error connecting: " + e.getMessage());
		}
	}

	public void configureSocketEvents() {
		clientSocket.on(Socket.EVENT_CONNECT, args -> Gdx.app.log("[SocketIO]", "Connection success"))
				.on("socketID", args -> {
					JSONObject data = (JSONObject) args[0];
					try {
						clientID = data.getString("id");
						Gdx.app.log("[SocketIO]", "Client Id: " + clientID);
					} catch (JSONException e) {
						Gdx.app.log("[SocketIO]", "Error getting client ID " + e);
					}
				}).on("newPlayer", args -> {
					JSONObject data = (JSONObject) args[0];

					try {
						String clientID = data.getString("id");
						GameState.instance.createNewPlayer(clientID);
						Gdx.app.log("[SocketIO]", "New Player connected");
					} catch (JSONException e) {
						Gdx.app.log("[SocketIO]", "Error connecting new player: " + e);
					}
				}).on("getPlayers", args -> {

					JSONArray objects = (JSONArray) args[0];
					try {
						for (int i = 0; i < objects.length(); i++) {
							GameState.instance.createNewPlayer(objects.getJSONObject(i).getString("id"));
							Gdx.app.log("SocketIO", "Players connected");
						}
					} catch (JSONException e) {
						Gdx.app.log("[SocketIO]", "Error getting players");
					}
				}).on("playerMoved", args -> {
					//TODO anti-cheat system
					//Send keys pressed instead of position. Reduces packets sent
					JSONObject data = (JSONObject) args[0];
					System.out.println("playerMoved EVENT");
					try {
						String id = data.getString("id");
						double x = data.getDouble("x");
						double y = data.getDouble("y");
						GameState.instance.moveClients(id, (float) x, (float) y);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}).on("playerRotation", args -> {
					
				}).on("bulletFired", args ->{
					
				}).on("playerDisconnected", args -> {
					
				}).on("sendPlayerHealth", args ->{
					
				}).on("recievePlayerHealth", args ->{
					
				});
		// TODO "playerDisconnected" event
	}

	Vector2 previous_pos = new Vector2();
	private final float update_interval = 1 / 30f;
	float timer;

	public void updateServer(float delta) {
		timer += delta;
		if (timer >= update_interval && has_moved() && GameState.instance.player != null) {
			JSONObject data = new JSONObject();
			try {
				data.put("x", GameState.instance.player.getPos(false).x);
				data.put("y", GameState.instance.player.getPos(false).y);
				data.put("id", clientID);
				clientSocket.emit("playerMoved", data);
			} catch (JSONException e) {
				Gdx.app.log("[SocketIO]", "Error sending player data");
			}
		}
	}

	public boolean has_moved() {
		if (previous_pos.x != GameState.instance.player.getPos(false).x || previous_pos.y != GameState.instance.player.getPos(false).y) {
			previous_pos.x = GameState.instance.player.getPos(false).x;
			previous_pos.y = GameState.instance.player.getPos(false).y;
			return true;
		}
		return false;
	}
}
