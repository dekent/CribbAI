package Cards;

import static Cards.Card.Suit.*;

public class Card implements Comparable<Card>{	
	public enum Suit {
		hearts, diamonds, clubs, spades
	}
	
	public Suit suit;
	public int rank;
	
	/**
	 * Constructor
	 * 
	 * @param s the suit of the card (hearts, diamonds, clubs, spades)
	 * @param r the rank of the card (1-13:ace-king)
	 */
	public Card(Suit s, int r)
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
	 * Returns the point value of a card (follows cribbage rules, override this for other scoring systems)
	 * 
	 * @return the point value of a card
	 */
	public int getCardValue()
	{
		if (this.rank > 10)
			return 10;
		else
			return this.rank;
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

	public int getCardIndex()
	{
		int index = 0;
		if (suit == clubs)
			index = 13;
		else if (suit == spades)
			index = 26;
		else if (suit == hearts)
			index = 39;
		return index + rank - 1;
	}
	
	public static Card getCardFromIndex(int i)
	{
		Card c = new Card(diamonds, 1);
		
		c.rank = i % 13 + 1;
		
		if (i >= 13 && i < 26)
			c.suit = clubs;
		else if (i >= 26 && i < 39)
			c.suit = spades;
		else if (i >= 39)
			c.suit = hearts;
		
		return c;
	}
	
	public static Card getCardFromString(String str)
	{
		int rank;
		Suit suit;
		switch (str.charAt(0))
		{
		case 'A':
			rank = 1;
			break;
		case 'T':
			rank = 10;
			break;
		case 'J':
			rank = 11;
			break;
		case 'Q':
			rank = 12;
			break;
		case 'K':
			rank = 13;
			break;
		default:
			rank = Character.getNumericValue(str.charAt(0));
			break;
		}
		switch (str.charAt(1))
		{
		case 'H':
			suit = Suit.hearts;
			break;
		case 'D':
			suit = Suit.diamonds;
			break;
		case 'C':
			suit = Suit.clubs;
			break;
		default:
			suit = Suit.spades;
			break;
		}
		
		return new Card(suit, rank);
	}
	
	@Override
	public int compareTo(Card o) {
		if (this.rank < o.rank)
			return -1;
		else if (this.rank > o.rank)
			return 1;
		else
			return this.getSuitString().compareTo(o.getSuitString());
	}
}
