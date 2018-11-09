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

    public int                doubleTime;

    public int                halfTime;

    public int                normalTime;

    public Timeline ( int ticsize ) {
        tic = ticsize;
        paused = false;
        pausedTime = 0;
        anchorTime = false;
        doubleTime = ticsize / 2;
        halfTime = ticsize * 2;
        normalTime = ticsize;
        start();
    }

    public Timeline ( int ticsize, Timeline t ) {
        tic = ticsize;
        paused = false;
        pausedTime = 0;
        optionalAnchor = t;
        anchorTime = true;
        doubleTime = tic / 2;
        halfTime = tic * 2;
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

    @Override
    public void doubleTime () {
        if ( this.tic != doubleTime ) {
            this.tic = doubleTime;
        }

    }

    @Override
    public void halfTime () {
        if ( this.tic != halfTime ) {
            this.tic = halfTime;
        }

    }

    public void normalTime () {
        this.tic = normalTime;
    }

}
