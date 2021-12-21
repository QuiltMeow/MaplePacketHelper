package ew.quilt.tool.data.input;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamByteStream implements ByteInputStream {

    private final InputStream is;
    private long read = 0;

    public InputStreamByteStream(final InputStream is) {
        this.is = is;
    }

    @Override
    public final int readByte() {
        int temp;
        try {
            temp = is.read();
            if (temp == -1) {
                throw new RuntimeException("讀取位元組發生錯誤");
            }
            ++read;
            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final long getBytesRead() {
        return read;
    }

    @Override
    public final long available() {
        try {
            return is.available();
        } catch (IOException e) {
            System.err.println("發生錯誤：" + e);
            return 0;
        }
    }

    @Override
    public final String toString(final boolean b) {
        return toString();
    }
}
