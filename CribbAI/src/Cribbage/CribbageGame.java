package Cribbage;

import Cards.Card;
import Cards.Deck;
import Cards.Hand;

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
				System.out.println(tempHand.getCards().toString());
				System.out.println("i: " + i + ", j: " + j);
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
					
					cribHand.addCard(c);
					for (int k = 0; k < 1000; k ++)
					{
						Card c1 = cribDeck.drawCard();
						Card c2 = cribDeck.drawCard();
						
						cribDeck.addToDeck(c1);
						cribDeck.addToDeck(c2);
						cribDeck.shuffle();
					}
					
					if (!dealing)
						cribUtility *= -1;
					
					cribDeck.addToDeck(c);
					cribDeck.shuffle();
				}
				
				System.out.println("Hand: " + tempHand.getCards().toString());
				System.out.println("Expected Utility: " + expectedUtility);
				System.out.println();
				
				expectedUtility = handUtility + cribUtility;
				
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
}
