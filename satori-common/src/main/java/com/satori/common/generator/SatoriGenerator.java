package com.satori.common.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.satori.model.po.BasicPO;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * @author YanFuYou
 * @date 2024-09-22
 * @description 代码生成
 */
@AllArgsConstructor
public class SatoriGenerator {

    private final FastAutoGenerator fastAutoGenerator;

    private Builder.GlobalConfig globalConfig;

    private Builder.PackageConfig packageConfig;

    private Builder.StrategyConfig strategyConfig;


    @NoArgsConstructor
    @Data
    @Accessors(fluent = true, chain = true)
    public static class Builder {
        private String url;

        private String name;

        private String pwd;

        private GlobalConfig globalConfig;

        private PackageConfig packageConfig;

        private StrategyConfig strategyConfig;

        public SatoriGenerator build() {
            Objects.requireNonNull(this.url, "datasource url can not be null!");
            Objects.requireNonNull(this.name, "db user url can not be null!");
            Objects.requireNonNull(this.pwd, "db password url can not be null!");
            FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(this.url, this.name, this.pwd);
            return new SatoriGenerator(fastAutoGenerator, this.globalConfig, this.packageConfig, this.strategyConfig);
        }


        @AllArgsConstructor
        @Getter
        @Setter
        public static class GlobalConfig {
            private String author;

            private boolean enableSwagger = false;

            private String outPutDir;
        }

        @AllArgsConstructor
        @Getter
        @Setter
        public static class PackageConfig {
            private String parent;

            private String moduleName;

            private String xmlPath;

        }

        @AllArgsConstructor
        @Getter
        @Setter
        public static class StrategyConfig {
            private String include;

            private boolean fileOverride = true;

            private String[] prefix;

            Class<? extends BasicPO> superPO;
        }
    }

    public void execute() {
        fastAutoGenerator.globalConfig(builder -> {
            builder.author(this.globalConfig.author);
            if (this.globalConfig.enableSwagger) {
                builder.enableSwagger();
            }
            builder.outputDir(this.globalConfig.outPutDir);
            builder.disableOpenDir();
        });

        fastAutoGenerator.dataSourceConfig(builder -> {
            builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                if (typeCode == Types.SMALLINT) {
                    return DbColumnType.INTEGER;
                }
                return typeRegistry.getColumnType(metaInfo);
            });
        });

        fastAutoGenerator.packageConfig(builder -> {
            builder.parent(this.packageConfig.parent)
                    .moduleName(this.packageConfig.moduleName)
                    .entity("dal.po")
                    .mapper("dal.mapper")
                    .service("service.basic")
                    .serviceImpl("service.basic.impl")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, this.packageConfig.xmlPath));
        });

        fastAutoGenerator.strategyConfig(builder -> {
                    String[] split = this.strategyConfig.include.split(",");
                    builder.addInclude(Arrays.asList(split))
                            .addTablePrefix(this.strategyConfig.prefix);
                    builder.entityBuilder()
                            .enableLombok()
                            .enableChainModel()
                            .formatFileName("%sPO")
                            .addIgnoreColumns("id", "is_deleted", "create_time", "update_time", "create_at", "update_at", "creator", "creator_id", "modifier", "modifier_id");
                    if (this.strategyConfig.fileOverride) {
                        builder.entityBuilder().enableFileOverride();
                    }
                    if (this.strategyConfig.superPO != null) {
                        builder.entityBuilder()
                                .superClass(this.strategyConfig.superPO);
                    }
                    builder.controllerBuilder()
                            .disable();
                })
                .templateEngine(new FreemarkerTemplateEngine());
        fastAutoGenerator.execute();
    }
}
