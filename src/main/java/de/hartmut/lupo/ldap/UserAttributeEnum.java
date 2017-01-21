/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hartmut.lupo.ldap;

import java.util.HashMap;

/**
 *
 * @author hartmut
 */
public enum UserAttributeEnum {
    UNKNOWN("n/a"),
    OBJECTCLASS("objectclass"),
    CN("cn"),
    SN("sn"),
    GN("gn"),
    UID("uid"),
    GIVENNAME("givenname"),
    PASSWORD("userpassword"),
    EMAIL("mail");

    private static final HashMap<String, UserAttributeEnum> str2Enum = new HashMap<String, UserAttributeEnum>();
    static {
        for(UserAttributeEnum attr: UserAttributeEnum.values()) {
            str2Enum.put(attr.toAttrStr(), attr);
        }
    }

    private String attrStr;

    UserAttributeEnum(String attrStr) {
        this.attrStr = attrStr;
    }

    public String toAttrStr() {
        return attrStr;
    }

    public static UserAttributeEnum fromAttrStr(final String str) {
        UserAttributeEnum result = str2Enum.get(str.toLowerCase());
        if(result == null) {
            result = UNKNOWN;
        }
        return result;
    }

}
