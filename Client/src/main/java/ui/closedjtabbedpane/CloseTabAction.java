package ui.closedjtabbedpane;
public interface CloseTabAction {
    default void closeTab(javax.swing.JTabbedPane tabbedPane, String tabForRemoveName){
        tabbedPane.removeTabAt(tabbedPane.indexOfTab(tabForRemoveName));
    }
    void afterRemoveTab(String removedTabName);
}
