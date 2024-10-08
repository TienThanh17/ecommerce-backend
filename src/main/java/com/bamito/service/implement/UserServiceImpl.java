package com.bamito.service.implement;

import com.bamito.dto.request.user.*;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.user.*;
import com.bamito.entity.Product;
import com.bamito.entity.Role;
import com.bamito.enums.RoleEnum;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.mapper.IUserMapper;
import com.bamito.entity.Address;
import com.bamito.entity.User;
import com.bamito.repository.IAddressRepository;
import com.bamito.repository.IProductRepository;
import com.bamito.repository.IRoleRepository;
import com.bamito.repository.IUserRepository;
import com.bamito.service.IJwtService;
import com.bamito.service.IUserService;
import com.bamito.util.EmailUtil;
import com.cloudinary.Cloudinary;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements IUserService {
    IUserRepository userRepository;
    IAddressRepository addressRepository;
    IRoleRepository roleRepository;
    IUserMapper userMapper;
    PasswordEncoder passwordEncoder;
    EmailUtil emailUtil;
    IJwtService jwtService;
    Cloudinary cloudinary;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));

        boolean isCorrectPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isCorrectPassword) throw new CustomizedException(ErrorCode.INCORRECT_PASSWORD);

        if (!user.getActive()) {
            regenerateOtp(user.getEmail());
            throw new CustomizedException(ErrorCode.EMAIL_IS_NOT_VERIFIED);
        }

        String accessToken = jwtService.generateToken(user, true);
        String refreshToken = jwtService.generateToken(user, false);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomizedException(ErrorCode.ACCOUNT_EXISTED);
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new CustomizedException(ErrorCode.PHONE_EXISTED);
        }
        User user = userMapper.toEntity(request);
        user.setRole(roleRepository.findById(request.getRole())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ROLE_NOT_EXISTED)));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(false);
        try {
            UUID otp = UUID.randomUUID();
            user.setOtpCode(otp.toString());
            user.setOtpExpiry(LocalDateTime.now().plusYears(1));
            emailUtil.sendLinkAuthenticateEmail(request.getEmail(), otp.toString());
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
        userRepository.save(user);
        Address address = Address.builder()
                .address(request.getAddress())
                .user(user)
                .build();
        addressRepository.save(address);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));

        boolean isCorrectPassword = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
        if (!isCorrectPassword) throw new CustomizedException(ErrorCode.INCORRECT_PASSWORD);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        if (!Objects.equals(user.getOtpCode(), request.getOtp()))
            throw new CustomizedException(ErrorCode.INVALID_OTP);
        if (user.getOtpExpiry().isBefore(LocalDateTime.now()))
            throw new CustomizedException(ErrorCode.EXPIRY_OTP);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void sendOtp(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        String otp = UUID.randomUUID().toString().substring(0, 8);
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(3));
        userRepository.save(user);
        emailUtil.sendOtpResetPassword(user.getEmail(), user.getUsername(), user.getOtpCode());
    }

    @Override
    public UserResponse register(RegisterRequest request) throws MessagingException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomizedException(ErrorCode.ACCOUNT_EXISTED);
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(false);

        UUID otp = UUID.randomUUID();
        user.setOtpCode(otp.toString());
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(3));

        Optional<Role> role = roleRepository.findById(RoleEnum.USER.toString());
        role.ifPresent(user::setRole);

        emailUtil.sendLinkAuthenticateEmail(request.getEmail(), otp.toString());

        userRepository.save(user);

        UserResponse userResponse = userMapper.toDto(user);
        userResponse.setRole(RoleEnum.USER.toString());

        return userResponse;
    }

    @Override
    public boolean verifyEmail(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        if (user.getOtpCode().equals(otp) && LocalDateTime.now().isBefore(user.getOtpExpiry())) {
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        String otp = UUID.randomUUID().toString();
        try {
            emailUtil.sendLinkAuthenticateEmail(email, otp);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(3));
        userRepository.save(user);
    }

    @Override
    public PaginationResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> userList = userPage.getContent();
        List<UserResponse> userResponseList = userList.stream().map(user -> {
            UserResponse userRes = userMapper.toDto(user);
            userRes.setAddresses(addressRepository.findAllByUserId(user.getId()));
            return userRes;
        }).toList();

        return PaginationResponse.<UserResponse>builder()
                .content(userResponseList)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPage(userPage.getTotalPages())
                .build();
    }

    @Override
    public UserInfoResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));

        UserResponse userResponse = userMapper.toDto(user);
        userResponse.setAddresses(addressRepository.findAllByUserId(user.getId()));
        Set<String> favorites = user.getFavoriteProducts()
                .stream()
                .map(Product::getProductId)
                .collect(Collectors.toSet());

        return new UserInfoResponse(userResponse, favorites);
    }

    @Override
    public UserResponse getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        return userMapper.toDto(user);
    }

    @Override
    public Set<RoleResponse> getAllRole() {
        return roleRepository.findAll().
                stream()
                .map(userMapper::toRoleResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest request, long id) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        if (Objects.nonNull(request.getUsername()) && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }
        if (Objects.nonNull(request.getPhoneNumber()) && !request.getPhoneNumber().isBlank()) {
            if (userRepository.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), user.getId())) {
                throw new CustomizedException(ErrorCode.PHONE_EXISTED);
            }
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (Objects.nonNull(request.getPassword()) && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (Objects.nonNull(request.getRole()) && !request.getRole().isBlank()) {
            Role role = roleRepository.findById(request.getRole())
                    .orElseThrow(() -> new CustomizedException(ErrorCode.ROLE_NOT_EXISTED));
            user.setRole(role);
        }
        if (Objects.nonNull(request.getAvatar()) && !request.getAvatar().isEmpty()) {
            Map cloudinaryObject = cloudinary.uploader()
                    .upload(request.getAvatar().getBytes(), Map.of("folder", "bamito"));
            if (user.getAvatarId() != null) {
                cloudinary.uploader().destroy(user.getAvatarId(), Map.of());
            }
            var avatarUrl = cloudinaryObject.get("secure_url");
            var avatarId = cloudinaryObject.get("public_id");
            user.setAvatarUrl(avatarUrl.toString());
            user.setAvatarId(avatarId.toString());
        }
        if (Objects.nonNull(request.getDateOfBirth())) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (Objects.nonNull(request.getEmail()) &&
                !request.getEmail().isBlank() &&
                !request.getEmail().equals(user.getEmail())
        ) {
            if (userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
                throw new CustomizedException(ErrorCode.ACCOUNT_EXISTED);
            }
            try {
                UUID otp = UUID.randomUUID();
                user.setOtpCode(otp.toString());
                user.setOtpExpiry(LocalDateTime.now().plusYears(1));
                emailUtil.sendLinkAuthenticateEmail(request.getEmail(), otp.toString());
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
            user.setActive(false);
            user.setEmail(request.getEmail());
        }
        userRepository.save(user);

        if (Objects.nonNull(request.getAddress()) && !request.getAddress().isBlank()) {
            Address address = addressRepository.findByUserId(user.getId());
            if (Objects.isNull(address)) {
                address = Address.builder()
                        .address(request.getAddress())
                        .user(user)
                        .build();
            } else {
                address.setAddress(request.getAddress());
            }
            addressRepository.save(address);
        }

        UserResponse userResponse = userMapper.toDto(user);
        userResponse.setAddresses(addressRepository.findAllByUserId(user.getId()));
        return userResponse;
    }

    @Override
    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
        userRepository.deleteById(id);
    }
}
