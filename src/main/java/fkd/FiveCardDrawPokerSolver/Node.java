package fkd.FiveCardDrawPokerSolver;


import java.util.Arrays;
import java.util.Random;

public class Node  {

	public Node[] children = new Node[0];
	public Node parent;
	public String type;
	public double betSize = 0;
	public double betAmount = 0;
	public int player;  //init in addChildren
	public double payoff;
	public final Random random = new Random();
	public double[][] regretSum;
	public int combos;
	public double[][] strategySum;
	public double[][] strategy;
	public boolean secondPlayerDraw = false;
	public int nDraw = 0;
	public double value;
	public boolean showdown;
	public int nActions;




	synchronized double[] getStrategy(double realizationWeight, int hand) {
	
		
		double normalizingSum = 0;
		for (int a = 0; a < nActions; a++) {
			strategy[hand][a] = regretSum[hand][a] > 0 ? regretSum[hand][a] : 0;
			normalizingSum += strategy[hand][a];
		}
		for (int a = 0; a < nActions; a++) {
			if (normalizingSum > 0)
				strategy[hand][a] /= normalizingSum;
			else
				strategy[hand][a] = 1.0 / nActions;
			strategySum[hand][a] += realizationWeight * strategy[hand][a];
		}

		return strategy[hand];
	}





	synchronized public double[] getAverageStrategy(int hand) {
		double[] avgStrategy = new double[nActions];
		double normalizingSum = 0;
		for (int a = 0; a < nActions; a++){
			
			normalizingSum += strategySum[hand][a];		
			//System.out.println(strategy[hand][a] +"    "+ normalizingSum);
		}
		for (int a = 0; a < nActions; a++) 
			if (normalizingSum > 0){
				avgStrategy[a] = strategySum[hand][a] / normalizingSum;

			}
			else{

				
				avgStrategy[a] = 1.0 / nActions; }
		return avgStrategy;
	}

	synchronized public void calcNodeValue(){
		if(type.equals("fold")){
			value = (parent.value-parent.betAmount);
			System.out.println("fold: "+value);
			return;
		}
		if(type.equals("bet") && betSize==0){
			System.out.println("Error: no betsize in bet node");
			return;
		}
		//under 1 blir bet i %
		if(betSize<1){
			betSize +=1;
			value += betSize*parent.value;

			betAmount = (betSize*parent.value)-parent.value;
		}else{
			value += betSize + parent.value;

			betAmount = betSize;

		}

		System.out.println(type +"   "+value);
	}



	public void printAvgStrategy(int hand){
		double[] avg = getAverageStrategy(hand);
		System.out.println(this.toString());
		for(int i = 0; i<children.length; i++){
			System.out.print("Hand "+(hand+1)+" = "+children[i].type + ": "); System.out.printf("%.2f",avg[i]); System.out.println("");
		}
	}

	public void addChildren(Node node){
		//set player
		//if(node.type.contains("draw") && !type.contains("draw")){
		//	node.player = player;
		//}else{
		//	node.player = 1-player;
		//}

		node.setParent(this);

		Node[] newChildren = new Node[children.length+1];
		for(int i=0; i<children.length;i++){
			newChildren[i] = children[i];
		}
		children = newChildren;
		children[children.length-1] = node;

		init();

	}

	public String getAllParent(String p){
		if(parent==null) return p;
		p = "--"+parent.type+p;
		p = parent.getAllParent(p);
		return p;
	}

	






	public void setParent(Node parent){
		this.parent = parent;
	}

	public Node getParent(){
		return parent;
	}

	public String type(Node node){
		if (node == null){
			return "";
		}else{
			return node.type.toString();
		}
	}

	public void init (){
		
		if(this.type.equals("draw1")){
			//nActions = CardHandler.strat[0].length*children.length;
			nDraw = 1;
			nActions = CardHandler.strat[0].length;
		}
		else if(this.type.equals("draw2"))
		{
			nActions = CardHandler.strat[1].length;
			nDraw = 2;
		}
		else if(this.type.equals("draw3"))
		{
			nActions = CardHandler.strat[2].length;
			nDraw = 3;
		}
		else if(this.type.equals("draw4"))
		{
			nActions = CardHandler.strat[3].length;
			nDraw = 4;
		}
		else if(this.type.equals("draw5"))
		{
			nActions = CardHandler.strat[4].length;
			nDraw = 5;
		}else{
			nActions = children.length;
			
		}
		
		
		//Add number of hand combinations in each node. Unique hand combinations pre-draw.
		if(!this.type.equals("root")){
			if(!(parent.parent == null)){
				combos = parent.combos==Abstractions.HAND_RANGE_SIZE_POST_DRAW || parent.parent.type.equals("decNode") ? Abstractions.HAND_RANGE_SIZE_POST_DRAW : Abstractions.HAND_RANGE_SIZE_PRE_DRAW;}
			else
				combos = Abstractions.HAND_RANGE_SIZE_PRE_DRAW;
		
		}else
			combos = Abstractions.HAND_RANGE_SIZE_PRE_DRAW;
			
			
		
		
		strategy = new double[combos][nActions];
		regretSum = new double[combos][nActions];
		strategySum = new double[combos][nActions];
		



	}

	public String toString(){
		return 		"";
	}
}
