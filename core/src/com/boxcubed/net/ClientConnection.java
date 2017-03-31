package com.boxcubed.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	public ClientConnection(Player player){
		this.player=player;
		player.setConnection(this);
		//TODO VERY Temporary 
		//GameState.instance.multiplayerPlayers.add(new Player(player.getBody().getWorld(), 2));
		//player2=GameState.instance.multiplayerPlayers.get(0);
		
		SocketHints hints=new SocketHints();
		//hints.connectTimeout=1000;
		hints.socketTimeout=1000;
		connection=Gdx.net.newClientSocket(Protocol.TCP, "localhost", 22222, hints);
		start();
		
	}
	@Override
	public void run() {
		Gdx.app.log("[Client]", "Client Thread started.");
		ObjectInputStream inob=null;
		ObjectOutputStream outob=null;
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
				
			    
			    
			
			
				
				
				/*try{
					String sMess=in.readLine();
					float[] mess=new float[sMess.split(":").length];
					if(sMess.contains("P"))continue;
				for(int i=0;i<mess.length;i++)
				mess[i]=Float.parseFloat(sMess.split(":")[i]);
				
				
				player.multiPos=universalLerpToPos(player.getPos(), new Vector2(mess[0], mess[1]));
				player2.multiPos=universalLerpToPos(player2.getPos(), new Vector2(mess[2], mess[3]));
				
				player2.setRotation(mess[4]);}catch(NullPointerException|SocketTimeoutException|SocketException e){}
					*/
				
				try{
					DataPacket packet=(DataPacket)inob.readObject();
					player.multiPos=universalLerpToPos(player.getPos(), packet.pos);
					
					for(int i=0;i<packet.players.size()-1;i++){
						Player p=GameState.instance.multiplayerPlayers.get(i);
						System.out.println(packet.players.get(i).x);
						p.multiPos.x=packet.players.get(i).x;
						p.multiPos.y=packet.players.get(i).y;
						p.rotation=(packet.players.get(i).rotation);
					}
					/*player2.multiPos=universalLerpToPos(player2.getPos(),packet.loc2);
					player2.setRotation(packet.rotation);*/
					//System.out.println(packet);
				}catch(ClassCastException e){String mess=(String)inob.readObject();System.out.println(mess);}catch(Exception e){e.printStackTrace();}
					
				
				
				
			
			
			outob.writeObject(new InputPacket(w, a, s, d, space, shift, rotation));
			
			//out.println("mov:"+w+":"+a+":"+s+":"+d+":"+shift+":"+space+":"+rotation);
			
			
			
			
			
			
			
			}catch(Exception e){System.err.println("Error occured on Client : "+e.getMessage());e.printStackTrace();}
			
		}
		
		Gdx.app.log("[Client]", "Shutting Down...");
		/*try {
			i.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();*/
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

}
