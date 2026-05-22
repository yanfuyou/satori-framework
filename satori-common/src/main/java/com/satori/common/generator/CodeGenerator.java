package com.satori.common.generator;

import cn.hutool.core.util.RadixUtil;

/**
 * @author yfy
 */
public class CodeGenerator {

    /**
     * 进制转换后长度为1~2位
     */
    private final String ip4IntEncode = RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_59, IdGenerator.IP_SUM);
    private final IdGenerator idGen = new IdGenerator();

    /**
     * 生成一个code码
     * 长度：除去prefix，8~10长度。
     *
     * @param prefix
     * @return
     */
    public String gen(String prefix) {
        String idStr = idGen.gen();
        // 7~8长度
        String code = RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_59, Long.parseLong(idStr));
        return prefix + ip4IntEncode + code;
    }
}
