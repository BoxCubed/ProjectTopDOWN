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
	int count;
	public ClientServerTest(){
		SocketHints hints=new SocketHints();
		//hints.connectTimeout=1000;
		hints.socketTimeout=1000;
		connection=Gdx.net.newClientSocket(Protocol.TCP, "localhost", 200, hints);
		start();
		
	}
	@Override
	public void run() {
		Gdx.app.log("[Client]", "Client Thread started");
		PrintWriter out = new PrintWriter(connection.getOutputStream(), true);;
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));;
		while(!stop){
			
			//Gdx.app.log("[client]", "tick");
			if(!connection.isConnected()){System.out.println("[Client] No connection");continue;}
			
			try{
				Thread.sleep(1000);
				count++;
			    
			    
			String mess=null;
			while(in.ready()){	
				mess=in.readLine();
				
				System.out.println("[Client] : "+mess);
			
			
			}
			out.println("Greetings from client for the "+count+" time");
			if(count==3){
				out.println("stop");
				stop=true;
				}
			
			
			
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
