package main;

import java.awt.Rectangle;
import java.util.ArrayList;

import objects.GameObject;
import objects.Ground;
import objects.Platform;
import objects.Player;
import processing.core.PApplet;
/**
 * 
 * @author Kevin Owczarski
 *
 */
public class GameLoop extends PApplet {

    public static Ground                g;
    Rectangle                           c;
    public static ArrayList<GameObject> gameobjs = new ArrayList<GameObject>();
    // Rectangle ob;
    // float velx;
    // float vely;
    float                               rVelx;
    float                               rVely;
    public static double                gravity  = .5;
    boolean                             justCol  = false;
    float                               moving   = 0;
    float                               walk     = 5;
    // boolean collidingP = false;
    boolean                             walking  = false;
    boolean                             jump;
    boolean                             re;
    float                               rC;
    float                               gC;
    float                               bC;
    ArrayList<Rectangle>                rect     = new ArrayList<Rectangle>();
    float                               velxs[];
    float                               velys[];
    Player                              player;
    Platform                            plat1;
    Platform                            plat2;
    Platform                            plat3;

    public static void main ( final String[] args ) {
        PApplet.main( "main.GameLoop" );

    }

    @Override
    public void settings () {
        size( 1300, 900 );
    }

    @Override
    public void setup () {
        // vely = 3;
        // velx = 0;
        rVely = 0;
        rVelx = 0;
        re = false;
        rC = random( 0, 255 );
        gC = random( 0, 255 );
        bC = random( 0, 255 );
        velxs = new float[10];
        velys = new float[10];
        for ( int i = 0; i < 7; i++ ) {
            velxs[i] = random( -5, 5 );
            velys[i] = random( -5, 5 );
        }
        rect.add( new Rectangle( 60, 800, 32, 32 ) );
        rect.add( new Rectangle( 160, 800, 32, 32 ) );
        rect.add( new Rectangle( 260, 800, 32, 32 ) );
        rect.add( new Rectangle( 560, 800, 32, 32 ) );
        rect.add( new Rectangle( 960, 800, 32, 32 ) );
        rect.add( new Rectangle( 1060, 800, 32, 32 ) );
        rect.add( new Rectangle( 1160, 800, 32, 32 ) );
        c = new Rectangle( 0, 0, 1300, 10 );
        // rect.add( new Rectangle( 0, 890, 900, 10) );
        // ob = new Rectangle( 30, 770, 100, 40 );
        g = new Ground();
        player = new Player( 32, 32, 255, 0, 0 );
        plat1 = new Platform( true, 30, 800, 100, 32, 255, 255, 0 );
        plat1.setMovingSettings( 850, 100, 0, 0, 2, 0 );
        plat2 = new Platform( true, 1000, 800, 100, 32, 126, 243, 233 );
        plat2.setMovingSettings( 850, 100, 0, 0, 3, 0 );
        plat3 = new Platform( false, 200, 600, 100, 32, 23, 43, 244 );
        player.setVelx( 0 );
        player.setVely( 3 );
        gameobjs.add( player );
        gameobjs.add( g );
        gameobjs.add( plat1 );
        gameobjs.add( plat2 );
        gameobjs.add( plat3 );
        // fill( 120, 50, 240 );
    }

