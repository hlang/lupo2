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

package de.hartmut.lupo.ldap;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

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
