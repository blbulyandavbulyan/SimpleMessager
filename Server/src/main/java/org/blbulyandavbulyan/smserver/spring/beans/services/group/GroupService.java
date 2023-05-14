package org.blbulyandavbulyan.smserver.spring.beans.services.group;

import org.blbulyandavbulyan.smserver.entities.Group;
import org.blbulyandavbulyan.smserver.interfaces.ManagerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.blbulyandavbulyan.smserver.spring.beans.repositories.GroupRepository;
import org.blbulyandavbulyan.smserver.spring.beans.services.group.exceptions.GroupAlreadyExists;
import org.blbulyandavbulyan.smserver.spring.beans.services.group.exceptions.GroupDoesNotExist;

@Service
public class GroupService implements ManagerInterface<Group> {
    @Autowired
    private GroupRepository groupRepository;
    @Override
    public int getRank(String targetName) {
        return get(targetName).getRank();
    }

    @Override
    public void setRank(String targetName, Integer rank) {
        Group group = get(targetName);
        group.setRank(rank);
        groupRepository.save(group);
    }

    @Override
    public void rename(String targetName, String newName) {
        Group group = get(targetName);
        group.setName(newName);
        groupRepository.save(group);
    }

    @Override
    public void delete(String targetName) {
        groupRepository.deleteByName(targetName);
    }

    @Override
    public void add(Group obj) {
        if(groupRepository.existsByName(obj.getName()))
            throw new GroupAlreadyExists();
        groupRepository.save(obj);
    }
    @Override
    public Group get(String groupName) {
        return groupRepository.findByName(groupName).orElseThrow(GroupDoesNotExist::new);
    }

    @Override
    public Group[] getAll() {
        return groupRepository.findAll().toArray(Group[]::new);
    }

    @Override
    public void ban(String targetName) {
        Group group = get(targetName);
        group.setBanned(true);
        groupRepository.save(group);
    }

    @Override
    public void unban(String targetName) {
        Group group = get(targetName);
        group.setBanned(false);
        groupRepository.save(group);
    }

    @Override
    public boolean exists(String targetName) {
        return groupRepository.existsByName(targetName);
    }

    @Override
    public boolean banned(String targetName) {
        return get(targetName).isBanned();
    }
}
