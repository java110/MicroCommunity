package com.java110.db;

import com.java110.utils.util.StringUtil;
import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;

/**
 * 数据源配置
 */
@Configuration
public class DataSourceConfig {

    //@Autowired
    private Filter statFilter;

    @Autowired
    private Environment env;
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
        String path = SHARDING_YML_PATH;

        String[] actives = env.getActiveProfiles();
        if (actives != null && actives.length > 0 && !"dev".equals(actives[0])) {
            path = "dataSource-" + actives[0] + ".yml";
        }

        String configString = new String(getYmlFile(path), "UTF-8");
        configString = configString.replaceAll("\\$\\{mysqlpwd\\}", env.getProperty("mysqlpwd"));

        String mysqlPort = StringUtil.isEmpty(env.getProperty("mysqlport")) ? "3306" : env.getProperty("mysqlport");
        configString = configString.replaceAll("\\$\\{mysqlport\\}", mysqlPort);

        String dbttname = StringUtil.isEmpty(env.getProperty("dbttname")) ? "TT" : env.getProperty("dbttname");
        String dbttuser = StringUtil.isEmpty(env.getProperty("dbttuser")) ? "TT" : env.getProperty("dbttuser");
        String dbhcname = StringUtil.isEmpty(env.getProperty("dbhcname")) ? "hc_community" : env.getProperty("dbhcname");
        String dbhcuser = StringUtil.isEmpty(env.getProperty("dbhcuser")) ? "hc_community" : env.getProperty("dbhcuser");

        configString = configString.replaceAll("\\$\\{dbttname\\}", dbttname)
                .replaceAll("\\$\\{dbttuser\\}", dbttuser)
                .replaceAll("\\$\\{dbhcname\\}", dbhcname)
                .replaceAll("\\$\\{dbhcuser\\}", dbhcuser);

        return YamlShardingDataSourceFactory.createDataSource(configString.getBytes("UTF-8"));
    }

    /**
     * 解析yml
     *
     * @return yaml 配置文件
     * @throws IOException                  IO 异常
     * @throws FileNotFoundException        文件未发现异常
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private byte[] getYmlFile(String path) throws IOException {
        Reader reader = null;
        InputStream inputStream = null;
        ByteArrayOutputStream swapStream = null;
        try {
            Resource resource = new ClassPathResource(path);

            inputStream = resource.getInputStream();
            swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in2b = swapStream.toByteArray();
            return in2b;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (swapStream != null) {
                swapStream.close();
            }
        }
    }
}
