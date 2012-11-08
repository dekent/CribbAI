package Cards;

import static Cards.Card.suits.*;

public class Card {	
	public enum suits {
		hearts, diamonds, clubs, spades
	}
	
	public suits suit;
	public int rank;
	
	/**
	 * Constructor
	 * 
	 * @param s the suit of the card (hearts, diamonds, clubs, spades)
	 * @param r the rank of the card (1-13:ace-king)
	 */
	public Card(suits s, int r)
	{
		suit = s;
		rank = r;
	}

	/**
	 * Method to check whether cards are of equal rank and suit
	 * 
	 * @param obj the card to compare to
	 */
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Card c = (Card) obj;
        if (this.suit == c.suit && this.rank == c.rank)
        	return true;
        else
        	return false;
    }
	
	/**
	 * Converts the suit into a string for printing
	 * 
	 * @return a one letter string representing the suit of the card
	 */
	public String getSuitString()
	{
		if (suit == hearts)
			return "H";
		else if (suit == diamonds)
			return "D";
		else if (suit == clubs)
			return "C";
		else
			return "S";
	}
	
	/**
	 * Converts the rank into a string for printing
	 * 
	 * @return a string representation of a card's rank ("A", "2", ..., "J", "Q", "K")
	 */
	public String getRankString()
	{
		if (rank == 1)
			return "A";
		else if (rank == 11)
			return "J";
		else if (rank == 12)
			return "Q";
		else if (rank == 13)
			return "K";
		else
			return "" + rank;
	}
	
	/**
	 * Generate a string representing the card in the form RankSuit (e.g. "AC" for the Ace of Clubs)
	 * 
	 * @return a string representation of the card
	 */
	public String toString()
	{
		return this.getRankString() + this.getSuitString();
	}
}
