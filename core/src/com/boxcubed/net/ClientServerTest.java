package com.boxcubed.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class ClientServerTest extends Thread{
	Socket connection;
	public boolean stop=false;
	int player;
	
	public ClientServerTest(int player){
		this.player=player;
		SocketHints hints=new SocketHints();
		//hints.connectTimeout=1000;
		hints.socketTimeout=1000;
		connection=Gdx.net.newClientSocket(Protocol.TCP, "localhost", 22222, hints);
		start();
		
	}
	@Override
	public void run() {
		Gdx.app.log("[Client]", "Client Thread started. To send a command do @p"+player+" (command)");
		PrintWriter out = new PrintWriter(connection.getOutputStream(), true);;
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));;
		BufferedReader inCon=new BufferedReader(new InputStreamReader(System.in));
		while(!stop){
			
			//Gdx.app.log("[client]", "tick");
			if(!connection.isConnected()){System.out.println("[Client] No connection");continue;}
			
			try{
				//Thread.sleep(1000);
				
			    
			    
			String mess=null;
			while(in.ready()){	
				mess=in.readLine();
				
				System.out.println("[Client] : "+mess);
			
			
			}
			
			while(inCon.ready()){
				String messCon=inCon.readLine();
				
					System.out.println("[Client] Sending command as p"+player+":"+messCon.replaceAll("@p"+player, ""));
				out.print(messCon.replaceAll("@p"+player, ""));}
				out.println("update");
			
			
			
			
			//Gdx.app.log("[client]", "attempting connection");
			
			
			
			}catch(Exception e){System.out.println("Error occured on Client : "+e.getMessage());}
			
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
