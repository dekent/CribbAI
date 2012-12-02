package Cribbage;

import java.util.ArrayList;
import java.util.Collections;

import Cards.Card;
import Cards.Hand;
import Cards.Card.Suit;
import static Cards.Card.Suit.*;

public class ScoreEvaluator {
	

	public static void probabilityCheck()
	{
		double a=0;
		double b=0;
		for (int i = 0; i < CribbageGame.dealProbabilities.length; i++)
		{
			a += CribbageGame.dealProbabilities[i];
			b += CribbageGame.nonDealProbabilities[i];
		}
		System.out.println(a + ", " + b);
	}
	
	@SuppressWarnings("unchecked")
	public static int evaluateHand(Hand h, Card c)
	{
		Hand hCopy = h.copy();
		
		int score = 0;
		
		if (hCopy.size() != 4)
			return score;
		
		//Check for flush
		boolean flush = true;
		ArrayList<Card> cardSet = hCopy.getCards();
		Suit flushSuit = cardSet.get(0).suit;
		for (int i = 1; i < cardSet.size(); i ++)
		{
			if (cardSet.get(i).suit != flushSuit)
				flush = false;
		}
		if (flush)
		{
			score += 4;
			if (c.suit == flushSuit)
				score ++;
		}
		
		//Check for right Jack
		for (int i = 0; i < cardSet.size(); i ++)
		{
			if (cardSet.get(i).rank == 11 && cardSet.get(i).suit == c.suit)
			{
				score += 1;
				break;
			}
		}
		
		//Check points for subsets of 5 cards
		boolean fiveStraitFound = false;
		boolean fourStraitFound = false;
		cardSet.add(c);
		Collections.sort(cardSet);
		boolean strait = true;
		int sum = 0;
		ArrayList<Card> cardSubset = new ArrayList<Card>();
		for (int i = 0; i < cardSet.size(); i ++)
		{
			sum += cardSet.get(i).getCardValue();
			if (i > 0 && cardSet.get(i).rank - cardSet.get(i-1).rank != 1)
				strait = false;
		}
		//handle ace special case in strait calculation
		if (!strait && cardSet.get(0).rank == 1)
		{
			strait = true;
			while (cardSet.get(0).rank == 1)
			{
				cardSet.add(cardSet.get(0));
				cardSet.remove(0);
			}
			for (int i = 0; i < cardSet.size(); i ++)
			{
				int cardRank = cardSet.get(i).rank;
				if (cardSet.get(i).rank == 1)
					cardRank = 14;
				if (i > 0 && cardRank - cardSet.get(i-1).rank != 1)
					strait = false;
			}
		}
		if (strait)
		{
			score += 5;
			fiveStraitFound = true;
		}
		if (sum == 15)
			score += 2;
		
		strait = true;
		sum = 0;
		
		Collections.sort(cardSet);
		//Check points for subsets of 4 cards (straits, 15s)
		for (int i = 0; i < cardSet.size(); i ++)
		{
			cardSubset = (ArrayList<Card>) cardSet.clone();
			cardSubset.remove(cardSet.get(i));

			for (int j = 0; j < cardSubset.size(); j ++)
			{
				sum += cardSubset.get(j).getCardValue();
				if (!fiveStraitFound && j > 0 && cardSubset.get(j).rank - cardSubset.get(j-1).rank != 1)
					strait = false;
			}
			//handle ace special case in strait calculation
			if (!strait && !fiveStraitFound && cardSubset.get(0).rank == 1)
			{
				strait = true;
				int count = 0;
				while (cardSubset.get(0).rank == 1)
				{
					cardSubset.add(cardSubset.get(0));
					cardSubset.remove(0);
					count ++;
					if (count > 4)
						break;
				}
				for (int j = 0; j < cardSubset.size(); j ++)
				{
					int cardRank = cardSubset.get(j).rank;
					if (cardSubset.get(j).rank == 1)
						cardRank = 14;
					if (j > 0 && cardRank - cardSubset.get(j-1).rank != 1)
						strait = false;
				}
			}
			if (!fiveStraitFound && strait)
			{
				score += 4;
				fourStraitFound = true;
			}
			if (sum == 15)
				score += 2;
		
			strait = true;
			sum = 0;
		}
		
		Collections.sort(cardSet);
		//Check points for subsets of 3 cards (straits, 15s) and 2 cards (15s, pairs)
		for (int i = 0; i < cardSet.size() - 1; i ++)
		{
			for (int j = i + 1; j < cardSet.size(); j ++)
			{
				cardSubset = (ArrayList<Card>) cardSet.clone();
				cardSubset.remove(cardSet.get(i));
				cardSubset.remove(cardSet.get(j));
				
				for (int k = 0; k < cardSubset.size(); k ++)
				{
					sum += cardSubset.get(k).getCardValue();
					if (!fiveStraitFound && !fourStraitFound && k > 0 && cardSubset.get(k).rank - cardSubset.get(k-1).rank != 1)
						strait = false;
				}
				//handle ace special case in strait calculation
				if (!strait && !fiveStraitFound && !fourStraitFound && cardSubset.get(0).rank == 1)
				{
					strait = true;
					int count = 0;
					while (cardSubset.get(0).rank == 1)
					{
						cardSubset.add(cardSubset.get(0));
						cardSubset.remove(0);
						count ++;
						if (count == 3)
							break;
					}
					for (int k = 0; k < cardSubset.size(); k ++)
					{
						int cardRank = cardSubset.get(k).rank;
						if (cardSubset.get(k).rank == 1)
							cardRank = 14;
						if (k > 0 && cardRank - cardSubset.get(k-1).rank != 1)
							strait = false;
					}
				}
				if (!fiveStraitFound && !fourStraitFound && strait)
					score += 3;
				if (sum == 15)
					score += 2;
			
				strait = true;
				sum = 0;
				
				cardSubset.clear();
				cardSubset.add(cardSet.get(i));
				cardSubset.add(cardSet.get(j));
				if (cardSubset.get(0).getCardValue() + cardSubset.get(1).getCardValue() == 15)
					score += 2;
				if (cardSubset.get(0).rank == cardSubset.get(1).rank)
					score += 2;
			}
		}
		
		return score;
	}
	
	public static int evaluatePeggingStack(ArrayList<Card> cards)
	{
		int score = 0;
		
		Card top = cards.get(cards.size()-1);
		
		//pairs
		int pairDepthCount = 0;
		for (int i = cards.size()-2; i >= 0; i--)
		{
			if (cards.get(i).rank == top.rank)
				pairDepthCount++;
			else
				break;
		}
		if (pairDepthCount == 1)
			score += 2;
		else if (pairDepthCount > 1)
			score += 6 * (pairDepthCount - 1);
		
		//count for 15 and 31
		int count = 0;
		for (int i = 0; i < cards.size(); i++)
			count += cards.get(i).getCardValue();
		
		if (count == 15 || count == 31)
			score += 2;
		
		//runs
		int runCount = 1;
		int topCardValue = top.rank;
		if (top.rank == 1)
			topCardValue = 14;
		int difference = 1;
		for (int i = cards.size() - 2; i >= 0; i --)
		{
			if (topCardValue - cards.get(i).rank == difference)
				runCount ++;
			else
				break;
			difference --;
		}
		if (runCount >= 3)
			score += runCount;
		
		//flushes
		int flushCount = 1;
		for (int i = cards.size() - 2; i >= 0; i --)
		{
			if (cards.get(i).suit == top.suit)
				flushCount ++;
			else
				break;
		}
		if (flushCount >= 4)
			score += flushCount;
		
		return score;
	}
}
