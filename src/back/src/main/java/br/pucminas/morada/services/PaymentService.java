// package br.pucminas.morada.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import br.pucminas.morada.models.payment.Payment;
// import br.pucminas.morada.models.payment.dto.PaymentUpdateDTO;
// import br.pucminas.morada.repositories.PaymentRepository;
// import jakarta.validation.Valid;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class PaymentService {

//     @Autowired
//     private PaymentRepository paymentRepository;

//     public Payment findById(Long id) {
//         Optional<Payment> optionalPayment = paymentRepository.findById(id);

//         if (optionalPayment.isPresent()) {
//             return optionalPayment.get();
//         } else {
//             throw new RuntimeException("Pagamento não encontrado.");
//         }
//     }

//     public List<Payment> findAll() {
//         return paymentRepository.findAll();
//     }

//     public Payment save(Payment payment) {
       
//         if (payment.getCreatedAt() == null) {
//             payment.setCreatedAt(LocalDateTime.now());
//         }
    
//         return paymentRepository.save(payment);
//     }

//     @Transactional
//     public Payment update(Long id, @Valid PaymentUpdateDTO paymentUpdateDTO) {
//         Payment payment = findById(id);

//         if (paymentUpdateDTO.getRentValue() != null) {
//             payment.setRentValue(paymentUpdateDTO.getRentValue());
//         }
//         if (paymentUpdateDTO.getCompetenceMonth() != null) {
//             payment.setCompetenceMonth(paymentUpdateDTO.getCompetenceMonth());
//         }
//         if (paymentUpdateDTO.getCompetenceYear() != null) {
//             payment.setCompetenceYear(paymentUpdateDTO.getCompetenceYear());
//         }
//         if (paymentUpdateDTO.getStatus() != null) {
//             payment.setStatus(paymentUpdateDTO.getStatus());
//         }

//         return paymentRepository.save(payment);
//     }
// }

