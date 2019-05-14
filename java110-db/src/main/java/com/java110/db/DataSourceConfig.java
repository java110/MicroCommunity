package com.java110.db;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import io.shardingsphere.core.yaml.sharding.YamlShardingConfiguration;
import io.shardingsphere.core.yaml.sharding.YamlShardingRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * 数据源配置
 */
@Configuration
public class DataSourceConfig {

    @Autowired
    private Filter statFilter;

    private static final String SHARDING_YML_PATH = "dataSource.yml";

    /**
     * 构建dataSource
     * 这里没有使用ShardingDataSourceFactory
     * 因为要为durid数据源配置监听Filter
     *
     * @return 数据源对象
     * @throws SQLException sql异常
     * @throws IOException  IO 异常
     * @since 1.8
     */
    @Bean
    public DataSource dataSource() throws SQLException, IOException {
        YamlShardingConfiguration config = parse();
        YamlShardingRuleConfiguration rule = config.getShardingRule();
        for (String key : config.getDataSources().keySet()) {
            DruidDataSource d = (DruidDataSource) config.getDataSources().get(key);
            d.setProxyFilters(Lists.newArrayList(statFilter));
        }
        return ShardingDataSourceFactory.createDataSource(config.getDataSources(),
                rule.getShardingRuleConfiguration(), config.getConfigMap(), config.getProps());
    }

    /**
     * 解析yml
     *
     * @return yaml 配置文件
     * @throws IOException                  IO 异常
     * @throws FileNotFoundException        文件未发现异常
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private YamlShardingConfiguration parse() throws IOException, FileNotFoundException, UnsupportedEncodingException {
        Resource certResource = new ClassPathResource(SHARDING_YML_PATH);
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(certResource.getInputStream(), "UTF-8")
        ) {
            return new Yaml(new Constructor(YamlShardingConfiguration.class)).loadAs(inputStreamReader, YamlShardingConfiguration.class);
        }
    }
}
