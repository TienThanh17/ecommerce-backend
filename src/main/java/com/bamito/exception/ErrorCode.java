package com.bamito.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(401, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "you don't have permission", HttpStatus.FORBIDDEN),
    CAN_NOT_SEND_OTP(1, "can't send otp to email", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCOUNT_EXISTED(2, "email existed", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXISTED(3, "account doesn't exist", HttpStatus.NOT_FOUND),
    INCORRECT_PASSWORD(4, "incorrect password", HttpStatus.BAD_REQUEST),
    CAN_NOT_CREATE_TOKEN(5, "can't create token", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_IS_NOT_VERIFIED(6, "email is didn't verified", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(7, "phone number existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(8, "role doesn't exist", HttpStatus.BAD_REQUEST),
    BRAND_EXISTED(9, "brand existed", HttpStatus.BAD_REQUEST),
    BRAND_NOT_EXISTED(10, "brand doesn't exist", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(11, "category existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(12, "category doesn't exist", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER(13, "invalid parameter", HttpStatus.BAD_REQUEST),
    INVALID_SORT_PARAMETER(14, "invalid sort column parameter", HttpStatus.BAD_REQUEST),
    SIZE_EXISTED(15, "size existed", HttpStatus.BAD_REQUEST),
    SIZE_NOT_EXISTED(16, "size doesn't exist", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(17, "product existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(18, "product doesn't exist", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_EXISTED(19, "product size existed", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_NOT_EXISTED(20, "product size doesn't exist", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER(21, "missing required parameter", HttpStatus.BAD_REQUEST),
    VOUCHER_EXISTED(22, "voucher existed", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_EXISTED(23, "voucher doesn't exist", HttpStatus.BAD_REQUEST),
    FAVORITE_EXISTED(24, "favorite existed", HttpStatus.BAD_REQUEST),
    FAVORITE_NOT_EXISTED(25, "favorite doesn't exist", HttpStatus.BAD_REQUEST),
    CART_DETAIL_EXISTED(26, "product already in cart", HttpStatus.BAD_REQUEST),
    CART_DETAIL_NOT_EXISTED(27, "cart detail doesn't exist", HttpStatus.BAD_REQUEST),
    CART_NOT_EXISTED(28, "cart doesn't exist", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_EXISTED(29, "payment doesn't exist", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(30, "order doesn't exist", HttpStatus.BAD_REQUEST),
    ORDER_DETAIL_NOT_EXISTED(31, "order detail doesn't exist", HttpStatus.BAD_REQUEST),
    FEEDBACK_NOT_EXISTED(32, "feedback doesn't exist", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
