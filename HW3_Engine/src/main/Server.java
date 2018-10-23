package main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import objects.Deathzone;
import objects.GameObject;
import objects.Ground;
import objects.ObjectInputStreamId;
import objects.Platform;
import objects.Player;
import processing.core.PApplet;

// Code taken from class moodle page
public class Server extends PApplet implements Runnable {

    private static CopyOnWriteArrayList<ObjectInputStreamId> input_streams;
    private static CopyOnWriteArrayList<ObjectOutputStream>  output_streams;
    public static ArrayList<GameObject>                      game_objects;

    private static ServerSocket                              ss;

    public Server () {
    }

    @Override
    public void settings () {
        size( Client.parentWidth, Client.parentHeight );
    }

    @Override
    public void setup () {
        final Ground g = new Ground();
        final Platform plat1 = new Platform( true, 30, 800, 100, 32, 255, 255, 0 );
        plat1.setMovingSettings( 800, 100, 0, 0, 2, 0 );
        final Platform plat2 = new Platform( true, 1000, 800, 100, 32, 126, 243, 233 );
        plat2.setMovingSettings( 850, 100, 0, 0, 3, 0 );
        final Platform plat3 = new Platform( false, 200, 600, 100, 32, 23, 43, 244 );
        Platform plat4 = new Platform( true, 155, 800, 100, 32, 126, 34, 122 );
        plat4.setMovingSettings( 0, 0, 550, 150, 0, 2 );
        final Platform plat5 = new Platform( false, 650, 800, 100, 32, 23, 43, 10 );
        final Platform plat6 = new Platform( false, 850, 800, 100, 32, 223, 43, 10 );
        Platform plat7 = new Platform( true, 850, 110, 100, 32, 236, 34, 222 );
        plat7.setMovingSettings( 0, 0, 870, 250, 0, 2 );
        Deathzone d = new Deathzone( 300, 962, 1100, 10 );
        game_objects = new ArrayList<GameObject>();
        game_objects.add( g );
        game_objects.add( plat1 );
        game_objects.add( plat2 );
        game_objects.add( plat3 );
        game_objects.add( plat4 );
        game_objects.add( plat5 );
        game_objects.add( plat6 );
        game_objects.add( plat7 );
        game_objects.add( d );
    }

    @Override
    public void run () {
        try {
            while ( true ) {
                System.out.println( "About to accept..." );
                final Socket s = ss.accept();
                System.out.println( "New connection Established" );
                synchronized ( this ) {
                    final Player newPlayer = new Player( 32, 32, 255, 0, 0 );
                    output_streams.add( new ObjectOutputStream( s.getOutputStream() ) );
                    input_streams.add( new ObjectInputStreamId( s.getInputStream(), newPlayer.GUID ) );
                    game_objects.add( newPlayer );
                }
                System.out.println( "Streams successfully added." );
            }
        }
        catch ( final IOException iox ) {
            iox.printStackTrace();
        }
    }

    public static void main ( final String[] args ) {
        PApplet.main( "main.Server" );
        try {
            ss = new ServerSocket( 5422 );
        }
        catch ( final IOException e ) {
            e.printStackTrace();
        }
        input_streams = new CopyOnWriteArrayList<ObjectInputStreamId>();
        output_streams = new CopyOnWriteArrayList<ObjectOutputStream>();

        final Server server = new Server();
        ( new Thread( server ) ).start();

        while ( true ) {
            synchronized ( server ) {
                for ( final ObjectInputStreamId din : input_streams ) {
                    try {
                        int f = din.readInt();
                        int anti = din.readInt();
                        for ( int i = 0; i < game_objects.size(); i++ ) {
                            if ( game_objects.get( i ).GUID == din.getId() ) {
                                game_objects.get( i ).handleMovement( f, anti );
                            }
                        }
                        // System.out.println( f );
                        // System.out.println( anti );
                    }
                    catch ( final IOException e ) {
                        for ( int i = 0; i < game_objects.size(); i++ ) {
                            if ( game_objects.get( i ).GUID == din.getId() ) {
                                game_objects.remove( i );
                                System.out.println( "Player" + din.getId() + "disconnected" );
                            }
                        }
                        input_streams.remove( din );
                    }
                }
            }
            synchronized ( server ) {
                if ( input_streams.size() >= 1 ) {
                    for ( int i = 0; i < game_objects.size(); i++ ) {
                        game_objects.get( i ).update();
                    }
                }
            }
            synchronized ( server ) {
                for ( final ObjectOutputStream dout : output_streams ) {
                    try {
                        dout.writeObject( game_objects );
                        dout.reset();
                    }
                    catch ( final IOException e ) {
                        // e.printStackTrace();
                        output_streams.remove( dout );
                        System.out.println( "Disconnected" );
                    }
                }
            }

            // Thread.sleep( 2000 );
        }

    }

}
