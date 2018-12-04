package space_game;

import java.awt.Rectangle;
import java.util.ArrayList;

import main.Client;
import objects.GameObject;
import objects.Spawnpoint;
import processing.core.PApplet;

public class SpacePlayer extends GameObject {
	
	public ArrayList<Spawnpoint> spawnlist;
	
	private static final long serialVersionUID = 1L;
	public int                   r;
    public int                   g;
    public int                   b;
	public SpacePlayer(final int width, final int height, final int r, final int g, final int b ) {
		Spawnpoint s1 = new Spawnpoint( 10, 838 );
        spawnlist.add( s1 );
        this.velx = 0;
        this.vely = 0;
        this.r = r;
        this.g = g;
        this.b = b;
        this.w = width;
        this.h = height;
        this.type = "spaceplayer";
        this.rect = new Rectangle( 0, 0, width, height );
	}

	@Override
	public void render(PApplet p) {
		// TODO Auto-generated method stub

	}
	
	@Override
    public void update ( long time ) {
        if ( this.rect.x > Client.parentWidth ) {
            this.rect.x = -31;
        }
        if ( this.rect.x < -32 ) {
            this.rect.x = Client.parentWidth - 1;
        }
        this.rect.y += this.vely;
        if ( this.vely > 5 ) {
            this.vely = 5;
        }
        // this.rect.x += this.velx;
        collides( time );
    }

    @Override
    public void handleMovement ( int move, int anti ) {
        if ( move != 0 ) {
            if ( move == 1 ) {
                this.velx = -3;
            }
            else if ( move == 2 ) {
                this.velx = 3;
            }
            else if ( move == 3 ) {
                jump = true;
            }
        }
        if ( anti != 0 ) {
            if ( anti == 1 ) {
                this.velx = 0;

            }
            else if ( anti == 2 ) {
                this.velx = 0;

            }
            else if ( anti == 3 ) {
                jump = false;
            }
        }
        this.rect.x += this.velx;
    }

    public void handleDeathEvent ( boolean d, int x, int y ) {
        if ( d ) {
            Spawnpoint s = spawnlist.get( 0 );
            this.rect.x = s.x;
            this.rect.y = s.y;
        }
        else {
            this.rect.x = x;
            this.rect.y = y;
        }
    }

    public void handleSpawnEvent () {
        Spawnpoint s = spawnlist.get( 0 );
        this.rect.x = s.x;
        this.rect.y = s.y;
    }

}
