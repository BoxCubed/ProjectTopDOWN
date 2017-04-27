package me.boxcubed.main.desktop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.KyroPlayer;
import com.boxcubed.net.Multiplayer_Player;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.net.StringPacket;
import com.boxcubed.net.packets.InputPacket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.boxcubed.main.Objects.collision.MapBodyBuilder;

public class KyroServer extends Thread {
	Server kServer;
	ArrayList<KyroPlayer> players;
	public static World world=new World(new Vector2(0, 0), true);
	public static KyroServer instance;
	public boolean stop=false;
	
	GsonBuilder gsonBuilder;
	TiledMap map;


	public KyroServer() {
		instance=this;
		start();
		
		
	}
	public static void main(String[] args){
		new KyroServer();
	}
	@Override
	public void run() {
		//The time between the last request they sent to server
		long startLoop=0,endLoop=0,delta=1,elapsedTime=0;
		float sleep=0.0001f;
		//hints.connectTimeout=1000;
		ConsoleThread inCon = null;
		Gson gson = null;
		
		

		try{
		log("Starting Project Top Down Multiplayer Server...");
		
		gson= new Gson();
		
		Gdx.files=new LwjglFiles();
		players=new ArrayList<>();
		map=new ServerTiledMapLoader().load("assets/maps/map2.tmx");
		MapBodyBuilder.buildShapes(map, 1, world);
		//TODO make collision class for multiplayer
				//world.setContactListener(new CollisionDetection());
				
		kServer=new Server();
		
		new Thread(()->{while(true)
			try {
				kServer.update(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}).start();
		kServer.bind(22222, 22222);
		
		
		Kryo kryo = kServer.getKryo();
		Log.set(Log.LEVEL_INFO);
	    NetworkManager.initKryo(kryo);
		addServerListeners();
		
		inCon=new ConsoleThread();
		
		log("\n---------------------------------------------"
			+ "\n Project Top Down Multiplayer Experience "
			+ "\n Brought to you by Box Cubed "
		  + "\n---------------------------------------------");
		}catch(Exception e){logError("Failed in creating server!:"+e.getMessage());stop=true;}
			
		
		while(!stop){
			
			try{
				startLoop=System.nanoTime();
				//Checking connections for both Players
		
						
				if(!players.isEmpty())
				for(int i=0;i<players.size();i++){
					KyroPlayer player=players.get(0);
					try{
						String packetData="";
						
						if(player.player==null)
							player.player=new Multiplayer_Player(world, p->world.destroyBody(p.getBody()));
						if(!player.connected)
							throw new SocketException();
					player.loc=player.player.getPos().cpy();
					player.rotation=player.player.rotation;
					players.remove(player);
					player.connection.sendUDP(new StringPacket(packetData));
					players.add(player);
					player.player.update(10);
					//System.out.println(jsonMaker.prettyPrint(playerData));
					/*InputPacket in=(InputPacket)player.in.readObject();
					player.player.processCommand(in);
					player.player.update(delta);*/
					
					
					
					
					}catch(SocketException e){log(player.name+" has disconnected: "+player.reason);
					player.player.dispose();
					players.remove(player); }
				}
				endLoop=System.nanoTime();	
				delta=endLoop-startLoop;
				elapsedTime+=delta;
				world.step((float)delta/1000000000f, 10, 5);
				String con=inCon.lastOutput;
				String[] conSplit=con.split(" ");
				switch(conSplit[0]){
				case "teleport":
					try{
						
								players.get(Integer.parseInt(conSplit[1])-1)
								.player.getBody().setTransform(new Vector2(Float.parseFloat(conSplit[2]), Float.parseFloat(conSplit[3])),
										players.get(Integer.parseInt(conSplit[1])-1).player.getBody().getTransform().getRotation());
								log("Teleported Player to given coords"); 
								
							
						
					}catch(Exception e){log("Incorrect usage! 'teleport x y player'");}
					break;
				case "stop":
					stop=true;
					break;
				case "":break;
				case "tps":
					if(conSplit.length==1)
					log("delta: "+(float)delta/1000000000f+" sleep: "+sleep+" Etime: "+elapsedTime);
					else {sleep=Float.parseFloat(conSplit[1]); log("sleep time changed");}
					break;
				case "list":
					log("There are "+players.size()+" player/s online");
					break;
				default:
					log("That isn't an option");
				}
				if(inCon.lastOutput!="")
				inCon.lastOutput="";
				
				
				//log(Long.toString(delta));
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
					
			
			
			
			
		
			
			}catch (Exception e){logError("Error occured: "+e.getMessage()); e.printStackTrace();}
			
	}
		log("Server Shutting Down...");
		try{players.forEach(player->{try {
			player.connection.sendTCP("disconnect:Server has Shut Down");
			player.connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}player.player.dispose();});
		players.clear();
		world.dispose();
			if(inCon!=null)
			inCon.stop=true;
			
			System.exit(0);
			
		}catch(Exception e){logError("Failed in shutting down!:"+e.getMessage());}
	}

private void addServerListeners() {
	kServer.addListener(new Listener(){
		@Override
		public void connected(Connection connection) {
			KyroPlayer player=new KyroPlayer(connection, Double.toString(Math.random()), 
					 new Vector2());
			PlayerListener lis=new PlayerListener(player);
			connection.addListener(lis);
			
			
			
			players.add(player);
		}
		@Override
		public void disconnected(Connection connection) {
		}@Override
		public void idle(Connection connection) {
			// TODO Auto-generated method stub
			super.idle(connection);
		}
		@Override
		public void received(Connection connection, Object object) {
			// TODO Auto-generated method stub
			super.received(connection, object);
		}
	});
		
	}
private void log(String s){
	/*Gdx.app.setLogLevel(Application.LOG_INFO);
	Gdx.app.log("[PTDM Server]", s);*/
	System.out.println("[PTDM Server]: "+s);
	
	
}
private void logError(String s){
	/*Gdx.app.setLogLevel(Application.LOG_ERROR);
	Gdx.app.log("![PTDM Server]", s);*/
	System.err.println("![PTDM Server]!: "+s);
	
	
	
}

class ConsoleThread extends Thread{
	BufferedReader in;
	public String lastOutput="";
	public boolean stop=false;
	public ConsoleThread(){
		in=new BufferedReader(new InputStreamReader(System.in));
		start();
		
	}
	@Override
	public void run() {
		while(!stop)
	try {
		lastOutput=in.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
class PlayerListener extends Listener{
	boolean gotName=false;
	KyroPlayer p;
	public PlayerListener(KyroPlayer p) {
		this.p=p;
	}
	
	@Override
	public void received(Connection connection, Object ob) {
		if(ob instanceof String){
			if(gotName==false){
				p.name=(String)ob;
				gotName=true;
				log((String)ob+" has joined");
				
			}
		}
		else if(ob instanceof InputPacket){
			p.player.command=(InputPacket)ob;
		}
			
		
			
	}
	@Override
	public void connected(Connection connection) {
		log("Player attempting connection...");
	}
	
	@Override
	public void disconnected(Connection connection) {
		if(gotName==false)
			log("Player failed connection");
		p.connected=false;
	}
	@Override
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
	
	
}


}
