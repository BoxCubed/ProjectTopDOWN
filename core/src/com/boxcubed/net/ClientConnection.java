package com.boxcubed.net;

import java.net.InetAddress;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.gson.Gson;

import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;
public class ClientConnection extends Thread{
	Client connection;
	public boolean stop=false;
	public byte w=0,s=0,a=0,d=0,shift=0,space=0;
	public float rotation=0;
	Player player,player2; 
	public String ip;
	private Object recieved=null;
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
		connection=new Client();
		
		connection.start();
		Kryo kryo = connection.getKryo();
	    kryo.register(InputPacket.class);
	    kryo.register(DataPacket.class);
	    kryo.register(KyroPlayer.class);
	    kryo.register(ArrayList.class);
	    kryo.register(Vector2.class);
	    kryo.register(String.class);
		connection.addListener(new PlayerListener());
		connection.connect(3000, InetAddress.getByName(ip.split(":")[0]), Integer.parseInt(ip.split(":")[1]), Integer.parseInt(ip.split(":")[1]));
		
		
		connection.sendTCP("BoxCubed");
		
		}catch(Exception e){System.out.println("Failed connecting to server: "+e.getMessage());state=ConnectionState.INVALIDIP; return;}
		state=ConnectionState.CONNECTED;
		
		player.setConnection(this,1);
		Gson gson;
		gson=new Gson();
		
		//PrintWriter out = new PrintWriter(connection.getOutputStream(), true);;
		/*BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));;*/
		
		while(!stop){
			
			
			try{
				
			    
			    
			
			
				
				
				try{
					if(recieved instanceof String&&((String)recieved).startsWith("disconnect:")){
						System.out.println((String)recieved); 
						state=ConnectionState.DISCONNECTED;
						//TODO Handle server stopping
						//TopDown.instance.setScreen(new MenuState(GameState.instance));
						break;}
					if(!(recieved instanceof String))throw new NullPointerException();
					DataPacket packet=gson.fromJson((String)recieved, DataPacket.class);
					player.multiPos=universalLerpToPos(player.getPos(), packet.pos);
					if(GameState.instance.multiplayerPlayers.size()>packet.players.size())
					
						GameState.instance.playerRemQueue++;
					else if(GameState.instance.multiplayerPlayers.size()<packet.players.size())
						GameState.instance.playerAddQueue++;
					
					for(int i=0;i<packet.players.size();i++){
						
						if(packet.players.size()==0)break;
						
						
						if(GameState.instance.multiplayerPlayers.size()!=packet.players.size())break;
						KyroPlayer player=packet.players.get(i);
						Player localPlayer=GameState.instance.multiplayerPlayers.get(i);
						localPlayer.multiPos=player.loc.cpy();
						localPlayer.rotation=player.rotation;
						localPlayer.name=player.name;
					}
					/*player2.multiPos=universalLerpToPos(player2.getPos(),packet.loc2);
					player2.setRotation(packet.rotation);*/
					//System.out.println(packet);
				}catch(NullPointerException e){/*TODO handle unknown instance*/}
				
				
					
				
				
				
			
			connection.sendUDP((new InputPacket(w, a, s, d, space, shift, rotation)));
			
			//out.println("mov:"+w+":"+a+":"+s+":"+d+":"+shift+":"+space+":"+rotation);
			
			
			
			
			
			
			
			}catch(Exception e){System.err.println("Error occured on Client : "+e.getMessage());e.printStackTrace();}
			
		}
		
		Gdx.app.log("[Client]", "Shutting Down...");
			connection.stop();
			connection.close();
		
		
	
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
class PlayerListener extends Listener{
@Override
public void received(Connection connection, Object object) {
	recieved=object;
}
}}
