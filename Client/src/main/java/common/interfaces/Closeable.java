package common.interfaces;

public interface Closeable extends java.io.Closeable {
    boolean isClosed();
    boolean isOpen();
}
