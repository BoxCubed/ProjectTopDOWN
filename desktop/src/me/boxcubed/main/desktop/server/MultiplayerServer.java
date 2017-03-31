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
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.DataPacket;
import com.boxcubed.net.InputPacket;
import com.boxcubed.net.Multiplayer_Player;
import com.boxcubed.net.SocketPlayer;

import me.boxcubed.main.Objects.collision.MapBodyBuilder;
@Deprecated
public class MultiplayerServer extends Thread {
	ServerSocket server;
	List<SocketPlayer> players;
	public World world=new World(new Vector2(0, 0), true);
	public static MultiplayerServer instance;
	public boolean stop=false;
	TiledMap map;
	

	public MultiplayerServer() {
		instance=this;
		Gdx.files=new LwjglFiles();
		players=new ArrayList<>();
		/*ServerSocketHints hints=new ServerSocketHints();
		hints.acceptTimeout=0;*/
		map=new ServerTiledMapLoader().load("assets/maps/map2.tmx");
		MapBodyBuilder.buildShapes(map, 1, world);
	
		try {
			server=new ServerSocket(22222, 2);
		} catch (IOException e) {
			logError("Failed to create server!:"+e.getMessage());
			return;
		}
		
		
		
		//TODO make collision class for multiplayer
		//world.setContactListener(new CollisionDetection());
		
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
		
		ConsoleThread inCon=new ConsoleThread();
		JoinThread joinThread=new JoinThread(world);
		 

		try{
		//Connection of players
		log("Starting Project Top Down Multiplayer Server...");
		/*log("Waiting for Player 1...");
		player1=server.accept();
		
		p1outob=new ObjectOutputStream(player1.getOutputStream());
		p1inob=new ObjectInputStream(player1.getInputStream());
		p1Delay=System.currentTimeMillis();
		log("Player one successfully connected!\nWaiting for Player 2...");
		player2=server.accept();

		
		p2outob=new ObjectOutputStream(player2.getOutputStream());
		p2inob=new ObjectInputStream(player2.getInputStream());
		p2Delay=System.currentTimeMillis();
		log("Player 2 successfully connected!");*/
		
		
		//assigning i/o
		
		
		
		//p1out = new PrintWriter(player1.getOutputStream(), true);
	    /*p1in = new BufferedReader(
	        new InputStreamReader(player1.getInputStream()));
	    
	    //p2out = new PrintWriter(player2.getOutputStream(), true);
	    p2in = new BufferedReader(
	        new InputStreamReader(player2.getInputStream()));*/
		log("\n---------------------------------------------"
			+ "\n Project Top Down Multiplayer Experience "
			+ "\n Brought to you by Box Cubed "
		  + "\n---------------------------------------------");
		}catch(Exception e){logError("Failed in creating server!:"+e.getMessage());e.printStackTrace();System.exit(0);}
			
		
		while(!stop){
			
			try{
				startLoop=System.currentTimeMillis();
				//Checking connections for both Players
				/*if(player1==null||player1.isClosed()){
					log("Player one lost connection! Halting until new player joins...");
					p2out.println("missP");
					player1=server.accept();
					log("Found new Player");
					p1Delay=System.currentTimeMillis();
					p2out.println("foundP");
					p1out = new PrintWriter(player1.getOutputStream(), true);
				    p1in = new BufferedReader(
				        new InputStreamReader(player1.getInputStream()));
				}
				if(player2==null||player2.isClosed()){
					log("Player two lost connection! Halting until new player joins...");
					p1out.println("missP");
					player2=server.accept();
					log("Found new Player");
					p2Delay=System.currentTimeMillis();
					p1out.println("foundP");
					  p2out = new PrintWriter(player2.getOutputStream(), true);
					    p2in = new BufferedReader(
					        new InputStreamReader(player2.getInputStream()));
					
				}*/
				//sending info to player
				//p1out.println(p1Char.getPos().x+":"+p1Char.getPos().y+":"+p2Char.getPos().x+":"+p2Char.getPos().y+":"+p2Char.rotation);
				/*try{
				p1outob.writeObject(new DataPacket(p1Char.getPos(), p2Char.getPos(), p2Char.rotation));
				p2outob.writeObject(new DataPacket(p2Char.getPos(), p1Char.getPos(), p1Char.rotation));
				p1outob.flush();
				p2outob.flush();}catch(SocketException e){Gdx.app.exit();}*/

				//p2out.println(p2Char.getPos().x+":"+p2Char.getPos().y+":"+p1Char.getPos().x+":"+p1Char.getPos().y+":"+p1Char.rotation);
				
				//Processing Movement 
					/*String mess="",mess2="";
					p1Delta=System.currentTimeMillis()-p1Delay;
					try{
						
					mess=p1in.readLine();
					mess2=p2in.readLine();
					
					}catch(Exception e){player1.close();continue;}
					//System.out.println(mess);
					
					if(mess.startsWith("mov")){
						p1Char.processCommand(mess.replaceFirst("mov:", ""));p1Delay=System.currentTimeMillis();}
					else if(mess.startsWith("disconnect")){
						player1.close();
						continue;
					}
					if(mess2.startsWith("mov")){
						p2Char.processCommand(mess2.replaceFirst("mov:", ""));p2Delay=System.currentTimeMillis();}
					else if(mess2.startsWith("disconnect")){
						player2.close();
						continue;
					}*/
						
				for(int i=0;i<players.size();i++){
					try{
					SocketPlayer player=players.get(i);
					players.get(i).x=player.player.getPos().x;
					players.get(i).y=player.player.getPos().y;
					players.get(i).rotation=player.player.rotation;
					players.remove(i);
					players.add(player);
					player.out.writeObject(new DataPacket(player.player.getPos(), players));
					InputPacket in=(InputPacket)player.in.readObject();
					player.player.processCommand(in);
					player.player.update(delta);
					
					
					
					
					}catch(ClassNotFoundException e){
						logError("FATAL ERROR: Missing Files: "+e.getMessage());Gdx.app.exit();}
					catch(SocketException |SocketTimeoutException e){log("Player Disconnected: "+e.getMessage());players.remove(i);}
				}
					 /*try{
						 
						 InputPacket in1=(InputPacket)p1inob.readObject(),in2=(InputPacket)p2inob.readObject();
						 p1Char.processCommand(in1);
						 p2Char.processCommand(in2);
					 }catch(Exception e){e.printStackTrace();}
					 */
				
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
				
				world.step(1f, 10, 5);
				//log(Long.toString(delta));
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				endLoop=System.currentTimeMillis();	
				delta=endLoop-startLoop;
			if(delta<10)	
			Thread.sleep(sleep-delta);
			
			
			
		
			
			}catch (InterruptedException | IOException e){logError("Error occured: "+e.getMessage()); e.printStackTrace();}
			
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
	

			inCon.stop=true;
			server.close();
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
							new ObjectInputStream(socket.getInputStream()), new Multiplayer_Player(wworld),new Vector2()));
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
