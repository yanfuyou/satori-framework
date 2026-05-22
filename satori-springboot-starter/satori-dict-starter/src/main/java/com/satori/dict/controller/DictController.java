package com.satori.dict.controller;


import com.satori.dict.service.DictService;
import com.satori.model.result.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yanfuyou
 * @date 2025/07/30 14:38:21
 * @description
 */
@Tag(name = "字典查询API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict")
@ConditionalOnProperty(prefix = "satori.dict", name = "enable-endpoint", havingValue = "true")
public class DictController {
    private final DictService dictService;

    @Operation(summary = "获取字典")
    @GetMapping("/get/{code}")
    public ResultVO<Map<Object, Object>> get(@PathVariable("code") @Parameter(description = "字典名称") String code) {
        Map<Object, Object> dict = dictService.getDict(code);
        return ResultVO.success(dict);
    }
}
