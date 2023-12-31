package account.Services;

import account.Exceptions.BadRequestException;
import account.Models.AppUser;
import account.Models.Payment;
import account.Models.Requests.NewPaymentRequest;
import account.Models.Responses.GetPayrollResponse;
import account.Repositories.PaymentRepository;
import account.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository repository, UserRepository userRepository) {
        this.paymentRepository = repository;
        this.userRepository = userRepository;
    }
    @Transactional
    public ResponseEntity<Map<String, String>> uploadPayrolls(List<NewPaymentRequest> newPayments) {
        for (NewPaymentRequest newPaymentRequest : newPayments) {
            Payment payment = createPayment(newPaymentRequest);
            if (paymentRepository.existsPaymentByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod())) {
                throw new BadRequestException("Error!");
            } else {
                paymentRepository.save(payment);
            }
        }
        Map<String, String> response = Map.of(
                "status", "Added successfully!"
        );
        return ResponseEntity.ok(response);
    }

    public Payment createPayment(NewPaymentRequest newPaymentRequest) {
        LocalDate period = parsePeriod(newPaymentRequest.getPeriod());
        AppUser employee = userRepository
                .findByEmailIgnoreCase(newPaymentRequest.getEmployee())
                .orElseThrow(() -> new BadRequestException("User does not exist!"));
        return new Payment(employee, period, newPaymentRequest.getSalary());
    }


    public ResponseEntity<Map<String, String>> updatePayroll(NewPaymentRequest newPaymentRequest) {
        Payment newPayment = createPayment(newPaymentRequest);
        Payment paymentInDatabase = paymentRepository
                .findPaymentByEmployeeAndPeriod(newPayment.getEmployee(), newPayment.getPeriod())
                .orElseThrow(() -> new BadRequestException("Error!"));

        paymentInDatabase.setSalary(newPayment.getSalary());
        paymentRepository.save(paymentInDatabase);

        Map<String, String> response = Map.of(
                "status", "Updated successfully!"
        );
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<GetPayrollResponse> getPayroll(
            UserDetails userDetails,
            String period) {
        AppUser employee = userRepository
                .findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException("User does not exist!"));

        Payment payment = paymentRepository
                .findPaymentByEmployeeAndPeriod(employee, parsePeriod(period))
                .orElseThrow(() -> new BadRequestException("Error!"));

        GetPayrollResponse response = new GetPayrollResponse(
                employee.getName(),
                employee.getLastname(),
                payment.getPeriod(),
                payment.getSalary()
        );

        return ResponseEntity.ok(response);
    }

    public LocalDate parsePeriod(String period) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse("01-" + period, formatter);
    }

    public ResponseEntity<List<GetPayrollResponse>> getAllPayrolls(UserDetails userDetails) {
        AppUser employee = userRepository
                .findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException("User does not exist!"));

        List<Payment> payments = paymentRepository
                .findPaymentsByEmployeeOrderByPeriodDesc(employee)
                .orElseThrow(() -> new BadRequestException("Error!"));

        List<GetPayrollResponse> payrollsResponse = new ArrayList<>();

        for (Payment payment : payments) {
            GetPayrollResponse payrollResponse = new GetPayrollResponse(
                    employee.getName(),
                    employee.getLastname(),
                    payment.getPeriod(),
                    payment.getSalary()
            );
            payrollsResponse.add(payrollResponse);
        }

        return ResponseEntity.ok(payrollsResponse);
    }
}
