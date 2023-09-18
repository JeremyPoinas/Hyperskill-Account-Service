package account.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AppUser {
    @Id
    @SequenceGenerator(name= "APP_USER_SEQ", sequenceName = "APP_USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.AUTO, generator="APP_USER_SEQ")
    private Long id;
    @NotEmpty(message = "name required")
    private String name;
    @NotEmpty(message = "lastname required")
    private String lastname;
    @Email(regexp = "^(.+)@acme\\.com$", message = "Mail does not include @acme.com")
    @NotEmpty(message = "email required")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "password required")
    @Size(min = 5, message = "The password length must be at least 5 chars!")
    private String password;
    @JsonIgnore
    @OneToMany(targetEntity=Payment.class, mappedBy = "employee")
    private List<Payment> payments = new ArrayList<>();

    public AppUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Payment> getPayments() {
        return payments;
    }
}
