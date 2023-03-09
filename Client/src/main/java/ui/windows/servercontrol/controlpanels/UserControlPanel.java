package ui.windows.servercontrol.controlpanels;

import general.message.servercommand.ServerCommand;
import ui.windows.servercontrol.interfaces.GeneralControlInterface;
import ui.windows.servercontrol.interfaces.UserControl;

import javax.swing.*;
import java.util.function.Consumer;

public class UserControlPanel extends ControlPanel{
    public UserControlPanel(String executorName, String commandTargetName, UserControl userControl, Consumer<String> doDelete) {
        super(executorName, commandTargetName, userControl, doDelete);
        //проверяем есть ли у пользователя право выполнять изменение пароля, и если есть добавляем кнопку на изменение пароля
        if(userControl.can(executorName, commandTargetName, ServerCommand.Command.CHANGE_PASSWORD)){
            JButton changePassword = new JButton("Сменить пароль");
            changePassword.addActionListener(l->{
                String newPassword = JOptionPane.showInputDialog(this, "Введите новый пароль для пользователя %s".formatted(commandTargetName));
                if(newPassword != null) {
                    if (!newPassword.isBlank() && !newPassword.isEmpty())
                        userControl.changePassword(commandTargetName, newPassword);
                    else JOptionPane.showMessageDialog(this, "Ошибка, новый пароль не может быть пустым!", "Пароль пуст!", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}
