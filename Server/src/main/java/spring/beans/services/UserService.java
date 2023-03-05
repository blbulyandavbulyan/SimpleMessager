package spring.beans.services;

import entities.User;
import loginandregister.LoginAndRegisterUserInterface;
import loginandregister.exceptions.UserAlreadyExistsException;
import manager.ManagerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.beans.repositories.UserRepository;

public class UserService implements ManagerInterface<User>, LoginAndRegisterUserInterface {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public int getRank(String targetName) {
        return userRepository.findByName(targetName).getRank();
    }
    @Override
    public boolean login(String userName, String password){
        User user = userRepository.findByName(userName);
        if(user == null)return false;
        return bCryptPasswordEncoder.matches(user.getPasswordHash(), password);
    }
    @Override
    public void register(String userName, String password){
        User user = new User();
        if(exists(userName))throw new UserAlreadyExistsException();
        user.setName(userName);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
    @Override
    public void setRank(String targetName, Integer rank) {
        User user = userRepository.findByName(targetName);
        user.setRank(rank);
    }

    @Override
    public void rename(String targetName, String newName) {
        User user = userRepository.findByName(targetName);
        user.setName(newName);
        userRepository.save(user);
    }

    @Override
    public void delete(String targetName) {
        userRepository.deleteByName(targetName);
    }

    @Override
    public void add(User obj) {
        userRepository.save(obj);
    }

    @Override
    public User get(String userName) {
        return userRepository.findByName(userName);
    }

    @Override
    public User[] getAll() {
        return userRepository.findAll().toArray(User[]::new);
    }

    @Override
    public void ban(String targetName) {
        User user = userRepository.findByName(targetName);
        user.setBanned(true);
        userRepository.save(user);
    }

    @Override
    public void unban(String targetName) {
        User user = userRepository.findByName(targetName);
        user.setBanned(false);
        userRepository.save(user);
    }

    public boolean exists(String username){
        User user = new User();
        user.setName(username);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("id").withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<User> userExample = Example.of(user, exampleMatcher);
        return userRepository.exists(userExample);
    }

    @Override
    public boolean banned(String targetName) {
        return userRepository.findByName(targetName).isBanned();
    }
}
