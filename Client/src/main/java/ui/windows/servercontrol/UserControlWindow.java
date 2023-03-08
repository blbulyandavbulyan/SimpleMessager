package ui.windows.servercontrol;

import ui.windows.servercontrol.interfaces.UserControl;

import javax.swing.*;
//класс окна управления пользователями на сервере
public class UserControlWindow extends JFrame {
    private UserControl userControl;
    public UserControlWindow(UserControl userControl){
        this.userControl = userControl;
    }
    private void initUi(){
        JPanel rootPanel = new JPanel();
        JTable jTable = new JTable();
    }
}
