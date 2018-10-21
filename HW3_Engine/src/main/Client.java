package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import objects.GameObject;
import processing.core.PApplet;

// Code taken from class moodle page
public class Client extends PApplet {

    private static ObjectInputStream     input_stream;
    private static DataOutputStream      output_stream;
    private static ArrayList<GameObject> game_objects;
    private static Socket                s;
    public static int                    parentHeight = 900;
    public static int                    parentWidth  = 1300;
    public static int                    id;

    public static void main ( final String[] args ) {
        try {
            s = new Socket( "127.0.0.1", 5422 );
            output_stream = new DataOutputStream( s.getOutputStream() );
            input_stream = new ObjectInputStream( s.getInputStream() );

            id = Integer.parseInt( args[0] );
            System.out.println( "Connected" );
            PApplet.main( "main.Client" );

        }
        catch ( final IOException e ) {
            try {
                input_stream.close();
                output_stream.close();
                s.close();
            }
            catch ( final IOException c ) {
                // nothing
            }
        }
    }

    @Override
    public void settings () {
        size( parentWidth, parentHeight );
    }

    @Override
    public void setup () {
        game_objects = new ArrayList<GameObject>();
    }

    @SuppressWarnings ( "unchecked" )
    @Override
    public void draw () {
        background( 51 );
        // int iter = 0;
        try {
            game_objects = (ArrayList<GameObject>) input_stream.readObject();
            System.out.println( "Reading" );
        }
        catch ( final IOException | ClassNotFoundException e ) {
            try {
                input_stream.close();
                output_stream.close();
                s.close();
            }
            catch ( final IOException c ) {
                c.printStackTrace();
            }
        }
        for ( int i = 0; i < game_objects.size(); i++ ) {
            game_objects.get( i ).render( this );
        }
        // output_stream.writeInt( id );
        // output_stream.writeInt( iter );
        // ++iter;

    }

    @Override
    public void stop () {
        try {
            input_stream.close();
            output_stream.close();
            s.close();
        }
        catch ( final IOException e ) {
            // nothing
        }
    }

}
