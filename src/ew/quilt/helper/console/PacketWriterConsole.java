package ew.quilt.helper.console;

import java.awt.Point;
import ew.quilt.tool.HexTool;
import ew.quilt.tool.data.MaplePacketLittleEndianWriter;

public class PacketWriterConsole {

    private final MaplePacketLittleEndianWriter mplew;

    public PacketWriterConsole() {
        mplew = new MaplePacketLittleEndianWriter();
    }

    public int getLength() {
        return mplew.getLength();
    }

    public void writeByte(byte value) {
        writeByte(value, "");
    }

    public void writeByte(byte value, String name) {
        mplew.write(value);
        System.out.println("[位元組" + (name.equals("") ? "" : " " + name) + "] " + value);
    }

    public void writeOPCode(short value) {
        mplew.writeShort(value);
        System.out.println("[封包包頭] 0x" + Integer.toHexString(value));
    }

    public void writeShort(short value) {
        writeShort(value, "");
    }

    public void writeShort(short value, String name) {
        mplew.writeShort(value);
        System.out.println("[短整數" + (name.equals("") ? "" : " " + name) + "] " + value);
    }

    public void writeInt(int value) {
        writeInt(value, "");
    }

    public void writeInt(int value, String name) {
        mplew.writeInt(value);
        System.out.println("[整數" + (name.equals("") ? "" : " " + name) + "] " + value);
    }

    public void writeLong(long value) {
        writeLong(value, "");
    }

    public void writeLong(long value, String name) {
        mplew.writeLong(value);
        System.out.println("[長整數" + (name.equals("") ? "" : " " + name) + "] " + value);
    }

    public void writeAsciiString(String value) {
        writeAsciiString(value, "");
    }

    public void writeAsciiString(String value, String name) {
        mplew.writeAsciiString(value);
        System.out.println("[字串" + (name.equals("") ? "" : " " + name) + "] " + value);
    }

    public void writeMapleAsciiString(String value) {
        writeMapleAsciiString(value, "");
    }

    public void writeMapleAsciiString(String value, String name) {
        mplew.writeMapleAsciiString(value);
        System.out.println("[楓之谷字串" + (name.equals("") ? "" : " " + name) + "] " + value);
    }

    public void writePos(Point pos) {
        writePos(pos, "");
    }

    public void writePos(Point pos, String name) {
        mplew.writePos(pos);
        System.out.println("[座標" + (name.equals("") ? "" : " " + name) + "] X : " + pos.x + " Y : " + pos.y);
    }

    public byte[] getPacket() {
        byte[] ret = mplew.getPacket();
        System.out.println("[封包] " + HexTool.toString(ret));
        return ret;
    }
}
