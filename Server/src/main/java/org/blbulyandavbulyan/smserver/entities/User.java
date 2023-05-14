package org.blbulyandavbulyan.smserver.entities;

import org.blbulyandavbulyan.smgeneral.message.servercommand.ServerCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String name;
    @Column
    @Getter(AccessLevel.NONE)
    private int rank;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_id")
    private Group group;
    @Column
    private boolean banned = false;
    @Column
    private String passwordHash;

    public User() {
    }
    public int getRank(){
        return rank + (group != null ? group.getRank() : 0);
    }
    public boolean canExecute(ServerCommand.Command command){
        return true;
    }
    public boolean allowedTargetType(ServerCommand.InputTargetType inputTargetType){
        return true;
    }
}
