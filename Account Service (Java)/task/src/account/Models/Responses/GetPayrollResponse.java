package account.Models.Responses;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class GetPayrollResponse {
    private String name;

    private String lastname;

    private String period;

    private String salary;

    public GetPayrollResponse(String name, String lastname, LocalDate period, Long salary) {
        this.name = name;
        this.lastname = lastname;
        setPeriod(period);
        setSalary(salary);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        String month = period.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String year = String.valueOf(period.getYear());
        this.period = month + "-" + year;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = String.format("%d dollar(s) %d cent(s)", salary / 100, salary - (salary/100*100));
    }
}
