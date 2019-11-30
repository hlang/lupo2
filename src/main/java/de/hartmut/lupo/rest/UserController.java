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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * hartmut on 05.02.17.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserRepo userRepo;

    private final Function<User, String> byLastname = user -> StringUtils.isEmpty(user.getLastName()) ? "" : user.getLastName();
    private final Function<User, String> byFirstname = user -> StringUtils.isEmpty(user.getFirstName()) ? "" : user.getFirstName();

    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResources<?>> searchUsers(
        @RequestParam(value = "uid", required = false, defaultValue = "")
                    String uid,
        @RequestParam(value = "firstname", required = false, defaultValue = "")
                    String firstname,
        @RequestParam(value = "lastname", required = false, defaultValue = "")
                    String lastname,
        @RequestParam(value = "email", required = false, defaultValue = "")
                    String email,
        @RequestParam(value = "number", required = false, defaultValue = "0")
            int number,
        @RequestParam(value = "size", required = false, defaultValue = "25")
            int size
    ) {
        List<User> matchingUsers = userRepo.findByUidOrFistnameOrLastnameOrEmail(uid, firstname, lastname, email);
        List<User> limitedUsers = matchingUsers.stream()
                .sorted(comparing(byLastname).thenComparing(byFirstname))
            .skip(number * size)
            .limit(size)
                .collect(Collectors.toList());
        PageMetadata metadata = new PageMetadata(size, number, matchingUsers.size());

        return ResponseEntity.ok(new PagedResources<>(limitedUsers, metadata));
    }

    @GetMapping("/{dn:.+}")
    public ResponseEntity<User> getUser(@PathVariable String dn) {
        LOGGER.debug("getUser(): {}", dn);
        User user = userRepo.findByDn(dn);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/uid/{uid}")
    public ResponseEntity<User> getUserByUid(@PathVariable String uid) {
        LOGGER.debug("getUserByUid(): uid={}", uid);
        List<User> users = userRepo.findByUid(uid);
        if(users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users.get(0));
    }

    @DeleteMapping("/{dn:.+}")
    public ResponseEntity<Void> deleteUser(@PathVariable String dn) {
        LOGGER.debug("deleteUser(): {}", dn);
        userRepo.delete(dn);


        return ResponseEntity.accepted().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOGGER.debug("createUser(): {}", user);
        userRepo.create(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        LOGGER.debug("updateUser(): {}", user);
        userRepo.update(user);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/passwd")
    public ResponseEntity<Void> setPassword(@RequestBody User user) {
        LOGGER.debug("setPassword(): {}", user);
        userRepo.setPassword(user);
        return ResponseEntity.ok(null);
    }
}
