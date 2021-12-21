package ew.quilt.tool.data.input;

import java.io.IOException;

public interface SeekableInputStreamBytestream extends ByteInputStream {

    void seek(long offset) throws IOException;

    long getPosition() throws IOException;

    @Override
    String toString(final boolean b);
}
