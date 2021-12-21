package ew.quilt.tool.data.input;

import java.io.IOException;
import ew.quilt.tool.HexTool;

public class ByteArrayByteStream implements SeekableInputStreamBytestream {

    private int pos = 0;
    private long bytesRead = 0;
    private final byte[] arr;

    public ByteArrayByteStream(final byte[] arr) {
        this.arr = arr;
    }

    @Override
    public long getPosition() {
        return pos;
    }

    @Override
    public void seek(final long offset) throws IOException {
        if (offset < 0 || offset > arr.length) {
            throw new IOException();
        }
        pos = (int) offset;
    }

    @Override
    public long getBytesRead() {
        return bytesRead;
    }

    @Override
    public int readByte() {
        ++bytesRead;
        return ((int) arr[pos++]) & 0xFF;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    @Override
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

    @Override
    public long available() {
        return arr.length - pos;
    }
}
