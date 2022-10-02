package general.privileges;

import java.util.HashMap;
import java.util.Map;

public class PrivilegesMapper {
    private final static Map<String, Privilege> stringToPrivilege = new HashMap<>();
    static {
        for (Privilege privilege : Privilege.values()) {
            stringToPrivilege.put(privilege.name(), privilege);
        }
    }

    public static void main(String[] args) {
        System.out.println(Privilege.DELETE_GROUP);
    }
}
