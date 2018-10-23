package objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectOutputStreamId extends ObjectOutputStream {

    public int id;

    public ObjectOutputStreamId ( OutputStream out ) throws IOException {
        super( out );
    }

}
