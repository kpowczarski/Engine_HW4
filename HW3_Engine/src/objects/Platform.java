package objects;

import java.awt.Rectangle;

import processing.core.PApplet;

public class Platform extends GameObject implements Renderable {

    public int     r;
    public int     g;
    public int     b;

    // public Rectangle platform;

    public boolean moving;

    public Platform ( final int GUID, final boolean moving, final int x, final int y, final int width, final int height,
            final int r, final int g, final int b ) {
        super( GUID );
        this.moving = moving;
        this.r = r;
        this.g = g;
        this.b = b;
        this.rect = new Rectangle( x, y, width, height );
        this.x = x;
        this.y = y;
        this.velx = 0;
        this.vely = 3;
        this.type = "plat";
    }

    @Override
    public void render ( final PApplet p ) {
        p.fill( r, g, b );
        p.rect( rect.x, rect.y, rect.width, rect.height );
    }

    @Override
    public void handleCollision ( final GameObject o ) {
        if ( o.type.equals( "player" ) ) {
            final Player p = (Player) o;
            System.out.println( p.rect.y + 31 );
            System.out.println( this.y );
            if ( p.rect.y < this.rect.y ) {
                p.rect.y = this.rect.y - 31;
                p.vely = this.vely;
                p.isColliding = true;
            }
            else if ( p.rect.y > this.rect.y ) {
                p.vely = this.vely;
                p.rect.y = this.rect.y + this.rect.height;
            }
            else if ( p.velx < 0 && p.rect.x + p.velx < this.rect.x ) {
                p.velx = this.velx;
            }

        }
    }

    @Override
    public void update ( final PApplet p ) {
        this.rect.y += this.vely;

    }

}
