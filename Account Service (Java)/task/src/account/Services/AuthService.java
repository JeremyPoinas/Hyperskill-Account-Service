package account.Services;

import account.Models.Account;
import account.Models.AccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public ResponseEntity<AccountDto> signup(Account account) {
        AccountDto accountRegistered = new AccountDto(account.getName(), account.getLastname(), account.getEmail());
        return new ResponseEntity<>(accountRegistered, HttpStatus.OK);
    }
}
