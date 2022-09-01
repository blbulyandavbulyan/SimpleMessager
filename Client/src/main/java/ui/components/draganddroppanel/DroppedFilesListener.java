package ui.components.draganddroppanel;

import java.io.File;

public interface DroppedFilesListener {
    void droppedFileAdded(File file);// вызывается когда файл в DragAndDropPanel был добавлен перетаскиванием
    void droppedFileDeleted(File file);// вызывается когда файл из DragAndDropPanel был удалён нажатием на кнопку с крестиком рядом с файлом
    void droppedFilesSetCleared();// вызывается, когда были удалены все перетащенные файлы в DragAndDropPanel
}
