package common;

import serverconnection.ServerConnection;
import serverconnection.exceptions.RegisterOrLoginInterrupted;
import ui.windows.ConnectToServerDialog;
import ui.windows.MainWindow;
import ui.windows.LoginOrRegisterWindow;

import java.io.*;
import java.net.InetSocketAddress;
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
        //TODO add read server address and server ports from command line arguments
        Integer serverPort = null;
        String serverAddress = null;
        try{
            ResourceBundle commandLineHelpsRb = ResourceBundle.getBundle("resources/locales/command_line_arguments_help");
            for(int i = 0; i < args.length;){
                switch (args[i]){
                    case "help", "-help", "--help"->{
                        try{
                            String argumentForHelp = args[i + 1];
                            try{
                                System.out.println(commandLineHelpsRb.getString(argumentForHelp));
                            }
                            catch (MissingResourceException e){
                                System.err.printf("Справки для аргумента %s нет\n", argumentForHelp);
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.NO_HELP_FOR_ARGUMENT.errorCode);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            System.out.println("Для справки по определённому аргументу введите: help argument,\n где argument - интересующий вас argument");
                            commandLineHelpsRb.getKeys().asIterator().forEachRemaining(System.out::println);
                        }
                        System.exit(0);
                    }
                    case "--server-port" ->{
                        try{
                            serverPort = Integer.parseInt(args[i + 1]);
                            if(serverPort < 0 || serverPort > 65535){
                                System.err.println("Введённый номер порта после --server-port не принадлежит диапазону [0; 665535]");
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_OUT_OF_RANGE.errorCode);
                            }
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.err.println("Вы ввели не число после --server-port");
                            e.printStackTrace();
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
                    case "--server-address" ->{
                        serverAddress = args[i+1];
                        i+=2;
                    }
                    default -> {
                        System.err.printf("Неверный аргумент %s", args[i]);
                        System.exit(RunErrorCodes.RUN_ERROR_CODES.INVALID_ARGUMENT.errorCode);
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Кажется вы указали недостаточно аргументов. ");
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
            try {
                ServerConnection connection = new ServerConnection(serverInetSocketAddress, 2 * 60 * 1000, low, null);
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
