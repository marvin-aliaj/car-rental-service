package com.spring.jdbc.car.rental.model.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum BOOKING_STATUS {
    PENDING(1, "Pending"),
    COMPLETED(2, "Completed"),
    CANCELLED(3, "Cancelled"),
    FAILED(4, "Failed");

    private final int code;
    private final String description;

    BOOKING_STATUS(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static BOOKING_STATUS fromCode(int code) {
        for (BOOKING_STATUS type : BOOKING_STATUS.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid USER_TYPE code: " + code);
    }

    public static BOOKING_STATUS fromDescription(String description) {
        for (BOOKING_STATUS type : BOOKING_STATUS.values()) {
            if (Objects.equals(type.getDescription(), description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TRANSACTION_STATUS code: " + description);
    }

    @Override
    public String toString() {
        return code + " - " + description;
    }
}
