package ew.quilt.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class EWException extends Exception {

    private static final int EW = 6987;
    private int type = 0;

    public EWException() {
        super();
    }

    public EWException(String reason) {
        super(reason);
    }

    public EWException(Throwable cause) {
        super(cause);
    }

    public EWException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public EWException(String reason, int type) {
        super(reason);
        this.type = type;
    }

    public EWException(Throwable cause, int type) {
        super(cause);
        this.type = type;
    }

    public EWException(String reason, Throwable cause, int type) {
        super(reason, cause);
        this.type = type;
    }

    public int getExceptionType() {
        return type;
    }

    public int errorInteger() {
        return -1;
    }

    public int defaultInteger() {
        return 0;
    }

    public double defaultDouble() {
        return 0;
    }

    public String defaultString() {
        return "";
    }

    public Object getNull() {
        return null;
    }

    public final int getEW() {
        return EW;
    }

    public void forceExit() {
        System.exit(EW);
    }

    public String stackTraceString() {
        StringWriter error = new StringWriter();
        printStackTrace(new PrintWriter(error));
        return error.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return "[EW 例外狀況物件] 原因 : " + getMessage();
    }
}
