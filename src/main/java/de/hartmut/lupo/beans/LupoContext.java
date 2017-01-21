/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hartmut.lupo.beans;

import de.hartmut.lupo.ldap.LdapService;

/**
 *
 * @author hartmut
 */
public class LupoContext {
    private UserInfoBean userInfoBean;
    private ConfigBean configBean;
    private LdapService ldapService;

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public ConfigBean getConfigBean() {
        return configBean;
    }

    public void setConfigBean(ConfigBean configBean) {
        this.configBean = configBean;
    }

    public LdapService getLdapService() {
        return ldapService;
    }

    public void setLdapService(LdapService ldapService) {
        this.ldapService = ldapService;
    }
}
