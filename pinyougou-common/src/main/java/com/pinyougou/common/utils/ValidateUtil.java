package com.pinyougou.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidateUtil {
    public static String parseError(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst().orElse("");
    }
}
