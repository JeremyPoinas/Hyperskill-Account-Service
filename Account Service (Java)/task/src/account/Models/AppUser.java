package account.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.util.*;

@JsonSerialize(using = AppUserSerializer.class)
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @JsonIgnore
    @ManyToMany(
            cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(name = "user_groups",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"
            ))
    private Set<RoleGroup> roles = new HashSet<>();

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

    public Set<RoleGroup> getRoles() {
        return roles;
    }

    public void addRole(RoleGroup role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(long roleId) {
        RoleGroup role = roles.stream()
                .filter(g -> g.getId() == roleId).findFirst()
                .orElse(null);
        if (role != null) {
            roles.remove(role);
            role.getUsers().remove(this);
        }
    }
}

class AppUserSerializer extends StdSerializer<AppUser> {

    public AppUserSerializer() {
        this(null);
    }

    public AppUserSerializer(Class<AppUser> user) {
        super(user);
    }

    @Override
    public void serialize(AppUser value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("lastname", value.getLastname());
        gen.writeStringField("email", value.getEmail());

        List<String> roles = value.getRoles().stream()
                .map(RoleGroup::getCode)
                .sorted(Comparator.naturalOrder())
                .toList();
        gen.writeObjectField("roles", roles);
        gen.writeEndObject();
    }
}