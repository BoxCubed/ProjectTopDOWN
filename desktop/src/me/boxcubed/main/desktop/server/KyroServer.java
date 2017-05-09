package me.boxcubed.main.desktop.server;

import java.io.BufferedReader;import java.io.IOException;import java.io.InputStreamReader;import java.util.ArrayList;import java.util.HashMap;import java.util.Iterator;import java.util.List;import com.badlogic.gdx.Gdx;import com.badlogic.gdx.backends.lwjgl.LwjglApplication;import com.badlogic.gdx.backends.lwjgl.LwjglFiles;import com.badlogic.gdx.maps.tiled.TiledMap;import com.badlogic.gdx.math.Vector2;import com.badlogic.gdx.physics.box2d.World;import com.boxcubed.net.NetworkManager;import com.boxcubed.net.packets.BulletFirePacket;import com.boxcubed.net.packets.InputPacket;import com.boxcubed.net.packets.LocalPlayerPosPacket;import com.boxcubed.net.packets.PlayerDisconnectPacket;import com.boxcubed.net.packets.PlayerUpdatePacket;import com.esotericsoftware.kryonet.Connection;import com.esotericsoftware.kryonet.Listener;import com.esotericsoftware.kryonet.Server;import com.esotericsoftware.minlog.Log;import me.boxcubed.main.Objects.collision.MapBodyBuilder;import me.boxcubed.main.Objects.interfaces.Entity;

public class KyroServer extends Thread {
	Server kServer;
	protected HashMap<Integer,KyroPlayer> players;	private List<Entity> entities;
	public static World world=new World(new Vector2(0, 0), true);
	public static KyroServer instance;	private LwjglApplication app;
	public boolean stop=false;//TODO set packet limiter 
	

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

		//hints.connectTimeout=1000;
		ConsoleThread inCon = null;

		
		

		try{
		log("Starting Project Top Down Multiplayer Server...");
		
		app=new LwjglApplication(new ServerVisualiser(world,()->{			stop=true;			try {				join();			} catch (InterruptedException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}		}));				
		
		Gdx.files=new LwjglFiles();
		players=new HashMap<>();		entities=new ArrayList<>();
		map=new ServerTiledMapLoader().load("assets/maps/map2.tmx");
		MapBodyBuilder.buildShapes(map, 1, world);
		//TODO make collision class for multiplayer
				//world.setContactListener(new CollisionDetection());
				
		kServer=new Server();
		
		kServer.start();
		kServer.bind(22222, 22222);
		 
		

		Log.set(Log.LEVEL_INFO);
	    NetworkManager.initKryo(kServer.getKryo());
		addServerListeners();
		
		inCon=new ConsoleThread();
		
		log("\n---------------------------------------------"
			+ "\n Project Top Down Multiplayer Experience "
			+ "\n Brought to you by Box Cubed "
		  + "\n---------------------------------------------");
		}catch(Exception e){logError("Failed in creating server!:"+e.getMessage());stop=true;}
			
		
		while(!stop){
			//Game loop workings
			try{
				startLoop=System.currentTimeMillis();				
				players.forEach(this::playPlayer);				updateEntities();				//process command 
				String con=inCon.lastOutput;
				String[] conSplit=con.split(" ");
				switch(conSplit[0]){
				case "teleport":
					try{
						
								players.get(Integer.parseInt(conSplit[1]))
								.player.getBody().setTransform(new Vector2(Float.parseFloat(conSplit[2]), Float.parseFloat(conSplit[3])),
										players.get(Integer.parseInt(conSplit[1])).player.getBody().getTransform().getRotation());								kServer.sendToUDP(Integer.parseInt(conSplit[1]), new LocalPlayerPosPacket(new Vector2(Float.parseFloat(conSplit[2]), Float.parseFloat(conSplit[3]))));
								log("Teleported Player to given coords"); 
								
							
						
					}catch(Exception e){log("Incorrect usage! 'teleport x y player'");}
					break;
				case "stop":
					app.exit();;
					break;
				case "":break;
				case "tps":

					log("delta: "+(float)delta +" Etime: "+elapsedTime);

					break;
				case "list":
					log("There are "+players.size()+" player/s online");
					break;				case "kick":					//TODO					log("WIP");					break;
				default:
					log("That isn't an option");
				}
				if(inCon.lastOutput!="")
				inCon.lastOutput="";
								endLoop=System.currentTimeMillis();					delta=endLoop-startLoop;				elapsedTime+=delta;				updateWorld();				
				Thread.sleep(10);
				//log(Long.toString(delta));
			}catch (Exception e){logError("Error occured: "+e.getMessage()); e.printStackTrace();}
			
	}
		log("Server Shutting Down...");
		try{players.forEach((id,player)->{player.player.dispose();kServer.sendToTCP(id, new PlayerDisconnectPacket(player.name, id, "Server has shut down"));});
		players.clear();
		world.dispose();
			if(inCon!=null)
			inCon.stop=true;
			
			
		}catch(Exception e){logError("Failed in shutting down!:"+e.getMessage());}
	}
