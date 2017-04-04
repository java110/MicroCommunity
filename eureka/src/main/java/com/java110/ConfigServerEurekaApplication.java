package com.java110;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
        import org.springframework.context.annotation.DependsOn;
        import org.springframework.context.annotation.Import;
        import org.springframework.context.annotation.PropertySource;

/**
 * Created by wuxw on 2016/12/26.
 */
@SpringBootApplication
@EnableEurekaServer
public class ConfigServerEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerEurekaApplication.class, args);
    }
}

