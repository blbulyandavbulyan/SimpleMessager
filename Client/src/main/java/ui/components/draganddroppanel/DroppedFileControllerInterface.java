package ui.components.draganddroppanel;

import ui.components.displayers.filedisplayers.FileDisplayer;

import java.io.File;
import java.util.Collection;

public interface DroppedFileControllerInterface {
    void removeFileDisplayerFromDragAndDropPanel(FileDisplayer fileDisplayer);
    Collection<FileDisplayer> getFileDisplayers();
}
