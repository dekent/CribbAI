package Cards;

import java.util.ArrayList;
import java.util.Random;

import static Cards.Card.Suit.*;

public class Deck {
	private ArrayList<Card> deck;
	
	/**
	 * Constructor to initialize a standard 52 card deck
	 */
	public Deck(boolean initialize)
	{
		deck = new ArrayList<Card>();
		if (initialize)
		{
			Random rand = new Random();
			
			for (int i = 0; i < 52; i ++)
			{
				Card tempCard;
				do
				{
					int rank = rand.nextInt(13) + 1;
					int suit = rand.nextInt(4);
				
					if (suit == 0)
						tempCard = new Card(hearts, rank);
					else if (suit == 1)
						tempCard = new Card(diamonds, rank);
					else if (suit == 2)
						tempCard = new Card(clubs, rank);
					else
						tempCard = new Card(spades, rank);
				}
				while (deck.contains(tempCard));
					
				deck.add(tempCard);
			}
		}
	}
	
	public Deck copy()
	{
		Deck d = new Deck(false);
		for (int i = 0; i < this.size(); i ++)
		{
			d.addToDeck(this.deck.get(i));
		}
		return d;
	}

	/**
	 * Randomly reorders the deck
	 */
	public void shuffle()
	{
		Random rand = new Random();
		ArrayList<Card> newDeck = new ArrayList<Card>();
		
		while(deck.size() > 0)
		{
			int i = rand.nextInt(deck.size());
			newDeck.add(deck.get(i));
			deck.remove(i);
		}
		
		deck = newDeck;
	}
	
	/**
	 * Returns the card on top of the deck and removes it from the deck
	 * 
	 * @return the top card of the deck
	 */
	public Card drawCard()
	{
		if (deck.size() > 0)
		{
			Card c = deck.get(0);
			deck.remove(0);
			return c;
		}
		else
			return null;
	}
	
	/**
	 * Adds a specified card to a random position in the deck
	 * 
	 * @param c the card to add to the deck
	 */
	public void addToDeck(Card c)
	{
		//TODO: make this random 
		deck.add(c);
	}
	
	/**
	 * Removes the first occurrence of a specified card from the deck
	 * 
	 * @param c the card to remove from the deck
	 */
	public void removeFromDeck(Card c)
	{
		deck.remove(c);
	}
	
	/**
	 * Reorder the deck by "cutting" it at a set index and swapping the set of cards before the cut point
	 * with the set of cards after the cut point
	 * 
	 * @param index the cut index
	 */
	@SuppressWarnings("unchecked")
	public void cut(int index)
	{
		if (index < deck.size())
		{
			ArrayList<Card> topCut = (ArrayList<Card>)deck.clone();
			topCut.removeAll(deck.subList(index, deck.size()));
			deck.removeAll(topCut);
			deck.addAll(topCut);
		}
	}
	
	/**
	 * Get the size of the deck in cards
	 * 
	 * @return the number of cards in the deck
	 */
	public int size()
	{
		return deck.size();
	}
	
	/**
	 * Generate a string based on the contents of the deck
	 * 
	 * @return a string representation of the deck
	 */
	public String toString()
	{
		String str = "";
		
		for (int i = 0; i < this.size(); i ++)
		{
			str += deck.get(i).toString() + "\n";
		}
		
		return str;
	}
	
	/**
	 * Test method
	 */
	public static void main(String[] args)
	{
		Deck d = new Deck(true);
		for (int i = 0; i < 47; i ++)
			System.out.println(d.drawCard().toString());
		System.out.println("Deck after draw:");
		System.out.println(d.toString());
		System.out.println("Deck after shuffle:");
		d.shuffle();
		System.out.println(d.toString());
		System.out.println("Deck after adding Ace of Clubs:");
		d.addToDeck(new Card(clubs, 1));
		System.out.println(d.toString());
		System.out.println("Deck after cut at index 4");
		d.cut(4);
		System.out.println(d.toString());
	}
}
