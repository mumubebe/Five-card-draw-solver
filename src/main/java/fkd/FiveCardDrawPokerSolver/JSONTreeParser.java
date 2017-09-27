package fkd.FiveCardDrawPokerSolver;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTreeParser {
	public static Node root;

	JSONTreeParser(){

	}

	public static void parseTree(File file) throws IOException {
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode = m.readTree(file);
		printNode(rootNode, root, 0);
	}

	private static void printNode(JsonNode parentNode, Node c, int depth) {

		Node currentNode = new Node();
		if (parentNode.isArray()) {
			printTabs(depth);
			System.out.println("[");
			Iterator<JsonNode> iter = parentNode.elements();

			while (iter.hasNext()) {
				JsonNode node = iter.next();
				if (node.isObject() || node.isArray()) {
					printNode(node, c, depth+1);
				} else {
					printTabs(depth);
					System.out.print("--"+node.asText());
				}
			}

			printTabs(depth);
			System.out.println("]");
		}
		if (parentNode.isObject()) {
			printTabs(depth);
			System.out.println("{");
			Iterator<String> iter = parentNode.fieldNames();

			if(c == null){
				root = new Node();
				currentNode = root;
			}

			while (iter.hasNext()) {
				String nodeName = iter.next();
				JsonNode node = parentNode.path(nodeName);

				printTabs(depth);
				System.out.println(nodeName + ": " + node.asText());

				if(nodeName=="value"){					
					currentNode.betSize = node.asDouble();
				}
				if(nodeName=="player"){					
					currentNode.player = node.asInt();
					System.out.println("PLAYER: "+currentNode.player);
				}	
				if(nodeName=="type"){
					currentNode.type = node.asText();
				}
				if (node.isObject() || node.isArray()) {					
					if(currentNode.type.equals("root")){
						currentNode.value = Abstractions.DEAD_MONEY;
						currentNode.player = 0;
						printNode(node, currentNode,depth+1 );					
					}else{
						if(currentNode.parent==null){
							c.addChildren(currentNode);
							c.children[c.children.length-1].calcNodeValue();
							System.out.println("player: "+currentNode.player);
							printNode(node, currentNode,depth+1 );						
						}
					}
				}

			}
			if(!(c==null)){
				if(currentNode.parent==null){
					//showdown nodes
					if(!currentNode.type.equals("fold")){	
						System.out.println("SHOWDOWN"+ currentNode.type);
						currentNode.showdown = true;
					}
					
					c.addChildren(currentNode);
					c.children[c.children.length-1].calcNodeValue();
					System.out.println(c.children[c.children.length-1].player);
					
					printTabs(depth);
					System.out.println("}");
				
				}
			}
		}
	}

	private static void printTabs(int count) {
		for (int i=0; i<count; i++) {
			System.out.print("\t");
		}
	}

}
