package Cribbage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Cards.Card;
import Cards.Deck;
import Cards.Hand;
import Cards.Card.Suit;
import Cards.PeggingStack;

public class CribbageGame {
	static double[] dealProbabilities = {0.0196, 0.0196, 0.0209, 0.013, 0.0155, 0.0179, 0.0233, 0.0266, 0.0207, 0.0146, 0.0172, 0.0192, 0.0177, 0.0172, 0.0208, 0.0189, 0.0183, 0.0167, 0.0189, 0.0237, 0.0252, 0.0203, 0.0184, 0.017, 0.0183, 0.0171, 0.0199, 0.0204, 0.0185, 0.0158, 0.0154, 0.0219, 0.0217, 0.023, 0.0221, 0.0186, 0.0167, 0.0191, 0.0164, 0.0183, 0.0212, 0.0214, 0.015, 0.0157, 0.02, 0.0246, 0.0226, 0.023, 0.0174, 0.0189, 0.0166, 0.0192};
	static double[] nonDealProbabilities = {0.0199, 0.0202, 0.0168, 0.0133, 0.0017, 0.0169, 0.0179, 0.0212, 0.0191, 0.0227, 0.0175, 0.0244, 0.0301, 0.0193, 0.0216, 0.0159, 0.0151, 0.0012, 0.0143, 0.0209, 0.0227, 0.0203, 0.0269, 0.0163, 0.0226, 0.0309, 0.0224, 0.0227, 0.0163, 0.0173, 0.0015, 0.0165, 0.0197, 0.0198, 0.0212, 0.0277, 0.0164, 0.0248, 0.0327, 0.0203, 0.0203, 0.0177, 0.0146, 0.0014, 0.0166, 0.0189, 0.0177, 0.0225, 0.027, 0.0171, 0.0216, 0.0356};
	
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
	
	/**
	 * determinePeggingMove
	 * 
	 * Determines the best pegging move (card to play) during a given game state (the pegging stack and the player's hand).
	 * The algorithm iterates through a set number of simulations for each card in the player's hand, assuming the opponent holds random cards.
	 * 
	 * @param	PeggingStack	peggingStack	the pegging stack, representing all cards played so far in the current phase
	 * @param	Hand			hand			the player's hand
	 * @return	Card							the card in the player's hand that represents the best pegging move
	 */
	public static Card determinePeggingMove(PeggingStack peggingStack, Hand hand)
	{
		// If there is only one card in the player's hand, it represents the best (and only) pegging move.
		if (hand.size() == 1)
			return hand.getCards().get(0);
		
		// Initialize the card and utility representing the current best known pegging move.
		Card bestCard = hand.getCards().get(0);
		double maxUtility = -999;
		
		// Initialize the deck representing all unplayed cards by removing all known cards in the player's hand and the pegging stack.
		Deck unplayedCards = new Deck(true);
		for (int i = 0; i < hand.size(); i++)
		{
			unplayedCards.removeFromDeck(hand.getCards().get(i));
		}
		for (int i = 0; i < peggingStack.size(); i++)
		{
			unplayedCards.removeFromDeck(peggingStack.getCard(i));
		}
		
		// Iterate through all cards in the player's hand to determine the best pegging move.
		for (int i = 0; i < hand.size(); i++)
		{
			if (!(peggingStack.getCount() + hand.getCards().get(i).getCardValue() > 31))
			{
				double cardUtility = 0;
				
				// Perform a set number of pegging simulations that start with this card.
				int iterations = 10000;
				for (int j = 0; j < iterations; j++)
				{
					// Create temporary copies of the player's hand, the pegging stack, and the temporary deck.
					Hand tempHand = hand.copy();
					PeggingStack tempPeggingStack = peggingStack.copy();
					Deck tempDeck = unplayedCards.copy();
					
					// Initialize the hand representing the opponent's cards (assumed random).
					Hand opponentHand = new Hand();
					for (int k = 0; k < 8 - peggingStack.size() - tempHand.size(); k++)
					{
						opponentHand.addCard(tempDeck.drawCard());
					}
					
					// Simulate the first move using this card, adding any points it earns to its utility.
					cardUtility += simulatePeggingMove(tempPeggingStack, tempHand, tempHand.getCards().get(i));
					
					// Initialize the variable that keeps track of whether it is the player's turn.
					boolean playerTurn = false;
					
					// As long as there are remaining cards, simulate the play of random cards from the player's hand and the opponent's assumed hand.
					while (tempHand.size() + opponentHand.size() > 0)
					{
						Random rand = new Random();
						int cardIndex = 0;
						
						// If it's the player's turn, simulate a move with a random card in the player's hand, adding any points it earns to this card's utility . . .
						if (playerTurn)
						{
							// Remove unplayable cards from consideration.
							Hand playableHand = tempHand.copy();
							for (int l = playableHand.size() - 1; l >= 0; l--)
							{
								if (tempPeggingStack.getCount() + playableHand.getCards().get(l).getCardValue() > 31)
									playableHand.discardCard(l);
							}
							
							// If the player has no playable cards, reset the coun		handCards.add(new Card(Suit.spades, 11));t and subtract one utility point for "One for the go" . . .
							if (playableHand.size() == 0)
							{
								cardUtility -= 1;
								tempPeggingStack.resetCount();
							}
							// . . . else, select a random card from all playable cards.
							else
							{
								cardIndex = rand.nextInt(playableHand.size());
								cardUtility += simulatePeggingMove(tempPeggingStack, tempHand, playableHand.getCards().get(cardIndex));
							}
							
							// If neither the player nor the opponent have cards remaining, add one utility point for "One for last."
							if (tempHand.size() + opponentHand.size() == 0)
								cardUtility += 1;
							
							playerTurn = false;
						}
						// . . . else, simulate a move with a random card in the opponent's hand, subtracting any points it earns from this card's utility.
						else
						{
							// Remove unplayable cards from consideration.
							Hand playableHand = opponentHand.copy();
							for (int l = playableHand.size() - 1; l >= 0; l--)
							{
								if (tempPeggingStack.getCount() + playableHand.getCards().get(l).getCardValue() > 31)
									playableHand.discardCard(l);
							}
							
							// If the opponent has no playable cards, reset the count and add utility for go . . .
							if (playableHand.size() == 0)
							{
								cardUtility += 1;
								tempPeggingStack.resetCount();
							}
							// . . . else, select a random card from all playable cards.
							else
							{
								cardIndex = rand.nextInt(playableHand.size());
								cardUtility -= simulatePeggingMove(tempPeggingStack, opponentHand, playableHand.getCards().get(cardIndex));
							}
							
							// If neither the player nor the opponent have cards remaining, subtract one utility point for "One for last."
							if (tempHand.size() + opponentHand.size() == 0)
								cardUtility -= 1;
							
							playerTurn = true;
						}
					}
					
					tempHand = hand.copy();
					tempPeggingStack = peggingStack.copy();
					tempDeck = unplayedCards.copy();
				}
				// Normalize this card's utility based on the number of iterations.
				cardUtility *= 1.0/(iterations);
				
				System.out.println("Utility: " + cardUtility);
				
				// If playing this card would yield the highest expected utility so far, consider it the best pegging move.
				if (cardUtility > maxUtility)
				{
					maxUtility = cardUtility;
					bestCard = hand.getCards().get(i);
				}
			}
		}
		
		return bestCard;
	}

