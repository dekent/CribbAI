package Cards;

import java.util.ArrayList;


public class Hand {
	ArrayList<Card> hand;
	
	/**
	 * Constructor to initialize an empty hand
	 */
	public Hand()
	{
		hand = new ArrayList<Card>();
	}
	
	/**
	 * Constructor to initialize a hand as a certain set of cards
	 * 
	 * @param cards the cards to initialize the hand with
	 */
	@SuppressWarnings("unchecked")
	public Hand(ArrayList<Card> cards)
	{
		hand = (ArrayList<Card>) cards.clone();
	}
	
	/**
	 * Constructor to initialize a hand by drawing cards from a specified deck
	 * (the cards will be removed from the deck after initialization)
	 * 
	 * @param d the deck to draw cards from
	 * @param size the number of cards to be drawn from the deck
	 */
	public Hand(Deck d, int size)
	{
		hand = new ArrayList<Card>();
		for (int i = 0; i < size; i ++)
		{
			hand.add(d.drawCard());
		}
	}
	
	/**
	 * Add a card to the hand
	 * 
	 * @param c the card to be added to the hand
	 */
	public void addCard(Card c)
	{
		hand.add(c);
	}
	
	/**
	 * Remove a card from the hand
	 * 
	 * @param c the card to be removed
	 */
	public void discardCard(Card c)
	{
		hand.remove(c);
	}
	
	/**
	 * Discard a card at a specified index
	 * 
	 * @param i the index of the card to be discarded
	 */
	public void discardCard(int i)
	{
		hand.remove(i);
	}
	
	/**
	 * Return a copy of a hand
	 * 
	 * @return a copy of this object
	 */
	public Hand copy()
	{
		Hand h = new Hand(this.hand);
		return h;
	}
}
