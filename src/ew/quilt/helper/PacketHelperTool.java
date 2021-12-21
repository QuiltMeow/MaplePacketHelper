package ew.quilt.helper;

import ew.quilt.exception.PacketStringWrongSizeException;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import org.apache.commons.lang3.StringEscapeUtils;

public class PacketHelperTool {

    public static final int VK_LOWER_A = 97, VK_LOWER_F = 102, VK_NEGATIVE = 45;

    public static void numberOnly(KeyEvent evt) {
        int keyChar = evt.getKeyChar();
        if (!(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)) {
            evt.consume();
        }
    }

    public static void hexOnly(KeyEvent evt) {
        hexArrayOnly(evt);
    }

    public static void octOnly(KeyEvent evt) {
        int keyChar = evt.getKeyChar();
        if (!(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_7)) {
            evt.consume();
        }
    }

    public static void binOnly(KeyEvent evt) {
        int keyChar = evt.getKeyChar();
        if (!(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_1)) {
            evt.consume();
        }
    }

    public static void numberWithNegative(KeyEvent evt) {
        int keyChar = evt.getKeyChar();
        if (!((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) || keyChar == VK_NEGATIVE)) {
            evt.consume();
        }
    }

    public static void hexArrayOnly(KeyEvent evt) {
        int keyChar = evt.getKeyChar();
        if (!((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)
                || (keyChar >= KeyEvent.VK_A && keyChar <= KeyEvent.VK_F)
                || (keyChar >= VK_LOWER_A && keyChar <= VK_LOWER_F)
                || keyChar == KeyEvent.VK_SPACE)) {
            evt.consume();
        }
    }

    public static String fixPacketInput(String input) throws PacketStringWrongSizeException {
        input = cleanNotHexString(input).toUpperCase();
        if (input.length() % 2 == 1) {
            throw new PacketStringWrongSizeException();
        }
        return insertPeriodically(input, " ", 2);
    }

    public static String fixPacketInputAutomaticFix(String input) {
        input = cleanNotHexString(input).toUpperCase();
        if (input.length() % 2 == 1) {
            input += "0";
        }
        return insertPeriodically(input, " ", 2);
    }

    public static String fixPacketStream(String input) {
        return cleanNotHexString(input).toUpperCase();
    }

    public static String insertPeriodically(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(text.length() + insert.length() * (text.length() / period) + 1);
        int index = 0;
        String prefix = "";
        while (index < text.length()) {
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index, Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }

    public static String handleEscapeString(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            char data = input.charAt(i);
            if ((data >= 0 && data <= 31) || data == 127) {
                sb.append(StringEscapeUtils.escapeJava(String.valueOf(data)));
            } else {
                sb.append(data);
            }
        }
        return sb.toString();
    }

    public static String cleanNotNumberString(String input) {
        String regEx = "[^0-9]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

    public static String cleanNotHexString(String input) {
        String regEx = "[^a-fA-F0-9]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

    public static String cleanNotOctString(String input) {
        String regEx = "[^0-7]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

    public static String cleanNotBinString(String input) {
        String regEx = "[^0-1]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

    public static void appendTextArea(JTextArea input, String data) {
        input.append(data);
        input.setCaretPosition(input.getDocument().getLength());
    }

    public static String removeLastLine(JTextArea input) throws BadLocationException {
        String ret;
        int lastLine = input.getDocument().getText(0, input.getDocument().getLength() - 2).lastIndexOf("\r\n");
        if (lastLine < 0) {
            lastLine = 0;
            ret = input.getText().substring(lastLine);
            input.getDocument().remove(lastLine, input.getDocument().getLength());
        } else {
            ret = input.getText().substring(lastLine + 2);
            input.getDocument().remove(lastLine + 2, input.getDocument().getLength() - lastLine - 2);
        }
        return ret;
    }
}
