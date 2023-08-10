package org.blbulyandavbulyan.smserver.interfaces;

public interface ServerLogger {
    void error(String msg);
    void info(String msg);

    void error(String s, Exception e);
}
