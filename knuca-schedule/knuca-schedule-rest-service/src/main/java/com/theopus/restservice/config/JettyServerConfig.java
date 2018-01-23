package com.theopus.restservice.config;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyServerConfig {

    private Integer port;
    private Integer maxThreads;
    private Integer minThreads;
    private Integer idleTimeout;

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactoryFactory() {
        final JettyEmbeddedServletContainerFactory factory =  new JettyEmbeddedServletContainerFactory(port);
        factory.addServerCustomizers((JettyServerCustomizer) server -> {
            final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
            threadPool.setMaxThreads(maxThreads);
            threadPool.setMinThreads(minThreads);
            threadPool.setIdleTimeout(idleTimeout);
        });
        return factory;
    }

    @Value("${server.port}")
    public void setPort(Integer port) {
        this.port = port;
    }

    @Value("${server.jetty.threadPool.maxThreads}")
    public void setMaxThreads(Integer maxThreads) {
        this.maxThreads = maxThreads;
    }

    @Value("${server.jetty.threadPool.minThreads}")
    public void setMinThreads(Integer minThreads) {
        this.minThreads = minThreads;
    }

    @Value("${server.jetty.threadPool.idleTimeout}")
    public void setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
    }
}
