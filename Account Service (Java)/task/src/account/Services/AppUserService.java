package account.Services;

import account.Exceptions.UserExistException;
import account.Models.AppUser;
import account.Models.UserDetailsImpl;
import account.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public AppUserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public ResponseEntity<AppUser> signup(AppUser appUser) {
        if (repository.existsUserByEmailIgnoreCase(appUser.getEmail())) {
            throw new UserExistException();
        } else {
            appUser.setPassword(encoder.encode(appUser.getPassword()));
            repository.save(appUser);
            return ResponseEntity.ok(appUser);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = repository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        return new UserDetailsImpl(appUser);
    }

    public ResponseEntity<AppUser> getUserInfo(UserDetailsImpl userDetails) {
        Optional<AppUser> user = repository.findByEmailIgnoreCase(userDetails.getUsername());
        return ResponseEntity.of(user);
    }
}
