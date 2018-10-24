package objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
/**
 * 
 * @author Kevin Owczarski
 * Overwritten stream with id to match input 
 *
 */
public class ObjectOutputStreamId extends ObjectOutputStream {

    public int id;

    public ObjectOutputStreamId ( OutputStream out ) throws IOException {
        super( out );
    }

}
