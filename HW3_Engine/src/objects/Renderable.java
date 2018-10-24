package objects;

import processing.core.PApplet;
/**
 * 
 * @author Kevin Owczarski
 * Interface that most object will have to render to a PApplet
 *
 */
public interface Renderable {

    public void render ( PApplet p );

}
