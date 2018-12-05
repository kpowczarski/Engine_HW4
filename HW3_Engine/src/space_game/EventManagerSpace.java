package space_game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PriorityQueue;

import objects.Event;
import objects.EventCompare;
import objects.Events;
import objects.GameObject;
import objects.ScriptManager;
import objects.Timeline;

public class EventManagerSpace implements Serializable {

    private static final long   serialVersionUID = 1L;

    public PriorityQueue<Event> events;

    public PriorityQueue<Event> recordBuffer;

    // public ArrayList<GameObject> objectsInitialState;
    // public ArrayList<GameObject> objectsFinalState;

    public boolean              recording        = false;

    public boolean              replay           = false;

    public int                  alienCountInit   = 18;

    public int                  alienCount       = 18;

    public EventManagerSpace ( ArrayList<GameObject> g ) {
        events = new PriorityQueue<Event>( 10, new EventCompare() );
        recordBuffer = new PriorityQueue<Event>( 50, new EventCompare() );
        // objectsInitialState = new ArrayList<GameObject>();
        // objectsFinalState = new ArrayList<GameObject>();
        recording = false;
        replay = false;
    }

    public void addEvent ( Event e ) {
        events.add( e );
    }

    public void addEventToBuffer ( Event e ) {
        recordBuffer.add( e );
    }

    public void startReplay () {
        // Server.replayTime.startA();
        for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
            Space_Server.game_objects.get( i ).saveFinalState();
            Space_Server.game_objects.get( i ).teleportStartReplay();
        }

    }

    public void handleReplayEvents ( long time ) {
        // long time = Server.replayTime.getCurrentTime();
        if ( recordBuffer.peek() == null ) {
            replay = false;
            Space_Server.replay = 0;
            Space_Server.time.normalTime();
            for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                Space_Server.game_objects.get( i ).restoreFinalState();
            }
        }
        while ( recordBuffer.peek() != null && recordBuffer.peek().timestamp <= time ) {
            Event curE = recordBuffer.poll();
            if ( curE.type == Events.DEATH ) {
                SpacePlayer p = (SpacePlayer) curE.ob1;
                ScriptManager.loadScript( "scripts/change_death_options.js" );
                ScriptManager.bindArgument( "player", p );
                ScriptManager.executeScript();
                // p.handleDeathEvent();
            }
            else if ( curE.type == Events.SPAWN ) {
                SpacePlayer pS = (SpacePlayer) curE.ob1;
                pS.handleSpawnEvent();
                Space_Server.game_objects.add( pS );
            }
            else if ( curE.type == Events.COLLISION ) {
                curE.ob1.handleCollision( curE.ob2 );
            }
            else if ( curE.type == Events.MOVEMENT ) {
                curE.ob1.handleMovement( curE.optionalArg1, curE.optionalArg2 );
            }
            else if ( curE.type == Events.STOPRECORD ) {
                replay = false;
                Space_Server.replay = 0;
                Space_Server.time.normalTime();
                for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                    Space_Server.game_objects.get( i ).restoreFinalState();
                }
            }

        }
    }

    public void handleEventsNow ( long time, long rtime ) {
        boolean moved = false;
        while ( events.peek() != null && events.peek().timestamp <= time ) {
            Event curE = events.poll();
            if ( recording && curE.type != Events.STOPRECORD ) {
                curE.timestamp = rtime;
                addEventToBuffer( curE );
                // System.out.println( "Added Event to buffer" );
            }
            if ( curE.type == Events.DEATH ) {
                if ( !recording ) {
                    for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                        if ( Space_Server.game_objects.get( i ).GUID == curE.ob2.GUID ) {
                            Space_Server.game_objects.remove( i );
                        }
                    }
                }
                if ( curE.ob1.type.equals( "spaceplayer" ) ) {
                    SpacePlayer p = (SpacePlayer) curE.ob1;
                    ScriptManager.loadScript( "scripts/change_death_options.js" );
                    ScriptManager.bindArgument( "player", p );
                    ScriptManager.executeScript();
                    // p.handleDeathEvent();
                }
                else {
                    Alien a = (Alien) curE.ob1;
                    alienCount -= 1;
                    if ( alienCount == alienCountInit - 2 ) {
                        Alien.alienMovementDelta = 50;
                    }
                    if ( alienCount == alienCountInit / 2 ) {
                        Alien.alienMovementDelta = 30;
                    }
                    if ( alienCount == 4 ) {
                        Alien.alienMovementDelta = 20;
                    }
                    if ( alienCount == 2 ) {
                        Alien.alienMovementDelta = 10;
                    }
                    if ( alienCount == 1 ) {
                        Alien.alienMovementDelta = 5;
                    }
                    a.handleDeathEvent();
                }
            }
            else if ( curE.type == Events.ALIENSMOVEDOWN ) {
                if ( !moved ) {
                    for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                        if ( Space_Server.game_objects.get( i ).type.equals( "alien" ) ) {
                            Space_Server.game_objects.get( i ).rect.y += 64;
                            Space_Server.game_objects.get( i ).velx *= -1;
                            moved = true;
                        }
                    }
                }
            }
            else if ( curE.type == Events.SPAWN ) {
                SpacePlayer pS = (SpacePlayer) curE.ob1;
                pS.handleSpawnEvent();
                Space_Server.game_objects.add( pS );
            }
            else if ( curE.type == Events.COLLISION ) {
                curE.ob1.handleCollision( curE.ob2 );
            }
            else if ( curE.type == Events.MOVEMENT ) {
                curE.ob1.handleMovement( curE.optionalArg1, curE.optionalArg2 );
            }
            else if ( curE.type == Events.SHOOT ) {
                Bullet b = new Bullet( curE.ob1.rect.x + 30, curE.ob1.rect.y - 17, false );
                Space_Server.game_objects.add( b );
            }
            else if ( curE.type == Events.RECORD ) {
                recording = true;
                Space_Server.replayTime = new Timeline( 1, Space_Server.time );
                curE.timestamp = Space_Server.replayTime.getCurrentTime();
                addEventToBuffer( curE );
                Space_Server.recording = 1;
                for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                    Space_Server.game_objects.get( i ).setReplay();
                }
            }
            else if ( curE.type == Events.STOPRECORD ) {
                recording = false;
                Space_Server.time.pause();
                curE.timestamp = rtime;
                addEventToBuffer( curE );
                Space_Server.recording = 0;
                Space_Server.pausedInit = true;
                Space_Server.replayInit = 1;
                Space_Server.replay = 1;

            }
        }
    }

    // public static void main(String args[]) {
    // ArrayList<GameObject> g = new ArrayList<GameObject>();
    // EventManager e = new EventManager(g);
    // Event e1 = new Event(Events.DEATH, 1, 123);
    // Event e2 = new Event(Events.COLLISION, 2, 123);
    // Event e3 = new Event(Events.DEATH, 3, 123);
    // Event e4 = new Event(Events.COLLISION, 4, 124);
    // e.addEvent(e2);
    // e.addEvent(e1);
    // e.addEvent(e3);
    // e.addEvent(e4);
    // e.events.poll();
    // e.events.poll();
    // }

}
