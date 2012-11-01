package Cards;

import static Cards.Card.suits.*;

public class Card {	
	public enum suits {
		hearts, diamonds, clubs, spades
	}
	
	public suits suit;
	public int rank;
	
	public Card(suits s, int r)
	{
		suit = s;
		rank = r;
	}

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
	
	public String toString()
	{
		return this.getRankString() + this.getSuitString();
	}
}
