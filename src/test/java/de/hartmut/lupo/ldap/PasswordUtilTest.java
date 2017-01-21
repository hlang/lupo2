package de.hartmut.lupo.ldap;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;

/**
 * hartmut on 21.01.17.
 */
public class PasswordUtilTest {

    @Test
    public void encryptLdap() throws Exception {

        String result = PasswordUtil.ldapEncryptSHA("pass");

        assertThat(result, startsWith("{SHA}"));
        assertThat(result, endsWith("nU4eI71bcnBGqeO0t9tXvY1u5oQ="));
    }
}