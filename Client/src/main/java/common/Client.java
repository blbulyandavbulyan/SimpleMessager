package common;

import serverconnection.ServerConnection;
import serverconnection.exceptions.RegisterOrLoginInterrupted;
import ui.windows.ConnectToServerDialog;
import ui.windows.MainWindow;
import ui.windows.LoginOrRegisterWindow;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ResourceBundle;

public class Client {
    static ResourceBundle guiRb = ResourceBundle.getBundle("resources/locales/guitext");
    public static void main(String[] args) throws IOException {
        //TODO add read server address and server ports from command line arguments
        ConnectToServerDialog connectToServerDialog = new ConnectToServerDialog(guiRb);
        connectToServerDialog.setVisible(true);
        if(!connectToServerDialog.isCancelled()){
            InetSocketAddress address = connectToServerDialog.getAddress();
            if(address != null){
                final LoginOrRegisterWindow low = new LoginOrRegisterWindow(guiRb);
                low.pack();
                try {
                    ServerConnection connection = new ServerConnection(address, 2 * 60 * 1000, low, null);
                    MainWindow mw = new MainWindow(connection, guiRb);
                    mw.pack();
                    mw.setVisible(true);
                }
                catch (RegisterOrLoginInterrupted ignored){
                }

            }
            else{
                System.err.println("address is null, terminating");
                System.exit(-1);
            }
        }
    }
}
