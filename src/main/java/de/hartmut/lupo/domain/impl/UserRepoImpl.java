/*
 * Copyright 2017-2018 Hartmut Lang
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static de.hartmut.lupo.ldap.LdapConstants.*;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * hartmut on 05.02.17.
 */
@Repository
public class UserRepoImpl implements UserRepo {
    private final Logger LOGGER = LoggerFactory.getLogger(UserRepoImpl.class);

    private final LdapTemplate ldapTemplate;
    private final BytesKeyGenerator bytesKeyGenerator;

    @Autowired
    public UserRepoImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
        this.bytesKeyGenerator = KeyGenerators.secureRandom();
    }

    @Override
    public User findByDn(String dn) {
        return ldapTemplate.lookup(dn, USER_CONTEXT_MAPPER);
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

    @Override
    public void create(User user) {
        user.sanitize();
        LOGGER.debug("create(): user: {}", user);
        DirContextAdapter context = new DirContextAdapter(buildDn(user));
        mapToContext(user, context);
        ldapTemplate.bind(context);
    }

    @Override
    public void update(User user) {
        Name dn = buildDn(user);
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        if (context.attributeExists(LDAP_ATTR_GN)) {
            Attribute attr = new BasicAttribute(LDAP_ATTR_GN);
            ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);
            ldapTemplate.modifyAttributes(dn, new ModificationItem[]{item});
        }
        context.setAttributeValue(LDAP_ATTR_GIVEN_NAME, user.getFirstName());
        context.setAttributeValue(LDAP_ATTR_SN, user.getLastName());
        context.setAttributeValue(LDAP_ATTR_MAIL, user.getEmail());
        ldapTemplate.modifyAttributes(context);
    }

    @Override
    public void delete(String dn) {
        ldapTemplate.unbind(dn);
    }

    @Override
    public void setPassword(User user) {
        DirContextOperations context = ldapTemplate.lookupContext(buildDn(user));
        context.setAttributeValue(LDAP_ATTR_USER_PASSWORD, encodePassword(user.getPassword()));
        ldapTemplate.modifyAttributes(context);
    }

    private Name buildDn(User user) {
        return LdapNameBuilder.newInstance()
            .add(LDAP_ATTR_CN, user.getFullName())
            .build();
    }

    private void mapToContext(User user, DirContextAdapter context) {
        context.setAttributeValues(LDAP_OBJECT_CLASS, new String[]{
            LDAP_OBJ_CLASS_TOP, LDAP_OBJ_CLASS_PERSON, LDAP_OBJ_CLASS_INET_ORG_PERSON});
        context.setAttributeValue(LDAP_ATTR_CN, user.getFullName());
        context.setAttributeValue(LDAP_ATTR_GIVEN_NAME, user.getFirstName());
        context.setAttributeValue(LDAP_ATTR_SN, user.getLastName());
        context.setAttributeValue(LDAP_ATTR_MAIL, user.getEmail());
        context.setAttributeValue(LDAP_ATTR_UID, user.getUid());
        if (user.getPassword() != null) {
            context.setAttributeValue(LDAP_ATTR_USER_PASSWORD, encodePassword(user.getPassword()));
        }
    }

    private byte[] encodePassword(String password) {
        return new LdapShaPasswordEncoder(bytesKeyGenerator)
            .encode(password)
            .getBytes(StandardCharsets.UTF_8);
    }

    private final static ContextMapper<User> USER_CONTEXT_MAPPER = new AbstractContextMapper<User>() {
        @Override
        public User doMapFromContext(DirContextOperations context) {
            User user = new User();

            user.setDn(context.getDn().toString());
            user.setFullName(context.getStringAttribute(LDAP_ATTR_CN));
            user.setFirstName(context.getStringAttribute(LDAP_ATTR_GIVEN_NAME));
            if (user.getFirstName() == null) {
                user.setFirstName(context.getStringAttribute(LDAP_ATTR_GN));
            }
            user.setLastName(context.getStringAttribute(LDAP_ATTR_SN));
            user.setEmail(context.getStringAttribute(LDAP_ATTR_MAIL));
            user.setUid(context.getStringAttribute(LDAP_ATTR_UID));

            return user;
        }
    };

}
