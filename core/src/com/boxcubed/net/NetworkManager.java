package com.boxcubed.net;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.packets.BulletFirePacket;
import com.boxcubed.net.packets.InputPacket;
import com.boxcubed.net.packets.LocalPlayerPosPacket;
import com.boxcubed.net.packets.ParticlePacket;
import com.boxcubed.net.packets.PlayerDisconnectPacket;
import com.boxcubed.net.packets.PlayerUpdatePacket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import me.boxcubed.main.Sprites.Bullet;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class NetworkManager extends Thread {
	Client connection;
	public boolean stop = false;
	public float rotation = 0;
	Player player;
	public String ip,name="BoxCubed",disconnectReason="Disconnected";
	public ConnectionState state;
	public byte w,a,s,d,shift,space;
	public Map<Integer,Player> multiplayerPlayers;
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
		k.register(Vector2.class);
		k.register(InputPacket.class);
		k.register(LocalPlayerPosPacket.class);
		k.register(PlayerUpdatePacket.class);
		k.register(PlayerDisconnectPacket.class);
		k.register(ParticlePacket.class);
		k.register(BulletFirePacket.class);
		
		
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
			multiplayerPlayers=new HashMap<>();
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

		//player.setConnection(this, 1);


		while (!stop) {

			try {
				if(!player.isDisposable())
				connection.sendUDP(new PlayerUpdatePacket(player.rotation, player.getPos(),connection.getID()));
				
				
				

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
				PlayerUpdatePacket packet=(PlayerUpdatePacket)ob;
				if(packet.id!=connection.getID()&&multiplayerPlayers.get(packet.id)!=null)
					updatePositionFromPacket(connection.getID(), (PlayerUpdatePacket)ob);
				else addPlayer(connection.getID(),(PlayerUpdatePacket)ob);
				
				
			}
			else if (ob instanceof PlayerDisconnectPacket){
				if(((PlayerDisconnectPacket) ob).id==connection.getID()){
					disconnectReason=((PlayerDisconnectPacket) ob).reason;
				}else 
				remPlayer(((PlayerDisconnectPacket) ob).id);
			}
			else if(ob instanceof BulletFirePacket)
				pendingFire.add((BulletFirePacket)ob);
				
			
			
		}
		@Override
		public void disconnected(Connection connection) {
			Gdx.app.log("[Client]", "lost connection: "+disconnectReason);
			multiplayerPlayers.forEach((id,p)->p.dispose());
			multiplayerPlayers.clear();
		}
		
	}
	private LinkedBlockingQueue<BulletFirePacket> pendingFire=new LinkedBlockingQueue<>();
	private List<Integer>dispose=new ArrayList<>();
	private World world=GameState.instance.getWorld();
	public synchronized void updatePlayers(float delta) {
		multiplayerPlayers.forEach((id,p)->{
			if(p.isDisposable()){
				p.dispose();
				dispose.add(id);
			}
			else p.update(delta);
		});
		dispose.forEach(multiplayerPlayers::remove);
		dispose.clear();
		if(!pendingFire.isEmpty()){
			BulletFirePacket bullet;
			try {
				bullet = pendingFire.take();
				GameState.instance.entities.add(new Bullet(world, bullet.location.x, bullet.location.y, (float) (Math.cos(Math.toRadians(bullet.rotation))), 
						(float) (Math.sin(Math.toRadians(bullet.rotation)))));//TODO bullet types
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		
	}
	public synchronized void renderPlayers(SpriteBatch sb){
		multiplayerPlayers.forEach((id,p)->
			p.render(sb));
	}
    private synchronized void addPlayer(int id,PlayerUpdatePacket ob) {
    	Player p=new Player(GameState.instance.getWorld(), 2);
    	multiplayerPlayers.put(id,p);
    	p.name=ob.name;
		
		
	}
    private  synchronized void remPlayer(int id) {
    	if(multiplayerPlayers.get(id)!=null)
    		multiplayerPlayers.get(id).setDisposable(true);
    	
    	
    	
		
		
	}
    private void updatePositionFromPacket(int id,PlayerUpdatePacket packet){
    	Player p=multiplayerPlayers.get(id);
    	p.multiPos=packet.location.cpy();
    	p.rotation=packet.rotation;
    	p.name=packet.name;
    	p.setHealth(packet.health);
    }
}
