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
