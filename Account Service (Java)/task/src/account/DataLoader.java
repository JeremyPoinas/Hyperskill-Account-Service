package account;

import account.Models.RoleGroup;
import account.Repositories.RoleGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private RoleGroupRepository groupRepository;

    @Autowired
    public DataLoader(RoleGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    public void createRoles() {
        try {
            groupRepository.save(new RoleGroup("ROLE_ADMINISTRATOR", "ADMINISTRATOR"));
            groupRepository.save(new RoleGroup("ROLE_USER", "USER"));
            groupRepository.save(new RoleGroup("ROLE_ACCOUNTANT", "ACCOUNTANT"));
        } catch (Exception ignored) {
        }
    }
}