package commons;

public class CardDTO {

    private final Card card;

    private final String password;

    /**
     * Constructor for CardDTO
     * @param card Card to be transferred
     * @param password password of board in which card belongs for authentication
     */
    public CardDTO(final Card card, final String password) {
        this.card = card;
        this.password = password;
    }

    /**
     * Getter for card
     * @return Card object
     */
    public Card getCard() {
        return card;
    }

    /**
     * Getter for password
     * @return password of board
     */
    public String getPassword() {
        return password;
    }
}
