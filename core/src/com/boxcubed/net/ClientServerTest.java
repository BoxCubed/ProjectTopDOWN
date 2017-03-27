package com.boxcubed.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;

public class ClientServerTest extends Thread{
	Socket connection;
	public boolean stop=false;
	public ClientServerTest(){
		connection=Gdx.net.newClientSocket(Protocol.TCP, "localhost", 200, null);
		start();
		
	}
	@Override
	public void run() {
		Gdx.app.log("[client]", "Cleint Thread started");
		
		while(!stop){
			Gdx.app.log("[client]", "tick");
			try{
				connection.getOutputStream().write(70);
				connection.getOutputStream().flush();
			//if(!connection.isConnected())System.out.println("[client] No connection : "+connection.getRemoteAddress());
			int mess;
			do{
				mess=connection.getInputStream().read();
			System.out.println("[client] : "+mess);
			
			}while(mess!=-1);
			//System.out.println("[client] : "+connection.getInputStream().read());
			
			//Thread.sleep(100);
			
			Gdx.app.log("[client]", "attempting connection");
			
			
			
			}catch(Exception e){System.out.println("Error occured on Client");}
			
		}
	
	}

}
