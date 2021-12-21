package ew.quilt.tool.data;

import java.io.IOException;
import ew.quilt.tool.HexTool;

public class ByteArrayByteStream {

    private int pos = 0;
    private long bytesRead = 0;
    private final byte[] arr;

    public ByteArrayByteStream(final byte[] arr) {
        this.arr = arr;
    }

    public int getByte(int position) {
        return ((int) arr[position]) & 0xFF;
    }

    public long getPosition() {
        return pos;
    }

    public void seek(final long offset) throws IOException {
        if (offset < 0 || offset > arr.length) {
            throw new IOException();
        }
        pos = (int) offset;
    }

    public int getlength() {
        return arr.length;
    }

    public int getLength() {
        return arr.length;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public int readByte() {
        bytesRead++;
        return ((int) arr[pos++]) & 0xFF;
    }

    public void unReadByte() {
        if (pos - 1 < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        --pos;
    }

    public int readLastByte() {
        return ((int) arr[pos]) & 0xFF;
    }

    public int[] readLastBytes(int bytes) {
        while (pos - bytes < 1) {
            bytes--;
        }
        int[] a = null;
        int b = 0;
        while (bytes > 0) {
            a[b] += ((int) arr[pos - bytes]);
            bytes--;
            b++;
        }
        return a;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(final boolean b) {
        String nows = "";
        if (arr.length - pos > 0) {
            byte[] now = new byte[arr.length - pos];
            System.arraycopy(arr, pos, now, 0, arr.length - pos);
            nows = HexTool.toString(now);
        }
        if (b) {
            return "全部：" + HexTool.toString(arr) + "\r\n目前：" + nows;
        } else {
            return "資料：" + nows;
        }
    }

    public long available() {
        return arr.length - pos;
    }
}
