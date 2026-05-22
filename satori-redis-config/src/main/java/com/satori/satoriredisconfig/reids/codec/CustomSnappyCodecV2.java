package com.satori.satoriredisconfig.reids.codec;

import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.codec.SnappyCodecV2;

/**
 * @author YanFuYou
 * @date 2024/02/24 下午 11:19
 */
public class CustomSnappyCodecV2 extends SnappyCodecV2 {
    @Override
    public Decoder<Object> getMapKeyDecoder() {
        return super.getMapKeyDecoder();
    }

    @Override
    public Encoder getMapKeyEncoder() {
        return super.getMapKeyEncoder();
    }
}
