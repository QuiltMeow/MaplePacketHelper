package ew.quilt.tool.data.input;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class GenericLittleEndianAccessor implements LittleEndianAccessor {

    private final ByteInputStream bs;

    public GenericLittleEndianAccessor(final ByteInputStream bs) {
        this.bs = bs;
    }

    @Override
    public final int readByteAsInt() {
        return bs.readByte();
    }

    @Override
    public final byte readByte() {
        return (byte) bs.readByte();
    }

    @Override
    public final int readInt() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    @Override
    public final short readShort() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    @Override
    public final char readChar() { // Java 2 Byte
        return (char) readShort();
    }

    @Override
    public final long readLong() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(read(Long.BYTES));
        buffer.flip();
        return buffer.getLong();
    }

    @Deprecated
    public final long readLongLegacy() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        final long byte5 = bs.readByte();
        final long byte6 = bs.readByte();
        final long byte7 = bs.readByte();
        final long byte8 = bs.readByte();
        return (long) ((byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1);
    }

    @Override
    public final float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public final double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final String readAsciiString(int n) {
        byte ret[] = new byte[n];
        for (int x = 0; x < n; ++x) {
            ret[x] = (byte) readByte();
        }
        return new String(ret, Charset.forName("Big5"));
    }

    @Override
    public final long getBytesRead() {
        return bs.getBytesRead();
    }

    @Override
    public final String readMapleAsciiString() {
        return readAsciiString(readShort());
    }

    @Override
    public final Point readPos() {
        final int x = readShort();
        final int y = readShort();
        return new Point(x, y);
    }

    @Override
    public final byte[] read(final int num) {
        byte[] ret = new byte[num];
        for (int x = 0; x < num; ++x) {
            ret[x] = readByte();
        }
        return ret;
    }

    @Override
    public void skip(final int num) {
        for (int x = 0; x < num; ++x) {
            readByte();
        }
    }

    @Override
    public final long available() {
        return bs.available();
    }

    @Override
    public final String toString() {
        return bs.toString();
    }

    @Override
    public final String toString(final boolean b) {
        return bs.toString(b);
    }
}
