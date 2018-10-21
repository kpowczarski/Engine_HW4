package objects;

import java.awt.Rectangle;

import main.GameLoop;

public abstract class GameObject implements Renderable {

    public final int     GUID;

    public int           x, y, w, h;

    public float         velx;

    public float         vely;

    public static double gravity = .5;

    public Rectangle     rect;

    public String        type;

    public GameObject ( final int GUID ) {
        this.GUID = GUID;
    }

    public Rectangle getRect () {
        return this.rect;
    }

    public abstract void update ();

    public void handleCollision ( final GameObject o ) {
        // nothing
    }

    public boolean collides () {
        // if ( p.player.intersects( GameLoop.g.ground ) ) {
        // p.isColliding = true;
        // p.player.y = GameLoop.g.ground.y - 32;
        // return true;
        // }
        for ( int i = 0; i < GameLoop.gameobjs.size(); i++ ) {
            final GameObject go = GameLoop.gameobjs.get( i );
            if ( go.GUID != this.GUID ) {
                if ( this.rect.intersects( go.getRect() ) ) {
                    go.handleCollision( this );
                    return true;
                }
            }
        }
        return false;

    }

}
