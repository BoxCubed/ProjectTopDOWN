package me.boxcubed.main.desktop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.Multiplayer_Player;

public class MultiplayerServer extends Thread {
	ServerSocket server;
	Socket player1,player2;
	public World world=new World(new Vector2(0, 0), true);
	public static MultiplayerServer instance;
	public Multiplayer_Player p1Char=new Multiplayer_Player(world),p2Char=new Multiplayer_Player(world);
	public boolean stop=false;

	public MultiplayerServer() {
		instance=this;
		ServerSocketHints hints=new ServerSocketHints();
		hints.acceptTimeout=0;
	
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
		long p1Delta=0,p2Delta=0,p1Delay=0,p2Delay=0,lastRun=System.currentTimeMillis(),delta;
		//hints.connectTimeout=1000;
		PrintWriter p1out=null;
		PrintWriter p2out=null;
		BufferedReader p1in=null;
		BufferedReader p2in=null;
		ConsoleThread inCon=new ConsoleThread();

		try{
		//Connection of players
		log("Starting Project Top Down Multiplayer Server...");
		log("Waiting for Player 1...");
		player1=server.accept();
		p1Delay=System.currentTimeMillis();
		log("Player one successfully connected!\nWaiting for Player 2...");
		//player2=server.accept();
		p2Delay=System.currentTimeMillis();
		log("Player 2 successfully connected!");
		
		
		//assigning i/o
		p1out = new PrintWriter(player1.getOutputStream(), true);
	    p1in = new BufferedReader(
	        new InputStreamReader(player1.getInputStream()));
	    /*p2out = new PrintWriter(player2.getOutputStream(), true);
	    p2in = new BufferedReader(
	        new InputStreamReader(player2.getInputStream()));*/
		log("\n---------------------------------------------"
			+ "\n Project Top Down Multiplayer Experience "
			+ "\n Brought to you by Box Cubed "
		  + "\n---------------------------------------------");
		}catch(IOException e){logError("Failed in creating server!:"+e.getMessage());stop=true;}
			
		
		while(!stop){
			try{
				//Checking connections for both Players
				if(player1==null||player1.isClosed()){
					log("Player one lost connection! Halting until new player joins...");
					//p2out.println("missP");
					player1=server.accept();
					log("Found new Player");
					p1Delay=System.currentTimeMillis();
					//p2out.println("foundP");
					p1out = new PrintWriter(player1.getOutputStream(), true);
				    p1in = new BufferedReader(
				        new InputStreamReader(player1.getInputStream()));
				}
				/*if(player2==null||player2.isInputShutdown()){
					log("Player two lost connection! Halting until new player joins...");
					p1out.println("missP");
					player1=server.accept();
					log("Found new Player");
					p2Delay=System.currentTimeMillis();
					p1out.println("foundP");
					  p2out = new PrintWriter(player2.getOutputStream(), true);
					    p2in = new BufferedReader(
					        new InputStreamReader(player2.getInputStream()));
					
				}*/
				//sending info to player
				delta=System.currentTimeMillis()-lastRun;
				
				p1out.println(p1Char.getPos().x+":"+p1Char.getPos().y+":"+p2Char.getPos().x+":"+p2Char.getPos().y);
				/*p2out.print("trans:"+p2Char.getPos().x+":"+p2Char.getPos().y);
				p2out.print("trans2:"+p1Char.getPos().x+":"+p1Char.getPos().y);
				p2out.println();*/
				//Processing Movement 
				p1Char.update(p1Delta);
				p2Delta=System.currentTimeMillis()-p2Delay;
					String mess="";
					p1Delta=System.currentTimeMillis()-p1Delay;
					try{
						
					mess=p1in.readLine();
					
					}catch(Exception e){player1.close();continue;}
					//System.out.println(mess);
					
					if(mess.startsWith("mov"))
						p1Char.processCommand(mess.replaceFirst("mov:", ""));
					else if(mess.startsWith("disconnect")){
						player1.close();
						continue;
					}
						
					 p1Delay=System.currentTimeMillis();
				
				String con=inCon.lastOutput;
				if(con.startsWith("teleport")){
					p1Char.getBody().setTransform(new Vector2(Float.parseFloat(con.split(":")[1]), Float.parseFloat(con.split(":")[2])), 0);
					log("Teleported player 1 to given cords");
					inCon.lastOutput="";
				
				}
				Thread.sleep(10);
				world.step(0.1f, 10, 5);
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				p2Delay=System.currentTimeMillis();
				
				
				
				
			
			}catch (Exception e){logError("Error occured: "+e.getMessage()); e.printStackTrace();}
			
	}
		log("Server Shutting Down...");
		try{
		player1.close();
		player2.close();;
		p1Char.dispose();
		p2Char.dispose();
		server.close();
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
}
