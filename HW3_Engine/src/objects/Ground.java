package objects;

import java.awt.Rectangle;

import processing.core.PApplet;

public class Ground extends GameObject implements Renderable {

    // public int x;

    // public int y;

    // public int width;

    // public int height;

    // public Rectangle ground;

    private static final long serialVersionUID = 1L;

    public Ground () {
        this.x = 0;
        this.y = 870;
        this.w = 300;
        this.h = 100;
        this.rect = new Rectangle( x, y, w, h );
        this.type = "g";
    }

    @Override
    public void render ( final PApplet p ) {
        p.fill( 0, 0, 0 );
        p.rect( x, y, w, h );
    }

    @Override
    public void handleCollision ( final GameObject o ) {
        final Player p = (Player) o;
        p.isColliding = true;
        p.rect.y = this.rect.y - 32;
    }

    @Override
    public void update () {
        // nothing

    }

}
