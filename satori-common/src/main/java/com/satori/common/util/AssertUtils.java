package com.satori.common.util;

import com.satori.model.code.ICodeService;
import com.satori.model.exception.ServiceException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * @author YanFuYou
 * @date 2024/09/25 21:39
 * @description
 */

@UtilityClass
public class AssertUtils {

    public static void isTrue(boolean expression, ICodeService<Object> codeService) {
        if (!expression) {
            throw codeService.buildEx();
        }
    }

    public static void isTrue(boolean expression, Supplier<ServiceException> exceptionSupplier) {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }
}
