package com.boxcubed.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class ClientConnection extends Thread{
	Socket connection;
	public boolean stop=false;
	Player player;
	public String commandBuffer="update";
	public ClientConnection(Player player){
		this.player=player;
		SocketHints hints=new SocketHints();
		//hints.connectTimeout=1000;
		hints.socketTimeout=1000;
		connection=Gdx.net.newClientSocket(Protocol.TCP, "localhost", 22222, hints);
		start();
		
	}
	@Override
	public void run() {
		Gdx.app.log("[Client]", "Client Thread started.");
		PrintWriter out = new PrintWriter(connection.getOutputStream(), true);;
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));;
		while(!stop){
			
			//Gdx.app.log("[client]", "tick");
			if(!connection.isConnected()){System.out.println("[Client] No connection");continue;}
			
			try{
				//Thread.sleep(1000);
				
			    
			    
			float[] mess=new float[4];
			while(in.ready()){	
				String sMess=in.readLine();
				for(int i=0;i<3;i++)
				mess[i]=Float.parseFloat(sMess.split(":")[i]);
				
				player.multiPos.x=mess[0];
				player.multiPos.y=mess[1];
				//System.out.println("[Client] : "+mess);
			
			
			}
			
			out.println(commandBuffer);
				commandBuffer="update";
			
			
			
			
			//Gdx.app.log("[client]", "attempting connection");
			
			
			
			}catch(Exception e){System.err.println("Error occured on Client : "+e.getMessage());e.printStackTrace();}
			
		}
		
		Gdx.app.log("[Client]", "Shutting Down...");
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
		connection.dispose();
	
	}

}
