package Cards;

import java.util.ArrayList;


public class PeggingStack {
	public ArrayList<Card> peggingStack;
	public int peggingStackCount;
	
	/**
	 * Constructor to initialize an empty pegging stack
	 */
	public PeggingStack()
	{
		peggingStack = new ArrayList<Card>();
		peggingStackCount = 0;
	}
	
	/**
	 * Constructor to initialize a pegging stack as a certain set of cards
	 * 
	 * @param cards the cards to initialize the pegging stack with
	 */
	@SuppressWarnings("unchecked")
	public PeggingStack(ArrayList<Card> cards)
	{
		peggingStack = (ArrayList<Card>) cards.clone();
		for (int i = 0; i < cards.size(); i++)
		{
			peggingStackCount += cards.get(i).getCardValue();
		}
	}
	
	/**
	 * Get a card at a specified index without changing the stack or the count
	 * 
	 * @param i the index of the card to get
	 * @return the card at the specified index
	 */
	public Card getCard(int i)
	{
		return peggingStack.get(i);
	}
	
	/**
	 * Add a card to the pegging stack, adding its value to the count
	 * 
	 * @param c the card to be added to the pegging stack
	 */
	public void addCard(Card c)
	{
		peggingStack.add(c);
		peggingStackCount += c.getCardValue();
	}
	
	/**
	 * Remove a card from the pegging stack, removing its value from the count
	 * 
	 * @param c the card to be removed
	 */
	public void discardCard(Card c)
	{
		peggingStack.remove(c);
		peggingStackCount -= c.getCardValue();
	}
	
	/**
	 * Discard a card at a specified index, removing its value from the count
	 * 
	 * @param i the index of the card to be discarded
	 */
	public void discardCard(int i)
	{
		int value = peggingStack.get(i).getCardValue();
		peggingStack.remove(i);
		peggingStackCount -= value;
	}
	
	/**
	 * Reset the pegging stack count to 0
	 */
	public void resetCount()
	{
		peggingStackCount = 0;
	}
	
	/**
	 * Return a copy of a pegging stack
	 * 
	 * @return a copy of this object
	 */
	public PeggingStack copy()
	{
		PeggingStack peggingStack = new PeggingStack();
		peggingStack.peggingStack = (ArrayList<Card>) this.peggingStack.clone();
		peggingStack.peggingStackCount = this.peggingStackCount;
		return peggingStack;
	}
	
	/**
	 * Getter for the ArrayList containing the cards
	 * 
	 * @return the ArrayList of cards
	 */
	public ArrayList<Card> getCards()
	{
		return peggingStack;
	}
	
	/**
	 * Getter for the int containing the pegging stack count
	 * 
	 * @return the pegging stack count
	 */
	public int getCount()
	{
		return peggingStackCount;
	}
	
	/**
	 * Get the number of cards in the pegging stack
	 * 
	 * @return the number of cards in the pegging stack
	 */
	public int size()
	{
		return peggingStack.size();
	}
}