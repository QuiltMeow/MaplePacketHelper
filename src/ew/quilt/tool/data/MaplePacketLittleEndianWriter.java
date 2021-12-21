package ew.quilt.tool.data;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import ew.quilt.tool.HexTool;
import java.nio.ByteBuffer;

public class MaplePacketLittleEndianWriter {

    private final ByteArrayOutputStream baos;
    private static Charset ASCII = Charset.forName("Big5");

    public static final int getlength(final String str) {
        byte[] bt = str.getBytes(Charset.forName("Big5"));
        return bt.length;
    }

    public MaplePacketLittleEndianWriter() {
        this(32);
    }

    public void writeMapleAsciiStrings(String s) {
        writeShort((short) s.getBytes().length);
        writeAsciiString(s);
    }

    public MaplePacketLittleEndianWriter(final int size) {
        this.baos = new ByteArrayOutputStream(size);
    }

    public final byte[] getPacket() {
        return baos.toByteArray();
    }

    public final int getLength() {
        return baos.size();
    }

    @Override
    public final String toString() {
        return HexTool.toString(baos.toByteArray());
    }

    public final void writeZeroBytes(final int i) {
        for (int x = 0; x < i; ++x) {
            baos.write((byte) 0);
        }
    }

    public final void writeZeroByte(final int i) {
        writeZeroBytes(i);
    }

    public final void write(final byte[] b) {
        for (int x = 0; x < b.length; ++x) {
            baos.write(b[x]);
        }
    }

    public final void write(final boolean b) {
        baos.write(b ? 1 : 0);
    }

    public final void write(final byte b) {
        baos.write(b);
    }

    public final void write(final int b) {
        baos.write((byte) b);
    }

    public final void writeByte(final byte b) {
        baos.write(b);
    }

    public final void writeShort(final int i) {
        baos.write((byte) (i & 0xFF));
        baos.write((byte) ((i >>> 8) & 0xFF));
    }

    public final void writeInt(final int i) {
        baos.write((byte) (i & 0xFF));
        baos.write((byte) ((i >>> 8) & 0xFF));
        baos.write((byte) ((i >>> 16) & 0xFF));
        baos.write((byte) ((i >>> 24) & 0xFF));
    }

    public final void writeAsciiString(final String s) {
        write(s.getBytes(ASCII));
    }

    public final void writeAsciiString(String s, final int max) {
        if (getlength(s) > max) {
            s = s.substring(0, max);
        }
        write(s.getBytes(ASCII));
        for (int i = getlength(s); i < max; ++i) {
            write(0);
        }
    }

    public final void writeMapleAsciiString(final String s) {
        writeShort((short) getlength(s));
        writeAsciiString(s);
    }

    public final void writeMapleAsciiString(String s, final int max) {
        if (getlength(s) > max) {
            s = s.substring(0, max);
        }
        writeShort((short) getlength(s));
        write(s.getBytes(ASCII));
        for (int i = getlength(s); i < max; i++) {
            write(0);
        }
    }

    public final void writePos(final Point s) {
        writeShort(s.x);
        writeShort(s.y);
    }

    public final void writeRect(final Rectangle s) {
        writeInt(s.x);
        writeInt(s.y);
        writeInt(s.x + s.width);
        writeInt(s.y + s.height);
    }

    public final void writeLong(final long l) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(l);
        write(buffer.array());
    }

    public final void writeReversedLong(final long l) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(Long.reverseBytes(l));
        write(buffer.array());
    }

    @Deprecated
    public final void writeLongLegacy(final long l) {
        baos.write((byte) (l & 0xFF));
        baos.write((byte) ((l >>> 8) & 0xFF));
        baos.write((byte) ((l >>> 16) & 0xFF));
        baos.write((byte) ((l >>> 24) & 0xFF));
        baos.write((byte) ((l >>> 32) & 0xFF));
        baos.write((byte) ((l >>> 40) & 0xFF));
        baos.write((byte) ((l >>> 48) & 0xFF));
        baos.write((byte) ((l >>> 56) & 0xFF));
    }

    @Deprecated
    public final void writeReversedLongLegacy(final long l) {
        baos.write((byte) ((l >>> 32) & 0xFF));
        baos.write((byte) ((l >>> 40) & 0xFF));
        baos.write((byte) ((l >>> 48) & 0xFF));
        baos.write((byte) ((l >>> 56) & 0xFF));
        baos.write((byte) (l & 0xFF));
        baos.write((byte) ((l >>> 8) & 0xFF));
        baos.write((byte) ((l >>> 16) & 0xFF));
        baos.write((byte) ((l >>> 24) & 0xFF));
    }

    public final byte[] subByteArray(int length) {
        byte[] array = baos.toByteArray();
        byte[] ret = new byte[length];
        for (int i = 0; i < length; ++i) {
            ret[i] = array[i];
        }
        return ret;
    }
}
