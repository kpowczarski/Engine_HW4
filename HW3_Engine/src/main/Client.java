package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import objects.GameObject;
import objects.ObjectOutputStreamId;
import objects.Timeline;
import processing.core.PApplet;

/**
 *
 * @author Kevin Owczarski Inspired by project 1
 *
 */
public class Client extends PApplet {

    private static ObjectInputStream     input_stream;
    private static ObjectOutputStreamId  output_stream;
    private static ArrayList<GameObject> game_objects;
    private static Timeline              time;
    private static Socket                s;
    public static int                    parentHeight = 900;
    public static int                    parentWidth  = 1300;
    public int                           move;
    public int                           anti;
    public int                           pause;
    public int                           speed;
    public int                           record;
    public int                           recording;
    public int                           replayInit;
    public int                           replay;
    public int                           backgroundColor;

    public static void main ( final String[] args ) {
        try {
            s = new Socket( "127.0.0.1", 5422 );
            output_stream = new ObjectOutputStreamId( s.getOutputStream() );
            input_stream = new ObjectInputStream( s.getInputStream() );

            System.out.println( "Connected" );
            PApplet.main( "main.Client" );

        }
        catch ( final IOException e ) {
            try {
                input_stream.close();
                output_stream.close();
                s.close();
                return;
            }
            catch ( final IOException c ) {
                return;
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
        move = 0;
        anti = 0;
        pause = 0;
        speed = -1;
        record = 0;
        recording = 0;
        replayInit = 0;
        replay = 0;
        backgroundColor = 51;
    }

    @SuppressWarnings ( "unchecked" )
    @Override
    public void draw () {
        background( backgroundColor );
        // int key = keyPressMove();
        try {
            // output_stream.writeInt( key );
            output_stream.writeInt( move );
            output_stream.writeInt( anti );
            output_stream.writeInt( pause );
            output_stream.writeInt( speed );
            output_stream.writeInt( record );
            move = 0;
            anti = 0;
            output_stream.reset();
        }
        catch ( final IOException e ) {
            try {
                input_stream.close();
                output_stream.close();
                s.close();
                exit();
            }
            catch ( final IOException c ) {
                exit();
            }
        }
        try {
            game_objects = (ArrayList<GameObject>) input_stream.readObject();
            time = (Timeline) input_stream.readObject();
            recording = input_stream.readInt();
            replayInit = input_stream.readInt();
            replay = input_stream.readInt();

        }
        catch ( final IOException | ClassNotFoundException e ) {
            try {
                input_stream.close();
                output_stream.close();
                s.close();
                exit();
            }
            catch ( final IOException c ) {
                exit();
            }
        }
        for ( int i = 0; i < game_objects.size(); i++ ) {
            game_objects.get( i ).render( this );
        }
        // time.render( this );
        if ( recording == 1 ) {
            this.fill( 255 );
            this.textSize( 20 );
            this.text( "Recording", 100, 30 );
        }
        if ( replayInit == 1 ) {
            this.fill( 255 );
            this.textSize( 20 );
            this.text( "Replay Speed", 540, 410 );
            this.text( "Press 1 for half speed, 2 for normal, and 3 for double", 350, 450 );
        }
        if ( replay == 1 ) {
            backgroundColor = 150;
        }
        else {
            backgroundColor = 51;
            speed = -1;
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

    public int keyPressMove () {
        if ( this.keyPressed == true ) {
            if ( this.key == 'a' ) {
                return 1;
            }
            else if ( this.key == 'd' ) {
                return 2;
            }
            else if ( this.key == ' ' ) {
                return 3;
            }
        }
        return 0;
    }

    @Override
    public void keyPressed () {
        if ( key == 'a' ) {
            move = 1;

        }
        else if ( key == 'd' ) {
            move = 2;
        }
        else if ( key == ' ' ) {
            move = 3;
        }
        else if ( key == 'p' ) {
            pause = 1;
        }
        else if ( key == 'u' ) {
            pause = 0;
        }
        else if ( key == 'r' ) {
            record = 1;
            // speed = 1;
        }
        else if ( key == 't' ) {
            record = 0;
            // speed = -1;
        }
        else if ( key == '1' ) {
            if ( replay == 1 || replayInit == 1 ) {
                speed = 0;
            }
            else {
                speed = -1;
            }
        }
        else if ( key == '2' ) {
            if ( replay == 1 || replayInit == 1 ) {
                speed = 1;
            }
            else {
                speed = -1;
            }
        }
        else if ( key == '3' ) {
            if ( replay == 1 || replayInit == 1 ) {
                speed = 2;
            }
            else {
                speed = -1;
            }
        }
    }

    @Override
    public void keyReleased () {
        if ( key == 'a' ) {
            anti = 1;
        }
        if ( key == 'd' ) {
            anti = 2;
        }
        if ( key == ' ' ) {
            anti = 3;
        }
    }

}
