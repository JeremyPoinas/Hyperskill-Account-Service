package account.Repositories;

import account.Models.RoleGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleGroupRepository extends CrudRepository<RoleGroup, Long> {
    Optional<RoleGroup> findByName(String code);
}