	/**
	 * simulatePeggingMove
	 * 
	 * Simulates a round of the pegging phase by playing a given card from the given hand onto the given pegging stack.
	 * The card, if played, is removed from the hand and added to the pegging stack.
	 * The number of points scored is returned as the result.  If the card cannot be played because it would exceed a pegging stack total of 31, -1 is returned.
	 * 
	 * @param	PeggingStack	peggingStack	the pegging stack, representing all cards played so far in the current phase
	 * @param	Hand			hand			the player's hand
	 * @param	Card			card			the card to play from the player's hand
	 * @return	int								the number of points scored by playing the card or -1 if the card cannot be played
	 */
	public static int simulatePeggingMove(PeggingStack peggingStack, Hand hand, Card card)
	{
		int pointsEarned = 0;
		
		// If the card would exceed the pegging stack total of 31, it cannot be played and the opponent would gain a point for "One for the go."
		if (peggingStack.getCount() + card.getCardValue() > 31)
			return -1;
		
		// If the played card brings the pegging stack count to 15 or 31, award 2 points.
		if (peggingStack.getCount() + card.getCardValue() == 15 || peggingStack.getCount() + card.getCardValue() == 31)
			pointsEarned += 2;
		
		// Check for points awarded by making a pair, three of a kind, or four of a kind.
		if (peggingStack.size() > 0)
		{
			// If the played card makes a pair, award 2 points. 
			if (peggingStack.getCard(peggingStack.size() - 1).rank == card.rank)
			{
				pointsEarned += 2;
				if (peggingStack.size() > 1)
				{
					// If the played card makes three of a kind, award 4 more points (for a total of 6 from pairs). 
					if (peggingStack.getCard(peggingStack.size() - 2).rank == card.rank)
					{
						pointsEarned += 4;
						if (peggingStack.size() > 2)
						{
							// If the played card makes four of a kind, award 6 more points (for a total of 12 from pairs). 
							if (peggingStack.getCard(peggingStack.size() - 3).rank == card.rank)
								pointsEarned += 6;
						}
					}
				}
			}
		}
		
		// Check for points awarded by forming a run of 3.
		if (peggingStack.size() > 1)
		{
			// Create a temporary copy of the pegging stack.
			PeggingStack tempPeggingStack = peggingStack.copy();
			
			// Add the top 2 cards of the pegging stack to a set of cards.
			ArrayList<Card> cardSet = new ArrayList<Card>();
			for (int i = tempPeggingStack.size() - 1; i >= tempPeggingStack.size() - 2; i--)
			{
				cardSet.add(tempPeggingStack.getCards().get(i));
			}
			
			// Add the played card to the set.
			cardSet.add(card);
			
			// Sort the three cards in the set.
			Collections.sort(cardSet);
			
			//TODO: Determine if there is a run of 3.
		}
		
		//TODO: Determine runs of other sizes.
		
		// Add the card to the pegging stack (and its count), then remove it from the player's hand.
		peggingStack.addCard(card);
		hand.discardCard(card);
		
		return pointsEarned;
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
