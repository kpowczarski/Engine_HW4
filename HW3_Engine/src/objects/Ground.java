package objects;

import java.awt.Rectangle;

import main.GameLoop;
import processing.core.PApplet;

public class Ground extends GameObject implements Renderable {

    // public int x;

    // public int y;

    // public int width;

    // public int height;

    // public Rectangle ground;

    public Ground ( final int GUID ) {
        super( GUID );
        this.x = 0;
        this.y = 890;
        this.w = 1300;
        this.h = 10;
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
        p.rect.y = GameLoop.g.rect.y - 32;
    }

    @Override
    public void update () {
        // nothing

    }

}
