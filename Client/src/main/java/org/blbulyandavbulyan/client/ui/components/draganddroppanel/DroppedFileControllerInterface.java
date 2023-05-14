package org.blbulyandavbulyan.client.ui.components.draganddroppanel;

import org.blbulyandavbulyan.client.ui.components.displayers.filedisplayers.FileDisplayer;

import java.util.Collection;

public interface DroppedFileControllerInterface {
    void removeFileDisplayerFromDragAndDropPanel(FileDisplayer fileDisplayer);
    Collection<FileDisplayer> getFileDisplayers();
}
