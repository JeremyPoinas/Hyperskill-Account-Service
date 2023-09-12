package account.Repositories;

import account.Models.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsUserByEmailIgnoreCase(String email);
}
