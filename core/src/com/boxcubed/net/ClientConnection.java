package com.boxcubed.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class ClientConnection extends Thread{
	Socket connection;
	public boolean stop=false;
	public byte w=0,s=0,a=0,d=0,shift=0,space=0;
	public float rotation=0;
	Player player,player2;
	public String commandBuffer="update";
	public ClientConnection(Player player){
		this.player=player;
		player.setConnection(this);
		//TODO VERY Temporary 
		GameState.instance.multiplayerPlayers=(new Player(player.getBody().getWorld(), 2));
		player2=GameState.instance.multiplayerPlayers;
		
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
				
			    
			    
			float[] mess=new float[5];
			
				
				String sMess=in.readLine();
				for(int i=0;i<4;i++)
				mess[i]=Float.parseFloat(sMess.split(":")[i]);
				
				player.multiPos.x=mess[0];
				player.multiPos.y=mess[1];
				player2.multiPos.x=mess[2];
				player2.multiPos.y=mess[3];
				player2.setRotation(mess[4]);
				
				
				//awdsaSystem.out.println("[Client] : "+sMess);
			
			
			
			
			out.println("mov:"+w+":"+a+":"+s+":"+d+":"+shift+":"+space+":"+rotation);
				//commandBuffer="update";
			
			
			
			
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