    @Override
    public void draw () {
        background( 51 );
        // if ( player.rect.x > width ) {
        // player.rect.x = -31;
        // }
        // if ( player.rect.x < -32 ) {
        // player.rect.x = width - 1;
        // }
        // if ( player.isColliding && jump != false ) {
        // player.vely = -10;
        // }
        plat1.render( this );
        plat2.render( this );
        plat3.render( this );
        if ( re ) {
            player.velx = 0;
        }
        re = false;
        // collidingP = false;
        // player.vely += gravity;
        // player.rect.y += player.vely;
        // if ( player.vely > 5 ) {
        // player.vely = 5;
        // }
        plat1.update();
        plat2.update();
        player.update();
        player.render( this );
        // if ( g.ground.intersects( player.player ) ) {
        // collidingP = true;
        // player.player.y = g.y - 32;
        // }
        // if ( player.rect.intersects( ob ) ) {
        // if ( player.rect.y > ob.y ) {
        // if ( player.rect.y > ob.y && player.rect.y < ob.y + 100 ) {
        // player.velx *= -1;
        // }
        // else {
        // player.vely *= -1;
        // player.velx *= -1;
        // }
        // re = true;
        //
        // }
        // else {
        // player.isColliding = true;
        // player.rect.y = ob.y - 32;
        // player.vely = 0;
        // }
        // }
        fill( 0, 0, 0 );
        // rect( ob.x, ob.y, 100, 40 );
        g.render( this );
        rect( c.x, c.y, 1300, 10 );
        stroke( 0 );
        fill( 255, 0, 0 );
        // player.rect.x += player.velx;

        for ( int i = 0; i < rect.size(); i++ ) {
            final Rectangle r = rect.get( i );
            if ( r.intersects( player.rect ) ) {
                if ( velxs[i] < 0 && player.velx < 0 ) {
                    velxs[i] += player.velx;
                }
                else if ( velxs[i] > 0 && player.velx > 0 ) {
                    velxs[i] += player.velx;
                }
                else {
                    velxs[i] *= -1;
                    velxs[i] += player.velx;
                }
                if ( velys[i] < 0 && player.vely < 0 ) {
                    velys[i] += player.vely;
                }
                else if ( velys[i] > 0 && player.vely > 0 ) {
                    velys[i] += player.vely;
                }
                else {
                    velys[i] *= -1;
                    velys[i] += player.vely;
                }

                // rVely *= -1;
                justCol = true;

            }
            for ( int j = 0; j < rect.size(); j++ ) {
                if ( j != i ) {
                    if ( r.intersects( rect.get( j ) ) ) {
                        if ( velys[i] == 0 && velys[j] == 0 ) {
                            velys[i] += 1;
                            velys[j] += 1;
                        }
                        if ( velys[i] == 0 ) {
                            velys[i] += velys[j];
                        }
                        if ( velys[j] == 0 ) {
                            velys[j] += velys[i];
                        }
                        velxs[i] *= -1;
                        velxs[j] *= -1;
                        velys[i] *= -1;
                        velys[j] *= -1;
                    }
                }
            }
            // rVely += gravity;
            if ( velxs[i] > 7 ) {
                velxs[i] = 7;
            }
            if ( velxs[i] < -7 ) {
                velxs[i] = -7;
            }

            if ( velys[i] > 7 ) {
                velys[i] = 7;
            }
            if ( velys[i] < -7 ) {
                velys[i] = -7;
            }
            if ( r.y > 858 ) {
                r.y = 858;
            }
            if ( r.y < 10 ) {
                r.y = 10;
                velys[i] += 1;
            }
            r.y += velys[i];
            if ( g.rect.intersects( r ) ) {
                velys[i] *= -1;
            }
            if ( c.intersects( r ) ) {
                velys[i] *= -1;
            }
            if ( r.x > width ) {
                r.x = -32;
            }
            if ( r.x < -32 ) {
                r.x = width;
            }
            r.x += velxs[i];
            fill( rC, gC, bC );
            rect( r.x, r.y, r.width, r.height );

        }
    }

    @Override
    public void keyPressed () {
        if ( key == 'a' ) {
            if ( !re ) {
                player.velx = -walk;
                walking = true;
            }

        }
        if ( key == 'd' ) {
            player.velx = walk;
            walking = true;
        }
        if ( key == ' ' ) {
            if ( player.isColliding ) {
                player.jump = true;
            }
        }
    }

    @Override
    public void keyReleased () {
        if ( key == 'a' ) {
            player.velx = 0;
            walking = false;
        }
        if ( key == 'd' ) {
            player.velx = 0;
            walking = false;
        }
        if ( key == ' ' ) {
            player.jump = false;
        }
    }

}
