package entities;

import general.message.servercommand.ServerCommand;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Entity
@Table(name = "groups")
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String name;
//    @ElementCollection
//    private Set<ServerCommand.Command> allowedCommands;
//    @ElementCollection
//    private Set<ServerCommand.InputTargetType> allowedInputTargetType;
    @OneToMany
    private Set<User> users;
    @Column
    private int rank;
    @Column
    boolean banned = false;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group() {
    }

    public boolean containsUser(User user){
        return users.contains(user);
    }
//    public boolean isCommandAllowed(ServerCommand.Command command){
//        return allowedCommands.contains(command);
//    }
//    public void allowCommand(ServerCommand.Command command){
//        allowedCommands.add(command);
//    }
    public void addUser(User user){
        users.add(user);
    }
    public boolean isBanned() {
        return banned;
    }
}
