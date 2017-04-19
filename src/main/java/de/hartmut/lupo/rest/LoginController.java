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

import de.hartmut.lupo.config.LupoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * hartmut on 29.01.17.
 */
@RestController
@RequestMapping("/api/currentuser")
public class LoginController {

    private final LupoConfig lupoConfig;

    @Autowired
    public LoginController(LupoConfig lupoConfig) {
        this.lupoConfig = lupoConfig;
    }

    @GetMapping
    public ResponseEntity<LoginUser> myuser(Principal user) {

        LoginUser loginUser = new LoginUser();
        loginUser.setName(user.getName());
        if (user instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;
            Object principal = token.getPrincipal();
            if (principal instanceof LdapUserDetails) {
                LdapUserDetails ldapUserDetails = (LdapUserDetails) principal;
                loginUser.setAdmin(isAdminUser(ldapUserDetails));
            }
        }
        return ResponseEntity.ok(loginUser);
    }

    private boolean isAdminUser(LdapUserDetails ldapUserDetails) {
        return ldapUserDetails.getDn().endsWith(lupoConfig.getAdminBase());
    }
}
