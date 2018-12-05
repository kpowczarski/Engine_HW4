package space_game;

import java.awt.Rectangle;

import objects.Event;
import objects.Events;
import objects.GameObject;
import processing.core.PApplet;

public class Alien extends GameObject {

    private static final long serialVersionUID   = 1L;

    public static int         alienMovementDelta = 100;

    int                       r;
    int                       g;
    int                       b;
    boolean                   render;
    long                      moveTime;

    public Alien ( int x, int y, boolean gr ) {
        this.type = "alien";
        moveTime = 0;
        render = true;
        this.velx = 10;
        if ( gr ) {
            r = 127;
            g = 255;
            b = 0;
        }
        else {
            r = 220;
            g = 20;
            b = 60;
        }
        this.w = 64;
        this.h = 32;
        this.rect = new Rectangle( x, y, w, h );

    }

    @Override
    public void render ( PApplet p ) {
        if ( render ) {
            p.fill( r, g, b );
            p.stroke( r, g, b );
            p.rect( rect.x, rect.y, w, h );
        }

    }

    @Override
    public void update ( long time ) {
        if ( time > moveTime ) {
            moveTime = time + alienMovementDelta;
            this.rect.x += this.velx;
            this.rect.x += this.velx;
            if ( this.rect.x + 204 > Space_Client.parentWidth ) {
                Event e = new Event( Events.ALIENSMOVEDOWN, this, Space_Server.time.getCurrentTime() );
                Space_Server.eventM.addEvent( e );
                // this.rect.x = this.rect.x - 2;
                // this.rect.y += 64;
                // this.velx *= -1;
            }
            if ( this.rect.x < 134 ) {
                Event e = new Event( Events.ALIENSMOVEDOWN, this, Space_Server.time.getCurrentTime() );
                Space_Server.eventM.addEvent( e );
                // this.rect.x = this.rect.x + 2;
                // this.rect.y += 64;
                // this.velx *= -1;
            }
        }
        // collides( time );
    }

    @Override
    public void handleCollision ( final GameObject o ) {
        if ( o.type.equals( "b" ) ) {
            Bullet b = (Bullet) o;
            if ( !b.evil ) {
                Event e = new Event( Events.DEATH, this, o, Space_Server.time.getCurrentTime() );
                Space_Server.eventM.addEvent( e );
                b.render = false;
            }
        }
    }

    public void handleDeathEvent () {
        if ( render ) {
            render = false;
            for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                if ( Space_Server.game_objects.get( i ).GUID == this.GUID ) {
                    Space_Server.game_objects.remove( i );
                }
            }
        }

    }

}
