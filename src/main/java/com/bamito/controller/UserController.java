package com.bamito.controller;

import com.bamito.dto.request.user.*;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.user.*;
import com.bamito.entity.Role;
import com.bamito.service.implement.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserServiceImpl userService;

    @NonFinal
    @Value("${frontend-url}")
    String FRONTEND_URL;

//    @PostMapping("/upload-image")
//    public ResponseObject<Map> uploadImage(@RequestParam("image") MultipartFile file) {
//        var result = userService.uploadImage(file);
//        return ResponseObject.<Map>builder()
//                .data(result)
//                .build();
//    }

    @PostMapping("/register")
    public ResponseObject<UserResponse> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        var result = userService.register(request);
        return ResponseObject.<UserResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/login")
    public ResponseObject<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var result = userService.login(request);
        if (Objects.nonNull(result.getAccessToken())) {
            Cookie accessCookie = new Cookie("accessToken", result.getAccessToken());
            accessCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(true);
            accessCookie.setPath("/");

            Cookie refreshCookie = new Cookie("refreshToken", result.getRefreshToken());
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");

            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);
        }

        return ResponseObject.<LoginResponse>builder()
                .data(result)
                .build();
    }

    @GetMapping("/verify-email")
    public RedirectView verifyEmail(@RequestParam("email") String email,
                                    @RequestParam("otp") String otp) {
        var result = userService.verifyEmail(email, otp);

        return new RedirectView(FRONTEND_URL + "/login");
    }

    @PatchMapping("/regenerate-otp")
    public ResponseObject<?> regenerateOtp(@RequestParam("email") String email) {
        userService.regenerateOtp(email);

        return ResponseObject.builder().build();
    }

    @PatchMapping("/send-otp")
    public ResponseObject<?> sendOtp(@RequestParam("email") String email) throws MessagingException {
        userService.sendOtp(email);
        return ResponseObject.builder().build();
    }

    @PatchMapping("/forgot-password")
    public ResponseObject<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request);
        return ResponseObject.builder().build();
    }

    @PostAuthorize("(returnObject.data.user.email == authentication.name) and " +
            "hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/get-user-info")
    public ResponseObject<UserInfoResponse> getUserInfo(@RequestParam("email") String email) {
        var result = userService.getUserInfo(email);

        return ResponseObject.<UserInfoResponse>builder()
                .data(result)
                .build();
    }

    @PostAuthorize("(returnObject.data.email == authentication.name) and " +
            "hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseObject<UserResponse> getUser(@RequestParam("id") long id) {
        var result = userService.getUser(id);

        return ResponseObject.<UserResponse>builder()
                .data(result)
                .build();
    }

    // Trường hợp nhận JSON
    @PatchMapping(consumes = "application/json")
    public ResponseObject<UserResponse> updateUserJson(
            @RequestBody UpdateUserRequest request,
            @RequestParam("id") long id
    ) throws IOException {
        var result = userService.updateUser(request, id);

        return ResponseObject.<UserResponse>builder()
                .data(result)
                .build();
    }

    // Trường hợp nhận form-data
    @PatchMapping(consumes = "multipart/form-data")
    public ResponseObject<UserResponse> updateUserForm(
            @ModelAttribute UpdateUserRequest request,
            @RequestParam("id") long id
    ) throws IOException {
        var result = userService.updateUser(request, id);

        return ResponseObject.<UserResponse>builder()
                .data(result)
                .build();
    }

    @GetMapping("/get-all-user")
    public ResponseObject<PaginationResponse<UserResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ) {
        var result = userService.getAllUsers(page, size);
        return ResponseObject.<PaginationResponse<UserResponse>>builder()
                .data(result)
                .build();
    }

    @DeleteMapping
    public ResponseObject<?> deleteUser(@RequestParam(name = "id") long id) {
        userService.deleteUser(id);

        return ResponseObject.builder().build();
    }

    @GetMapping("/get-all-role")
    public ResponseObject<Set<RoleResponse>> getAllRole() {
        var result = userService.getAllRole();

        return ResponseObject.<Set<RoleResponse>>builder()
                .data(result)
                .build();
    }

    @PostMapping
    public ResponseObject<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseObject.builder().build();
    }

    @PatchMapping("/change-password")
    public ResponseObject<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseObject.builder().build();
    }
}
