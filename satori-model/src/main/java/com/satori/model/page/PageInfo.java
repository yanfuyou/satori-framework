package com.satori.model.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author YanFuYou
 * @date 2024/09/28 16:37
 * @description
 */
public class PageInfo<T> implements IPage<T> {
    @Schema(description = "页码")
    private Long current;

    @Schema(description = "页面数量")
    private Long size;

    @Schema(description = "查询总数")
    private Boolean searchCount;

    @Schema(description = "总数")
    private Long total = 0L;

    @Schema(description = "数据集")
    private List<T> records;

    public PageInfo() {
    }

    public PageInfo(Long current, Long size, boolean searchCount) {
        this.current = current;
        this.size = size;
        this.searchCount = searchCount;
    }

    public PageInfo(PageRequest req) {
        this.current = req.getPageNum();
        this.size = req.getPageSize();
        this.searchCount = req.getSearchCount();
    }

    @Override
    public List<OrderItem> orders() {
        return List.of();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public PageInfo<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public PageInfo<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public PageInfo<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public PageInfo<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @Override
    public boolean searchCount() {
        return this.searchCount;
    }

    public <R> IPage<R> convert(Supplier<List<R>> supplier) {
        return new PageInfo<R>()
                .setTotal(this.getTotal())
                .setSize(this.getSize())
                .setCurrent(this.getCurrent())
                .setRecords(supplier.get());
    }


    @SuppressWarnings("unchecked")
    @Override
    public <R> PageInfo<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return ((PageInfo<R>) this).setRecords(collect);
    }
}
