package org.blbulyandavbulyan.client.common;

import org.blbulyandavbulyan.client.common.enumerationtostream.ConvertEnumerationToStream;
import org.blbulyandavbulyan.client.serverconnection.ServerConnection;
import org.blbulyandavbulyan.client.serverconnection.exceptions.RegisterOrLoginInterrupted;
import org.blbulyandavbulyan.client.serverconnection.exceptions.ServerHasDBProblemsException;
import org.blbulyandavbulyan.client.ui.common.DisplayErrors;
import org.blbulyandavbulyan.client.ui.windows.connecting.ConnectToServerDialog;
import org.blbulyandavbulyan.client.ui.windows.connecting.LoginOrRegisterWindow;
import org.blbulyandavbulyan.client.ui.windows.main.MainWindow;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Client {
    static ResourceBundle guiRb = ResourceBundle.getBundle("resources/locales/guitext");

    private static class RunErrorCodes {
        private static int nextErrorCode = -1;
        enum RUN_ERROR_CODES {
            PORT_IS_NOT_A_NUMBER(),
            PORT_OUT_OF_RANGE(),
            INVALID_ARGUMENT_COUNTS(),
            INVALID_ARGUMENT(),
            NO_HELP_FOR_ARGUMENT();
            public final int errorCode;

            RUN_ERROR_CODES() {
                this.errorCode = nextErrorCode--;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Integer serverPort = null;
        String serverAddress = null;
        ResourceBundle commandLineRb = ResourceBundle.getBundle("resources/locales/command_line_interface");
        try{

            for(int i = 0; i < args.length;){
                switch (args[i]){
                    case "help", "-help", "--help"->{
                        try{
                            String argumentForHelp = args[i + 1];
                            try{
                                System.out.println(commandLineRb.getString("argumentHelp" + '.' +argumentForHelp));
                            }
                            catch (MissingResourceException e){
                                System.err.println(commandLineRb.getString("commandLine.error.NoHelpForArgument")+ ' ' + argumentForHelp);
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.NO_HELP_FOR_ARGUMENT.errorCode);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            System.out.println(commandLineRb.getString("commandLine.guideToGetHelp"));
                            ConvertEnumerationToStream.convert(commandLineRb.getKeys()).
                                    map(s -> s.split("\\.")).
                                    filter(sArray-> sArray.length == 2 && sArray[0].equals("argumentHelp")).
                                    map(strings -> strings[0] +
                                            '.' +
                                            strings[1]).forEach((key) -> System.out.println(commandLineRb.getString(key)));
                        }
                        System.exit(0);
                    }
                    case "--server-port" ->{
                        try{
                            serverPort = Integer.parseInt(args[i + 1]);
                            if(serverPort < 0 || serverPort > 65535){
                                System.err.println(commandLineRb.getString("commandLine.error.PortOutOfRange"));
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_OUT_OF_RANGE.errorCode);
                            }
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.err.println(commandLineRb.getString("commandLine.error.PortIsNotaNumber"));
                            e.printStackTrace();
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
                    case "--server-address" ->{
                        serverAddress = args[i+1];
                        i+=2;
                    }
                    default -> {
                        System.err.printf(commandLineRb.getString("commandLine.error.WrongArgument") + ' ' + args[i]);
                        System.exit(RunErrorCodes.RUN_ERROR_CODES.INVALID_ARGUMENT.errorCode);
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.err.println(commandLineRb.getString("commandLine.error.InvalidArgumentsCount"));
            System.exit(RunErrorCodes.RUN_ERROR_CODES.INVALID_ARGUMENT_COUNTS.errorCode);
            return;
        }
        InetSocketAddress serverInetSocketAddress;
        if(serverPort != null && serverAddress != null)serverInetSocketAddress = new InetSocketAddress(serverAddress, serverPort);
        else {
            ConnectToServerDialog connectToServerDialog = new ConnectToServerDialog(guiRb);
            connectToServerDialog.setVisible(true);
            if(connectToServerDialog.isCancelled())return;
            else serverInetSocketAddress = connectToServerDialog.getAddress();
        }

        if(serverInetSocketAddress != null){
            final LoginOrRegisterWindow low = new LoginOrRegisterWindow(guiRb);
            low.pack();
            boolean repeat = true;
            while (repeat){
                try {
                    ServerConnection connection = new ServerConnection(serverInetSocketAddress, 2 * 60 * 1000, low, null);
                    MainWindow mw = new MainWindow(connection, guiRb);
                    mw.pack();
                    mw.setVisible(true);
                    repeat = false;
                }
                catch (ServerHasDBProblemsException e){
                    e.printStackTrace();
                    DisplayErrors.showErrorMessage(null, "connectionErrors.serverHasDbProblem", "errorMessages.connectionErrors.serverHasDbProblem", guiRb);
                    
                }
                catch (RegisterOrLoginInterrupted ignored){
                    repeat = false;
                }
                catch(ConnectException | UnknownHostException e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, e instanceof UnknownHostException ? guiRb.getString("errorMessages.UnknownHost") + ' ' + e.getMessage() : e.getMessage(), guiRb.getString("errorCaptions.ConnectionError"), JOptionPane.ERROR_MESSAGE);
                    ConnectToServerDialog connectToServerDialog = new ConnectToServerDialog(guiRb);
                    connectToServerDialog.setVisible(true);
                    if(connectToServerDialog.isCancelled())return;
                    else serverInetSocketAddress = connectToServerDialog.getAddress();
                }
            }
        }
        else{
            System.err.println("address is null, terminating");
            System.exit(-1);
        }
    }
}
