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

/**
 * hartmut on 05.02.17.
 */
public class LdapConstants {
    public static final String LDAP_OBJECT_CLASS = "objectclass";
    public static final String LDAP_OBJ_CLASS_TOP = "top";
    public static final String LDAP_OBJ_CLASS_INET_ORG_PERSON = "inetOrgPerson";
    public static final String LDAP_OBJ_CLASS_PERSON = "person";

    public static final String LDAP_ATTR_UID = "uid";
    public static final String LDAP_ATTR_CN = "cn";
    public static final String LDAP_ATTR_GIVEN_NAME = "givenName";
    public static final String LDAP_ATTR_SN = "sn";
    public static final String LDAP_ATTR_MAIL = "mail";
    public static final String LDAP_ATTR_USER_PASSWORD = "userPassword";
}
