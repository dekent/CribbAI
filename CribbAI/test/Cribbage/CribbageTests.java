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
		//tester.testDiscard();
		tester.learnDiscardProbability(1000);
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
		//Hand hand = new Hand(deck, 6);
		Hand hand = new Hand();
		hand.addCard(new Card(Suit.spades, 3));
		hand.addCard(new Card(Suit.hearts, 4));
		hand.addCard(new Card(Suit.clubs, 5));
		hand.addCard(new Card(Suit.clubs, 10));
		hand.addCard(new Card(Suit.spades, 10));
		hand.addCard(new Card(Suit.clubs, 7));
		
		System.out.println(hand.getCards().toString());
		
		Card[] discard = CribbageGame.selectDiscard(hand, 0, 0, false);
		System.out.println("Discard:");
		System.out.println(discard[0]);
		System.out.println(discard[1]);
		
	}
	
	public double[][] learnDiscardProbability(int iter)
	{
		double[][] probabilities = new double[2][52];
		Card[] discard;
		for (int i = 0; i < iter; i ++)
		{
			System.out.println("Iteration: " + i);
			Deck deck = new Deck(true);
			Hand hand = new Hand(deck, 6);
			discard = CribbageGame.selectDiscard(hand, 0, 0, true);
			probabilities[0][discard[0].getCardIndex()] ++;
			probabilities[0][discard[1].getCardIndex()] ++;
			discard = CribbageGame.selectDiscard(hand, 0, 0, false);
			probabilities[1][discard[0].getCardIndex()] ++;
			probabilities[1][discard[1].getCardIndex()] ++;
		}
		for (int i = 0; i < 52; i ++)
		{
			probabilities[0][i] /= (double)iter;
			probabilities[1][i] /= (double)iter;
		}
		
		System.out.println("Probabilities for dealing:");
		for (int i = 0; i < 52; i ++)
		{
			System.out.print(probabilities[0][i] + ", ");
		}
		
		System.out.println();
		System.out.println("Probabilities for not dealing:");
		for (int i = 0; i < 52; i ++)
		{
			System.out.print(probabilities[1][i] + ", ");
		}
		
		return probabilities;
	}
}
