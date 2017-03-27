package com.boxcubed.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class ServerLauncher extends Thread{
	ServerSocket server;
	Socket accept;
	public boolean stop=false;

	public ServerLauncher() {
		ServerSocketHints hints=new ServerSocketHints();
		hints.acceptTimeout=0;
		server=Gdx.net.newServerSocket(Protocol.TCP, 200, hints);
		start();
		
		
	}
	@Override
	public void run() {
		SocketHints hints=new SocketHints();
		//hints.connectTimeout=1000;
		hints.socketTimeout=1000;
		Gdx.app.log("[Server]", "Server Thread started");
		accept=server.accept(hints);
		PrintWriter out = new PrintWriter(accept.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(
	        new InputStreamReader(accept.getInputStream()));
		while(!stop){
			try{
				if(accept==null||!accept.isConnected()){
					System.out.println("[Server] Connection lost! Waiting for new client...");
					accept=server.accept(hints);
				
				}
				//Gdx.app.log("[Server]", "Server tick");
				Thread.sleep(1000);
				String mess="";
				while(in.ready()){
					
					mess=in.readLine();
				System.out.println("[Server] : "+mess);
				}
				
			   // out.println("Greetings from server!");;
				if(mess.equals("stop"))
					accept.dispose();
			
			//System.out.println(accept.getRemoteAddress());
			
			
			}catch (Exception e){System.out.println("[Server] : Error occured "+ e.getMessage());}
			
	}
		Gdx.app.log("[Server]", "Server Shutting Down...");
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();
		accept.dispose();
		server.dispose();
		
	}

}
