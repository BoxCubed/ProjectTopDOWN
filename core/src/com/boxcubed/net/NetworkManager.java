package com.boxcubed.net;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.boxcubed.net.packets.InputPacket;
import com.boxcubed.net.packets.LocalPlayerPosPacket;
import com.boxcubed.net.packets.PlayerDisconnectPacket;
import com.boxcubed.net.packets.PlayerUpdatePacket;
import com.boxcubed.utils.BoxoUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.gson.Gson;

import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class NetworkManager extends Thread {
	Client connection;
	public boolean stop = false;
	public float rotation = 0;
	Player player;
	public String ip,name="BoxCubed";
	public ConnectionState state;
	public byte w,a,s,d,shift,space;
	public NetworkManager(Player player) {
		this(player, "localhost:22222");

	}

	public NetworkManager(Player player, String ip) {
		this.player = player;
		state = ConnectionState.CONNECTING;
		this.ip = ip;

		// TODO VERY Temporary
		// GameState.instance.multiplayerPlayers.add(new
		// Player(player.getBody().getWorld(), 2));
		// player2=GameState.instance.multiplayerPlayers.get(0);

		start();

	}
	public static void initKryo(Kryo k){
		k.register(String.class);
		k.register(InputPacket.class);
		k.register(LocalPlayerPosPacket.class);
		k.register(PlayerUpdatePacket.class);
		k.register(PlayerDisconnectPacket.class);
	}
	@Override
	public void run() {

		Gdx.app.log("[Client]", "Client Thread started.");
		try {
			connection = new Client();
			connection.start();
			initKryo(connection.getKryo());
			connection.addListener(new PlayerListener());
			connection.connect(3000, InetAddress.getByName(ip.split(":")[0]), Integer.parseInt(ip.split(":")[1]),
					Integer.parseInt(ip.split(":")[1]));

			connection.sendTCP("name:"+name);

		} catch (Exception e) {
			System.out.println("Failed connecting to server: " + e.getMessage());
			state = ConnectionState.INVALIDIP;
			
			try {
				connection.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			return;
		}
		state = ConnectionState.CONNECTED;

		player.setConnection(this, 1);


		while (!stop) {

			try {
				connection.sendUDP(new PlayerUpdatePacket(player.rotation, player.getPos()));
				
				

			} catch (Exception e) {
				System.err.println("Error occured on Client : " + e.getMessage());
				e.printStackTrace();
			}
			

		}

		Gdx.app.log("[Client]", "Shutting Down...");
		connection.stop();
		
		connection.close();
		try {
			connection.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public enum ConnectionState {
		CONNECTED, INVALIDIP, DISCONNECTED, CONNECTING
	}

	class PlayerListener extends Listener {
		@Override
		public void received(Connection connection, Object ob) {
			if(ob instanceof LocalPlayerPosPacket){
				player.multiPos=((LocalPlayerPosPacket)ob).pos.cpy();
			}
			else if (ob instanceof PlayerUpdatePacket){
				
			}
			
		}
	}
}
