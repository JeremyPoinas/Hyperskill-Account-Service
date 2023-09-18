package account.Models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name="id_employee", nullable=false)
    private AppUser employee;

    private LocalDate period;

    private Long salary;

    public Payment() {
    }

    public Payment(AppUser employee, LocalDate period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public AppUser getEmployee() {
        return employee;
    }
}
