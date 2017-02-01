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
