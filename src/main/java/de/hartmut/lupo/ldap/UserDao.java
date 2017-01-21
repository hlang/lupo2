/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hartmut.lupo.ldap;

import de.hartmut.lupo.beans.UserInfoBean;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapEntryIdentification;
import org.springframework.ldap.core.LdapEntryIdentificationContextMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import javax.naming.directory.ModificationItem;
import java.util.List;

/**
 *
 * @author hartmut
 */
public class UserDao {
    private static final String OBJECT_CLASS_INET_ORG_PERSON = "inetOrgPerson";

    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public Boolean authenticateByUserId(String base, String uid, String secret) {
        AndFilter filter = buildUidFilter(uid);
        return ldapTemplate.authenticate(base, filter.encode(), secret);
    }

    public String getUserDnByUserId(String base, String uid) {
        String resultDn = null;
        AndFilter filter = buildUidFilter(uid);
        List search = ldapTemplate.search(
                base, "(uid=admin)",
                new LdapEntryIdentificationContextMapper());
        if (!search.isEmpty()) {
            Object objFound = search.get(0);
            if (objFound instanceof LdapEntryIdentification) {
                resultDn = ((LdapEntryIdentification) objFound).getAbsoluteDn().toString();
            }
        }

        return resultDn;
    }

    public UserInfoBean getUserByUserId(String base, String uid) {
        UserInfoBean userInfo = null;
        AndFilter filter = buildUidFilter(uid);
        List search = ldapTemplate.search(
                base, filter.encode(),
                new UserContextMapper());
        if (!search.isEmpty()) {
            Object objFound = search.get(0);
            if (objFound instanceof UserInfoBean) {
                userInfo = (UserInfoBean) objFound;
            }
        }

        return userInfo;
    }
    
    public void modify(String dn, List<ModificationItem> modItems) {
        ldapTemplate.modifyAttributes(dn, modItems.toArray(new ModificationItem[modItems.size()]));
    }

    private AndFilter buildUidFilter(String uid) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(UserAttributeEnum.OBJECTCLASS.toAttrStr(), OBJECT_CLASS_INET_ORG_PERSON));
        filter.and(new EqualsFilter(UserAttributeEnum.UID.toAttrStr(), uid));
        return filter;
    }

    private static class UserContextMapper extends AbstractContextMapper {

        @Override
        protected Object doMapFromContext(DirContextOperations dco) {
            UserInfoBean userInfo = new UserInfoBean();
            userInfo.setDn(dco.getDn().toString());
            userInfo.setUserId(dco.getStringAttribute(UserAttributeEnum.UID.toAttrStr()));
            if (dco.attributeExists(UserAttributeEnum.GN.toAttrStr())) {
                userInfo.setFirstname(dco.getStringAttribute(UserAttributeEnum.GN.toAttrStr()));
            }
            if (dco.attributeExists(UserAttributeEnum.GIVENNAME.toAttrStr())) {
                userInfo.setFirstname(dco.getStringAttribute(UserAttributeEnum.GIVENNAME.toAttrStr()));
            }
            if (dco.attributeExists(UserAttributeEnum.SN.toAttrStr())) {
                userInfo.setLastname(dco.getStringAttribute(UserAttributeEnum.SN.toAttrStr()));
            }
            userInfo.setEmail(dco.getStringAttribute(UserAttributeEnum.EMAIL.toAttrStr()));
            return userInfo;
        }
    }
}
