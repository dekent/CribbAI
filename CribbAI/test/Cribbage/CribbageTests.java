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
		tester.learnDiscardProbability(5000);
		//tester.testHandEvaluation();
		//tester.testDrawWithProbability(5000);
		//ScoreEvaluator.probabilityCheck();
	}
	
	public void testHandEvaluation()
	{		
		Deck deck = new Deck(true);
		//Hand hand = new Hand(deck, 4);
		//Card card = deck.drawCard();
		
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.clubs, 2));
		cards.add(new Card(Suit.clubs, 13));
		cards.add(new Card(Suit.diamonds,3));
		cards.add(new Card(Suit.hearts, 4));
		Card card = new Card(Suit.clubs, 1);
		Hand hand = new Hand(cards);
		
		System.out.println(hand.getCards().toString() + " " + card.toString());
		
		System.out.println(ScoreEvaluator.evaluateHand(hand, card));
	}
	
	public void testDiscard()
	{		
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
			probabilities[0][i] /= ((double)iter*2.0);
			probabilities[1][i] /= ((double)iter*2.0);
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
	
	public double[][] testDrawWithProbability(int iter)
	{
		double[][] probabilities = new double[2][52];
		Card[] discard = new Card[2];
		Deck d = new Deck(true);
		for (int i = 0; i < iter; i ++)
		{
			System.out.println("Iteration: " + i);
			discard[0] = CribbageGame.drawCardWithProbability(true, d);
			discard[1] = CribbageGame.drawCardWithProbability(true, d);
			probabilities[0][discard[0].getCardIndex()] ++;
			probabilities[0][discard[1].getCardIndex()] ++;
			discard[0] = CribbageGame.drawCardWithProbability(false, d);
			discard[1] = CribbageGame.drawCardWithProbability(false, d);
			probabilities[1][discard[0].getCardIndex()] ++;
			probabilities[1][discard[1].getCardIndex()] ++;
		}
		for (int i = 0; i < 52; i ++)
		{
			probabilities[0][i] /= ((double)iter*2.0);
			probabilities[1][i] /= ((double)iter*2.0);
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
