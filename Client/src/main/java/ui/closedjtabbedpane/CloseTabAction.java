package ui.closedjtabbedpane;
public interface CloseTabAction {
    default void closeTab(javax.swing.JTabbedPane tabbedPane, int tabForRemoveIndex){
        tabbedPane.removeTabAt(tabForRemoveIndex);
    }
    void afterRemoveTab(String removedTabName);
}
