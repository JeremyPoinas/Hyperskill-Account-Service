package account.Controllers;

import account.Models.Account;
import account.Models.AccountDto;
import account.Services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<AccountDto> authUser(@RequestBody @Valid Account account) {
        return authService.signup(account);
    }
}
