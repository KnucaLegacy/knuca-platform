package com.theopus.restservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class ConfigurableEnvironmentProcessor implements BeanFactoryPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableEnvironmentProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, String> env = System.getenv();
        Properties properties = new Properties();

        String sqlUrl = env.get("DATABASE_URL");
        String databaseName = env.get("DATABASE_NAME");
        if (!Objects.isNull(sqlUrl) && !Objects.isNull(databaseName)) {
            String url = "jdbc:mysql://" + sqlUrl + "/" + databaseName + "?useUnicode=true&characterEncoding=utf8";
            properties.put("spring.datasource.url", url);
            LOG.info("Has been set mysql-url {} from environment variable DATABASE_URL", sqlUrl);
        }

        if (!Objects.isNull(sqlUrl) && !Objects.isNull(databaseName)) {
            properties.put("spring.datasource.url", "jdbc:mysql://" + sqlUrl + "?useUnicode=true&characterEncoding=utf8");
            LOG.info("Has been set mysql-url {} with database {} from environment variable DATABASE_NAME", sqlUrl, databaseName);
        }

        String userName = env.get("DATABASE_USER");
        if (!Objects.isNull(userName)) {
            properties.put("spring.datasource.username", userName);
            LOG.info("Has been set db user {} from environment variable USER", userName);
        }

        String password = env.get("DATABASE_PASSWORD");
        if (!Objects.isNull(password)) {
            properties.put("spring.datasource.password", password);
            LOG.info("Has been set db password {} from environment variable DATABASE_PASSWORD", password);
        }

        ConfigurableEnvironment environment = beanFactory.getBean(ConfigurableEnvironment.class);
        environment.getPropertySources().addLast(new PropertiesPropertySource("EnvProps", properties));
        environment.getPropertySources().addFirst(new PropertiesPropertySource("EnvProps", properties));
    }
}
