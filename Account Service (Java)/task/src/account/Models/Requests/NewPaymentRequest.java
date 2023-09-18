package account.Models.Requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class NewPaymentRequest {

    @NotEmpty(message = "You must send an email!")
    private String employee;
    @Pattern(regexp = "(0[0-9]|1[0-2])-([1-2][0-9]{3})", message = "Wrong date!")
    private String period;

    @Min(value = 0, message = "Salary must be non negative!")
    private long salary;

    public NewPaymentRequest() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}