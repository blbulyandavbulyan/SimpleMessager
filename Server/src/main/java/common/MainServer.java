package common;

import org.slf4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.beans.repositories.UserRepository;
import spring.beans.services.group.GroupService;
import spring.beans.services.user.UserService;
import spring.configs.SpringConfig;

import java.util.HashMap;
import java.util.Map;

public class MainServer {
    private static final Map<String, String> helpForArguments = new HashMap<>();
    private static class RunErrorCodes {
        private static int nextErrorCode = -1;
        enum RUN_ERROR_CODES {
            PORT_IS_NOT_A_NUMBER(),
            PORT_OUT_OF_RANGE(),
            INVALID_ARGUMENT_COUNTS(),
            INVALID_ARGUMENT(),
            BACKLOG_IS_NOT_A_NUMBER(),
            NO_HELP_FOR_ARGUMENT();
            public final int errorCode;

            RUN_ERROR_CODES() {
                this.errorCode = nextErrorCode--;
            }
        }
    }
    static {
        helpForArguments.put(
                "--listen-port",
                "Использование: --listen-port %port%, где %port% - число в диапазоне от 0 до 65535,\n это ни что иное как порт, на котором сервер будет слушать соединения."
        );
        helpForArguments.put(
                "--listen-address",
                "Использование: --listen-address %address%, где %address% - ip адрес, на котором будет принимать соединения сервер."
        );
        helpForArguments.put(
                "--backlog",
                "Использование: --backlog %backlog%, где %backlog% - число, которое означает максимальное число клиентов в очереди на подключение,\n если указать значение <= 0, то будет выбрано значение по умолчанию."
        );
//        helpForArguments.put(
//                "--db-subname",
//                "Использование: --db-subname %subname%, где %subname% - параметр подключения к БД.\n В случае если драйвер базы стоит по умолчанию, то данный параметр просто представляет путь к файлу базы данных SQLite.\n Он не обязательно должен существовать, если он не существует, то он будет создан.\n Если же выбран другой драйвер, то значение параметра, зависит от него. \nВсе необходимые таблицы добавляются автоматически, если в БД их нет"
//        );
//        helpForArguments.put(
//                "--dbms-name",
//                "Использование: --dbms-name %driver-name%, где %driver-name% - имя драйвера, для подключения к базе.\n Драйвер должен быть включён в jar архив, по умолчанию поддерживается только SQLite"
//        );
    }
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        UserRepository userRepository = annotationConfigApplicationContext.getBean(UserRepository.class);
        UserService userService = annotationConfigApplicationContext.getBean(UserService.class);
        GroupService groupService = annotationConfigApplicationContext.getBean(GroupService.class);
        StartupParameters startupParameters = parseCommandLineArguments(args);
        Server server = new Server(startupParameters, userService, groupService);
        server.start();
    }
    private static StartupParameters parseCommandLineArguments(String[] args){
        StartupParameters startupParameters = new StartupParameters();
        try{
            for(int i = 0; i < args.length;){
                switch (args[i]){
                    case "help", "-help", "--help"->{
                        try{
                            String argumentForHelp = args[i + 1];
                            String helpForCommand = helpForArguments.get(argumentForHelp);
                            if(helpForCommand == null){
                                System.err.printf("Справки для аргумента %s нет\n", argumentForHelp);
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.NO_HELP_FOR_ARGUMENT.errorCode);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            System.out.println("Для справки по определённому аргументу введите: help argument,\n где argument - интересующий вас argument");
                            for (String argHelp : helpForArguments.values()) {
                                System.out.println(argHelp);
                            }
                        }
                        System.exit(0);
                    }
                    case "--listen-port" ->{
                        try{
                            int port = Integer.parseInt(args[i + 1]);
                            if(port < 0 || port > 65535){
                                System.err.println("Введённый номер порта после --listen-port не принадлежит диапазону [0; 665535]");
                                System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_OUT_OF_RANGE.errorCode);
                            }
                            else startupParameters.port = port;
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.err.println("Вы ввели не число после --listen-port");
                            e.printStackTrace();
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.PORT_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
                    case "--listen-address" ->{
                        startupParameters.listenAddress = args[i+1];
                        i+=2;
                    }
                    case "--backlog" ->{
                        try{
                            startupParameters.backlog = Integer.parseInt(args[i+1]);
                            i+=2;
                        }
                        catch (NumberFormatException e){
                            System.exit(RunErrorCodes.RUN_ERROR_CODES.BACKLOG_IS_NOT_A_NUMBER.errorCode);
                        }
                    }
//                    case "--db-subname"->{
//                        startupParameters.dbSubname = args[i+1];
//                        i+=2;
//                    }
//                    case "--dbms-name"->{
//                        startupParameters.dbmsName = args[i+1];
//                        i+=2;
//                    }
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
        }
        return startupParameters;
    }
}
