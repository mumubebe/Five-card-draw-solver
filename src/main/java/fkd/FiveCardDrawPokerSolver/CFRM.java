package fkd.FiveCardDrawPokerSolver;

import java.util.Random;




public class CFRM implements Runnable{
	public static final Random random = new Random();
	private CardHandler h = new CardHandler();
	public Node root;
	private Thread t;
	static int iter = 0;
	private int[] dcards;
	public int thread;
	private Node currentNode;

	public CFRM(Node root, int thread){
		this.root = root;
		this.thread = thread;
	}

	@Override
	public void run() {
		double util = 0;
		System.out.println("Calc started..");
		for (int i = 0; i < Abstractions.ITERATIONS; i++) {
			util += cfr(CardHandler._shuffle(), root, 1, 1);
		}
		System.out.println("Average game value: " + util / Abstractions.ITERATIONS);
	}

	private synchronized double cfr(int[] cards, Node action, double p0, double p1) {



		int opponent = 1 - action.player;


		/*
		 * Terminal node
		 */
		if(action.type.equals("fold")){			
			return action.value;
		} else if (action.showdown){

			boolean isBestHand = -cards[action.player] > -cards[opponent]; 
			if(isBestHand) 
				return action.value; 
			else if(!isBestHand){
				return -action.value;

			}else{
				return 0;
			}

		}


		double[] strategy = action.getStrategy(action.player == 0 ? p0 : p1, cards[action.player]);
		double[] util = new double[action.nActions];

		double nodeUtil = 0;
		Node[] successors = action.children;

		/*
		 * STAND
		 */
		if(action.type.equals("stand")){
			for (int a = 0; a < action.nActions; a++) { //First loop through all strategies. action.nAction include both strategies for card drawing and next node.
				dcards =  new int[2];

				//New cards-array for new cards and keep the old cards for regret calculations in current node.
				dcards[0] = CardHandler.eval(cards[0]);
				dcards[1] = CardHandler.eval(cards[1]);


				util[a] = action.player == 0 
						? - cfr(dcards, successors[0], p0 * strategy[a], p1)
								: - cfr(dcards, successors[0], p0, p1 * strategy[a]);
						nodeUtil += strategy[a] * util[a];

			}
		}

		/*
		 * Draw-node
		 */
		else if(action.nDraw>0){			
			for (int a = 0; a < action.nActions; a++) { //First loop through all strategies. action.nAction include both strategies for card drawing and next node.
				dcards =  new int[2];

				//New cards-array for new cards and keep the old cards for regret calculations in current node.
				dcards[0] = action.player == 0 ? CardHandler.draw(cards[0], action.nDraw ,a) : cards[0];
				dcards[1] = action.player == 0 ? cards[1] : CardHandler.draw(cards[1], action.nDraw, a);


				util[a] = action.player == 0 
						? - cfr(dcards, successors[0], p0 * strategy[a], p1)
								: - cfr(dcards, successors[0], p0, p1 * strategy[a]);
						nodeUtil += strategy[a] * util[a];

			}
		}else{


			/*
			 * Regular node
			 */
			for (int a = 0; a < action.nActions; a++) {
				util[a] = action.player == 0 
						? - cfr(cards, successors[a], p0 * strategy[a], p1)
								: - cfr(cards, successors[a], p0, p1 * strategy[a]);
						nodeUtil += strategy[a] * util[a];
			}
		}

		/*
		 * Calculate for current node
		 */
		for (int a = 0; a < action.nActions; a++) {
			double regret = util[a] - nodeUtil;

			//Check who is parent player for multiplying freq.
			if(!action.type.equals("root")){
				action.regretSum[cards[action.player]][a] += (action.parent.player== 0 ? p0 : p1) * regret;
			}
			else
				action.regretSum[cards[action.player]][a] += (action.player == 0 ? p1 : p0) * regret;

		}

		//For know which value to return. Same player in parent node == positive value, else negative
		if(action.type.equals("root"))
			return nodeUtil;
		return action.parent.player == action.player ?  -nodeUtil: nodeUtil;

	}

	public void freq(){
		double[] freqs = new double[currentNode.children.length];
		for(int i=0; i<currentNode.combos; i++){
			for(int j=0; j<freqs.length; j++){
				freqs[j] += currentNode.getAverageStrategy(i)[j];
			}
		}
		
		//print strat
		for(int i=0; i<freqs.length; i++){
			System.out.println(currentNode.children[i].type + ": "+freqs[i]/currentNode.combos);
		}
	}
	
	public void ls(){
		System.out.println(currentNode.getAllParent("")+"--"+currentNode.type);
	}
	
	public void goTo(String n, int c){
		
		if(currentNode==null)
			currentNode = root;
		if(n.equals("+")){
			if(c<=currentNode.children.length){
				currentNode = currentNode.children[c];
			}else{
				System.out.println("No children found in index");
			}
		}
		if(n.equals("-"))
			currentNode = currentNode.parent;
		System.out.println("Current node: "+currentNode.type);
	}


	public void printResults(int n){		
		System.out.println("Results: ");		
		for(int i=0; i<currentNode.children.length; i++){
			System.out.println(h.h[n].toString() +"  "+currentNode.children[i].type+": "+ currentNode.getAverageStrategy(n)[i]);
		}
		



		//}
	}

	public void start () {
		System.out.println("Starting thread");
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}


}

