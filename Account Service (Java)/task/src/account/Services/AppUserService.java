package account.Services;

import account.Exceptions.*;
import account.Models.AppUser;
import account.Models.RoleGroup;
import account.Repositories.RoleGroupRepository;
import account.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleGroupRepository groupRepository;

    private final PasswordEncoder encoder;

    private final List<String> breachedPasswords = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
    );
    @Autowired
    public AppUserService(UserRepository userRepository, RoleGroupRepository groupRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.encoder = encoder;
    }

    public ResponseEntity<AppUser> signup(AppUser appUser) {
        if (userRepository.existsUserByEmailIgnoreCase(appUser.getEmail())) {
            throw new BadRequestException("User exist!");
        }
        assertPasswordBreached(appUser.getPassword());

        if (userRepository.count() == 0) {
            grantRole(appUser, "ADMINISTRATOR");
        } else {
            grantRole(appUser, "USER");
        }

        appUser.setEmail(appUser.getEmail().toLowerCase());
        appUser.setPassword(encoder.encode(appUser.getPassword()));
        userRepository.save(appUser);
        return ResponseEntity.ok(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadRequestException("User not found!"));

        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .authorities(getAuthorities(appUser)).build();
    }

    public AppUser loadAppUserByEmail(String email) {
        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    private Collection<GrantedAuthority> getAuthorities(AppUser appUser) {
        Set<RoleGroup> userRoles = appUser.getRoles();
        Collection<GrantedAuthority> authorities = new ArrayList<> (userRoles.size());
        for (RoleGroup userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.getCode().toUpperCase()));
        }
        return authorities;
    }

    public ResponseEntity<Map<String, String>> changePassword(User userDetails, String newPassword) {
        assertPasswordBreached(newPassword);

        AppUser appUser = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("User exist!"));
        assertPasswordDifferent(newPassword, appUser.getPassword());

        appUser.setPassword(encoder.encode(newPassword));
        userRepository.save(appUser);
        Map<String, String> response = Map.of(
                "status", "The password has been updated successfully",
                "email", appUser.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    private void assertPasswordBreached(String password) {
        if (breachedPasswords.contains(password)) {
            throw new BadRequestException("The password is in the hacker's database!");
        }
    }

    private void assertPasswordDifferent(String newPassword, String hashOldPassword) {
        if (encoder.matches(newPassword, hashOldPassword)) {
            throw new BadRequestException("The passwords must be different!");
        }
    }

    public ResponseEntity<List<AppUser>> getUsers() {
        List<AppUser> users = userRepository.findAllByOrderByIdAsc();
        return ResponseEntity.ok(users);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteUser(String email) {
        if (!userRepository.existsUserByEmailIgnoreCase(email)) {
            throw new NotFoundException("User not found!");
        }

        AppUser appUser = loadAppUserByEmail(email);
        if (hasAdminRole(appUser)) {
            throw new BadRequestException("Can't remove ADMINISTRATOR role!");
        }

        userRepository.delete(appUser);
        Map<String, String> response = Map.of(
                "status", "Deleted successfully!",
                "user", email
        );
        return ResponseEntity.ok(response);
    }


    public void grantRole(AppUser user, String role) {
        RoleGroup _role = groupRepository.findByName(role)
                .orElseThrow(() -> new NotFoundException("Role not found!"));

        boolean isAdmin = hasAdminRole(user);

        if (!user.getRoles().isEmpty() && (isAdmin || role.equals("ADMINISTRATOR"))) {
            throw new BadRequestException("The user cannot combine administrative and business roles!");
        }

        user.addRole(_role);
        userRepository.save(user);
    }

    private boolean hasAdminRole(AppUser user) {
        for (RoleGroup role : user.getRoles()) {
            if (role.getName().equals("ADMINISTRATOR")) {
                return true;
            }
        }
        return false;
    }

    public void removeRole(AppUser user, String role) {
        if (role.equals("ADMINISTRATOR")) {
            throw new BadRequestException("Can't remove ADMINISTRATOR role!");
        }

        RoleGroup _role = groupRepository.findByName(role)
                .orElseThrow(() -> new BadRequestException("Role not found!"));

        if (!user.getRoles().contains(_role)) {
            throw new BadRequestException("The user does not have a role!");
        }
        if (user.getRoles().size() == 1) {
            throw new BadRequestException("The user must have at least one role!");
        }

        user.removeRole(_role.getId());
        userRepository.save(user);
    }
}
