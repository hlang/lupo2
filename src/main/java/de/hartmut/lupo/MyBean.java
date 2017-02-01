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
