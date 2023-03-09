package ui.windows.servercontrol.controlpanels;

import general.message.servercommand.ServerCommand;
import ui.windows.servercontrol.interfaces.GeneralControlInterface;

import javax.swing.*;
import java.util.function.Consumer;

public class ControlPanel extends JPanel {
    protected String executorName;
    protected String commandTargetName;
    protected GeneralControlInterface generalControlInterface;
    protected JButton delete;
    protected JButton banOrUnban;
    private Consumer<String> doAfterDelete;
    public ControlPanel(String executorName, String commandTargetName, GeneralControlInterface generalControlInterface, Consumer<String> doAfterDelete){
        this.executorName = executorName;
        this.commandTargetName = commandTargetName;
        this.generalControlInterface = generalControlInterface;
        this.doAfterDelete = doAfterDelete;
        initUi();
    }
    private void initUi(){
        JButton ban = generalControlInterface.can(executorName, commandTargetName, ServerCommand.Command.BAN) ? new JButton("Заблокировать") : null;
        JButton unban = generalControlInterface.can(executorName, commandTargetName, ServerCommand.Command.UNBAN) ? new JButton("Разблокировать") : null;
        if(generalControlInterface.can(executorName, commandTargetName, ServerCommand.Command.DELETE))delete = new JButton("Удалить");
        if(ban != null)ban.addActionListener(l->{
            generalControlInterface.ban(commandTargetName);
            if(unban != null)banOrUnban = unban;
            else ban.setEnabled(false);
        });
        if(unban != null)unban.addActionListener(l->{
            generalControlInterface.unban(commandTargetName);
            if(ban != null)banOrUnban = ban;
            else unban.setEnabled(false);
        });
        banOrUnban = generalControlInterface.isBanned(commandTargetName) ? unban : ban;
        delete.addActionListener(l->{
            generalControlInterface.delete(commandTargetName);
            doAfterDelete.accept(commandTargetName);
        });
        this.add(new JLabel(commandTargetName));
        if(banOrUnban != null)this.add(banOrUnban);
        if(delete != null)this.add(delete);
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getCommandTargetName() {
        return commandTargetName;
    }
}
