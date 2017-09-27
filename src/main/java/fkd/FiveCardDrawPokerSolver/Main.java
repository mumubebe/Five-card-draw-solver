package fkd.FiveCardDrawPokerSolver;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;

import Spear.Card;
import Spear.FiveCardEvaluator;
import Spear.Hand;






public class Main {

	static Node root;
	
	static CFRM[] cf = new CFRM[4];
	private static FiveCardEvaluator f = new FiveCardEvaluator();

	public static void main(String[] args) throws JsonProcessingException, IOException, ClassNotFoundException {
	
		CardHandler.readSerialize();
		start();
		
	
		//System.out.println(f.evaluate(CardHandler.draw(1, 1, 1)));
		//CardHandler.generate5();
		
		//CardHandler.generateBuckets();
		
	}
	
	public static void start(){
		try {
			JSONTreeParser.parseTree(new File("Minitree.json"));
		} catch (IOException e1) {
			System.out.println(e1);
			e1.printStackTrace();
		}
		root = JSONTreeParser.root;
		
		
		cf[0] = new CFRM(root, 0);
		cf[0].start();

	

		boolean run = true;
		System.out.println("Q: quit - R:results ");
		while(run){			
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			System.out.println(input);
			if(input.contains("R")){
				String[] s = input.split(" ");
				
				cf[0].printResults(Integer.parseInt(s[1]));
		
			}else if(input.equals("ls")){
				cf[0].ls();
			}
			else if(input.equals("Q")){
				run=false;
			}else if (input.contains("freq")){
				cf[0].freq();
			}else if (input.contains("go")){
				String[] s = input.split(" ");
				
				cf[0].goTo(s[1], s[1].equals("+") ? Integer.parseInt(s[2]) : 0);
			}
		}
	}
	
	
	public static void threads(int t) throws InterruptedException{
		for(int i=1; i<=t;i++){
			cf[i] = new CFRM(root, i);
			cf[i].start();
		//	Thread.sleep(5000);
		}
	}




}
