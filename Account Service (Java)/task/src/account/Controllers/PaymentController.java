package account.Controllers;

import account.Models.Requests.NewPaymentRequest;
import account.Services.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Validated
@RestController
public class PaymentController {
    PaymentService paymentService;
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/api/acct/payments")
    public ResponseEntity<Map<String, String>> uploadPayrolls(@RequestBody List<@Valid NewPaymentRequest> newPayments) {
        return paymentService.uploadPayrolls(newPayments);
    }

    @PutMapping("/api/acct/payments")
    public ResponseEntity<Map<String, String>> updatePayroll(@RequestBody @Valid NewPaymentRequest newPayment) {
        return paymentService.updatePayroll(newPayment);
    }

    @GetMapping("/api/empl/payment")
    public ResponseEntity<?> getPayroll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @Pattern(regexp = "(0[0-9]|1[0-2])-([1-2][0-9]{3})", message = "Wrong date!") String period) {
        if (period == null) {
            return paymentService.getAllPayrolls(userDetails);
        } else {
            return paymentService.getPayroll(userDetails, period);
        }
    }
}
