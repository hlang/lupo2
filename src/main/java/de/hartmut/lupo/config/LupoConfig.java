/*
 * Copyright 2017 Hartmut Lang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
