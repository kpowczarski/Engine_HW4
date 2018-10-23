package objects;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectInputStreamId extends ObjectInputStream {

    public int id;

    public ObjectInputStreamId ( final InputStream in, final int id ) throws IOException {
        super( in );
        this.id = id;
    }

    public int getId () {
        return id;
    }

}
