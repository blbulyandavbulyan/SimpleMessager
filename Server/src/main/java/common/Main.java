package common;

import entities.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.beans.repositories.UserRepository;
import spring.configs.SpringConfig;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        UserRepository userRepository = annotationConfigApplicationContext.getBean(UserRepository.class);
        User user = userRepository.findByName("test");
        System.out.println(user.getName());
    }
}
