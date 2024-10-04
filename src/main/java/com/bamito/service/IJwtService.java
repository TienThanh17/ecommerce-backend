package com.bamito.service;

import com.bamito.entity.User;

public interface IJwtService {
    String generateToken(User user, boolean isAccessToken);
}
