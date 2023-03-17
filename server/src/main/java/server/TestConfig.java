package server;

import commons.Board;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.util.TreeSet;

public class TestConfig {
    /**
     * Gets a board for testing
     * @return instnace of Board
     */
    @Bean(name = "testBoard")
    public Board getBoard() {
        return new Board("joinkey", "test", "",  new TreeSet<>(), new Timestamp(12345L));
    }

}
