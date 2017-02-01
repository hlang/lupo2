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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hartmut
 */
public class LdapUtil {

    private static final Logger logger = LoggerFactory.getLogger(LdapUtil.class);
    private static final String UidSearchFilter = "(&(objectClass=inetOrgPerson)(uid={0}))";
    private static final String UidStartSearchFilter = "(&(objectClass=inetOrgPerson)(uid={0}*))";
    private static final String CnSearchFilter = "(&(objectClass=inetOrgPerson)(cn={0}*))";
    private String ldapServerUrl;
    private String searchBase;

    public LdapUtil(String ldapServerUrl, String searchBase) {
        this.ldapServerUrl = ldapServerUrl;
        this.searchBase = searchBase;
    }

    /**
     * Returns true if the specified password can be authenticated against
     * Ldap-Server
     *
     * @param loginName the loginName of the user
     * @param ppassword the password to check for
     * @return true if the password is o.k., false if not
     */
    public boolean authenticate(String loginName, String ppassword) {
        boolean userIsOK = false;


        // get the CN
        String keyDn = getDn(loginName);

        if (keyDn != null) {
            keyDn += searchBase;
            // Set up the environment for creating the initial context
            Hashtable env = new Hashtable(11);
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapServerUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, keyDn);
            env.put(Context.SECURITY_CREDENTIALS, ppassword);

            try {
                // Create inital context
                new InitialDirContext(env);

                // user was validated
                userIsOK = true;
            } catch (NamingException e) {
            }
        }

