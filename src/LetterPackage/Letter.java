package LetterPackage;

/**
 *
 * @author Jirka
 */
public class Letter extends SimpleLetter {

    //private String texture;
    private final String quantity;

    public String getQuantity() {
        return quantity;
    }

    public Letter(String type, int value, String quantity, int list, int texture) {
        super(type, value, list, texture);

        this.quantity = quantity;
    }

}
