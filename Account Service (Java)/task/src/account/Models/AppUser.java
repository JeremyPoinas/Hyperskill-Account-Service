package account.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class AppUser {
    @Id
    @SequenceGenerator(name= "APP_USER_SEQ", sequenceName = "APP_USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.AUTO, generator="APP_USER_SEQ")
    private long id;
    @NotEmpty(message = "name required")
    @NotNull
    private String name;
    @NotEmpty(message = "lastname required")
    @NotNull
    private String lastname;
    @Email(regexp = "^(.+)@acme\\.com$", message = "Mail does not include @acme.com")
    @NotNull
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "password required")
    @NotNull
    private String password;

    public AppUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
