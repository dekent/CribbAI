package Cribbage;

import java.util.ArrayList;

import Cards.Card;
import Cards.Deck;
import Cards.Hand;
import Cards.Card.Suit;

public class CribbageTests {
	public static void main(String[] args)
	{
		Deck deck = new Deck();
		//Hand hand = new Hand(deck, 4);
		//Card card = deck.drawCard();
		
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.clubs, 6));
		cards.add(new Card(Suit.clubs, 7));
		cards.add(new Card(Suit.diamonds, 7));
		cards.add(new Card(Suit.clubs, 8));
		Card card = new Card(Suit.clubs, 1);
		Hand hand = new Hand(cards);
		
		System.out.println(hand.getCards().toString() + " " + card.toString());
		
		System.out.println(ScoreEvaluator.evaluateHand(hand, card));
	}
}
