package common;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.beans.repositories.UserRepository;
import spring.beans.services.group.GroupService;
import spring.beans.services.user.UserService;
import spring.configs.SpringConfig;

public class MainServer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        UserRepository userRepository = annotationConfigApplicationContext.getBean(UserRepository.class);
        UserService userService = annotationConfigApplicationContext.getBean(UserService.class);
        GroupService groupService = annotationConfigApplicationContext.getBean(GroupService.class);

//        Server server = new Server()
        if(userRepository.existsByName("david")){
            System.out.println("Такой пользователь существует!!!");
        }
        if(userService.exists("david")){
            System.out.println("Такой пользователь существует!!!");
        }
    }
}
