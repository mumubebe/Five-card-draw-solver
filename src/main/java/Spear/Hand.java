package Spear;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

public class Hand implements Comparable<Hand>, Serializable {
	private static final int NO_CARDS = 52;
	private static final Random random = new Random();
	private static FiveCardEvaluator f = new FiveCardEvaluator();
	public ArrayList<Hand> bucket = new ArrayList<>();
	private BitSet bitSet;
	public int[] iso = new int[5];
	public String isomorphValue = "";
	public boolean inAction = false;
	public Card[] cards;;
	public Card[] org = new Card[5];
	public int[] ords = new int[5];
	public int value;
	public int CARD_NUMBER;



	public Hand() {
		bitSet = new BitSet(NO_CARDS);
	}



	public Hand(Card[] c) {
		this.cards = c;
		org[0] = c[0];
		org[1] = c[1];
		org[2] = c[2];
		org[3] = c[3];
		org[4] = c[4];
		bitSet = new BitSet(NO_CARDS);
		for (int i = 0; i<cards.length; i++) {
			bitSet.set(cards[i].ordinal);
			ords[i] = cards[i].ordinal;
		}
		setIsomorphValue();

	}
	//Add card which includes in this bucket
	public void addBucketedCard(Hand b){
		bucket.add(b);
	}

	//get bucketed cards
	public Hand[] getBucketedCards(){
		return bucket.toArray(new Hand[bucket.size()]);
	}
	
	/**
	 * FIX THIS CODE!
	 */
	public void setIsomorphValue(){
		String hand = this.toString();

		String temp = "";
		int club = 1;
		String cstring = "";
		int heart = 1;
		String hstring = "";
		int spade = 1;
		String sstring = "";
		int diamond = 1;
		String dstring = "";


		for(int i=2; i<=10; i+=2){
			if(hand.substring(i-1,i).equals("d")){
				diamond++;
				dstring += hand.substring(i-2,i-1).replace("T", "10").replaceAll("J", "11").replaceAll("Q", "12").replaceAll("K", "13").replaceAll("A", "14");

			}
			if(hand.substring(i-1,i).equals("h")){
				heart++;
				hstring += hand.substring(i-2,i-1).replace("T", "10").replaceAll("J", "11").replaceAll("Q", "12").replaceAll("K", "13").replaceAll("A", "14");
			}
			if(hand.substring(i-1,i).equals("s")){
				spade++;
				sstring += hand.substring(i-2,i-1).replace("T", "10").replaceAll("J", "11").replaceAll("Q", "12").replaceAll("K", "13").replaceAll("A", "14");
			}
			if(hand.substring(i-1,i).equals("c")){
				club++;
				cstring += hand.substring(i-2,i-1).replace("T", "10").replaceAll("J", "11").replaceAll("Q", "12").replaceAll("K", "13").replaceAll("A", "14");
			}


		}

		int c = 0;
		for(int i=2; i<=10; i+=2){

			temp = hand.substring(i-2,i-1).replace("T", "10").replaceAll("J", "11").replaceAll("Q", "12").replaceAll("K", "13").replaceAll("A", "14");
			
			
			
			if(hand.substring(i-1,i).equals("d")){
				temp = Integer.toString(diamond-1)+temp;
				if(heart*diamond*spade*club==18){
					temp = dstring+temp;
				}

			}
			if(hand.substring(i-1,i).equals("h")){
				temp = Integer.toString(heart-1)+temp;
				if(heart*diamond*spade*club==18){
					temp = hstring+temp;
				}
			}
			if(hand.substring(i-1,i).equals("s")){
				temp = Integer.toString(spade-1)+temp;
				if(heart*diamond*spade*club==18){
					temp = sstring+temp;
				}
			}
			if(hand.substring(i-1,i).equals("c")){
				temp = Integer.toString(club-1)+temp;
				if(heart*diamond*spade*club==18){
					temp = cstring+temp;
				}
			}

			iso[c] = Integer.parseInt(temp);
			c++;
			temp = "";
		}

		//System.out.println(toString()+ "--"+iso[0]+","+iso[1]+","+iso[2]+","+iso[3]+","+iso[4]);
		Arrays.sort(iso);
		//System.out.println(toString()+ "--"+iso[0]+","+iso[1]+","+iso[2]+","+iso[3]+","+iso[4]);


	}

	// Indiana: reset hand by given ordinals
	public void reset(int[] cards) {
		bitSet = new BitSet(NO_CARDS);
		for (int i = 0; i < cards.length; i++) {
			bitSet.set(cards[i]);
		}
	}

	// Indiana: add card to the hand by given card ordinal
	public void addCard(int card) {
		bitSet.set(card);
	}

	public Hand plus(Hand hand){
		this.bitSet.or(hand.bitSet);
		return this;
	}

	public Hand plus(Card[] cards) {
		for (Card card : cards) {
			this.bitSet.set(card.ordinal);
		}
		return this;
	}

	public Hand minus(Hand hand) {
		this.bitSet.andNot(hand.bitSet);
		return this;
	}

	public Hand minus(Card[] cards) {
		for (Card card : cards) {
			this.bitSet.clear(card.ordinal);
		}
		return this;
	}

	public Card[] toCards() {
		int noCards = noCards();
		Card[] result = new Card[noCards];

		int j = -1;
		for (int i = 0; i < result.length; i++) {
			j = bitSet.nextSetBit(j+1);
			result[i] = Card.values()[j];
		}

		return result;
	}

	public int noCards() {
		return bitSet.cardinality();
	}

	public boolean intersects(Hand hand) {
		return bitSet.intersects(hand.bitSet);
	}

	public Hand plus(Card card) {
		this.bitSet.set(card.ordinal);
		return this;
	}



	public Hand minus(Card card) {
		this.bitSet.clear(card.ordinal);
		return this;
	}

	public String toString() {
		return Card.toString(this.toCards());
	}

	public Card removeRandom() {
		int cardNo;
		do {
			cardNo = random.nextInt(52);
		} while (!this.bitSet.get(cardNo));		

		this.bitSet.clear(cardNo);
		return Card.values()[cardNo];
	}

	public Hand clear() {
		this.bitSet.clear();
		return this;
	}

	public boolean intersects(Pair p) {
		Card[] cards = p.getCards();
		if(this.contains(cards[0])) return true;
		if(this.contains(cards[1])) return true;
		return false;
	}

	private boolean contains(Card card) {
		return this.bitSet.get(card.ordinal);
	}

	public Hand minus(Pair p) {
		Card[] cards = p.getCards();
		this.minus(cards);
		return this;
	}

	public Hand plus(Pair p) {
		Card[] cards = p.getCards();
		this.plus(cards);
		return this;
	}

	public static Hand parse(String s) {
		Hand result = new Hand();
		int noCards = s.length()/2;
		for (int i = 0; i < noCards; i++) {
			result.plus(Card.parse( s.substring(2*i, 2*i+2)));
		}
		return result;
	}

	@Override
	public int compareTo(Hand o) {
		return new Integer(this.value).compareTo(new Integer(o.value));
	}



}
