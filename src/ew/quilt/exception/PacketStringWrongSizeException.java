package ew.quilt.exception;

public class PacketStringWrongSizeException extends Exception {

    public PacketStringWrongSizeException() {
        super("封包字串長度錯誤");
    }
}
