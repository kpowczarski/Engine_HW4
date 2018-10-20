package objects;

import java.awt.Rectangle;

import processing.core.PApplet;

public class Player extends GameObject implements Renderable {

    // public Rectangle player;

    // public int width;

    // public int height;

    // public float velx;

    // public float vely;

    public PApplet parent;

    public int     r;
    public int     g;
    public int     b;

    public boolean isColliding;

    public Player ( final int GUID, final int x, final int y, final int width, final int height, final int r,
            final int g, final int b ) {
        super( GUID );

        this.rect = new Rectangle( x, y, width, height );
        this.velx = 0;
        this.vely = 3;
        this.r = r;
        this.g = g;
        this.b = b;
        this.w = width;
        this.h = height;
        isColliding = false;
        this.type = "player";
    }

    @Override
    public void render ( final PApplet p ) {
        parent = p;
        parent.fill( r, g, b );
        parent.rect( rect.x, rect.y, w, h );

    }

    public void setVelx ( final float velx ) {
        this.velx = velx;
    }

    public void setVely ( final float vely ) {
        this.vely = vely;
    }

    @Override
    public void update ( final PApplet p ) {
        parent = p;
        isColliding = false;
        collides();
        this.render( parent );
    }

}
