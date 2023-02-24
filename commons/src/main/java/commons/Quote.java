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
package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Person person;
    private String quote;

    @SuppressWarnings("unused")
    private Quote() {
        // for object mappers
    }

    /**
     * Constructs a quote
     * @param person the person that made the quote
     * @param quote the string that the person quoted
     */
    public Quote(final Person person, final String quote) {
        this.person = person;
        this.quote = quote;
    }


    public Long getId() { return id; }

    public void setId(final long id) { this.id = id; }

    /**
     * Compares two objects
     * @param obj The object to compare to
     * @return Whether the Quote and the other obj are equal
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Gets the hashcode of the current quote
     * @return a hashcode representing the current quote
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Converts the quote to a string representation
     * @return The string representation of a Quote
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    /**
     * Gets quote
     * @return the quote string
     */
    public String getQuote() {
        return quote;
    }

    /**
     * Sets the quote
     * @param quote the quote string
     */
    public void setQuote(final String quote) {
        this.quote = quote;
    }

    /**
     * Gets the person
     * @return the person
     */

    public Person getPerson() {
        return person;
    }

    /**
     * Sets the person
     * @param person the person to set
     */
    public void setPerson(final Person person) {
        this.person = person;
    }
}