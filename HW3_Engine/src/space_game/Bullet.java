package space_game;

import java.awt.Rectangle;

import objects.GameObject;
import processing.core.PApplet;

public class Bullet extends GameObject {

    private static final long serialVersionUID = 1L;

    int                       r;
    int                       g;
    int                       b;
    boolean                   render;
    boolean                   evil;

    public Bullet ( int x, int y, boolean evil ) {
        this.type = "b";
        this.w = 2;
        this.h = 6;
        render = true;
        this.evil = evil;
        if ( evil ) {
            this.vely = 8;
        }
        else {
            this.vely = -8;
        }
        r = 255;
        g = 255;
        b = 255;
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
        this.rect.y += this.vely;
        this.rect.x += this.velx;
        if ( ( this.y > Space_Client.parentHeight || this.y < 0 ) && !Space_Server.eventM.recording ) {
            for ( int i = 0; i < Space_Server.game_objects.size(); i++ ) {
                if ( Space_Server.game_objects.get( i ).GUID == this.GUID ) {
                    Space_Server.game_objects.remove( i );
                }
            }
        }
        collides( time );
    }

    public void changeOptions ( boolean d, int r, int g, int b, int w, int h, int velx, int vely ) {
        if ( !d ) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.rect.width = w;
            this.w = w;
            this.h = h;
            this.rect.height = h;
            this.velx = velx;
            this.vely = vely;
        }
    }

}
