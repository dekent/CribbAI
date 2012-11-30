package Cribbage;

import java.util.ArrayList;
import java.util.Random;

import Cards.Card;
import Cards.Deck;
import Cards.Hand;
import Cards.Card.Suit;

public class CribbageGame {
	static double[] dealProbabilities = {0.0153, 0.0227, 0.0208, 0.0153, 0.0122, 0.0212, 0.0265, 0.0248, 0.0215, 0.0145, 0.017, 0.0167, 0.0159, 0.0185, 0.0205, 0.0211, 0.0134, 0.0169, 0.0191, 0.0267, 0.0231, 0.0212, 0.0146, 0.0169, 0.0187, 0.0164, 0.0205, 0.0213, 0.0212, 0.0147, 0.0165, 0.0194, 0.0254, 0.0262, 0.0178, 0.0154, 0.0156, 0.0162, 0.019, 0.0214, 0.0207, 0.0195, 0.0176, 0.0146, 0.0203, 0.0254, 0.0263, 0.0224, 0.0176, 0.0185, 0.0183, 0.0167};
	static double[] nonDealProbabilities = {0.0169, 0.0228, 0.0188, 0.0155, 0.0011, 0.0161, 0.0214, 0.0203, 0.0221, 0.0228, 0.0154, 0.0236, 0.0285, 0.0223, 0.021, 0.016, 0.0163, 0.0022, 0.0156, 0.0216, 0.0182, 0.0215, 0.0238, 0.0163, 0.0241, 0.0317, 0.0212, 0.0215, 0.0179, 0.0153, 0.0015, 0.0147, 0.0226, 0.022, 0.0192, 0.0259, 0.015, 0.0212, 0.0292, 0.0224, 0.0202, 0.0187, 0.0169, 0.0024, 0.0136, 0.021, 0.0212, 0.0227, 0.026, 0.018, 0.0232, 0.0306};
	
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
						Card c1 = CribbageGame.drawCardWithProbability(!dealing, cribDeck);
						cribDeck.removeFromDeck(c1);
						Card c2 = CribbageGame.drawCardWithProbability(!dealing, cribDeck);
						cribDeck.removeFromDeck(c2);
						
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
	
	public static Card drawCardWithProbability(boolean dealing, Deck d)
	{
		Card c = new Card(Suit.clubs, 1);
		Random rand = new Random();
		double value;
		double count;
		
		do 
		{
			value = rand.nextInt(1000) / 1000.0;
			count = 0.0;
			if (dealing)
			{
				for (int i = 0; i < dealProbabilities.length; i++)
				{
					count += dealProbabilities[i];
					if (value <= count)
					{
						c = Card.getCardFromIndex(i);
						break;
					}
				}
			}
			else
			{
				for (int i = 0; i < nonDealProbabilities.length; i++)
				{
					count += nonDealProbabilities[i];
					if (value <= count)
					{
						c = Card.getCardFromIndex(i);
						break;
					}
				}
			}
		} while (!d.getDeckCards().contains(c));
		
		return c;
	}
}
