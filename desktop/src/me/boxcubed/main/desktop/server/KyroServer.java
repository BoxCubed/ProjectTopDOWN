package me.boxcubed.main.desktop.server;

import java.io.BufferedReader;

public class KyroServer extends Thread {
	Server kServer;
	HashMap<Integer,KyroPlayer> players;
	public static World world=new World(new Vector2(0, 0), true);
	public static KyroServer instance;
	public boolean stop=false;
	

	TiledMap map;


	public KyroServer() {
		instance=this;
		start();
		
		
	}
	public static void main(String[] args){
		new KyroServer();
	}
	@Override
	public void run() {
		//The time between the last request they sent to server
		long startLoop=0,endLoop=0,delta=1,elapsedTime=0;
		float sleep=0.0001f;
		//hints.connectTimeout=1000;
		ConsoleThread inCon = null;

		
		

		try{
		log("Starting Project Top Down Multiplayer Server...");
		

		
		Gdx.files=new LwjglFiles();
		players=new HashMap<>();
		map=new ServerTiledMapLoader().load("assets/maps/map2.tmx");
		MapBodyBuilder.buildShapes(map, 1, world);
		//TODO make collision class for multiplayer
				//world.setContactListener(new CollisionDetection());
				
		kServer=new Server();
		
		kServer.start();
		kServer.bind(22222, 22222);
		
		

		Log.set(Log.LEVEL_INFO);
	    NetworkManager.initKryo(kServer.getKryo());
		addServerListeners();
		
		inCon=new ConsoleThread();
		
		log("\n---------------------------------------------"
			+ "\n Project Top Down Multiplayer Experience "
			+ "\n Brought to you by Box Cubed "
		  + "\n---------------------------------------------");
		}catch(Exception e){logError("Failed in creating server!:"+e.getMessage());stop=true;}
			
		
		while(!stop){
			
			try{
				startLoop=System.currentTimeMillis();
				players.forEach((id,player)->{
				String con=inCon.lastOutput;
				String[] conSplit=con.split(" ");
				switch(conSplit[0]){
				case "teleport":
					try{
						
								players.get(Integer.parseInt(conSplit[1]))
								.player.getBody().setTransform(new Vector2(Float.parseFloat(conSplit[2]), Float.parseFloat(conSplit[3])),
										players.get(Integer.parseInt(conSplit[1])).player.getBody().getTransform().getRotation());
								log("Teleported Player to given coords"); 
								
							
						
					}catch(Exception e){log("Incorrect usage! 'teleport x y player'");}
					break;
				case "stop":
					stop=true;
					break;
				case "":break;
				case "tps":
					if(conSplit.length==1)
					log("delta: "+(float)delta/1000000000f+" sleep: "+sleep+" Etime: "+elapsedTime);
					else {sleep=Float.parseFloat(conSplit[1]); log("sleep time changed");}
					break;
				case "list":
					log("There are "+players.size()+" player/s online");
					break;
				default:
					log("That isn't an option");
				}
				if(inCon.lastOutput!="")
				inCon.lastOutput="";
				
				Thread.sleep(10);
				//log(Long.toString(delta));
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
					
			
			
			
			
		
			
			}catch (Exception e){logError("Error occured: "+e.getMessage()); e.printStackTrace();}
			
	}
		log("Server Shutting Down...");
		try{players.forEach((id,player)->{player.player.dispose();});
		players.clear();
		world.dispose();
			if(inCon!=null)
			inCon.stop=true;
			
			System.exit(0);
			
		}catch(Exception e){logError("Failed in shutting down!:"+e.getMessage());}
	}
private synchronized void updateWorld(){
private void addServerListeners() {
	kServer.addListener(new Listener(){
		@Override
		public void connected(Connection connection) {
			
			PlayerListener lis=new PlayerListener();
			connection.addListener(lis);
			
			
			

		}
		@Override
		public void disconnected(Connection connection) {
		}@Override
		public void idle(Connection connection) {
			// TODO Auto-generated method stub
			super.idle(connection);
		}
		@Override
		public void received(Connection connection, Object object) {


		}
	});
		
	}
private void log(String s){
	/*Gdx.app.setLogLevel(Application.LOG_INFO);
	Gdx.app.log("[PTDM Server]", s);*/
	System.out.println("[PTDM Server]: "+s);
	
	
}
private void logError(String s){
	/*Gdx.app.setLogLevel(Application.LOG_ERROR);
	Gdx.app.log("![PTDM Server]", s);*/
	System.err.println("![PTDM Server]!: "+s);
	
	
	
}

class ConsoleThread extends Thread{
	BufferedReader in;
	public String lastOutput="";
	public boolean stop=false;
	public ConsoleThread(){
		in=new BufferedReader(new InputStreamReader(System.in));
		start();
		
	}
	@Override
	public void run() {
		while(!stop)
	try {
		lastOutput=in.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
class PlayerListener extends Listener{
	boolean gotName=false;
	public KyroPlayer p;
	
	
	@Override
	public void received(Connection connection, Object ob) {
		if(ob instanceof String){
			if(gotName==false&&((String)ob).startsWith("name:")){
				
				log(p.name+" has joined");
				
			}
		}
		
			
		
			
	}
	
	public void connected(Connection connection) {
		log("Player attempting connection...");
	}
	
	@Override
	public void disconnected(Connection connection) {
		if(gotName==false)
			log("Player failed connection");
		p.connected=false;
	}
	@Override
	public void idle(Connection connection) {
		// TODO Auto-generated method stub
		super.idle(connection);
	}
	
	
}


}