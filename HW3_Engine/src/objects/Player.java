package objects;

import java.awt.Rectangle;

import processing.core.PApplet;

public class Player extends GameObject implements Renderable {

    public Rectangle player;

    public int       width;

    public int       height;

    public float     velx;

    public float     vely;

    public PApplet   parent;

    public int       r;
    public int       g;
    public int       b;

    public Player ( final int GUID, final int x, final int y, final int width, final int height, final int r,
            final int g, final int b ) {
        super( GUID );
        player = new Rectangle( x, y, width, height );
        velx = 0;
        vely = 3;
        this.r = r;
        this.g = g;
        this.b = b;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render ( final PApplet p ) {
        parent = p;
        parent.fill( r, g, b );
        parent.rect( player.x, player.y, width, height );

    }

    public void setVelx ( final float velx ) {
        this.velx = velx;
    }

    public void setVely ( final float vely ) {
        this.vely = vely;
    }

}
