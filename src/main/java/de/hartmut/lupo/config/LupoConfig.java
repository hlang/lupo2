package de.hartmut.lupo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * hartmut on 28.01.17.
 */
@Component
@ConfigurationProperties(prefix = "lupo")
public class LupoConfig {
    private String ldapServerHost;
    private String ldapServerPort;
    private String userSearchBase;
    private String groupSearchBase;

    public String getLdapUrl() {
        return "ldap://"+ getLdapServerHost() + ":" + getLdapServerPort();
    }

    public String getLdapServerHost() {
        return ldapServerHost;
    }

    public void setLdapServerHost(String ldapServerHost) {
        this.ldapServerHost = ldapServerHost;
    }

    public String getLdapServerPort() {
        return ldapServerPort;
    }

    public void setLdapServerPort(String ldapServerPort) {
        this.ldapServerPort = ldapServerPort;
    }

    public String getUserSearchBase() {
        return userSearchBase;
    }

    public void setUserSearchBase(String userSearchBase) {
        this.userSearchBase = userSearchBase;
    }

    public String getGroupSearchBase() {
        return groupSearchBase;
    }

    public void setGroupSearchBase(String groupSearchBase) {
        this.groupSearchBase = groupSearchBase;
    }
}
