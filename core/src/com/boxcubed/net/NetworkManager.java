package com.boxcubed.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.packets.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.boxcubed.main.Objects.interfaces.GunType;
import me.boxcubed.main.Sprites.Bullet;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;
import me.boxcubed.main.States.MenuState;
import me.boxcubed.main.TopDown;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkManager extends Thread {
    public final InputPacket move;
    private final String ip;
    private final BitmapFont font = new BitmapFont();
    private final LinkedBlockingQueue<BulletFirePacket> pendingFire = new LinkedBlockingQueue<>();
    private final List<Integer> dispose = new ArrayList<>();
    private final World world = GameState.instance.getWorld();
    public boolean stop = false;
    public String name = "BoxCubed";
    public ConnectionState state;
    private Client connection;
    private Player player;
    private String disconnectReason = "Unknown";
    private InputPacket lastMove;
    private Map<Integer, Player> multiplayerPlayers;

    public NetworkManager(Player player) {
        this(player, "localhost:22222");

    }

    public NetworkManager(Player player, String ip) {
        super("PTD Client");
        this.player = player;
        state = ConnectionState.CONNECTING;
        this.ip = ip;
        lastMove = new InputPacket();
        move = new InputPacket();
        start();


    }

    public static void initKryo(Kryo k) {
        k.register(String.class);
        k.register(StringPacket.class);
        k.register(Vector2.class);
        k.register(InputPacket.class);
        k.register(LocalPlayerPosPacket.class);
        k.register(PlayerUpdatePacket.class);
        k.register(PlayerDisconnectPacket.class);
        k.register(ParticlePacket.class);
        k.register(BulletFirePacket.class);


    }

    public void setPlayer(Player p) {
        player = p;
    }

    @Override
    public void run() {
        Gdx.app.log("[Client]", "Client Thread started.");
        try {
            connection = new Client();
            connection.start();
            initKryo(connection.getKryo());
            connection.addListener(new PlayerListener());
            connection.connect(3000, InetAddress.getByName(ip.split(":")[0]), Integer.parseInt(ip.split(":")[1]),
                    Integer.parseInt(ip.split(":")[1]));
            multiplayerPlayers = new HashMap<>();
            //Log.set(Log.LEVEL_DEBUG);
            font.getData().setScale(0.5f);
            connection.sendTCP("name:" + name);


        } catch (Exception e) {
            System.out.println("Failed connecting to server: " + e.getMessage());
            state = ConnectionState.INVALID_IP;


            return;
        }
        state = ConnectionState.CONNECTED;

        //player.setConnection(this, 1);


        while (!stop) {

            try {
                if (!lastMove.equals(move)) {
                    connection.sendUDP(move);
                    lastMove = new InputPacket(move);
                }
                Thread.sleep(10);


            } catch (Exception e) {
                System.err.println("Error occured on Client : " + e.getMessage());
                e.printStackTrace();
            }


        }

        Gdx.app.log("[Client]", "Shutting Down...");
        connection.stop();


    }

    public synchronized void updatePlayers(float delta) {
        for (Map.Entry<Integer, Player> entry : multiplayerPlayers.entrySet()) {
            Player p = entry.getValue();
            if (p.isDisposable()) {
                p.dispose();
                dispose.add(entry.getKey());
            } else p.update(delta);

        }
        for (int i : dispose) {
            multiplayerPlayers.remove(i);

        }
        dispose.clear();

        if (!pendingFire.isEmpty()) {
            BulletFirePacket bullet;
            try {
                bullet = pendingFire.take();
                GameState.instance.entities.add(new Bullet(world, bullet.location.x, bullet.location.y, (float) (Math.cos(Math.toRadians(bullet.rotation))),
                        (float) (Math.sin(Math.toRadians(bullet.rotation))), bullet.rotation, GunType.valueOf(bullet.type), GameState.instance.player));//TODO bullet types
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }

    public synchronized void renderPlayers(SpriteBatch sb) {
        for (Entry<Integer, Player> entry : multiplayerPlayers.entrySet()) {
            entry.getValue().render(sb);
            GlyphLayout layout = entry.getValue().name;
            font.draw(sb, layout, entry.getValue().getPos(true).x - layout.width / 2, entry.getValue().getPos(true).y - 10);
        }


    }

    private synchronized void addPlayer(int id, PlayerUpdatePacket ob) {
        Player p = new Player(GameState.instance.getWorld(), 2);
        p.name.setText(font, ob.name);
        multiplayerPlayers.put(id, p);


    }

    private synchronized void remPlayer(int id) {
        if (multiplayerPlayers.get(id) != null)
            multiplayerPlayers.get(id).setDisposable(true);


    }

    private synchronized void disconnect() {

        for (Player p : multiplayerPlayers.values())
            p.setDisposable(true);


        multiplayerPlayers.clear();
        TopDown.instance.setScreen(new MenuState(GameState.instance));
        try {
            connection.stop();
            connection.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            state = ConnectionState.DISCONNECTED;
        }

    }

    public void onFire(Vector2 pos, float rotation, String type) {
        connection.sendTCP(new BulletFirePacket(rotation, type));

    }

    private void updatePositionFromPacket(int id, PlayerUpdatePacket packet) {
        Player p = multiplayerPlayers.get(id);
        p.multiPos = packet.location.cpy();
        p.rotation = packet.rotation;
        p.name.setText(font, packet.name);
        p.setHealth(packet.health);
    }

    public enum ConnectionState {
        CONNECTED, INVALID_IP, DISCONNECTED, CONNECTING
    }

    private class PlayerListener extends Listener {
        @Override
        public void received(Connection connection, Object ob) {
            if (ob instanceof LocalPlayerPosPacket) {
                player.multiPos = ((LocalPlayerPosPacket) ob).pos.cpy();
            } else if (ob instanceof PlayerUpdatePacket) {
                PlayerUpdatePacket packet = (PlayerUpdatePacket) ob;
                if (packet.id != connection.getID() && multiplayerPlayers.get(packet.id) != null)
                    updatePositionFromPacket(packet.id, (PlayerUpdatePacket) ob);
                else if (packet.id != connection.getID()) addPlayer(packet.id, (PlayerUpdatePacket) ob);
                else {
                    player.setMultiPos(packet.location.cpy());
                    player.setHealth(packet.health);
                }


            } else if (ob instanceof PlayerDisconnectPacket) {
                if (((PlayerDisconnectPacket) ob).id == connection.getID()) {
                    disconnectReason = ((PlayerDisconnectPacket) ob).reason;
                    System.out.println(disconnectReason);
                    disconnect();

                } else
                    remPlayer(((PlayerDisconnectPacket) ob).id);
            } else if (ob instanceof BulletFirePacket)
                pendingFire.add((BulletFirePacket) ob);


        }

        @Override
        public void disconnected(Connection connection) {
            Gdx.app.log("[Client]", "lost connection: " + disconnectReason);
            state = ConnectionState.DISCONNECTED;
            disconnect();

        }

    }
}
