package com.boxcubed.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class MultiplayerServer extends Thread{
	ServerSocket server;
	Socket player1,player2;
	public boolean stop=false;

	public MultiplayerServer() {
		ServerSocketHints hints=new ServerSocketHints();
		hints.acceptTimeout=0;
		server=Gdx.net.newServerSocket(Protocol.TCP, 200, hints);
		start();
		
		
	}
	@Override
	public void run() {
		SocketHints hints=new SocketHints();
		//The time between the last request they sent to server
		float p1Delta=0,p2Delta=0;
		//hints.connectTimeout=1000;
		hints.socketTimeout=1000;
		log("Starting Project Top Down Multiplayer Server...");
		log("Waiting for Player 1...");
		player1=server.accept(hints);
		log("Player one successfully connected!\nWaiting for Player 2...");
		player2=server.accept(hints);
		log("Player 2 successfully connected!");
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
					p1out.println("foundP");
					  p2out = new PrintWriter(player2.getOutputStream(), true);
					    p2in = new BufferedReader(
					        new InputStreamReader(player2.getInputStream()));
					
				}
				//Processing Movement 
				
				
			
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
	
	
}
}
