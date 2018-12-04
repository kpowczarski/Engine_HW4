package space_game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Server;
import objects.Deathzone;
import objects.Event;
import objects.EventManagerSpace;
import objects.Events;
import objects.GameObject;
import objects.Ground;
import objects.ObjectInputStreamId;
import objects.Platform;
import objects.Player;
import objects.Timeline;
import processing.core.PApplet;

/**
 *
 * @author Kevin Owczarski Inspired by project 1
 *
 */
public class Space_Server extends PApplet implements Runnable {

    private static CopyOnWriteArrayList<ObjectInputStreamId> input_streams;
    private static CopyOnWriteArrayList<ObjectOutputStream>  output_streams;
    public static ArrayList<GameObject>                      game_objects;
    public static ArrayList<String>                          pause;
    public static int                                        recordA;
    public static EventManagerSpace                          eventM;
    public static Timeline                                   time;
    public static Timeline                                   replayTime;
    public static int                                        recording  = 0;
    public static int                                        replayInit = 0;
    public static int                                        replay     = 0;
    public static boolean                                    pausedInit = false;
    private static ServerSocket                              ss;

    public Space_Server () {
    }

    @Override
    public void settings () {
        size( 100, 100 );
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
                    Event spawnE = new Event( Events.SPAWN, newPlayer, time.getCurrentTime() );
                    eventM.addEvent( spawnE );
                    output_streams.add( new ObjectOutputStream( s.getOutputStream() ) );
                    input_streams.add( new ObjectInputStreamId( s.getInputStream(), newPlayer.GUID ) );
                    pause.add( "0" );
                    // game_objects.add( newPlayer );
                }
                System.out.println( "Streams successfully added." );
            }
        }
        catch ( final IOException iox ) {
            iox.printStackTrace();
        }
    }

    public static void main ( final String[] args ) {
        PApplet.main( "space_game.Space_Server" );
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
        eventM = new EventManagerSpace( game_objects );
        time = new Timeline( 10 );
        pause = new ArrayList<String>();
        recordA = -1;
        try {
            ss = new ServerSocket( 5422 );
        }
        catch ( final IOException e ) {
            e.printStackTrace();
        }
        input_streams = new CopyOnWriteArrayList<ObjectInputStreamId>();
        output_streams = new CopyOnWriteArrayList<ObjectOutputStream>();

        final Space_Server server = new Space_Server();
        ( new Thread( server ) ).start();
        long currentT1 = time.getCurrentTime();
        while ( true ) {
            synchronized ( server ) {
                int index = 0;
                for ( final ObjectInputStreamId din : input_streams ) {
                    try {
                        int f = din.readInt();
                        int anti = din.readInt();
                        int temppause = din.readInt();
                        int speed = din.readInt();
                        int record = din.readInt();
                        if ( record == 1 && !eventM.recording ) {
                            recordA = index;
                            Event r = new Event( Events.RECORD, time.getCurrentTime() );
                            eventM.addEvent( r );
                        }
                        if ( record == 0 && eventM.recording && recordA == index ) {
                            // recordA = -1;
                            Event r = new Event( Events.STOPRECORD, time.getCurrentTime() );
                            eventM.addEvent( r );
                        }
                        if ( temppause == 1 ) {
                            pause.set( index, "1" );
                        }
                        if ( temppause == 0 ) {
                            pause.set( index, "0" );
                        }
                        if ( speed == 2 && recordA == index ) {
                            if ( eventM.replay || replayInit == 1 ) {
                                if ( Server.replayInit == 1 ) {
                                    Server.replayInit = 0;
                                    Server.eventM.startReplay();
                                    pausedInit = false;
                                    time.unpause();
                                    time.doubleTime();
                                    replayTime.startA();
                                    eventM.replay = true;
                                }
                                else {
                                    time.doubleTime();
                                }
                            }
                        }
                        if ( speed == 0 && recordA == index ) {
                            if ( eventM.replay || replayInit == 1 ) {
                                if ( Server.replayInit == 1 ) {
                                    Server.replayInit = 0;
                                    Server.eventM.startReplay();
                                    pausedInit = false;
                                    time.unpause();
                                    time.halfTime();
                                    replayTime.startA();
                                    eventM.replay = true;
                                }
                                else {
                                    time.halfTime();
                                }
                            }
                        }
                        if ( speed == 1 && recordA == index ) {
                            if ( eventM.replay || replayInit == 1 ) {
                                if ( Server.replayInit == 1 ) {
                                    Server.replayInit = 0;
                                    Server.eventM.startReplay();
                                    pausedInit = false;
                                    time.unpause();
                                    time.normalTime();
                                    replayTime.startA();
                                    eventM.replay = true;
                                }
                                else {
                                    time.normalTime();
                                }
                            }
                        }
                        if ( !eventM.replay ) {
                            for ( int i = 0; i < game_objects.size(); i++ ) {
                                if ( game_objects.get( i ).GUID == din.getId() ) {
                                    // game_objects.get( i ).handleMovement( f,
                                    // anti
                                    // );
                                    Event eMove = new Event( Events.MOVEMENT, game_objects.get( i ), f, anti,
                                            time.getCurrentTime() );
                                    eventM.addEvent( eMove );
                                }
                            }
                        }
                        // System.out.println( f );
                        // System.out.println( anti );
                    }
                    catch ( final IOException e ) {
                        for ( int i = 0; i < game_objects.size(); i++ ) {
                            if ( game_objects.get( i ).GUID == din.getId() ) {
                                game_objects.remove( i );
                                System.out.println( "Player Id " + din.getId() + " disconnected" );
                            }
                        }
                        input_streams.remove( din );
                    }
                    index++;
                }
            }
            // boolean normal = true;
            // boolean doubleT = false;
            // boolean half = false;
            boolean paused = false;
            for ( int i = 0; i < input_streams.size(); i++ ) {
                // System.out.println( pause.get( i ) );
                if ( pause.get( i ).equals( "1" ) ) {
                    paused = true;
                }

            }
            if ( !time.paused && paused ) {
                time.pause();
            }
            else if ( time.paused && !paused && !pausedInit ) {
                time.unpause();
            }
            synchronized ( server ) {
                if ( currentT1 != time.getCurrentTime() ) {
                    currentT1 = time.getCurrentTime();
                    long currentRT = 0;
                    if ( eventM.recording || eventM.replay ) {
                        currentRT = replayTime.getCurrentTime();
                    }
                    if ( input_streams.size() >= 1 ) {
                        if ( eventM.replay ) {
                            for ( int i = 0; i < game_objects.size(); i++ ) {
                                game_objects.get( i ).update( currentRT );
                            }
                            eventM.handleReplayEvents( currentRT );
                        }
                        if ( !eventM.replay ) {
                            for ( int i = 0; i < game_objects.size(); i++ ) {
                                game_objects.get( i ).update( currentT1 );
                            }
                            if ( eventM.recording ) {
                                eventM.handleEventsNow( currentT1, currentRT );
                            }
                            else {
                                eventM.handleEventsNow( currentT1, 0 );
                            }
                        }

                    }
                }
            }
            synchronized ( server ) {
                for ( final ObjectOutputStream dout : output_streams ) {
                    try {
                        dout.writeObject( game_objects );
                        dout.writeObject( time );
                        dout.writeInt( recording );
                        dout.writeInt( replayInit );
                        dout.writeInt( replay );
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
