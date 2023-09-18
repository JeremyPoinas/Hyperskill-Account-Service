package account.Repositories;

import account.Models.AppUser;
import account.Models.Payment;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
    boolean existsPaymentByEmployeeAndPeriod(AppUser employee, LocalDate period);

    Optional<Payment> findPaymentByEmployeeAndPeriod(AppUser employee, LocalDate period);

    Optional<List<Payment>> findPaymentsByEmployeeOrderByPeriodDesc(AppUser employee);
}
