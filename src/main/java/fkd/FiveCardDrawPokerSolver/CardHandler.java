package fkd.FiveCardDrawPokerSolver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Spear.Card;
import Spear.FiveCardEvaluator;
import Spear.Hand;
import Spear.Rank;
import Spear.Suit;

public class CardHandler {
	public static Hand[] h = new Hand[Abstractions.PRE_ISO];
	private static FiveCardEvaluator f = new FiveCardEvaluator();
	private static ArrayList<Hand> buckets = new ArrayList<>();
	private static ArrayList<int[]> seen = new ArrayList<>();
	private static Card[] cards = new Card[52];
	private static Card[] currHand = new Card[5];
	private static Random r = new Random();


	public final static int[][][] strat = {
			{{0},{1},{2},{3},{4}}, 													//1 card
			{{0,1},{0,2},{0,3},{0,4},{1,2},{1,3},{1,4},{2,3},{2,4},{3,4}},		    //2 card
			{{0,1,2},{0,1,3},{0,1,4},{0,2,3},{0,2,4},{0,3,4},{1,2,3},{1,2,4}},		//3 card
			{{0,1,2,3},{0,1,2,4},{0,1,3,4},{0,2,3,4},{1,2,3,4}},					//4 card
			{{0,1,2,3,4}}															//5 card
	};

	public static int[] _shuffle(){
		int[] hand = new int[2];
		hand[0] = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		hand[1] = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		while((hand[0]==hand[1])){
			hand[1] = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		}
		return hand;
	}

	public static int eval(int hand){
		return f.evaluate(h[hand].toCards());

	}

	public static int _draw(int hand, int s){

		int n = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		while(n == hand){
			n = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		}


		return n;


	}

	public static int draw(int hand, int num, int s){

		currHand = h[hand].toCards();
		
		if(s>0){ //If stand pat then return current hand.
			int n;
			for(int i = 0; i<strat[num-1][s].length; i++){
				n = r.nextInt(cards.length);
				while( (cards[n].ordinal()==currHand[0].ordinal()) || (cards[n].ordinal()==currHand[1].ordinal()) || (cards[n].ordinal()==currHand[2].ordinal()) || (cards[n].ordinal()==currHand[3].ordinal()) || (cards[n].ordinal()==currHand[4].ordinal())){			
					n = r.nextInt(cards.length);
				}
				currHand[strat[num-1][s][i]] = cards[n];
			}
		}
		return f.evaluate(currHand);
	}

	public int[] shuffle(){
		int[] hand = new int[2];
		hand[0] = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		hand[1] = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		while((hand[0]==hand[1])){
			hand[1] = r.nextInt(Abstractions.HAND_RANGE_SIZE_PRE_DRAW);
		}
		return hand;
	}

	public static void readSerialize() throws FileNotFoundException, IOException, ClassNotFoundException{
		System.out.print("Reading cards...");
		genCard();
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("hands.ser"));
		h = (Hand[]) in.readObject();
		in.close();
		System.out.print("Card initilize done");
	}

	public static void serialize() throws FileNotFoundException, IOException{

		ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream("hands.ser")
				);
		out.writeObject(h);
		out.flush();
		out.close();

	}

	public static void genCard(){
		int c = 51;
		for (int r = 0; r<Rank.values().length; r++) {
			for(int s = 0; s<Suit.values().length; s++){			
				cards[c] = new Card(Rank.values()[r], Suit.values()[s]);
				c--;
			}
		}
	}

	public static void generate5(){
		int c = 51;
		for (int r = 0; r<Rank.values().length; r++) {
			for(int s = 0; s<Suit.values().length; s++){			
				cards[c] = new Card(Rank.values()[r], Suit.values()[s]);
				c--;
			}
		}

		Card[] genHand = new Card[5];
		c = 0;
		for(int i = 0; i<52; i++){
			for(int j = 0; j<51; j++){
				for(int k = 0; k<50; k++){
					for(int l = 0; l<49; l++){
						for(int m = 0; m<48; m++){
							if(i>j && j>k && k>l && l>m){								
								genHand[0] = cards[i];
								genHand[1] = cards[j];
								genHand[2] = cards[k];
								genHand[3] = cards[l];
								genHand[4] = cards[m];
								h[c] = new Hand(genHand);


								if(!ifSeen(h[c].iso, c)){
									System.out.println(buckets.size());
									seen.add(h[c].iso);
									buckets.add(h[c]);
									//System.out.println(buckets.size());
								}
								c++;
							}



						}
					}
				}
			}
		}
		System.out.println(seen.size());
		h = buckets.toArray(new Hand[buckets.size()]);

	}

	private static boolean ifSeen(int[] a, int c){

		for(int i=seen.size()-1; i>0; i--){
			if(Arrays.equals(seen.get(i), a)){
				//buckets.get(i).bucket.add(h[c]);
				return true;
			}
		}

		return false;
	}

	public static void generateBuckets(){

		for (int i = 0; i<h.length; i++){

			if(!(h[i] == null)){
				for(int j = 0; j<h.length; j++){
					if(!(h[j] == null)){

						if(h[i].isomorphValue == h[j].isomorphValue){
							if(buckets.size()>0)
								if(h[i] == buckets.get(buckets.size()-1))
									buckets.add(h[i]);
							if(!(h[j]==h[i]))
								h[j] = null;
						}
					}
				}
			}

		}
		System.out.println(buckets.size());
	}

	public static boolean isRainbow(Hand h){

		return true;
	}

	/*
	 * Same hand value bucket
	 */
	private static void simpleBucket(int hand){

	}



}
