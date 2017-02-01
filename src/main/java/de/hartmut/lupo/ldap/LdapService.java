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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hartmut.lupo.ldap;

import de.hartmut.lupo.beans.UserInfoBean;

/**
 *
 * @author hartmut
 */
public interface LdapService {
    public void setAuthorized(UserInfoBean user);

    public void setAnonymous();

    public boolean isValid(UserInfoBean user);

    public UserInfoBean readUser(String userId, UserInfoBean bindUser);

    public boolean updateUser(UserInfoBean oldUser, UserInfoBean newUser, UserInfoBean bindUser);

    /**
     * update a accout password
     * @param oldUser the current password, required to bind to LDAP
     * @param newUser contains the new password
     * @return ture if password update was successful
     */
    public boolean updatePassword(UserInfoBean oldUser, UserInfoBean newUser, UserInfoBean bindUser);

    /**
     *
     * @param newUser
     * @param bindUser
     * @return
     */
    public boolean createUserAccount(UserInfoBean newUser, UserInfoBean bindUser);

    /**
     * search an account by the userId
     * @param userId
     * @return true, if account was found
     */
    public boolean searchByUserId(String userId);

    /**
     * serach for a list of users matching the oracle.request
     * @param request
     * @param bindUser the admin-user required to bind to LDAP
     * @return
     */
//    public List<LdapUser> searchByRequestString(Request request, LdapUser bindUser);
}
