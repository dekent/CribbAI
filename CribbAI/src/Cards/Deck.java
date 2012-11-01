package Cards;

import java.util.ArrayList;
import java.util.Random;

import static Cards.Card.suits.*;

public class Deck {
	private ArrayList<Card> deck;
	
	public Deck()
	{
		deck = new ArrayList<Card>();
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
	
	public Card drawCard()
	{
		Random rand = new Random();
		int index = rand.nextInt(this.size());
		Card c = deck.get(index);
		deck.remove(index);
		return c;
	}
	
	public void addToDeck(Card c)
	{
		deck.add(c);
	}
	
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
	
	public int size()
	{
		return deck.size();
	}
	
	public String toString()
	{
		String str = "";
		
		for (int i = 0; i < this.size(); i ++)
		{
			str += deck.get(i).toString() + "\n";
		}
		
		return str;
	}
	
	public static void main(String[] args)
	{
		Deck d = new Deck();
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
