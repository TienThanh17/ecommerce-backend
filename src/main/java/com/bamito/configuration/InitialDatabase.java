package com.bamito.configuration;

import com.bamito.entity.Payment;
import com.bamito.entity.User;
import com.bamito.enums.PaymentEnum;
import com.bamito.enums.RoleEnum;
import com.bamito.entity.Role;
import com.bamito.repository.IPaymentRepository;
import com.bamito.repository.IRoleRepository;
import com.bamito.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialDatabase {

    @Bean
    CommandLineRunner runner(IRoleRepository roleRepository,
                             IUserRepository userRepository,
                             IPaymentRepository paymentRepository,
                             PasswordEncoder passwordEncoder)
    {
        return args -> {
            if (!roleRepository.existsById(RoleEnum.ADMIN.toString())) {
                Role adminRole = Role.builder()
                        .name(RoleEnum.ADMIN.toString())
                        .description("Administrator")
                        .build();

                roleRepository.save(adminRole);
            }
            if (!roleRepository.existsById(RoleEnum.USER.toString())) {
                Role userRole = Role.builder()
                        .name(RoleEnum.USER.toString())
                        .description("User customer")
                        .build();

                roleRepository.save(userRole);
            }
            if (!paymentRepository.existsById(PaymentEnum.COD.toString())) {
                Payment payment = Payment.builder()
                        .name(PaymentEnum.COD.toString())
                        .description("COD payment")
                        .build();
                paymentRepository.save(payment);
            }
            if (!paymentRepository.existsById(PaymentEnum.PAYPAL.toString())) {
                Payment payment = Payment.builder()
                        .name(PaymentEnum.PAYPAL.toString())
                        .description("PAYPAL payment")
                        .build();
                paymentRepository.save(payment);
            }
            if (!paymentRepository.existsById(PaymentEnum.VNPAY.toString())) {
                Payment payment = Payment.builder()
                        .name(PaymentEnum.VNPAY.toString())
                        .description("VNPAY payment")
                        .build();
                paymentRepository.save(payment);
            }
            if (!userRepository.existsByEmail("admin@bamito.com")) {
                Role role = roleRepository.findById(RoleEnum.ADMIN.toString()).orElse(null);
                User user = User.builder()
                        .email("admin@bamito.com")
                        .password(passwordEncoder.encode("12345678"))
                        .username("admin")
                        .phoneNumber("0325677402")
                        .active(true)
                        .role(role)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
