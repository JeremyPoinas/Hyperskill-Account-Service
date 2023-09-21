package account.Controllers;

import account.Models.AppUser;
import account.Models.Requests.PasswordChangedRequest;
import account.Models.Requests.RoleUpdateRequest;
import account.Services.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    AppUserService appUserService;
    @Autowired
    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<AppUser> signup(@RequestBody @Valid AppUser appUser) {
        return appUserService.signup(appUser);
    }

    @PostMapping("/api/auth/changepass")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal User userDetails,
            @RequestBody @Valid PasswordChangedRequest request
    ) {
        return appUserService.changePassword(userDetails, request.getNewPassword());
    }

    @GetMapping("/api/admin/user/")
    public ResponseEntity<List<AppUser>> getUsers() {
        return appUserService.getUsers();
    }

    @DeleteMapping("/api/admin/user/{email}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String email) {
        return appUserService.deleteUser(email);
    }

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<AppUser> updateRole(@RequestBody @Valid RoleUpdateRequest request) {
        AppUser user = appUserService.loadAppUserByEmail(request.getEmail());
        String role = request.getRole();

        if (request.getOperation().equals("GRANT")) {
            appUserService.grantRole(user, role);
        } else {
            appUserService.removeRole(user, role);
        }
        return ResponseEntity.ok(user);
    }
}
