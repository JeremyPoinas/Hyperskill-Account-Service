package account.Services;

import account.Exceptions.UserExistException;
import account.Exceptions.WrongPasswordException;
import account.Models.AppUser;
import account.Models.UserDetailsImpl;
import account.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository repository;

    private final PasswordEncoder encoder;

    private final List<String> breachedPasswords = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
    );

    public AppUserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public ResponseEntity<AppUser> signup(AppUser appUser) {
        assertPasswordBreached(appUser.getPassword());
        if (repository.existsUserByEmailIgnoreCase(appUser.getEmail())) {
            throw new UserExistException();
        } else {
            appUser.setEmail(appUser.getEmail().toLowerCase());
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

    public ResponseEntity<Map<String, String>> changePassword(UserDetailsImpl userDetails, String newPassword) {
        assertPasswordBreached(newPassword);
        assertPasswordDifferent(newPassword, userDetails.getPassword());

        AppUser appUser = repository.findByEmailIgnoreCase(userDetails.getUsername()).orElseThrow(UserExistException::new);
        appUser.setPassword(encoder.encode(newPassword));
        repository.save(appUser);
        Map<String, String> response = Map.of(
                "status", "The password has been updated successfully",
                "email", appUser.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    private void assertPasswordBreached(String password) {
        if (breachedPasswords.contains(password)) {
            throw new WrongPasswordException("The password is in the hacker's database!");
        }
    }

    private void assertPasswordDifferent(String newPassword, String hashOldPassword) {
        if (encoder.matches(newPassword, hashOldPassword)) {
            throw new WrongPasswordException("The passwords must be different!");
        }
    }
}
