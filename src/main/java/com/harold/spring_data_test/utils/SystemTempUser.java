package com.harold.spring_data_test.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SystemTempUser {
    @NotNull(message = "is required")
    @Size(min = 8, message = "is required")
    private String phone;
}
