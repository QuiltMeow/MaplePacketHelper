package ew.quilt.tool.data.output;

import java.io.ByteArrayOutputStream;

public class BAOSByteOutputStream implements ByteOutputStream {

    private final ByteArrayOutputStream baos;

    public BAOSByteOutputStream(final ByteArrayOutputStream baos) {
        super();
        this.baos = baos;
    }

    @Override
    public void writeByte(final byte b) {
        baos.write(b);
    }
}
