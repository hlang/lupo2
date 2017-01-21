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