private synchronized void updateWorld(){	world.step(10f/1000f, 10, 5);}private synchronized void updatePlayer(InputPacket packet,Multiplayer_Player player) {	if(player==null)		return;	player.command=packet;	player.rotation=packet.rotation;}private synchronized void removePlayer(KyroPlayer p,Connection connection,String reason){	players.remove(connection.getID());	kServer.sendToAllExceptTCP(connection.getID(),new PlayerDisconnectPacket(p.name, connection.getID(), ""));	connection.sendTCP(new PlayerDisconnectPacket(p.name, connection.getID(), "Disconnected"));	p.player.dispose();}private synchronized void playPlayer(int id,KyroPlayer player){	if(player.player==null)		player.player=new Multiplayer_Player(world);	else{		player.player.update(1);			kServer.sendToAllUDP(new PlayerUpdatePacket(player.player.rotation, player.player.getPos(), id,player.player.getHealth(),player.name));				}}private synchronized void updateEntities(){	Iterator<Entity> it=entities.iterator();	while(it.hasNext()){		Entity e=it.next();		if(e.isDisposable()){			e.dispose();			it.remove();			continue;		}		e.update(1);				}}private synchronized void addEntity(Entity entity){	entities.add(entity);	}
private void addServerListeners() {
	kServer.addListener(new Listener(){
		@Override
		public void connected(Connection connection) {
			
			PlayerListener lis=new PlayerListener();
			connection.addListener(lis);
			
			
			

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
	boolean gotName=false;	int failedNameRecieve;
	public KyroPlayer p;		  public PlayerListener() {	}
		@Override
	public void received(Connection connection, Object ob) {		if(failedNameRecieve==10){			connection.sendTCP(new PlayerDisconnectPacket("", connection.getID(), "Failed to send name!"));			connection.close();}
		if(ob instanceof String){
			if(gotName==false&&((String)ob).startsWith("name:")){				gotName=true;				p=new KyroPlayer(connection, Double.toString(Math.random()));				p.name=((String)ob).split(":")[1].trim();				p.id=connection.getID();				players.put(connection.getID(), p);								
				
				log(p.name+" has joined");
				
			}
		}		if(gotName==false){log("Player attempted to send packet without name: "+connection.getID());failedNameRecieve++;return;}		else if(ob instanceof InputPacket){			InputPacket packet=(InputPacket)ob;						updatePlayer(packet,p.player);								}		else if(ob instanceof BulletFirePacket){			BulletFirePacket packet=(BulletFirePacket)ob;			kServer.sendToAllExceptTCP(connection.getID(), new BulletFirePacket(p.player.getPos(), packet.rotation,packet.type));			addEntity(new NetworkBullet(world, p.player.getPos().x, p.player.getPos().y, (float)Math.cos(Math.toRadians(packet.rotation)), 					(float)Math.sin(Math.toRadians(packet.rotation)), packet.rotation, KyroServer.this));		}
		
			
		
			
	}
		@Override
	public void connected(Connection connection) {
		log("Player attempting connection...");
	}
	
	@Override
	public void disconnected(Connection connection) {
		if(gotName==false)
			log("Player failed connection");		log(p.name+" has disconnected");		
		p.connected=false;				removePlayer(p,connection,"Disconnected");
	}
	@Override
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
	
	
}


}
