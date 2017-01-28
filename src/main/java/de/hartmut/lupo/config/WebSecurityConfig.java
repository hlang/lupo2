package de.hartmut.lupo.config;

import de.hartmut.lupo.config.LupoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * hartmut on 21.01.17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LupoConfig lupoConfig;

    @Autowired
    public WebSecurityConfig(LupoConfig lupoConfig) {
        this.lupoConfig = lupoConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.ldapAuthentication()
                .userSearchFilter("(uid={0})")
                .userSearchBase(lupoConfig.getUserSearchBase())
                .groupSearchBase(lupoConfig.getGroupSearchBase())
                .contextSource().url(lupoConfig.getLdapUrl());
        auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=system")
                .contextSource().url(lupoConfig.getLdapUrl());
    }
}
