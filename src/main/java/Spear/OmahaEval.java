package Spear;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OmahaEval {
	static int[] table;
	final static int p2 = 52*52;
	final static int p3 = 52*52*52;
	final static int p4 = 52*52*52*52;
	final static int[] ps = new int[]{1,52,p2,p3,p4};
	static List<int[]> permutations;
	public static void main(String[] args) {
		initTables();
		initPermutations();

	}


	private static void permuteHelper(List<int[]> ret, int[] arr, int index){
	    if(index >= arr.length - 1){ //If we are at the last element - nothing left to permute
	        ret.add(arr.clone());
	        return;
	    }

	    for(int i = index; i < arr.length; i++){ //For each index in the sub array arr[index...end]

	        //Swap the elements at indices index and i
	        int t = arr[index];
	        arr[index] = arr[i];
	        arr[i] = t;

	        //Recurse on the sub array arr[index+1...end]
	        permuteHelper(ret,arr, index+1);

	        //Swap the elements back
	        t = arr[index];
	        arr[index] = arr[i];
	        arr[i] = t;
	    }
	}
	/*
	public static void initTables() {
		long start =System.currentTimeMillis();
		table = new int[52*52*52*52*52];
		FiveCardEvaluator.init_deck();
		int[] deck = FiveCardEvaluator.deck;
		
		for(int i=0;i<52;i++) {
			for(int j=0;j<52;j++) {
				if(j==i)
					continue;
				for(int k=0;k<52;k++) {
					if(k==j || k==i)
						continue;
					for(int l=0;l<52;l++) {
						if(l==k || l==j || l==i)
							continue;
						for(int a=0;a<52;++a) {
							if(a==l || a==k || a==j || a==i)
								continue;
	
							table[a+l*52+k*p2+j*p3+i*p4] = FiveCardEvaluator.eval_5cards(deck[i],deck[j],deck[k],deck[l],deck[a]);
						}
					}
				}
			}
		}
		System.out.println("Omaha eval tables generated in "+(System.currentTimeMillis()-start)/1000d +" seconds");
	}*/
	public static void initTables() {
		long start =System.currentTimeMillis();
		initPermutations();
		table = new int[52*52*52*52*52];
		FiveCardEvaluator.init_deck();
		int[] deck = FiveCardEvaluator.deck;
		int[] ps = new int[]{1,52,p2,p3,p4};
		int[] h= new int[5];
		for(h[0]=0;h[0]<52;h[0]++) {
			for(h[1]=h[0]+1;h[1]<52;h[1]++) {
				for(h[2]=h[1]+1;h[2]<52;h[2]++) {
					for(h[3]=h[2]+1;h[3]<52;h[3]++) {
						for(h[4]=h[3]+1;h[4]<52;h[4]++) {
							int val =  FiveCardEvaluator.eval_5cards(deck[h[0]],deck[h[1]],deck[h[2]],deck[h[3]],deck[h[4]]);
							for(int[] p : permutations) {
								table[h[p[0]]+h[p[1]]*52+p2*h[p[2]]+p3*h[p[3]]+p4*h[p[4]]] = val;
							}
						}
					}
				}
			}
		}
		System.out.println("Omaha eval tables generated in "+(System.currentTimeMillis()-start)/1000d +" seconds");


	}
	public static boolean isEqual(int[] a, int[] b) {
		for(int i=0;i<a.length;++i) {
			if(a[i]!=b[i])
				return false;
		}
		return true;
	}
	public static void save() {
		File fout = new File("omahatable.txt");
		FileWriter fw;
		try {
			fw = new FileWriter(fout);

			for (int i = 0; i <table.length; i++) {
				fw.write(table[i]+" ");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void initPermutations() {
		
		permutations = new ArrayList<int[]>();
		permuteHelper(permutations,new int[]{0,1,2,3,4},0);
		
	}
	/*
	public static void initPermutations() {
		permutations = new ArrayList<int[]>();
		List<int[]> twocard = new ArrayList<int[]>();
		twocard.add(new int[]{0,1});
		twocard.add(new int[]{0,2});
		twocard.add(new int[]{0,3});
		twocard.add(new int[]{1,2});
		twocard.add(new int[]{1,3});
		twocard.add(new int[]{2,3});
		
		List<int[]> fivecard = new ArrayList<int[]>();
		fivecard.add(new int[]{4,5,6});
		fivecard.add(new int[]{4,5,7});
		fivecard.add(new int[]{4,5,8});
		
		fivecard.add(new int[]{4,6,7});
		fivecard.add(new int[]{4,6,8});
		
		fivecard.add(new int[]{4,7,8});
		
		fivecard.add(new int[]{5,6,7});
		fivecard.add(new int[]{5,6,8});
		
		fivecard.add(new int[]{5,7,8});
		
		fivecard.add(new int[]{6,7,8});
		
		for(int[] two : twocard) {
			for(int[] five : fivecard) {
				permutations.add(new int[]{two[0],two[1],five[0],five[1],five[2]});
			}
		}
		
	}
	/*
	public static int eval(int[] cards, int[] store) {
		int max=Integer.MAX_VALUE;
		int val;
		for(int[] permutation : permutations) {
			for(int i=0;i<permutation.length;i++) {
				store[i] = cards[permutation[i]];
			}
			val = eval5(store);
			if(val<max)
				max=val;
		}
		return max;
	}*/
	public static int eval(int[] cards) {
		
		int val;
		
		
			//val = table[cards[permutation[0]]+cards[permutation[1]]*52+cards[permutation[2]]*p2+cards[permutation[3]]*p3+cards[permutation[4]]*p4];
			
			int c0p4=cards[0]*p4;
			
			int max = table[cards[6]+cards[1]*52+cards[4]*p2+cards[5]*p3+c0p4];
			val = table[cards[7]+cards[1]*52+cards[4]*p2+cards[5]*p3+c0p4];			if(val<max)max=val;
			val = table[cards[8]+cards[1]*52+cards[4]*p2+cards[5]*p3+c0p4];			if(val<max)max=val;
			val = table[cards[7]+cards[1]*52+cards[4]*p2+cards[6]*p3+c0p4];			if(val<max)max=val;
			val = table[cards[8]+cards[1]*52+cards[4]*p2+cards[6]*p3+c0p4];			if(val<max)max=val;
			val = table[cards[8]+cards[1]*52+cards[4]*p2+cards[7]*p3+c0p4];			if(val<max)max=val;
			val = table[cards[7]+cards[1]*52+cards[5]*p2+cards[6]*p3+c0p4];			if(val<max)max=val;
			val = table[cards[8]+cards[1]*52+cards[5]*p2+cards[6]*p3+c0p4];			if(val<max)max=val;
			val = table[cards[8]+cards[1]*52+cards[5]*p2+cards[7]*p3+c0p4];			if(val<max) max=val;
			val = table[cards[8]+cards[1]*52+cards[6]*p2+cards[7]*p3+c0p4];if(val<max) max=val;

			val = table[cards[6]+cards[2]*52+cards[4]*p2+cards[5]*p3+c0p4];if(val<max) max=val;
			val = table[cards[7]+cards[2]*52+cards[4]*p2+cards[5]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[4]*p2+cards[5]*p3+c0p4];if(val<max) max=val;
			val = table[cards[7]+cards[2]*52+cards[4]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[4]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[4]*p2+cards[7]*p3+c0p4];if(val<max) max=val;
			val = table[cards[7]+cards[2]*52+cards[5]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[5]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[5]*p2+cards[7]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[6]*p2+cards[7]*p3+c0p4];if(val<max) max=val;
			
			val = table[cards[6]+cards[3]*52+cards[4]*p2+cards[5]*p3+c0p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[4]*p2+cards[5]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[5]*p3+c0p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[4]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[7]*p3+c0p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[5]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[5]*p2+cards[6]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[5]*p2+cards[7]*p3+c0p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[6]*p2+cards[7]*p3+c0p4];if(val<max) max=val;
			
			int c1p4 = cards[1]*p4;
			
			val = table[cards[6]+cards[2]*52+cards[4]*p2+cards[5]*p3+c1p4];if(val<max) max=val;
			val = table[cards[7]+cards[2]*52+cards[4]*p2+cards[5]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[4]*p2+cards[5]*p3+c1p4];if(val<max) max=val;
			val = table[cards[7]+cards[2]*52+cards[4]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[4]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[4]*p2+cards[7]*p3+c1p4];if(val<max) max=val;
			val = table[cards[7]+cards[2]*52+cards[5]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[5]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[5]*p2+cards[7]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[2]*52+cards[6]*p2+cards[7]*p3+c1p4];if(val<max) max=val;
			
			val = table[cards[6]+cards[3]*52+cards[4]*p2+cards[5]*p3+c1p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[4]*p2+cards[5]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[5]*p3+c1p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[4]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[7]*p3+c1p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[5]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[5]*p2+cards[6]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[5]*p2+cards[7]*p3+c1p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[6]*p2+cards[7]*p3+c1p4];if(val<max) max=val;
			
			val = table[cards[6]+cards[3]*52+cards[4]*p2+cards[5]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[4]*p2+cards[5]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[5]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[4]*p2+cards[6]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[6]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[4]*p2+cards[7]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[7]+cards[3]*52+cards[5]*p2+cards[6]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[5]*p2+cards[6]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[5]*p2+cards[7]*p3+cards[2]*p4];if(val<max) max=val;
			val = table[cards[8]+cards[3]*52+cards[6]*p2+cards[7]*p3+cards[2]*p4];if(val<max) max=val;
		
		return max;
	}
	public static int eval2cards(int[] cards, int[] store) {
		int max=Integer.MAX_VALUE;
		int val;
		store[0] = cards[0];
		store[1] = cards[1];
		for(int j=0;j<10;j++) {
			int[] permutation = permutations.get(j);
			for(int i=2;i<permutation.length;i++) {
				store[i] = cards[permutation[i]];
			}
			val = eval5(store);
			if(val==0) {
				System.out.println(store[0] + " "+store[1] + " "+store[2] + " "+store[3] + " "+store[4]);
			}
			if(val<max && val!=0)
				max=val;
		}
		return max;
	}
	public static int eval5(int cards) {

		return table[cards];
	}
	public static int eval5(int[] cards) {

		return table[cards[0] + cards[1]*52 + cards[2]*p2 + cards[3]*p3 + cards[4]*p4];
	}
	public static int card(int rank, int suit) {
		return rank+suit*13;
	}

}
