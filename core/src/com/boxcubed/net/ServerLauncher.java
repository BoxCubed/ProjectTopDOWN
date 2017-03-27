package com.boxcubed.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;

public class ServerLauncher extends Thread{
	ServerSocket server;
	Socket accept;
	public boolean stop=false;

	public ServerLauncher() {
		server=Gdx.net.newServerSocket(Protocol.TCP, 200, null);
		start();
		
		
	}
	@Override
	public void run() {
		
		Gdx.app.log("[Server]", "Server Thread started");
		accept=server.accept(null);
		PrintWriter out = new PrintWriter(accept.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(
	        new InputStreamReader(accept.getInputStream()));
		while(!stop){
			try{
				if(accept==null||!accept.isConnected())
				accept=server.accept(null);
				//Gdx.app.log("[Server]", "Server tick");
				Thread.sleep(1000);
				String mess=null;
				while(in.ready()){
					mess=in.readLine();
				System.out.println("[Server] : "+mess);
				}
				
				
			    out.println("Greetings from server!");;
				out.flush();
				
			
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
