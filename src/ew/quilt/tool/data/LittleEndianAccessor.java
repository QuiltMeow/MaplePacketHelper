package ew.quilt.tool.data;

import java.awt.Point;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class LittleEndianAccessor {

    private final ByteArrayByteStream bs;

    public LittleEndianAccessor(final ByteArrayByteStream bs) {
        this.bs = bs;
    }

    public final byte readByte() {
        return (byte) bs.readByte();
    }

    public final int readInt() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public final int getlength() {
        return bs.getlength();
    }

    public final int getLength() {
        return bs.getlength();
    }

    public final short readShort() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    public final int readUShort() {
        int quest = readShort();
        if (quest < 0) {
            quest += 65536;
        }
        return quest;
    }

    public final char readChar() {
        return (char) readShort();
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

    public final long readLong() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(read(Long.BYTES));
        buffer.flip();
        return buffer.getLong();
    }

    public final float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public final String readAsciiString(int n) {
        byte ret[] = new byte[n];
        for (int x = 0; x < n; ++x) {
            ret[x] = (byte) readByte();
        }
        return new String(ret, Charset.forName("Big5"));
    }

    public final long getBytesRead() {
        return bs.getBytesRead();
    }

    public final long getPosition() {
        return bs.getPosition();
    }

    public final String readMapleAsciiString() {
        return readAsciiString(readShort());
    }

    public final Point readPos() {
        final int x = readShort();
        final int y = readShort();
        return new Point(x, y);
    }

    public final byte[] read(final int num) {
        byte[] ret = new byte[num];
        for (int x = 0; x < num; ++x) {
            ret[x] = readByte();
        }
        return ret;
    }

    public final void unReadByte() {
        bs.unReadByte();
    }

    public final void unReadInt() {
        for (int byte_ = 0; byte_ < 4; ++byte_) {
            bs.unReadByte();
        }
    }

    public final void unReadShort() {
        for (int byte_ = 0; byte_ < 2; ++byte_) {
            bs.unReadByte();
        }
    }

    public final void unReadLong() {
        for (int byte_ = 0; byte_ < 8; ++byte_) {
            bs.unReadByte();
        }
    }

    public final void unReadAsciiString(final int n) {
        for (int byte_ = 0; byte_ < n; ++byte_) {
            bs.unReadByte();
        }
    }

    public final void unReadPos() {
        for (int byte_ = 0; byte_ < 4; ++byte_) {
            bs.unReadByte();
        }
    }

    public final void unRead(final int num) {
        for (int byte_ = 0; byte_ < num; ++byte_) {
            bs.unReadByte();
        }
    }

    public final byte readLastByte() {
        return (byte) bs.readLastByte();
    }

    public final int readLastInt() {
        unReadInt();
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public final short readLastShort() {
        unReadShort();
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    public final long readLastLong() {
        unReadLong();
        return readLong();
    }

    public final String readLastAsciiString(final int n) {
        for (int y = 0; y < n; ++y) {
            unReadByte();
        }
        byte ret[] = new byte[n];
        for (int x = 0; x < n; ++x) {
            ret[x] = (byte) readByte();
        }
        return new String(ret, Charset.forName("Big5"));
    }

    public final Point readLastPos() {
        unReadInt();
        short x = readShort();
        short y = readShort();
        return new Point(x, y);
    }

    public final byte[] readLastBytes(final int num) {
        for (int byte_ = 0; byte_ < num; ++byte_) {
            bs.unReadByte();
        }
        byte[] ret = new byte[num];
        for (int x = 0; x < num; ++x) {
            ret[x] = readByte();
        }
        return ret;
    }

    public final long available() {
        return bs.available();
    }

    @Override
    public final String toString() {
        return bs.toString();
    }

    public final String toString(final boolean b) {
        return bs.toString(b);
    }

    public final void seek(final long offset) {
        try {
            bs.seek(offset);
        } catch (IOException e) {
            System.err.println("找尋失敗 : " + e);
        }
    }

    public final void skip(final int num) {
        seek(getPosition() + num);
    }

    public final byte getByte(int position) {
        return (byte) bs.getByte(position);
    }

    public final short getShort(int position) {
        final int byte1 = bs.getByte(position);
        final int byte2 = bs.getByte(position + 1);
        return (short) ((byte2 << 8) + byte1);
    }

    public final int getInt(int position) {
        final int byte1 = bs.getByte(position);
        final int byte2 = bs.getByte(position + 1);
        final int byte3 = bs.getByte(position + 2);
        final int byte4 = bs.getByte(position + 3);
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public final long getLong(int position) {
        final byte[] read = new byte[Long.BYTES];
        for (int i = 0; i < Long.BYTES; ++i) {
            read[i] = (byte) bs.getByte(position + i);
        }
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(read);
        buffer.flip();
        return buffer.getLong();
    }

    public final String getAsciiString(int position, int size) {
        byte ret[] = new byte[size];
        for (int i = 0; i < size; ++i) {
            ret[i] = (byte) getByte(position + i);
        }
        return new String(ret, Charset.forName("Big5"));
    }

    public final String getMapleAsciiString(int position) {
        short length = getShort(position);
        return getAsciiString(position + 2, length);
    }

    public final Point getPos(int position) {
        final short x = getShort(position);
        final short y = getShort(position + 2);
        return new Point(x, y);
    }

    public final byte[] getBytes(int position, int length) {
        byte[] ret = new byte[length];
        for (int x = 0; x < length; ++x) {
            ret[x] = getByte(position + x);
        }
        return ret;
    }

    public final byte[] getPacket() {
        byte[] ret = new byte[bs.getlength()];
        for (int i = 0; i < bs.getlength(); ++i) {
            ret[i] = (byte) bs.getByte(i);
        }
        return ret;
    }

    public final byte[] subByteArray(int position) {
        byte[] ret = new byte[bs.getlength() - position];
        for (int i = position; i < bs.getlength(); ++i) {
            ret[i - position] = (byte) bs.getByte(i);
        }
        return ret;
    }
}
