package account.Models.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class RoleUpdateRequest {
    @NotEmpty
    @JsonProperty("user")
    private String email;

    @NotEmpty
    private String role;

    @Pattern(regexp = "(GRANT)|(REMOVE)")
    private String operation;

    public RoleUpdateRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
