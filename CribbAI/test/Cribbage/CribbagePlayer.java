package Cribbage;

import java.util.ArrayList;
import java.util.Scanner;

import Cards.Card;
import Cards.Hand;
import Cards.PeggingStack;

public class CribbagePlayer {
	public static void main(String args[])
	{
		boolean dealing = true;
		Hand hand;
		while(true)
		{
			hand = discardPhase(dealing);
			peggingPhase(hand, dealing);
			dealing = !dealing;
		}
	}
	
	public static Hand discardPhase(boolean dealing)
	{
		Scanner scan = new Scanner(System.in);
		ArrayList<Card> cards = new ArrayList<Card>();
		System.out.println("Input your 6 card hand: ");
		for (int i = 0; i < 6; i ++)
		{
			cards.add(Card.getCardFromString(scan.nextLine()));
		}
		System.out.println("Enter your score: ");
		int playerScore = scan.nextInt();
		System.out.println("Enter the opponent's score: ");
		int opponentScore = scan.nextInt();
		Hand hand = new Hand(cards);
		Card[] discard = CribbageGame.selectDiscard(hand, playerScore, opponentScore, dealing);
		System.out.println("Discard: ");
		System.out.println(discard[0].toString());
		System.out.println(discard[1].toString());
		hand.discardCard((discard[0]));
		hand.discardCard((discard[1]));
		
		return hand;
	}
	
	public static void peggingPhase(Hand hand, boolean dealing)
	{
		Scanner scan = new Scanner(System.in);
		PeggingStack stack = new PeggingStack();
		
		Card peggingCard;
		
		boolean goCalled = false;
		
		while (hand.size() > 0)
		{
			if (!dealing)
			{
				//Your move
				peggingCard = CribbageGame.determinePeggingMove(stack, hand, dealing);
				
				if (stack.getCount() + peggingCard.getCardValue() > 31)
				{
					System.out.println("Go");
					if (goCalled)
					{
						stack.resetCount();
						goCalled = false;
					}
					else
						goCalled = true;
				}
				else
				{
					goCalled = false;
					stack.addCard(peggingCard);
					hand.discardCard(peggingCard);
					System.out.println("Play " + peggingCard.toString());
				}
				
				if (stack.getCount() == 31)
				{
					stack.resetCount();
					goCalled = false;
				}
				
				//Opponent's move
				System.out.println("Input the opponent's play (enter go for no play): ");
				String input = scan.nextLine();
				if (input.equals("GO"))
				{
					if (goCalled)
					{
						stack.resetCount();
						goCalled = false;
					}
					else
						goCalled = true;
				}
				else
				{
					goCalled = false;
					stack.addCard(Card.getCardFromString(input));
				}
				
				if (stack.getCount() == 31)
				{
					stack.resetCount();
					goCalled = false;
				}
			}
			else
			{				
				//Opponent's move
				System.out.println("Input the opponent's play (enter go for no play): ");
				String input = scan.nextLine();
				if (input.equals("GO"))
				{
					if (goCalled)
					{
						stack.resetCount();
						goCalled = false;
					}
					else
						goCalled = true;
				}
				else
				{
					goCalled = false;
					stack.addCard(Card.getCardFromString(input));
				}
				
				if (stack.getCount() == 31)
				{
					stack.resetCount();
					goCalled = false;
				}
				
				//Your move
				peggingCard = CribbageGame.determinePeggingMove(stack, hand, dealing);
				
				if (stack.getCount() + peggingCard.getCardValue() > 31)
				{
					System.out.println("Go");
					if (goCalled)
					{
						stack.resetCount();
						goCalled = false;
					}
					else
						goCalled = true;
				}
				else
				{
					goCalled = false;
					stack.addCard(peggingCard);
					hand.discardCard(peggingCard);
					System.out.println("Play " + peggingCard.toString());
				}
				
				if (stack.getCount() == 31)
				{
					stack.resetCount();
					goCalled = false;
				}
			}

		}
	}
}
