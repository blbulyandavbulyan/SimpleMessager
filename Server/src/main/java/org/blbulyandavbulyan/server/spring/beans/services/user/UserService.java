package org.blbulyandavbulyan.server.spring.beans.services.user;

import org.blbulyandavbulyan.server.entities.User;
import org.blbulyandavbulyan.server.interfaces.loginandregister.LoginAndRegisterUserInterface;
import org.blbulyandavbulyan.server.interfaces.loginandregister.exceptions.UserAlreadyExistsException;
import org.blbulyandavbulyan.server.interfaces.ManagerInterface;
import org.blbulyandavbulyan.server.spring.beans.services.user.exceptions.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.blbulyandavbulyan.server.spring.beans.repositories.UserRepository;

@Service
public class UserService implements ManagerInterface<User>, LoginAndRegisterUserInterface {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public int getRank(String targetName) {
        return get(targetName).getRank();
    }
    @Override
    public boolean login(String userName, String password){
        try {
            User user = get(userName);
            return bCryptPasswordEncoder.matches(password, user.getPasswordHash());
        }
        catch (UserDoesNotExistsException e){
            return false;
        }
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
        User user = get(targetName);
        user.setRank(rank);
        userRepository.save(user);
    }

    @Override
    public void rename(String targetName, String newName) {
        User user = get(targetName);
        user.setName(newName);
        userRepository.save(user);
    }

    @Override
    public void delete(String targetName) {
        userRepository.deleteByName(targetName);
    }

    @Override
    public void add(User obj) {
        if(!userRepository.existsByName(obj.getName()))
            userRepository.save(obj);
        else throw new UserAlreadyExistsException();
    }

    @Override
    public User get(String userName) {
        return userRepository.findByName(userName).orElseThrow(UserDoesNotExistsException::new);
    }

    @Override
    public User[] getAll() {
        return userRepository.findAll().toArray(User[]::new);
    }

    @Override
    public void ban(String targetName) {
        User user = get(targetName);
        user.setBanned(true);
        userRepository.save(user);
    }

    @Override
    public void unban(String targetName) {
        User user = get(targetName);
        user.setBanned(false);
        userRepository.save(user);
    }

    public boolean exists(String username){
        return userRepository.existsByName(username);
    }

    @Override
    public boolean banned(String targetName) {
        return get(targetName).isBanned();
    }

    public void setPassword(String targetName, String password) {
        User user = get(targetName);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
}
