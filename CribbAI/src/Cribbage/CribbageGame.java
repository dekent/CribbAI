package Cribbage;

import java.util.ArrayList;

import Cards.Card;
import Cards.Deck;
import Cards.Hand;
import Cards.Card.Suit;

public class CribbageGame {
	public static Card[] selectDiscard(Hand h, int playerScore, int opponentScore, boolean dealing)
	{
		Card[] discardCards = new Card[2];
		
		double maxUtility = -9999;
		int discard1 = 0;
		int discard2 = 1;
		
		for (int i = 0; i < h.size() - 1; i ++)
		{
			for (int j = i + 1; j < h.size(); j ++)
			{
				double expectedUtility = 0;
				double handUtility = 0, cribUtility = 0;
				
				Hand tempHand = h.copy();
				//System.out.println(tempHand.getCards().toString());
				//System.out.println("i: " + i + ", j: " + j);
				tempHand.discardCard(j);
				tempHand.discardCard(i);
				
				Deck deck = new Deck(true);
				for (int k = 0; k < h.size(); k++)
				{
					deck.removeFromDeck(h.getCards().get(k));
				}
				
				Deck cribDeck = deck.copy();
				Hand cribHand = new Hand();
				cribHand.addCard(h.getCards().get(i));
				cribHand.addCard(h.getCards().get(j));
				
				while (deck.size() > 0)
				{
					Card c = deck.drawCard();
					cribDeck.removeFromDeck(c);
					
					//Calculate expected utility of saved hand
					handUtility += 1.0/46.0 * ScoreEvaluator.evaluateHand(tempHand, c);
					
					int iterations = 100;
					for (int k = 0; k < iterations; k ++)
					{
						Card c1 = cribDeck.drawCard();
						Card c2 = cribDeck.drawCard();
						
						cribHand.addCard(c1);
						cribHand.addCard(c2);
						cribUtility += 1.0/(iterations) * ScoreEvaluator.evaluateHand(cribHand, c);
						//System.out.println(cribUtility);
						
						cribDeck.addToDeck(c1);
						cribDeck.addToDeck(c2);
						cribHand.discardCard(c1);
						cribHand.discardCard(c2);
					}
						
					cribDeck.addToDeck(c);
				}
				
				cribUtility /= 46.0;
				
				//System.out.println("Crib utility: " + cribUtility);
				
				if (!dealing)
					cribUtility *= -1;
				
				//Set priority of crib points
				double alpha = 1;
				if (!dealing)
				{
					if (handUtility + playerScore >= 121)
					{
						alpha = 0;
					}
					else if (handUtility + playerScore + 5 >= 121)
					{
						//TODO: Look into scaling
						if (opponentScore + 20 >= 121)
							alpha = 0.1;
						else
							alpha = 0.5;
					}
				}
				
				//TODO: Consider adding pegging utility - machine learn pegging value for each individual card or for sets of cards
				expectedUtility = handUtility + alpha*cribUtility;
				
				//System.out.println("Hand: " + tempHand.getCards().toString());
				//System.out.println("Expected Utility: " + expectedUtility);
				//System.out.println();
				
				if (expectedUtility > maxUtility)
				{
					maxUtility = expectedUtility;
					discard1 = i;
					discard2 = j;
				}
			}
		}
		
		discardCards[0] = h.getCards().get(discard1);
		discardCards[1] = h.getCards().get(discard2);
		return discardCards;
	}
	
	public static Card determinePeggingMove(ArrayList<Card> peggingStack, Hand hand)
	{
		Card c = new Card(Suit.clubs,1);
		
		return c;
	}
}
