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

package de.hartmut.lupo.domain;

import java.util.List;

/**
 * hartmut on 05.02.17.
 */
public interface UserRepo {
    List<User> findAll();

    List<User> findByUid(String uid);

    List<User> findByUidOrFistnameOrLastnameOrEmail(String uid, String firstname, String lastname, String email);

}
