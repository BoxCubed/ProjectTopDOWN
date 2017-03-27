package com.boxcubed.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.collision.CollisionDetection;

public class MultiplayerServer extends Thread{
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
	
		server=Gdx.net.newServerSocket(Protocol.TCP, 22222, hints);
		
		
		
		//TODO make collision class for multiplayer
		world.setContactListener(new CollisionDetection());
		
		start();
		
		
	}
	@Override
	public void run() {
		SocketHints hints=new SocketHints();
		//The time between the last request they sent to server
		float p1Delta=0,p2Delta=0,p1Delay,p2Delay,lastRun=System.currentTimeMillis(),delta;
		//hints.connectTimeout=1000;
		hints.socketTimeout=100;
		
		//Connection of players
		log("Starting Project Top Down Multiplayer Server...");
		log("Waiting for Player 1...");
		player1=server.accept(hints);
		p1Delay=System.currentTimeMillis();
		log("Player one successfully connected!\nWaiting for Player 2...");
		player2=server.accept(hints);
		p2Delay=System.currentTimeMillis();
		log("Player 2 successfully connected!");
		
		
		//assigning i/o
		PrintWriter p1out = new PrintWriter(player1.getOutputStream(), true);
	    BufferedReader p1in = new BufferedReader(
	        new InputStreamReader(player1.getInputStream()));
	    PrintWriter p2out = new PrintWriter(player2.getOutputStream(), true);
	    BufferedReader p2in = new BufferedReader(
	        new InputStreamReader(player2.getInputStream()));
		log("\n---------------------------------------------"
			+ "\n Project Top Down Multiplayer Experience "
			+ "\n Brought to you by Box Cubed "
		  + "\n---------------------------------------------");
		
		while(!stop){
			try{
				//Checking connections for both Players
				if(player1==null||!player1.isConnected()){
					log("Player one lost connection! Halting until new player joins...");
					p2out.println("missP");
					player1=server.accept(hints);
					log("Found new Player");
					p1Delay=System.currentTimeMillis();
					p2out.println("foundP");
					p1out = new PrintWriter(player1.getOutputStream(), true);
				    p1in = new BufferedReader(
				        new InputStreamReader(player1.getInputStream()));
				}
				if(player2==null||!player2.isConnected()){
					log("Player two lost connection! Halting until new player joins...");
					p1out.println("missP");
					player1=server.accept(hints);
					log("Found new Player");
					p2Delay=System.currentTimeMillis();
					p1out.println("foundP");
					  p2out = new PrintWriter(player2.getOutputStream(), true);
					    p2in = new BufferedReader(
					        new InputStreamReader(player2.getInputStream()));
					
				}
				
				delta=System.currentTimeMillis()-lastRun;
				
				//Processing Movement 
				p1Char.update(p1Delta);
				p2Delta=System.currentTimeMillis()-p2Delay;
				
				if(p1in.ready()){
					p1Delta=System.currentTimeMillis()-p1Delay;
					String mess=p1in.readLine();
					if(mess.startsWith("mov"))
						p1Char.processCommand(mess.split(":")[1]);
					else if(mess.startsWith("disconnect")){
						player1.dispose();
						continue;
					}
						
						
					
					
					
					
					
					
					
					
					
					p1Delay=System.currentTimeMillis();
				}
				
				
				world.step(delta, 10, 5);
				
				p1out.print("trans:"+p1Char.getPos().x+":"+p1Char.getPos().y+":delta:"+p1Delta);
				p1out.print(":trans2:"+p2Char.getPos().x+":"+p2Char.getPos().y);
				p1out.println();
				/*p2out.print("trans:"+p2Char.getPos().x+":"+p2Char.getPos().y);
				p2out.print("trans2:"+p1Char.getPos().x+":"+p1Char.getPos().y);
				p2out.println();*/
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				p2Delay=System.currentTimeMillis();
				
				
				
				
			
			}catch (Exception e){logError("Error occured: "+e.getMessage());}
			
	}
		Gdx.app.log("[Server]", "Server Shutting Down...");
		player1.dispose();
		player2.dispose();
		server.dispose();
		
	}

private void log(String s){
	Gdx.app.setLogLevel(Application.LOG_INFO);
	Gdx.app.log("[PTDM Server]", s);
	
	
}
private void logError(String s){
	Gdx.app.setLogLevel(Application.LOG_ERROR);
	Gdx.app.log("![PTDM Server]", s);
	System.out.println(s);
	
	
}
}
