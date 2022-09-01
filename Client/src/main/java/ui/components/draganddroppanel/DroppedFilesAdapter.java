package ui.components.draganddroppanel;

import java.io.File;

public interface DroppedFilesAdapter extends DroppedFilesListener {
    default void droppedFileAdded(File file){

    }
    default void droppedFileDeleted(File file){

    }
    default void droppedFilesSetCleared() {
    }
}
