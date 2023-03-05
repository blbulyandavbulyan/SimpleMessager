package spring.beans.repositories;

import entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    void deleteByName(String name);
    boolean existsByName(String name);
    Optional<Group> findByName(String name);
}
