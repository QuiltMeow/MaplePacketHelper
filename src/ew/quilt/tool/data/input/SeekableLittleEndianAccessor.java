package ew.quilt.tool.data.input;

public interface SeekableLittleEndianAccessor extends LittleEndianAccessor {

    void seek(final long offset);

    long getPosition();
}
