package de.hartmut.lupo;

import de.hartmut.lupo.ldap.LupoLdapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * hartmut on 21.01.17.
 */
@Component
public class MyBean implements CommandLineRunner {

    @Autowired
    private LupoLdapContext lupoLdapContext;
    public void run(String... args) {
        boolean authenticated = lupoLdapContext.authenticate("cn=Thomas Masterman Hardy,ou=people,o=sevenSeas",
                "pass");

        System.out.println("Authenticated: " + authenticated);

        boolean auth2 = lupoLdapContext.authenticateUid("thardy", "pass");
        System.out.println("Authenticated by UID: " + auth2);
    }

}
