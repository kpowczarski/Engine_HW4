package space_game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import objects.Event;
import objects.Events;
import objects.GameObject;
import objects.ObjectInputStreamId;
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
                    final SpacePlayer newPlayer = new SpacePlayer( 64, 16, 255, 0, 0 );
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
        game_objects = new ArrayList<GameObject>();
        Alien a1 = new Alien( 356, 64, true );
        Alien a2 = new Alien( 500, 64, false );
        Alien a3 = new Alien( 648, 64, true );
        Alien a4 = new Alien( 796, 64, false );
        Alien a5 = new Alien( 944, 64, true );
        Alien a6 = new Alien( 1092, 64, false );
        game_objects.add( a1 );
        game_objects.add( a2 );
        game_objects.add( a3 );
        game_objects.add( a4 );
        game_objects.add( a5 );
        game_objects.add( a6 );
        Alien a1r2 = new Alien( 356, 128, false );
        Alien a2r2 = new Alien( 500, 128, true );
        Alien a3r2 = new Alien( 648, 128, false );
        Alien a4r2 = new Alien( 796, 128, true );
        Alien a5r2 = new Alien( 944, 128, false );
        Alien a6r2 = new Alien( 1092, 128, true );
        game_objects.add( a1r2 );
        game_objects.add( a2r2 );
        game_objects.add( a3r2 );
        game_objects.add( a4r2 );
        game_objects.add( a5r2 );
        game_objects.add( a6r2 );
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
        long shootTime = time.getCurrentTime() + 50;
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
                        int shoot = din.readInt();
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
                                if ( Space_Server.replayInit == 1 ) {
                                    Space_Server.replayInit = 0;
                                    Space_Server.eventM.startReplay();
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
                                if ( Space_Server.replayInit == 1 ) {
                                    Space_Server.replayInit = 0;
                                    Space_Server.eventM.startReplay();
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
                                if ( Space_Server.replayInit == 1 ) {
                                    Space_Server.replayInit = 0;
                                    Space_Server.eventM.startReplay();
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
                                    if ( shoot == 1 && time.getCurrentTime() > shootTime ) {
                                        shootTime = time.getCurrentTime() + 50;
                                        Event r = new Event( Events.SHOOT, game_objects.get( i ),
                                                time.getCurrentTime() );
                                        eventM.addEvent( r );
                                    }
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
