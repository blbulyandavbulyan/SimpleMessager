package common;

import serverconnection.ServerConnection;
import serverconnection.exceptions.RegisterOrLoginInterrupted;
import ui.ConnectToServerDialog;
import ui.MainWindow;
import ui.LoginOrRegisterWindow;

import java.io.*;
import java.net.InetSocketAddress;

public class Client {
    public static void main(String[] args) throws IOException {
        ConnectToServerDialog connectToServerDialog = new ConnectToServerDialog();
        connectToServerDialog.setVisible(true);
        if(!connectToServerDialog.isCancelled()){
            InetSocketAddress address = connectToServerDialog.getAddress();
            if(address != null){
                final LoginOrRegisterWindow low = new LoginOrRegisterWindow();
                low.pack();
                try {
                    ServerConnection connection = new ServerConnection(address, 2 * 60 * 1000, low, null);
                    MainWindow mw = new MainWindow(connection);
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
