package com.satori.common.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author yanfuyou
 * @date 2025/08/26 17:42:27
 * @description 金额工具类
 */
@UtilityClass
public class AmountUtil {
    /**
     * 元转分（默认向下取整）
     *
     * @param yuan 金额（元）
     * @return 分（long）
     */
    public static long yuanToFen(BigDecimal yuan) {
        if (yuan == null) {
            return 0L;
        }
        return yuan.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.DOWN)
                .longValue();
    }

    /**
     * 元转分（指定舍入模式）
     *
     * @param yuan         金额（元）
     * @param roundingMode 舍入模式
     * @return 分（long）
     */
    public static long yuanToFen(BigDecimal yuan, RoundingMode roundingMode) {
        if (yuan == null) {
            return 0L;
        }
        return yuan.multiply(BigDecimal.valueOf(100))
                .setScale(0, roundingMode)
                .longValue();
    }

    /**
     * 分转元（保留2位小数，默认四舍五入）
     *
     * @param fen 金额（分）
     * @return 元（BigDecimal）
     */
    public static BigDecimal fenToYuan(long fen) {
        return BigDecimal.valueOf(fen)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * 分转元（指定精度和舍入模式）
     *
     * @param fen          金额（分）
     * @param scale        小数位数
     * @param roundingMode 舍入模式
     * @return 元（BigDecimal）
     */
    public static BigDecimal fenToYuan(long fen, int scale, RoundingMode roundingMode) {
        return BigDecimal.valueOf(fen)
                .divide(BigDecimal.valueOf(100), scale, roundingMode);
    }

    /**
     * 金额相加（单位：分）
     *
     * @param fen1 金额1（分）
     * @param fen2 金额2（分）
     * @return 结果（分）
     */
    public static long addFen(long fen1, long fen2) {
        return fen1 + fen2;
    }

    /**
     * 金额相减（单位：分）
     *
     * @param fen1 金额1（分）
     * @param fen2 金额2（分）
     * @return 结果（分）
     */
    public static long subtractFen(long fen1, long fen2) {
        return fen1 - fen2;
    }

    /**
     * 金额乘法（单位：分，返回分）
     *
     * @param fen          金额（分）
     * @param multiplicand 乘数
     * @return 结果（分，long）
     */
    public static long multiplyFen(long fen, BigDecimal multiplicand) {
        BigDecimal result = BigDecimal.valueOf(fen)
                .multiply(multiplicand);
        return result.setScale(0, RoundingMode.DOWN).longValue();
    }

    /**
     * 金额除法（单位：分，返回分）
     *
     * @param fen     金额（分）
     * @param divisor 除数
     * @return 结果（分，long）
     */
    public static long divideFen(long fen, BigDecimal divisor) {
        BigDecimal result = BigDecimal.valueOf(fen)
                .divide(divisor, 0, RoundingMode.DOWN);
        return result.longValue();
    }
}
