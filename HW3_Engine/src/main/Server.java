package main;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import objects.GameObject;
import objects.Ground;
import processing.core.PApplet;

// Code taken from class moodle page
public class Server extends PApplet implements Runnable {

    private static CopyOnWriteArrayList<DataInputStream>    input_streams;
    private static CopyOnWriteArrayList<ObjectOutputStream> output_streams;
    private static ArrayList<GameObject>                    game_objects;

    private static ServerSocket                             ss;

    public Server () {
    }

    @Override
    public void settings () {
        size( Client.parentWidth, Client.parentHeight );
    }

    @Override
    public void setup () {
        final Ground g = new Ground( 00 );
        game_objects.add( g );
    }

    @Override
    public void run () {
        try {
            while ( true ) {
                System.out.println( "About to accept..." );
                final Socket s = ss.accept();
                System.out.println( "New connection Established" );
                synchronized ( this ) {
                    output_streams.add( new ObjectOutputStream( s.getOutputStream() ) );
                    input_streams.add( new DataInputStream( s.getInputStream() ) );
                }
                System.out.println( "Streams successfully added." );
            }
        }
        catch ( final IOException iox ) {
            iox.printStackTrace();
        }
    }

    public static void main ( final String[] args ) {
        try {
            ss = new ServerSocket( 5422 );
        }
        catch ( final IOException e ) {
            e.printStackTrace();
        }
        input_streams = new CopyOnWriteArrayList<DataInputStream>();
        output_streams = new CopyOnWriteArrayList<ObjectOutputStream>();

        final Server server = new Server();
        ( new Thread( server ) ).start();

        PApplet.main( "main.Server" );
        while ( true ) {
            synchronized ( server ) {
                for ( final ObjectOutputStream dout : output_streams ) {
                    try {
                        for ( int i = 0; i < game_objects.size(); i++ ) {
                            game_objects.get( i ).update();
                        }
                        dout.writeObject( game_objects );
                        dout.reset();
                    }
                    catch ( final IOException e ) {
                        output_streams.remove( dout );
                    }
                }
            }
            synchronized ( server ) {
                for ( final DataInputStream din : input_streams ) {
                    try {
                        din.readInt();
                    }
                    catch ( final IOException e ) {
                        input_streams.remove( din );
                    }
                }
            }

            // Thread.sleep( 2000 );
        }

    }

}
