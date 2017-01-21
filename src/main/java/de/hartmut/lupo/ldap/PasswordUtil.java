/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hartmut.lupo.ldap;

import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author hartmut
 */
public class PasswordUtil {

    public static String ldapEncryptSHA(String pwd) throws NoSuchAlgorithmException {
        return new LdapShaPasswordEncoder().encodePassword(pwd, null);
    }
}
