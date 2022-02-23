package com.java110.db;

import com.java110.config.properties.code.Java110Properties;
import com.java110.core.trace.Java110TraceSqlInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MyBatis基础配置
 *
 * @author liuzh
 * @since 2015-12-19 10:11
 */
@Configuration
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {

    @Autowired
    private Java110Properties java110Properties;

    @Autowired
    DataSource dataSource;
//
//    @Bean(name = "dataSource")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        return druidDataSource;
//    }


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("tk.mybatis.springboot.model");
        //bean.setPlugins(new Interceptor[]{new Java110MybatisInterceptor()});
        bean.setPlugins(new Interceptor[]{new Java110MybatisInterceptor(),new Java110TraceSqlInterceptor()});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // bean.setMapperLocations(resolver.getResources("classpath:mapper/*/*.xml"));
            Resource[] resources = null;
            List<Resource> resourceList = new ArrayList<Resource>();
            for (String path : java110Properties.getMappingPath().split(",")) {
                resources = resolver.getResources(path);
                resourceList.addAll(Arrays.asList(resources));
            }
            bean.setMapperLocations(resourceList.toArray(new Resource[resourceList.size()]));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }


    public Java110Properties getJava110Properties() {
        return java110Properties;
    }

    public void setJava110Properties(Java110Properties java110Properties) {
        this.java110Properties = java110Properties;
    }
}
