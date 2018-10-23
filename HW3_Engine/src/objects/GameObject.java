package objects;

import java.awt.Rectangle;
import java.io.Serializable;

import main.Server;

public abstract class GameObject implements Renderable, Serializable {

    private static final long serialVersionUID = 1L;

    public final int          GUID;

    public static int         GUIDcount        = 0;

    public int                x, y, w, h;

    public float              velx;

    public float              vely;

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

    public void update () {
        // nothing
    }

    public void handleCollision ( final GameObject o ) {
        // nothing
    }

    public boolean collides () {
        // if ( p.player.intersects( GameLoop.g.ground ) ) {
        // p.isColliding = true;
        // p.player.y = GameLoop.g.ground.y - 32;
        // return true;
        // }
        for ( int i = 0; i < Server.game_objects.size(); i++ ) {
            final GameObject go = Server.game_objects.get( i );
            if ( go.GUID != this.GUID ) {
                if ( this.rect.intersects( go.getRect() ) ) {
                    go.handleCollision( this );
                    return true;
                }
            }
        }
        return false;

    }

    public void handleMovement ( int f, int anti ) {
        // nothing if not player

    }

}
