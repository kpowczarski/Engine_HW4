package objects;

import java.awt.Rectangle;

import main.Client;
import processing.core.PApplet;

public class Player extends GameObject implements Renderable {

    // public Rectangle player;

    // public int width;

    // public int height;

    // public float velx;

    // public float vely;

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
    public void render ( final PApplet parent ) {
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
    public void update () {
        if ( this.rect.x > Client.parentWidth ) {
            this.rect.x = -31;
        }
        if ( this.rect.x < -32 ) {
            this.rect.x = Client.parentWidth - 1;
        }
        if ( this.isColliding && jump != false ) {
            this.vely = -10;
        }
        isColliding = false;
        this.vely += gravity;
        this.rect.y += this.vely;
        if ( this.vely > 5 ) {
            this.vely = 5;
        }
        this.rect.x += this.velx;
        collides();
    }

}
