package com.boxcubed.net;

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
		
		while(true){
			

			try{
				accept=server.accept(null);
				Gdx.app.log("[Server]", "Server tick");
			
			accept.getOutputStream().write(66);
			accept.getOutputStream().flush();
				//Thread.sleep(100);
			int mess;
			do{
				mess=accept.getInputStream().read();
			System.out.println("[server] : "+mess);
			
			}while(mess!=-1);
			//System.out.println(accept.getRemoteAddress());
			
			
			}catch (Exception e){System.out.println("[server] : No connection/Error occured");}
	}}

}
