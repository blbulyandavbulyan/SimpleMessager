package org.blbulyandavbulyan.smgeneral.dtos;

public interface UserDto {
    Long getId();
    String getName();
    int getRank();
    GroupDto getGroup();
    boolean getBanned();

}
