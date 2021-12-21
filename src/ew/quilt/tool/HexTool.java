package ew.quilt.tool;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class HexTool {

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final String toString(final byte byteValue) {
        final int tmp = byteValue << 8;
        char[] retstr = new char[]{HEX[(tmp >> 12) & 0x0F], HEX[(tmp >> 8) & 0x0F]};
        return String.valueOf(retstr);
    }

    public static final String toString(final int intValue) {
        return Integer.toHexString(intValue);
    }

    public static final String toString(final byte[] bytes) {
        StringBuilder hexed = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hexed.append(toString(bytes[i]));
            hexed.append(' ');
        }
        return hexed.substring(0, hexed.length() - 1);
    }

    public static final String toMacString(final byte[] bytes) {
        StringBuilder hexed = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hexed.append(toString(bytes[i]));
            hexed.append('-');
        }
        return hexed.substring(0, hexed.length() - 1);
    }

    public static final String toStringFromAscii(final byte[] bytes) {
        byte[] ret = new byte[bytes.length];
        for (int x = 0; x < bytes.length; x++) {
            if (bytes[x] < 32 && bytes[x] >= 0) {
                ret[x] = '.';
            } else {
                int chr = ((short) bytes[x]) & 0xFF;
                ret[x] = (byte) chr;
            }
        }
        try {
            return new String(ret, "Big5");
        } catch (UnsupportedEncodingException ex) {
        }
        return "";
    }

    public static final String toPaddedStringFromAscii(final byte[] bytes) {
        final String str = toStringFromAscii(bytes);
        StringBuilder ret = new StringBuilder(str.length() * 3);
        for (int i = 0; i < str.length(); i++) {
            ret.append(str.charAt(i));
            ret.append("  ");
        }
        return ret.toString();
    }

    public static byte[] getByteArrayFromHexString(final String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nexti = 0;
        int nextb = 0;
        boolean highoc = true;
        outer:
        for (;;) {
            int number = -1;
            while (number == -1) {
                if (nexti == hex.length()) {
                    break outer;
                }
                char chr = hex.charAt(nexti);
                if (chr >= '0' && chr <= '9') {
                    number = chr - '0';
                } else if (chr >= 'a' && chr <= 'f') {
                    number = chr - 'a' + 10;
                } else if (chr >= 'A' && chr <= 'F') {
                    number = chr - 'A' + 10;
                } else {
                    number = -1;
                }
                nexti++;
            }
            if (highoc) {
                nextb = number << 4;
                highoc = false;
            } else {
                nextb |= number;
                highoc = true;
                baos.write(nextb);
            }
        }
        return baos.toByteArray();
    }

    public static final String getOpcodeToString(final int op) {
        return "0x" + Integer.toHexString(op).toUpperCase();
    }

    public static String toHex(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            sb.append(Integer.toHexString(input.charAt(i)));
        }
        return sb.toString().toUpperCase();
    }

    public static String toOctal(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            sb.append(Integer.toOctalString(input.charAt(i)));
        }
        return sb.toString();
    }

    public static String toBinary(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            sb.append(Integer.toBinaryString(input.charAt(i)));
        }
        return sb.toString();
    }
}
