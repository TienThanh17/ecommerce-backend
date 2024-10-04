package com.bamito.mapper;

import com.bamito.dto.request.user.CreateUserRequest;
import com.bamito.dto.request.user.RegisterRequest;
import com.bamito.dto.response.user.RoleResponse;
import com.bamito.dto.response.user.UserInfoResponse;
import com.bamito.dto.response.user.UserResponse;
import com.bamito.entity.Address;
import com.bamito.entity.Product;
import com.bamito.entity.Role;
import com.bamito.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IUserMapper extends IEntityMapper<User, UserResponse> {
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequest createUserRequest);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    User toEntity(CreateUserRequest createUserRequest);

    @Override
    @Mapping(source = "role.name", target = "role")
    @Mapping(target = "addresses", expression = "java(mapAddress(user.getAddresses()))")
    UserResponse toDto(User user);

    RoleResponse toRoleResponse(Role role);

    default Set<String> mapAddress(Set<Address> addresses) {
        return addresses.stream()
                .map(Address::getAddress)
                .collect(Collectors.toSet());
    }

//    @Mapping(source = "role.name", target = "role")
//    @Mapping(target = "favoriteProducts", expression = "java(mapProductId(user.getFavoriteProducts()))")
//    UserInfoResponse toUserInfoResponse(User user);
//
//    default Set<String> mapProductId(Set<Product> products) {
//        return products.stream()
//                .map(Product::getId)
//                .collect(Collectors.toSet());
//    }
}

