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

package de.hartmut.lupo.rest;

import de.hartmut.lupo.domain.User;
import de.hartmut.lupo.domain.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * hartmut on 05.02.17.
 */
@RestController
public class UserController {

    private final UserRepo userRepo;

    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(value = "uid", required = false, defaultValue = "")
                    String uid,
            @RequestParam(value = "firstname", required = false, defaultValue = "")
                    String firstname,
            @RequestParam(value = "lastname", required = false, defaultValue = "")
                    String lastname,
            @RequestParam(value = "email", required = false, defaultValue = "")
                    String email,
            @RequestParam(value = "offset", required = false, defaultValue = "0")
                    int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "25")
                    int limit
    ) {
        List<User> matchingUsers = userRepo.findByUidOrFistnameOrLastnameOrEmail(uid, firstname, lastname, email);
        List<User> limitedUsers = matchingUsers.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return ResponseEntity.ok(limitedUsers);
    }
}
