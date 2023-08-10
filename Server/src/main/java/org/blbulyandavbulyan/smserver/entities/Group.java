package org.blbulyandavbulyan.smserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
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
    @OneToMany(cascade = CascadeType.PERSIST)
    private Collection<User> users;
    @Column
    private int rank;
    @Column
    boolean banned = false;

    public Group() {
    }

}
