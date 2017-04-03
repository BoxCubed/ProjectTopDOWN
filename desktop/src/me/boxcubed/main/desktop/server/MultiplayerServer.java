package me.boxcubed.main.desktop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


import com.boxcubed.net.DataPacket;
import com.boxcubed.net.InputPacket;
import com.boxcubed.net.Multiplayer_Player;
import com.boxcubed.net.SocketPlayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import me.boxcubed.main.Objects.collision.MapBodyBuilder;

public class MultiplayerServer extends Thread {
	ServerSocket server;
	ArrayList<SocketPlayer> players;

	public static World world=new World(new Vector2(0, 0), true);

	public static MultiplayerServer instance;
	public boolean stop=false;
	
	GsonBuilder gsonBuilder;
	TiledMap map;
	


	public MultiplayerServer() {
		instance=this;

		start();
		
		
	}
	public static void main(String[] args){
		new MultiplayerServer();
	}
	@Override
	public void run() {
		//The time between the last request they sent to server
		long startLoop=0,endLoop=0,delta=1,sleep=10;
		//hints.connectTimeout=1000;

		ConsoleThread inCon = null;
		JoinThread joinThread = null;
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
				
		server=new ServerSocket(22222, 2);
		inCon=new ConsoleThread();
		joinThread=new JoinThread(world);
		

		log("\n---------------------------------------------"
			+ "\n Project Top Down Multiplayer Experience "
			+ "\n Brought to you by Box Cubed "
		  + "\n---------------------------------------------");

		}catch(Exception e){logError("Failed in creating server!:"+e.getMessage());stop=true;}

			
		
		while(!stop){
			
			try{
				startLoop=System.currentTimeMillis();
				//Checking connections for both Players
		
						
				
				for(int i=0;i<players.size();i++){
					SocketPlayer player=players.get(0);
					try{
					String playerData="";
					
					DataPacket packet;
					player.loc=player.player.getPos().cpy();
					player.rotation=player.player.rotation;
					players.remove(player);
					packet=new DataPacket(player.player.getPos(), players,i);
					

					playerData=gson.toJson(packet);

					players.add(player);
					//System.out.println(jsonMaker.prettyPrint(playerData));
					player.out.writeObject(playerData);
					InputPacket in=(InputPacket)player.in.readObject();
					player.player.processCommand(in);
					player.player.update(delta);
					
					
					
					
					}catch(ClassNotFoundException e){
						logError("FATAL ERROR: Missing Files: "+e.getMessage());Gdx.app.exit();}
					catch(SocketException |SocketTimeoutException e){log("Player Disconnected: "+e.getMessage());
					player.player.dispose();
					players.remove(player);}
				}
					
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
					log("delta: "+delta+" sleep: "+sleep);
					else {sleep=Long.parseLong(conSplit[1]); log("sleep time changed");}
					break;
				case "list":
					log("There are "+players.size()+" players online");
					break;
				default:
					log("That isn't an option");
				}
				inCon.lastOutput="";
				
				world.step(delta, 10, 5);
				//log(Long.toString(delta));
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				endLoop=System.currentTimeMillis();	
				delta=endLoop-startLoop;
			if(delta<10)	
			Thread.sleep(sleep-delta);
			
			
			
		
			
			}catch (Exception e){logError("Error occured: "+e.getMessage()); e.printStackTrace();}
			
	}
		log("Server Shutting Down...");
		try{players.forEach(player->{try {
			player.out.writeObject("disconnect:Server has Shut Down");
			player.out.flush();player.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}player.player.dispose();});
		players.clear();

		world.dispose();
			if(inCon!=null)
			inCon.stop=true;
			if(server!=null)
			server.close();
			if(joinThread!=null)

			joinThread.stop=true;
			System.exit(0);
			
		}catch(IOException e){logError("Failed in shutting down!:"+e.getMessage());}
		
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
class JoinThread extends Thread{
	boolean stop=false;
	World wworld;
	public JoinThread(World world){
		this.wworld=world;
		start();
	}
	@Override
	public void run() {
		while(!stop){
			Socket socket=null;
			try {
				socket=server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(!stop)
				e.printStackTrace();
				continue;
			}
			
			
				try {
					players.add(new SocketPlayer(socket,Double.toString(Math.random()), new ObjectOutputStream(socket.getOutputStream()), 
							new ObjectInputStream(socket.getInputStream()), new Multiplayer_Player(wworld,player->wworld.destroyBody(player.getBody())),new Vector2()));
					log("Player has joined");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logError("Player Failed to join: "+e.getMessage());
				}
			
				// TODO Auto-generated catch block
			
			
		}
		
	}
}


}
