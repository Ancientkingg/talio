/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Objects;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private static String adminPassword;

    /**
     * The main method of the server
     *
     * @param args The arguments to the server application
     */
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);

        adminPassword = generateRandomPassword();
        logger.info("\nAdministrator password: " + adminPassword); // prints the admin password in server console
    }

    /**
     * Generate a random string of length 30 as password
     * (the size is because there is no restriction to the number of
     * attempts users can make to guess password)
     * @return A randomly generated String of length 30
     */
    private static String generateRandomPassword() {
        return RandomStringUtils.randomAscii(30);
    }

    /**
     * Checks if password provided as parameter matches the administrator password of the server
     * @param userPassword password provided by user
     * @return correct/incorrect
     */
    public static boolean validatePassword(final String userPassword) {
        return Objects.equals(adminPassword, userPassword);
    }
}