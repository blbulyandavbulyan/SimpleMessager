package ui.windows.servercontrol.usercontrol;

import common.MapEntry.Entry;
import ui.windows.servercontrol.controlpanels.UserControlPanel;
import ui.windows.servercontrol.interfaces.UserControl;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

//класс окна управления пользователями на сервере
public class UserControlWindow extends JFrame {
    private UserControl userControl;
    protected Map<String, UserControlPanel> userNameUserControlPanelSet;
    protected JPanel rootPanel;
    public UserControlWindow(String executorName, UserControl userControl){
        this.userControl = userControl;
        Consumer<String> doAfterDelete = s -> {
            rootPanel.remove(userNameUserControlPanelSet.get(s));
            userNameUserControlPanelSet.remove(s);
        };
        //targetName -> new UserControlPanel(executorName, targetName, userControl)
        userNameUserControlPanelSet = new HashMap<>();
        Arrays.stream(userControl.findAllTargetNames()).
                forEach((targetUserName)-> userNameUserControlPanelSet.put(targetUserName, new UserControlPanel(executorName, targetUserName, userControl, doAfterDelete)));

    }
    private void initUi(){
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

    }
}
