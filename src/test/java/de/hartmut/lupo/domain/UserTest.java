/*
 * Copyright 2017-2019 Hartmut Lang
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

package de.hartmut.lupo.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * hartmut on 10.10.19.
 */
public class UserTest {
    @Test
    public void sanitizeNoSpaces() {
        User user = new User();
        user.setUid("myUid");
        user.setEmail("alpha.beta@test.de");

        user.sanitize();

        assertThat(user.getUid(),is("myUid"));
        assertThat(user.getEmail(),is("alpha.beta@test.de"));

    }

    @Test
    public void sanitizeWithSpaces() {
        User user = new User();
        user.setUid(" myUid ");
        user.setEmail(" alpha.beta@test.de ");

        user.sanitize();

        assertThat(user.getUid(),is("myUid"));
        assertThat(user.getEmail(),is("alpha.beta@test.de"));
    }

    @Test
    public void sanitizeWithNullEmail() {
        User user = new User();
        user.setUid(" myUid ");

        user.sanitize();

        assertThat(user.getUid(),is("myUid"));
        assertThat(user.getEmail(),is(nullValue()));
    }
}
