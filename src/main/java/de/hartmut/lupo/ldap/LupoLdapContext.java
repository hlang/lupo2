/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hartmut.lupo.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Component;

import javax.naming.directory.DirContext;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 *
 * @author hartmut
 */
@Component
public class LupoLdapContext {

    private static final Logger logger = LoggerFactory.getLogger(LupoLdapContext.class);
    private static final String UidSearchFilter = "(&(objectClass=inetOrgPerson)(uid={0}))";


    private final LdapContextSource ldapContextSource;
    private final LdapTemplate ldapTemplate;

    @Autowired
    public LupoLdapContext(LdapContextSource ldapContextSource, LdapTemplate ldapTemplate) {
        this.ldapContextSource = ldapContextSource;
        this.ldapTemplate = ldapTemplate;
    }

    public LdapTemplate getLdapTemplate() {
        return new LdapTemplate(ldapContextSource);
    }

    public LdapContextSource getLdapContextSource() {
        return ldapContextSource;
    }

    public boolean authenticate(String userDn, String credentials) {
        DirContext ctx = null;
        try {
            ctx = ldapContextSource.getContext(userDn, credentials);
            return true;
        } catch (Exception e) {
            // Context creation failed - authentication did not succeed
            logger.info("Login failed. DN: {}", userDn);
            return false;
        } finally {
            // It is imperative that the created DirContext instance is always closed
            LdapUtils.closeContext(ctx);
        }
    }


    public boolean authenticateUid(String uid, String password) {
        try {
            ldapTemplate.authenticate(query().where("uid").is(uid), password);
            return true;
        } catch(AuthenticationException ex) {
            logger.info("Login failed. uid: {}", uid);
        }
        return false;
    }
}
