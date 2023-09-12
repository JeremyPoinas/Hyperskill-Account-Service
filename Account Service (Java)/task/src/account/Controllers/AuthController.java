package account.Controllers;

import account.Models.AppUser;
import account.Models.UserDetailsImpl;
import account.Services.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("api/empl/payment")
    public ResponseEntity<AppUser> test(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return appUserService.getUserInfo(userDetails);
    }
}
