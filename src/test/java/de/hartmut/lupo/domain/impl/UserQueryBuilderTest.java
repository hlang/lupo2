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

import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.ldap.query.LdapQuery;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * hartmut on 05.02.17.
 */
public class UserQueryBuilderTest {

    @Test
    public void objectClassOnly() throws Exception {
        UserQueryBuilder instance = new UserQueryBuilder("myObjClass");

        LdapQuery query = instance.build();

        assertThat(query.filter().encode(), is("(objectclass=myObjClass)"));
    }

    @Test
    public void withUid() throws Exception {
        UserQueryBuilder instance = new UserQueryBuilder("myObjClass");

        LdapQuery query = instance
                .withUid("myId")
                .build();

        assertThat(query.filter().encode(), is("(&(objectclass=myObjClass)(uid=*myId*))"));
    }

    @Test
    public void allAttributes() throws Exception {
        UserQueryBuilder instance = new UserQueryBuilder("myObjClass");

        LdapQuery query = instance
                .withUid("myId")
                .withFirstname("first")
                .withLastname("last")
                .withEmail("mail")
                .build();

        assertThat(query.filter().encode(), is("(&(objectclass=myObjClass)(|(uid=*myId*)(givenName=*first*)(sn=*last*)(mail=*mail*)))"));
    }
}