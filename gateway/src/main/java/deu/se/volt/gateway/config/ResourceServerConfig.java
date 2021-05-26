package deu.se.volt.gateway.config;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;


@Configuration
@EnableResourceServer
@EnableZuulProxy
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override public void configure(HttpSecurity http) throws Exception
    { http.anonymous()
            .disable()
            .authorizeRequests()
            .antMatchers("/product/**")
            .authenticated()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(new OAuth2AccessDeniedHandler()); } }

