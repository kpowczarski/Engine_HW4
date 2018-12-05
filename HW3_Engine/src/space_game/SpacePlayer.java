package space_game;

import java.awt.Rectangle;
import java.util.ArrayList;

import objects.Event;
import objects.Events;
import objects.GameObject;
import objects.Spawnpoint;
import processing.core.PApplet;

public class SpacePlayer extends GameObject {

    public ArrayList<Spawnpoint> spawnlist;

    private static final long    serialVersionUID = 1L;
    public int                   r;
    public int                   g;
    public int                   b;

    public SpacePlayer ( final int width, final int height, final int r, final int g, final int b ) {
        spawnlist = new ArrayList<Spawnpoint>();
        Spawnpoint s1 = new Spawnpoint( 10, 838 );
        spawnlist.add( s1 );
        this.velx = 0;
        this.vely = 0;
        this.r = r;
        this.g = g;
        this.b = b;
        this.w = width;
        this.h = height;
        this.type = "spaceplayer";
        this.rect = new Rectangle( 0, 0, width, height );
    }

    @Override
    public void render ( PApplet p ) {
        p.fill( r, g, b );
        p.stroke( r, g, b );
        p.rect( rect.x, rect.y, w, h );
        p.triangle( rect.x + 16, rect.y, rect.x + 32, rect.y - 16, rect.x + 48, rect.y );

    }

    @Override
    public void update ( long time ) {
        // this.rect.y += this.vely;
        // this.rect.x += this.velx;
        collides( time );
    }

    @Override
    public void handleMovement ( int move, int anti ) {
        if ( move != 0 ) {
            if ( move == 1 ) {
                this.velx = -3;
            }
            else if ( move == 2 ) {
                this.velx = 3;
            }
            else if ( move == 3 ) {
                // jump = true;
            }
        }
        if ( anti != 0 ) {
            if ( anti == 1 ) {
                this.velx = 0;

            }
            else if ( anti == 2 ) {
                this.velx = 0;

            }
            else if ( anti == 3 ) {
                // jump = false;
            }
        }
        this.rect.x += this.velx;
        if ( this.rect.x + 70 > Space_Client.parentWidth ) {
            this.rect.x = this.rect.x - 3;
        }
        if ( this.rect.x < 6 ) {
            this.rect.x = this.rect.x + 3;
        }

    }

    public void handleDeathEvent ( boolean d, int x, int y ) {
        if ( d ) {
            Spawnpoint s = spawnlist.get( 0 );
            this.rect.x = s.x;
            this.rect.y = s.y;
        }
        else {
            this.rect.x = x;
            this.rect.y = y;
        }
    }

    public void handleSpawnEvent () {
        Spawnpoint s = spawnlist.get( 0 );
        this.rect.x = s.x;
        this.rect.y = s.y;
    }

    @Override
    public void handleCollision ( final GameObject o ) {
        if ( o.type.equals( "b" ) ) {
            Bullet b = (Bullet) o;
            if ( b.evil ) {
                Event e = new Event( Events.DEATH, this, o, Space_Server.time.getCurrentTime() );
                Space_Server.eventM.addEvent( e );
                b.render = false;
            }
        }
    }

}
