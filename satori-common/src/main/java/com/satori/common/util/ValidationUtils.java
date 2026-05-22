package com.satori.common.util;

import com.satori.model.code.ReplyCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanfuyou
 * @date 2025/07/30 11:22:27
 * @description 校验工具类
 */
@UtilityClass
public class ValidationUtils {
    private static final ValidatorFactory FACTORY = Validation.byProvider(HibernateValidator.class)
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    /**
     * 校验对象，遇到错误立即抛出异常
     */
    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object, groups);
        if (!violations.isEmpty()) {
            String msg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw ReplyCode.PARAMETER_FAIL.buildEx(msg);
        }
    }

    /**
     * 校验对象，返回所有错误信息
     */
    public static <T> List<String> validateErrInfo(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object, groups);
        return violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());
    }

}