        return userIsOK;
    }

    /**
     * Returns the CN (common name) for a given login name
     *
     * @param uid the loginName of the user
     * @return CN as a String(if found), or null (else)
     */
    public String getDn(String uid) {
        String keyDn = null;

        DirContext ctx = null;
        try {
            // Step 1: Bind anonymously
            ctx = getAnonContext();

            // Step 2: Search the directory
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[0]);
            ctls.setReturningObjFlag(true);
            NamingEnumeration enm = ctx.search(searchBase, UidSearchFilter, new String[]{uid}, ctls);

            if (enm.hasMore()) {
                SearchResult result = (SearchResult) enm.next();
                keyDn = result.getNameInNamespace();

                logger.info("getDn(): dn=" + keyDn);
            }
        } catch (NamingException e) {
            logger.info(e.getMessage());
        } finally {
            try {
                ctx.close();
            } catch (NamingException ex) {
                logger.error("Closing failed with " + ex, ex);
            }
        }

        return keyDn;
    }

    /**
     * Returns a list of DNs that match with their CN (common name) the
     * searchStr but not more than limit
     *
     * @param loginName the loginName of the user
     * @return CN as a String(if found), or null (else)
     */
    public Set<String> searchMatchingCn(String searchStr, int limit) {
        Set<String> dnList = new HashSet<String>();

        DirContext ctx = null;
        try {
            // Step 1: Bind anonymously
            ctx = getAnonContext();

            // Step 2: Search the directory
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[0]);
            ctls.setReturningObjFlag(true);
            NamingEnumeration enm = ctx.search(searchBase, CnSearchFilter, new String[]{searchStr}, ctls);

            while (enm.hasMore()) {
                SearchResult result = (SearchResult) enm.next();
                String keyDn = result.getNameInNamespace();
                dnList.add(keyDn);

                logger.info("getMatchingCn(): found dn={}", keyDn);
            }
        } catch (NamingException e) {
            logger.info(e.getMessage());
        } finally {
            try {
                ctx.close();
            } catch (NamingException ex) {
                logger.error("Closing failed with " + ex, ex);
            }
        }

        return dnList;
    }

    /**
     * Returns a list of DNs that match with their uid
     *
     * @param loginName the loginName of the user
     * @return CN as a String(if found), or null (else)
     */
    public Set<String> searchMatchingUid(String searchStr, int limit, AuthDn authDn) {
        Set<String> dnList = new HashSet<String>();

        DirContext ctx = null;
        try {
            // Step 1: Bind
            ctx = getSimpleAuthContext(authDn);

            // Step 2: Search the directory
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[0]);
            ctls.setReturningObjFlag(true);
            NamingEnumeration enm = ctx.search(searchBase, UidStartSearchFilter, new String[]{searchStr}, ctls);

            while (enm.hasMore()) {
                SearchResult result = (SearchResult) enm.next();
                String keyDn = result.getNameInNamespace();
                dnList.add(keyDn);

                logger.info("searchMatchingUid(): found dn=" + keyDn);
            }
        } catch (NamingException e) {
            logger.info(e.getMessage());
        } finally {
            try {
                ctx.close();
            } catch (NamingException ex) {
                logger.error("Closing failed with " + ex, ex);
            }
        }

        return dnList;
    }

    public boolean authenticateLdap(String userDn, String credentials) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapServerUrl);
        contextSource.setBase(searchBase);

        DirContext ctx = null;
        try {
            ctx = contextSource.getContext(userDn, credentials);
            return true;
        } catch (Exception e) {
            // Context creation failed - authentication did not succeed
            logger.error("Login failed", e);
            return false;
        } finally {
            // It is imperative that the created DirContext instance is always closed
            LdapUtils.closeContext(ctx);
        }
    }

    public boolean authenticate2(String uid, String password) {
        boolean userIsOK = false;

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapServerUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        DirContext ctx = null;
        try {
            // Step 1: Bind anonymously
            ctx = new InitialDirContext(env);

            // Step 2: Search the directory
            String filter = "(&(objectClass=inetOrgPerson)(uid={0}))";
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[0]);
            ctls.setReturningObjFlag(true);
            NamingEnumeration enm = ctx.search(searchBase, filter, new String[]{uid}, ctls);

            String dn = null;
            if (enm.hasMore()) {
                SearchResult result = (SearchResult) enm.next();
                dn = result.getNameInNamespace();

                logger.info("dn: {}", dn);
            }

            if (dn == null || enm.hasMore()) {
                // uid not found or not unique
                logger.info("Authentication failed");
                return false;
            }

            // Step 3: Bind with found DN and given password
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            // Perform a lookup in order to force a bind operation with JNDI
            ctx.lookup(dn);
            userIsOK = true;
            logger.info("Authentication successful");

        } catch (NamingException e) {
            logger.info(e.getMessage());
        } finally {
            try {
                ctx.close();
            } catch (NamingException ex) {
                logger.error("Closing failed with " + ex, ex);
            }
        }

        return userIsOK;
    }

    /**
     * authenticate a DN
     *
     * @param dn
     * @param pass
     * @return
     */
    public boolean authenticateDn(final String dn, final String pass) {
        boolean userIsOK = false;

        DirContext ctx = null;
        try {
            // get the simple auth-context
            ctx = getSimpleAuthContext(new AuthDn(dn, pass));

            // Perform a lookup in order to force a bind operation with JNDI
            ctx.lookup(dn);
            userIsOK = true;
            logger.info("Authentication successful");

        } catch (NamingException e) {
            logger.info(e.getMessage());
        } finally {
            try {
                if (ctx != null) {
                    ctx.close();
                }
            } catch (NamingException ex) {
                logger.error("Closing failed with " + ex, ex);
            }
        }

        return userIsOK;
    }

    /**
     * add a new InetOrg entry
     *
     * @param cn
     * @param userAttrs
     * @param authDn the authentication to use
     * @return
     */
    public boolean addInetOrgEntry(final String cn, Attributes userAttrs, final AuthDn authDn) {

        boolean addIsOk = false;

        DirContext ctx = null;
        try {
            // Step 1: Bind with simple-authentication
            ctx = getSimpleAuthContext(authDn);

            // create the attributes
            Attributes attributes = new BasicAttributes();
            // add the objectClasse
            Attribute objectClassAttr = new BasicAttribute("objectClass");
            objectClassAttr.add("top");
            objectClassAttr.add("person");
            objectClassAttr.add("organizationalPerson");
            objectClassAttr.add("inetOrgPerson");
            attributes.put(objectClassAttr);
            // add CN and other
            attributes.put(UserAttributeEnum.CN.toAttrStr(), cn);
            NamingEnumeration<String> ids = userAttrs.getIDs();
            while (ids.hasMore()) {
                String nextId = ids.next();
                attributes.put(userAttrs.get(nextId));
            }

            // create the sub-context
            String dn = UserAttributeEnum.CN.toAttrStr() + "=" + cn + "," + searchBase;
            logger.info("addInetOrgEntry(): add DN " + dn);
            ctx.createSubcontext(dn, attributes);

            // Perform a lookup in order to force a bind operation with JNDI
            //ctx.lookup(dn);
            addIsOk = true;
            logger.info("addInetOrgEntry(): add successful");

        } catch (NamingException e) {
            logger.info(e.getMessage());
        } finally {
            try {
                ctx.close();
            } catch (NamingException ex) {
                logger.error("Closing failed with " + ex, ex);
            }
        }

        return addIsOk;
    }

    /**
     * read the attributes for a given DN
     *
     * @param dn
     * @param authDn the authentication to use
     * @return
     */
    public Attributes readAttributes(String dn, AuthDn authDn) {
        Attributes answer;

        DirContext ctx = null;
        try {
            // get simple-auth context
            ctx = getSimpleAuthContext(authDn);

            // read the attributes
            answer = ctx.getAttributes(dn);
        } catch (NamingException e) {
            logger.info("get attributes for >" + dn + "< failed with " + e);
            answer = null;
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                    logger.error("readAttributes(): close failed with " + ex, ex);
                }
            }
        }
        return answer;
    }

    /**
     * update attributes for a given dn
     *
     * @param dn
     * @param modItems
     * @param authDn the authentication to use for the update
     * @return
     */
    public boolean updateAttributes(String dn, List<ModificationItem> modItems, final AuthDn authDn) {
        boolean result = false;

        DirContext ctx = null;
        try {
            // get simple-auth context
            ctx = getSimpleAuthContext(authDn);

            // create the arrays
            ModificationItem[] modArray = modItems.toArray(new ModificationItem[modItems.size()]);
            // call the modification
            ctx.modifyAttributes(dn, modArray);
            result = true; // success
        } catch (NamingException e) {
            logger.info("modifing attributes for >" + dn + "< failed with " + e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                    logger.error("updateAttributes(): close failed with " + ex, ex);
                }
            }
        }

        return result;
    }

    private DirContext getAnonContext() throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapServerUrl);

        DirContext ctx = new InitialDirContext(env);

        return ctx;
    }

    private DirContext getSimpleAuthContext(final AuthDn authDn) throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapServerUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, authDn.getDn());
        env.put(Context.SECURITY_CREDENTIALS, authDn.getPassword());

        DirContext ctx = new InitialDirContext(env);

        return ctx;
    }
}
