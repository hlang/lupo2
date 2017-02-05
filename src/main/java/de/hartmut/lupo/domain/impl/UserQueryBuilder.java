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

import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.util.StringUtils;

import static de.hartmut.lupo.ldap.LdapConstants.*;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * hartmut on 05.02.17.
 */
public class UserQueryBuilder {

    private final String objectClass;
    private ContainerCriteria filterCriteria;

    public UserQueryBuilder(String objectClass) {
        this.objectClass = objectClass;
    }

    public UserQueryBuilder withUid(String uid) {
        if (!StringUtils.isEmpty(uid)) {
            filterCriteria = createOrAddCriteria(filterCriteria, LDAP_ATTR_UID, uid);
        }

        return this;
    }

    public UserQueryBuilder withFirstname(String firstname) {
        if (!StringUtils.isEmpty(firstname)) {
            filterCriteria = createOrAddCriteria(filterCriteria, LDAP_ATTR_GIVEN_NAME, firstname);
        }

        return this;
    }

    public UserQueryBuilder withLastname(String lastname) {
        if (!StringUtils.isEmpty(lastname)) {
            filterCriteria = createOrAddCriteria(filterCriteria, LDAP_ATTR_SN, lastname);
        }

        return this;
    }

    public UserQueryBuilder withEmail(String email) {
        if (!StringUtils.isEmpty(email)) {
            filterCriteria = createOrAddCriteria(filterCriteria, LDAP_ATTR_MAIL, email);
        }

        return this;
    }

    public LdapQuery build() {
        ContainerCriteria userCriteria = query()
                .where(LDAP_OBJECT_CLASS).is(objectClass);
        if (filterCriteria != null) {
            userCriteria = userCriteria.and(filterCriteria);
        }
        return userCriteria;
    }

    private ContainerCriteria createOrAddCriteria(ContainerCriteria baseCriteria,
                                                  String attribute, String searchStr) {
        if (baseCriteria == null) {
            return query().where(attribute).whitespaceWildcardsLike(searchStr);
        }

        return baseCriteria.or(attribute).whitespaceWildcardsLike(searchStr);
    }

}
