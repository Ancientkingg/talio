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
package server.api;

import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.QuoteRepository;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final Random random;
    private final QuoteRepository repo;

    /**
     * Constructs a quote controller
     * @param random the random generator
     * @param repo the repository that stores the quotes
     */
    public QuoteController(final Random random, final QuoteRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * Gets all quotes
     * @return a list of quotes
     */
    @GetMapping(path = { "", "/" })
    public List<Quote> getAll() {
        return repo.findAll();
    }

    /**
     * Gets a quote by id
     * @param id the id of the quote to search for
     * @return a response entity around the quote that was searched for
     */
    @GetMapping("/{id}")
    public ResponseEntity<Quote> getById(final @PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Adds a quote
     * @param quote the quote to be added
     * @return a response entity around the quote that was added
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Quote> add(final @RequestBody Quote quote) {

        if (quote.getPerson() == null || isNullOrEmpty(quote.getPerson().getFirstName()) || isNullOrEmpty(quote.getPerson().getLastName())
                || isNullOrEmpty(quote.getQuote()))
        {
            return ResponseEntity.badRequest().build();
        }

        final Quote saved = repo.save(quote);
        return ResponseEntity.ok(saved);
    }

    /**
     * Checks whether a string is null or empty
     * @param s the string to check
     * @return whether the given string is null or empty
     */
    private static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Gets a random quote
     * @return a response entity around a random quote that was fetched
     */
    @GetMapping("rnd")
    public ResponseEntity<Quote> getRandom() {
        final var quotes = repo.findAll();
        final var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(quotes.get(idx));
    }
}