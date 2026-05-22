package com.satori.common.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yfy
 */
public class IdGenerator {
    public static final String LOCAL_HOST;

    static {
        try {
            LOCAL_HOST = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String[] IP_ARR = LOCAL_HOST.split("\\.");
    public static final int IP_SUM = Arrays.stream(IP_ARR).mapToInt(Integer::parseInt).sum();
    /**
     * 默认的起始时间（绝对不可修改），为2024-3-1
     */
    public static long DEFAULT_TWEPOCH = 1709222400000L;
    /**
     * 默认回拨时间，2S
     */
    public static long DEFAULT_TIME_OFFSET = 2000L;

    /**
     * 序列掩码，用于限定序列最大值不能超过4095
     */
    private static final long SEQUENCE_MASK = 4095L;
    /**
     * 自增序号，当高频模式下时，同一毫秒内生成N个ID，则这个序号在同一毫秒下，自增以避免ID重复。
     */
    private long sequence = 0L;
    private long lastTimestamp = -1L;


    /**
     * 生成ID
     *
     * @return 返回一串ID，纯数字。
     * 15~16位长度，5年内长度是15
     */
    public synchronized String gen() {
        long l = timeSeq();
        return String.format("%d%04d", l, sequence);
    }

    /**
     * 私有方法，生成一个时间序列，5年内长度是11
     */
    private long timeSeq() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < this.lastTimestamp) {
            if (this.lastTimestamp - timestamp < DEFAULT_TIME_OFFSET) {
                // 容忍指定的回拨，避免NTP校时造成的异常
                timestamp = lastTimestamp;
            } else {
                // 如果服务器时间有问题(时钟后退) 报错。
                throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
            }
        }
        if (this.lastTimestamp == timestamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 同一毫秒的序列数已经达到最大
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒内，序列号置为 1 - 2 随机数
            sequence = ThreadLocalRandom.current().nextLong(1, 3);
        }
        lastTimestamp = timestamp;
        return timestamp - DEFAULT_TWEPOCH;
    }

    /**
     * 生成一个至少15位长度的纯数字ID，具备一定的分布式能力（加入了本机IP计算），能满足大部分场景
     * 5年内长度是15
     */
    public long genId() {

        long l = timeSeq();
        String idStr = String.format("%d%04d", l, (sequence + IP_SUM) & (8192 - 1));
        return Long.parseLong(idStr);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return SystemClock.now();
    }


    public long genId(String prefix) {
        return Long.parseLong(prefix + gen());
    }


}
