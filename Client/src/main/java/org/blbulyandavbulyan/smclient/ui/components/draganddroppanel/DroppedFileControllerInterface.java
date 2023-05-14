package org.blbulyandavbulyan.smclient.ui.components.draganddroppanel;

import org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers.FileDisplayer;

import java.util.Collection;

public interface DroppedFileControllerInterface {
    void removeFileDisplayerFromDragAndDropPanel(FileDisplayer fileDisplayer);
    Collection<FileDisplayer> getFileDisplayers();
}
