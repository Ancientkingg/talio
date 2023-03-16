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

import java.sql.Timestamp;
import java.time.Clock;
import java.util.Random;
import java.util.TreeSet;

import commons.Board;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    /**
     * Gets a random generator
     * @return a random generator
     */
    @Bean
    public Random getRandom() {
        return new Random();
    }

    /**
     * Gets the clock
     * @return the clock
     */
    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }


    /**
     * Gets a board for testing
     * @return instnace of Board
     */
    @Bean
    public Board getBoard() {
        return new Board("joinkey", "test", "",  new TreeSet<>(), new Timestamp(12345L));
    }
}