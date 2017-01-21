/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hartmut.lupo.beans;



/**
 *
 * @author hartmut
 */
public class UserInfoBean {

    static public String AdminUserId = "admin";
    private String dn;
    private String userId;
    private String password;
    private String firstname;
    private String lastname;
    private String email;

    public boolean isAdmin() {
        return userId.equals(AdminUserId);
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
