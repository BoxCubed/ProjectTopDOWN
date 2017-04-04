package com.boxcubed.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.google.gson.Gson;

import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;
public class ClientConnection extends Thread{
	Socket connection;
	public boolean stop=false;
	public byte w=0,s=0,a=0,d=0,shift=0,space=0;
	public float rotation=0;
	Player player,player2; 
	String ip;
	public ConnectionState state;
	Json jsonReader=new Json(OutputType.json);
	public ClientConnection(Player player){
		this(player, "localhost:22222");
		
		
	}
	public ClientConnection(Player player, String ip){
		this.player=player;
		state=ConnectionState.CONNECTING;
		this.ip=ip;
		
		//TODO VERY Temporary 
		//GameState.instance.multiplayerPlayers.add(new Player(player.getBody().getWorld(), 2));
		//player2=GameState.instance.multiplayerPlayers.get(0);
		
		
		start();
		
		
	}
	@Override
	public void run() {
		
		Gdx.app.log("[Client]", "Client Thread started.");
		SocketHints hints=new SocketHints();
		//hints.connectTimeout=1000;
		hints.socketTimeout=1000;
		try{
		connection=Gdx.net.newClientSocket(Protocol.TCP, ip.split(":")[0], Integer.parseInt(ip.split(":")[1]), hints);
		
		}catch(Exception e){System.out.println("Failed connecting to server: "+e.getMessage());state=ConnectionState.INVALIDIP; return;}
		state=ConnectionState.CONNECTED;
		
		player.setConnection(this,1);
		ObjectInputStream inob=null;
		ObjectOutputStream outob=null;
		Gson gson;
		gson=new Gson();
		try{
			
			
		outob=new ObjectOutputStream(connection.getOutputStream());
		inob=new ObjectInputStream(connection.getInputStream());
		}catch(Exception e){e.printStackTrace();Gdx.app.exit();}
		
		//PrintWriter out = new PrintWriter(connection.getOutputStream(), true);;
		/*BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));;*/
		
		while(!stop){
			
			if(!connection.isConnected()){System.out.println("[Client] No connection");continue;}
			
			try{
				
			    
			    
			
			
				
				
				try{
					String packetString=(String) inob.readObject();
					if(packetString.startsWith("disconnect:")){
						System.out.println(packetString); 
						//TODO Handle server stopping
						//TopDown.instance.setScreen(new MenuState(GameState.instance));
						break;}
					DataPacket packet=gson.fromJson(packetString, DataPacket.class);
					player.multiPos=universalLerpToPos(player.getPos(), packet.pos);
					if(GameState.instance.multiplayerPlayers.size()>packet.players.size())
					
						GameState.instance.playerRemQueue++;
					else if(GameState.instance.multiplayerPlayers.size()<packet.players.size())
						GameState.instance.playerAddQueue++;
					
					for(int i=0;i<packet.players.size();i++){
						
						if(packet.players.size()==0)break;
						
						
						if(GameState.instance.multiplayerPlayers.size()!=packet.players.size())break;
						SocketPlayer player=packet.players.get(i);
						Player localPlayer=GameState.instance.multiplayerPlayers.get(i);
						localPlayer.multiPos=player.loc.cpy();
						localPlayer.rotation=player.rotation;
						localPlayer.name=player.name;
					}
					/*player2.multiPos=universalLerpToPos(player2.getPos(),packet.loc2);
					player2.setRotation(packet.rotation);*/
					//System.out.println(packet);
				}catch(ClassCastException e){String mess=(String)inob.readObject();System.out.println(mess);}catch(SocketException|EOFException e){
					//TODO handle sudden server stop
				}
				
				
					
				
				
				
			
			try{
			outob.writeObject(new InputPacket(w, a, s, d, space, shift, rotation));}
			catch(SocketException e){}
			
			//out.println("mov:"+w+":"+a+":"+s+":"+d+":"+shift+":"+space+":"+rotation);
			
			
			
			
			
			
			
			}catch(Exception e){System.err.println("Error occured on Client : "+e.getMessage());e.printStackTrace();}
			
		}
		
		Gdx.app.log("[Client]", "Shutting Down...");
		try {
			inob.close();
			outob.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connection.dispose();
	
	}
	private Vector2 universalLerpToPos(Vector2 start,Vector2 finish){
    	final float speed=0.5f,ispeed=1.0f-speed;
		Vector3 target = new Vector3(
				(float)finish.x, 
				(float)finish.y, 
				0);
		Vector3 cameraPosition = new Vector3(start, 0);
		cameraPosition.scl(ispeed);
		target.scl(speed);
		cameraPosition.add(target);

		return new Vector2(cameraPosition.x, cameraPosition.y);
    	
    }
	public enum ConnectionState{CONNECTED,INVALIDIP,DISCONNECTED,CONNECTING}

}
