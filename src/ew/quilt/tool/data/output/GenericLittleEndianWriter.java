package ew.quilt.tool.data.output;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class GenericLittleEndianWriter implements LittleEndianWriter {

    private static final Charset ASCII = Charset.forName("Big5");
    private ByteOutputStream bos;

    public static final int getlength(final String str) {
        byte[] bt = str.getBytes(Charset.forName("Big5"));
        return bt.length;
    }

    protected GenericLittleEndianWriter() {
    }

    protected final void setByteOutputStream(final ByteOutputStream bos) {
        this.bos = bos;
    }

    public GenericLittleEndianWriter(final ByteOutputStream bos) {
        this.bos = bos;
    }

    @Override
    public final void writeZeroBytes(final int i) {
        for (int x = 0; x < i; ++x) {
            bos.writeByte((byte) 0);
        }
    }

    @Override
    public final void write(final byte[] b) {
        for (int x = 0; x < b.length; ++x) {
            bos.writeByte(b[x]);
        }
    }

    @Override
    public final void write(final byte b) {
        bos.writeByte(b);
    }

    @Override
    public final void write(final int b) {
        bos.writeByte((byte) b);
    }

    @Override
    public final void writeShort(final short i) {
        bos.writeByte((byte) (i & 0xFF));
        bos.writeByte((byte) ((i >>> 8) & 0xFF));
    }

    @Override
    public final void writeShort(final int i) {
        bos.writeByte((byte) (i & 0xFF));
        bos.writeByte((byte) ((i >>> 8) & 0xFF));
    }

    @Override
    public final void writeInt(final int i) {
        bos.writeByte((byte) (i & 0xFF));
        bos.writeByte((byte) ((i >>> 8) & 0xFF));
        bos.writeByte((byte) ((i >>> 16) & 0xFF));
        bos.writeByte((byte) ((i >>> 24) & 0xFF));
    }

    @Override
    public final void writeAsciiString(final String s) {
        write(s.getBytes(ASCII));
    }

    @Override
    public final void writeAsciiString(String s, final int max) {
        if (getlength(s) > max) {
            s = s.substring(0, max);
        }
        write(s.getBytes(ASCII));
        for (int i = getlength(s); i < max; ++i) {
            write(0);
        }
    }

    @Override
    public final void writeMapleAsciiString(final String s) {
        writeShort((short) getlength(s));
        writeAsciiString(s);
    }

    @Override
    public final void writePos(final Point s) {
        writeShort(s.x);
        writeShort(s.y);
    }

    @Override
    public final void writeLong(final long l) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(l);
        write(buffer.array());
    }

    @Deprecated
    public final void writeLongLegacy(final long l) {
        bos.writeByte((byte) (l & 0xFF));
        bos.writeByte((byte) ((l >>> 8) & 0xFF));
        bos.writeByte((byte) ((l >>> 16) & 0xFF));
        bos.writeByte((byte) ((l >>> 24) & 0xFF));
        bos.writeByte((byte) ((l >>> 32) & 0xFF));
        bos.writeByte((byte) ((l >>> 40) & 0xFF));
        bos.writeByte((byte) ((l >>> 48) & 0xFF));
        bos.writeByte((byte) ((l >>> 56) & 0xFF));
    }
}
