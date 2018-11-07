package objects;

import java.io.Serializable;

import processing.core.PApplet;

public class Timeline implements TimelineI, Serializable, Renderable {

    private static final long serialVersionUID = 1L;

    public long               startTime;

    public long               pausedTime;

    public long               pauseStart;

    public int                tic;

    public boolean            paused;

    public Timeline           optionalAnchor;

    public boolean            anchorTime;

    public Timeline ( int ticsize ) {
        tic = ticsize;
        paused = false;
        pausedTime = 0;
        anchorTime = false;
        start();
    }

    public Timeline ( int ticsize, Timeline t ) {
        tic = ticsize;
        paused = false;
        pausedTime = 0;
        optionalAnchor = t;
        anchorTime = true;
        start();
    }

    @Override
    public long getCurrentTime () {
        if ( paused ) {
            return ( pauseStart - startTime - pausedTime ) / tic;
        }
        long elapsed;
        if ( anchorTime ) {
            elapsed = optionalAnchor.getCurrentTime() - startTime;
        }
        else {
            elapsed = System.currentTimeMillis() - startTime;
        }
        long r = ( elapsed - pausedTime ) / tic;
        return r;
    }

    @Override
    public void pause () {
        if ( !paused ) {
            pauseStart = System.currentTimeMillis();
            paused = true;
        }

    }

    @Override
    public void unpause () {
        if ( paused ) {
            pausedTime += System.currentTimeMillis() - pauseStart;
            paused = false;
        }
    }

    @Override
    public void start () {
        startTime = System.currentTimeMillis();

    }

    @Override
    public void render ( PApplet p ) {
        p.fill( 255 );
        p.textSize( 20 );
        p.text( "Time: " + getCurrentTime(), 1100, 30 );

    }

}
