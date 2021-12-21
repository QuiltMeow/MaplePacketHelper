package ew.quilt.helper.console;

import java.awt.Point;
import ew.quilt.tool.HexTool;
import ew.quilt.tool.data.ByteArrayByteStream;
import ew.quilt.tool.data.LittleEndianAccessor;

public class PacketReaderConsole {

    private final LittleEndianAccessor slea;

    public PacketReaderConsole(String input) {
        slea = new LittleEndianAccessor(new ByteArrayByteStream(HexTool.getByteArrayFromHexString(input)));
    }

    public byte readByte() {
        return readByte("");
    }

    public byte readByte(String name) {
        byte ret = slea.readByte();
        System.out.println("[位元組" + (name.equals("") ? "" : " " + name) + "] " + ret);
        return ret;
    }

    public short readOPCode() {
        short ret = slea.readShort();
        System.out.println("[封包包頭] 0x" + Integer.toHexString(ret));
        return ret;
    }

    public short readShort() {
        return readShort("");
    }

    public short readShort(String name) {
        short ret = slea.readShort();
        System.out.println("[短整數" + (name.equals("") ? "" : " " + name) + "] " + ret);
        return ret;
    }

    public int readInt() {
        return readInt("");
    }

    public int readInt(String name) {
        int ret = slea.readInt();
        System.out.println("[整數" + (name.equals("") ? "" : " " + name) + "] " + ret);
        return ret;
    }

    public long readLong() {
        return readLong("");
    }

    public long readLong(String name) {
        long ret = slea.readLong();
        System.out.println("[長整數" + (name.equals("") ? "" : " " + name) + "] " + ret);
        return ret;
    }

    public String readAsciiString(int length) {
        return readAsciiString(length, "");
    }

    public String readAsciiString(int length, String name) {
        String ret = slea.readAsciiString(length);
        System.out.println("[字串" + (name.equals("") ? "" : " " + name) + "] " + ret);
        return ret;
    }

    public String readMapleAsciiString() {
        return readMapleAsciiString("");
    }

    public String readMapleAsciiString(String name) {
        String ret = slea.readMapleAsciiString();
        System.out.println("[楓之谷字串" + (name.equals("") ? "" : " " + name) + "] " + ret);
        return ret;
    }

    public Point readPos() {
        return readPos("");
    }

    public Point readPos(String name) {
        Point ret = slea.readPos();
        System.out.println("[座標" + (name.equals("") ? "" : " " + name) + "] X : " + ret.x + " Y : " + ret.y);
        return ret;
    }

    public long available() {
        long ret = slea.available();
        System.out.println("[剩餘封包] " + ret);
        return ret;
    }

    public byte[] skip(int length) {
        byte[] ret = slea.read(length);
        System.out.println("[跳過封包] " + HexTool.toString(ret));
        return ret;
    }
}
