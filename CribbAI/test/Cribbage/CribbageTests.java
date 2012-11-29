package Cribbage;

import java.util.ArrayList;

import Cards.Card;
import Cards.Deck;
import Cards.Hand;
import Cards.Card.Suit;

public class CribbageTests {
	public static void main(String[] args)
	{
		CribbageTests tester = new CribbageTests();
		tester.testDiscard();
	}
	
	public void testHandEvaluation()
	{
		/**
		 * Test A on top of strait
		 */
		
		Deck deck = new Deck(true);
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
	
	public void testDiscard()
	{
		/**
		 * Test evaluation of this hand:
		 * [QS, 10S, AC, 4C, AS, KD]
		 * Should tie with discarding Q 10 and K 10 but discarding Q K should be lower utility
		 */
		
		Deck deck = new Deck(true);
		Hand hand = new Hand(deck, 6);
		
		System.out.println(hand.getCards().toString());
		
		Card[] discard = CribbageGame.selectDiscard(hand, 0, 0, true);
		System.out.println("Discard:");
		System.out.println(discard[0]);
		System.out.println(discard[1]);
		
	}
}
