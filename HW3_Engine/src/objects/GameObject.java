package objects;

import java.awt.Rectangle;
import java.io.Serializable;

import main.Server;
import main.Space_Server;

/**
 *
 * @author Kevin Owczarski Abstract Game object that is inherited by all objects
 *
 */
public abstract class GameObject implements Renderable, Serializable {

    private static final long serialVersionUID = 1L;

    public final int          GUID;

    public static int         GUIDcount        = 0;

    public int                x, y, w, h;

    public float              velx;

    public float              vely;

    public int                rx, ry;

    public float              rvelx, rvely;

    public int                fx, fy;

    public float              fvelx, fvely;

    public boolean            rjump            = false;

    public boolean            fjump            = false;

    public static double      gravity          = .5;

    public boolean            jump             = false;

    public Rectangle          rect;

    public String             type;

    public GameObject () {
        this.GUID = GUIDcount;
        GUIDcount++;
    }

    public Rectangle getRect () {
        return this.rect;
    }

    public void update ( long time ) {
        // nothing
    }

    public void handleCollision ( final GameObject o ) {
        // nothing
    }

    public boolean collides ( long time ) {
        // if ( p.player.intersects( GameLoop.g.ground ) ) {
        // p.isColliding = true;
        // p.player.y = GameLoop.g.ground.y - 32;
        // return true;
        // }
    	if (Server.eventM != null) {
	        for ( int i = 0; i < Server.game_objects.size(); i++ ) {
	            final GameObject go = Server.game_objects.get( i );
	            if ( go.GUID != this.GUID ) {
	                if ( this.rect.intersects( go.getRect() ) ) {
	                    // go.handleCollision( this );
	                    if ( Server.eventM.replay ) {
	                        Event e = new Event( Events.COLLISION, go, this, Server.replayTime.getCurrentTime() );
	                        Server.eventM.addEventToBuffer( e );
	                        return true;
	                    }
	                    Event e = new Event( Events.COLLISION, go, this, time );
	                    Server.eventM.addEvent( e );
	                    return true;
	                }
	            }
	        }
    	}
    	else {
    		for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                final GameObject go = Space_Server.game_objects.get( i );
                if ( go.GUID != this.GUID ) {
                    if ( this.rect.intersects( go.getRect() ) ) {
                        // go.handleCollision( this );
                        Event e = new Event( Events.COLLISION, go, this, time );
                        Space_Server.eventM.addEvent( e );
                        return true;
                    }
                }
            }
    	}
        return false;

    }

    public void handleMovement ( int f, int anti ) {
        // nothing if not player

    }

    public void setReplay () {
        this.rx = this.rect.x;
        this.ry = this.rect.y;
        this.rvelx = this.velx;
        this.rvely = this.vely;
        this.rjump = this.jump;
    }

    public void teleportStartReplay () {
        this.rect.x = this.rx;
        this.rect.y = this.ry;
        this.velx = this.rvelx;
        this.vely = this.rvely;
        this.jump = this.rjump;
    }

    public void saveFinalState () {
        this.fx = this.rect.x;
        this.fy = this.rect.y;
        this.fvelx = this.velx;
        this.fvely = this.vely;
        this.fjump = this.jump;
    }

    public void restoreFinalState () {
        this.rect.x = this.fx;
        this.rect.y = this.fy;
        this.velx = this.fvelx;
        this.vely = this.fvely;
        this.jump = this.fjump;
    }

}
