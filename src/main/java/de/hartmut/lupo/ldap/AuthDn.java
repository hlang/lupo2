/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hartmut.lupo.ldap;

/**
 *
 * @author hartmut
 */
public class AuthDn {

    private final String dn;
    private final String password;

    public AuthDn(String dn, String password) {
        this.dn = dn;
        this.password = password;
    }

    public String getDn() {
        return dn;
    }

    public String getPassword() {
        return password;
    }
}
