package objects;

import java.io.Serializable;

import main.Server;
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

    public long               timesincelastchange;

    public long               elapsed;

    public Timeline ( int ticsize ) {
        tic = ticsize;
        paused = false;
        pausedTime = 0;
        anchorTime = false;
        doubleTime = ticsize / 2;
        halfTime = ticsize * 2;
        normalTime = ticsize;
        elapsed = 0;
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
        normalTime = ticsize;
        elapsed = 0;
        startA();
    }

    @Override
    public long getCurrentTime () {
        if ( paused ) {
            return ( pauseStart - startTime - pausedTime ) / tic;
        }
        long elapse;
        if ( anchorTime ) {
            elapse = optionalAnchor.getCurrentTime() - timesincelastchange;
        }
        else {
            elapse = System.currentTimeMillis() - timesincelastchange;
        }
        long r = ( elapse - pausedTime ) / tic;
        r += elapsed;
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
        timesincelastchange = startTime;

    }

    public void startA () {
        startTime = optionalAnchor.getCurrentTime();
        timesincelastchange = startTime;
        elapsed = 0;
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
            long t = System.currentTimeMillis() - timesincelastchange;
            elapsed += ( t - pausedTime ) / doubleTime;
            timesincelastchange = System.currentTimeMillis();
            this.tic = doubleTime;
        }

    }

    @Override
    public void halfTime () {
        if ( this.tic != halfTime ) {
            long t = System.currentTimeMillis() - timesincelastchange;
            elapsed += ( t - pausedTime ) / halfTime;
            timesincelastchange = System.currentTimeMillis();
            this.tic = halfTime;
        }
        if ( Server.replayInit == 1 ) {
            Server.replayInit = 0;
            Server.eventM.startReplay();
        }

    }

    public void normalTime () {
        if ( this.tic != normalTime ) {
            long t = System.currentTimeMillis() - timesincelastchange;
            elapsed += ( t - pausedTime ) / normalTime;
            timesincelastchange = System.currentTimeMillis();
            // long t = System.currentTimeMillis() - startTime;
            // elapsed += ( t - pausedTime ) / normalTime;
            this.tic = normalTime;
        }
        if ( Server.replayInit == 1 ) {
            Server.replayInit = 0;
            Server.eventM.startReplay();
        }
    }

}
