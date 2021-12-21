package ew.quilt.tool.data.input;

public interface ByteInputStream {

    int readByte();

    long getBytesRead();

    long available();

    String toString(final boolean b);
}
