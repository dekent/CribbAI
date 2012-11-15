package Cribbage;

import java.util.ArrayList;
import java.util.Collections;

import Cards.Card;
import Cards.Hand;
import Cards.Card.Suit;
import static Cards.Card.Suit.*;

public class ScoreEvaluator {
	@SuppressWarnings("unchecked")
	public static int evaluateHand(Hand h, Card c)
	{
		int score = 0;
		
		if (h.size() != 4)
			return score;
		
		//Check for flush
		boolean flush = true;
		ArrayList<Card> cardSet = h.getCards();
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
		if (strait)
		{
			score += 5;
			fiveStraitFound = true;
		}
		if (sum == 15)
			score += 2;
		
		strait = true;
		sum = 0;
		
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
		
		return score;
	}
}
