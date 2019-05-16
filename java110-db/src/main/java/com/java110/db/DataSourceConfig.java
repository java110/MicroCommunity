package com.java110.db;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlMasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * 数据源配置
 */
@Configuration
public class DataSourceConfig {

    //@Autowired
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
//        YamlShardingConfiguration config = parse();
//        YamlShardingRuleConfiguration rule = config.getShardingRule();
//        for (String key : config.getDataSources().keySet()) {
//            DruidDataSource d = (DruidDataSource) config.getDataSources().get(key);
//            d.setProxyFilters(Lists.newArrayList(statFilter));
//        }
//        return ShardingDataSourceFactory.createDataSource(config.getDataSources(),
//                rule.getShardingRuleConfiguration(), config.getConfigMap(), config.getProps());

        return YamlShardingDataSourceFactory.createDataSource(getYmlFile());
    }

    /**
     * 解析yml
     *
     * @return yaml 配置文件
     * @throws IOException                  IO 异常
     * @throws FileNotFoundException        文件未发现异常
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private File getYmlFile() throws IOException {
        Resource certResource = new ClassPathResource(SHARDING_YML_PATH);
        return certResource.getFile();
    }
}
