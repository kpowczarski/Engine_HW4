package objects;

import processing.core.PApplet;

public class Spawnpoint extends GameObject {

    private static final long serialVersionUID = 1L;

    public Spawnpoint ( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render ( PApplet p ) {
        // doesn't need to be rendered
    }

}
