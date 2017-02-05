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

package de.hartmut.lupo.domain.impl;

import de.hartmut.lupo.domain.User;
import de.hartmut.lupo.domain.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.query.ConditionCriteria;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.naming.ldap.LdapName;
import java.util.List;

import static de.hartmut.lupo.ldap.LdapConstants.*;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * hartmut on 05.02.17.
 */
@Repository
public class UserRepoImpl implements UserRepo {

    private final LdapTemplate ldapTemplate;

    @Autowired
    public UserRepoImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public List<User> findAll() {
        return ldapTemplate.search(query()
                        .where(LDAP_OBJECT_CLASS).is(LDAP_OBJ_CLASS_PERSON),
                USER_CONTEXT_MAPPER);
    }

    @Override
    public List<User> findByUid(String uid) {
        LdapQuery query = query()
                .where(LDAP_OBJECT_CLASS).is(LDAP_OBJ_CLASS_PERSON)
                .and(LDAP_ATTR_UID).whitespaceWildcardsLike(uid);

        return ldapTemplate.search(query, USER_CONTEXT_MAPPER);
    }

    @Override
    public List<User> findByUidOrFistnameOrLastnameOrEmail(
            String uid, String firstname, String lastname, String email) {
        LdapQuery query = new UserQueryBuilder(LDAP_OBJ_CLASS_PERSON)
                .withUid(uid)
                .withFirstname(firstname)
                .withLastname(lastname)
                .withEmail(email)
                .build();
        return ldapTemplate.search(query, USER_CONTEXT_MAPPER);
    }

    private final static ContextMapper<User> USER_CONTEXT_MAPPER = new AbstractContextMapper<User>() {
        @Override
        public User doMapFromContext(DirContextOperations context) {
            User user = new User();

            LdapName dn = LdapUtils.newLdapName(context.getDn().toString());
            user.setDn(dn.toString());
            user.setFullName(context.getStringAttribute(LDAP_ATTR_CN));
            user.setFirstName(context.getStringAttribute(LDAP_ATTR_GIVEN_NAME));
            user.setLastName(context.getStringAttribute(LDAP_ATTR_SN));
            user.setEmail(context.getStringAttribute(LDAP_ATTR_MAIL));
            user.setUid(context.getStringAttribute(LDAP_ATTR_UID));

            return user;
        }
    };

}
