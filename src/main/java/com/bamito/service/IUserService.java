package com.bamito.service;

import com.bamito.dto.request.user.*;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.user.*;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Set;

public interface IUserService {
    UserResponse register(RegisterRequest request) throws MessagingException;
    LoginResponse login(LoginRequest request);
    PaginationResponse<UserResponse> getAllUsers(int page, int size);
    UserResponse updateUser(UpdateUserRequest request, long id) throws IOException;
    void deleteUser(long id);
    boolean verifyEmail(String email, String otp);
    void regenerateOtp(String email);
    UserInfoResponse getUserInfo(String email);
    UserResponse getUser(long id);
    Set<RoleResponse> getAllRole();
    void createUser(CreateUserRequest request);
    void changePassword(ChangePasswordRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void sendOtp(String email) throws MessagingException;
}

