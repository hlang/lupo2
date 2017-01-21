package de.hartmut.lupo.ldap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * hartmut on 21.01.17.
 */
@Configuration
public class LdapConfig {

    @Bean
    public LdapContextSource getContextSource() throws Exception{
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://localhost:10389");
        ldapContextSource.setBase("ou=people,o=sevenSeas");
        ldapContextSource.setAnonymousReadOnly(true);
        return ldapContextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() throws Exception{
        LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
        ldapTemplate.setIgnorePartialResultException(true);
        return ldapTemplate;
    }
}
