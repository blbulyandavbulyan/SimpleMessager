package ui.ghosttextt;

import java.awt.*;

public interface GhostTextInterface {
    void setGhostText(String ghostText);
    String getGhostText();
    void setGhostTextEnabled(boolean enabled);
    boolean isGhostTextEnabled();
    void setGhostTextColor(Color ghostTextColor);
    Color getGhostTextColor();
    boolean isEmpty();
}
