import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Node{
	public int id;
	public String state;
	public boolean visited;
	public int parentId;
	public int depth;
	public int pathCost;
	public Node parent;
	
	public Node(int newId,String newState, int newParentId, int newDepth, int newPathCost, Node prnt){
		id = newId;
		state = newState;
		parentId = newParentId;
		depth = newDepth;
		pathCost = newPathCost;
		visited = false;
		parent = prnt;
	}
}

public class agent {
	
	public void bfs(String src, String dest, String[] nms, int[][] adj) throws IOException{
		String source = src;
		String destination = dest;
		String names[] = nms;
		int adjacency[][] = adj;
		String reverseOutput = "";
		boolean visited[] = new boolean[names.length];
		int pathCost = 0;
		ArrayList<Node> queue = new ArrayList<Node>();
		ArrayList<Node> visitedQueue = new ArrayList<Node>();
		boolean flag = false;
		
		for(int i = 0; i < names.length; i++){
			visited[i] = false;
		}
		
		int count = 1;
		String expansion = "";
		String output = "";
		Node start = new Node(count, source, 0, 0, 0, null);
		Node k = start, node = start;
		
		if(start.visited == false){
			queue.add(start);
			while(!queue.isEmpty()){
				k = queue.get(0);
				queue.remove(0);
				visitedQueue.add(k);
				if(k.state.equals(destination)){
					expansion += k.state;
					flag = true;
					break;
				}
				k.visited = true;
				int nodeNumber = findNodeNumber(k.state, names);
				if(!(nodeNumber == -1)){
					visited[nodeNumber] = true;
				}
				expansion += k.state + "-";
				for(int j = 0; j < names.length; j++){
					if(checkAdjacent(j, nodeNumber, adjacency)){
						if(visited[j] == false){
							if(!nodeInQueues(queue, visitedQueue, names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								queue.add(node);
								
								Collections.sort(queue, new Comparator<Node>() {
									@Override
									public int compare(Node s1, Node s2) {
										if(s1.depth == s2.depth){
											return s1.state.compareToIgnoreCase(s2.state);
										}
										return 0;
									}
								});
							} else if(checkQueueDepthCost(queue, k.depth + 1, names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								queue.add(node);
								
								Collections.sort(queue, new Comparator<Node>() {
									@Override
									public int compare(Node s1, Node s2) {
										if(s1.depth == s2.depth){
											return s1.state.compareToIgnoreCase(s2.state);
										}
										return 0;
									}
								});
							}
						} else if(visited[j] == true){
							if(checkVisitedDepthCost(visitedQueue, k.depth + 1, names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								queue.add(node);
								
								Collections.sort(queue, new Comparator<Node>() {
									@Override
									public int compare(Node s1, Node s2) {
										if(s1.depth == s2.depth){
											return s1.state.compareToIgnoreCase(s2.state);
										}
										return 0;
									}
								});
							}
						}		
					}
				}
			}
		}
		
		reverseOutput += k.state;
		pathCost = k.pathCost;
		do{
			k = k.parent;
			reverseOutput += "-" + k.state;
		} while(k.parentId != 0);
		
		String component[] = reverseOutput.split("-");
		for(int i = component.length - 1; i >= 0; i--){
			if(i == 0){
				output += component[i];
			} else{
				output += component[i] + "-";
			}
		}
		
		FileWriter write = new FileWriter("output.txt");
		PrintWriter writer = new PrintWriter(write);
		if(flag == true){
			writer.println(expansion);
			writer.println(output);
			writer.println(pathCost);
			writer.close();
		} else {
			writer.println("NoPathAvailable");
			writer.close();
		}
	}
	
	public void dfs(String src, String dest, String[] nms, int[][] adj) throws IOException{
		String source = src;
		String destination = dest;
		String names[] = nms;
		int adjacency[][] = adj;
		String reverseOutput = "";
		boolean visited[] = new boolean[names.length];
		int pathCost = 0;
		ArrayList<Node> queue = new ArrayList<Node>();
		ArrayList<Node> reverseQueue = new ArrayList<Node>();
		ArrayList<Node> visitedQueue = new ArrayList<Node>();
		boolean flag = false;
		
		for(int i = 0; i < names.length; i++){
			visited[i] = false;
		}
		
		int count = 1;
		String expansion = "";
		String output = "";
		Node start = new Node(count, source, 0, 0, 0, null);
		Node k = start, node = start;
		
		if(start.visited == false){
			queue.add(start);
			while(!queue.isEmpty()){
				k = queue.get(0);
				queue.remove(0);
				visitedQueue.add(k);
				if(k.state.equals(destination)){
					expansion += k.state;
					flag = true;
					break;
				}
				k.visited = true;
				int nodeNumber = findNodeNumber(k.state, names);
				if(!(nodeNumber == -1)){
					visited[nodeNumber] = true;
				}
				expansion += k.state + "-";
				for(int j = 0; j < names.length; j++){
					if(checkAdjacent(j, nodeNumber, adjacency)){
						if(visited[j] == false){
							if(!nodeInQueues(queue, visitedQueue, names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								reverseQueue.add(node);
							} else if(checkQueueDepthCost(queue, k.depth + 1, names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								reverseQueue.add(node);
							}
						} else if(visited[j] == true){
							if(checkVisitedDepthCost(visitedQueue, k.depth + 1, names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								reverseQueue.add(node);
							}
						}		
					}
				}
				
				Collections.sort(reverseQueue, new Comparator<Node>() {
			        @Override
			        public int compare(Node s1, Node s2) {
			        	return s2.state.compareToIgnoreCase(s1.state);
			        }
			    });
				
				while(!reverseQueue.isEmpty()){
					queue.add(0, reverseQueue.get(0));
					reverseQueue.remove(0);
				}
			}
		}
		
		reverseOutput += k.state;
		pathCost = k.pathCost;
		do{
			k = k.parent;
			reverseOutput += "-" + k.state;
		} while(k.parentId != 0);
		
		String component[] = reverseOutput.split("-");
		for(int i = component.length - 1; i >= 0; i--){
			if(i == 0){
				output += component[i];
			} else{
				output += component[i] + "-";
			}
		}
		
		FileWriter write = new FileWriter("output.txt");
		PrintWriter writer = new PrintWriter(write);
		if(flag == true){
			writer.println(expansion);
			writer.println(output);
			writer.println(pathCost);
			writer.close();
		} else {
			writer.println("NoPathAvailable");
			writer.close();
		}
	}
	
	public void ucs(String src, String dest, String[] nms, int[][] adj) throws IOException{
		String source = src;
		String destination = dest;
		String names[] = nms;
		int adjacency[][] = adj;
		String reverseOutput = "";
		boolean visited[] = new boolean[names.length];
		int pathCost = 0;
		ArrayList<Node> queue = new ArrayList<Node>();
		ArrayList<Node> visitedQueue = new ArrayList<Node>();
		boolean flag = false;
		
		for(int i = 0; i < names.length; i++){
			visited[i] = false;
		}
		
		int count = 1;
		String expansion = "";
		String output = "";
		Node start = new Node(count, source, 0, 0, 0, null);
		Node k = start, node = start;
		
		if(start.visited == false){
			queue.add(start);
			while(!queue.isEmpty()){
				k = queue.get(0);
				queue.remove(0);
				visitedQueue.add(k);
				if(k.state.equals(destination)){
					expansion += k.state;
					flag = true;
					break;
				}
				k.visited = true;
				int nodeNumber = findNodeNumber(k.state, names);
				if(!(nodeNumber == -1)){
					visited[nodeNumber] = true;
				}
				expansion += k.state + "-";
				for(int j = 0; j < names.length; j++){
					if(checkAdjacent(j, nodeNumber, adjacency)){
						if(visited[j] == false){
							if(!nodeInQueues(queue, visitedQueue, names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								int position = findPosition(queue, node);
								if(position == -1) {
									queue.add(node);
								} else {
									queue.add(position, node);
								}
							} else if(checkQueuePathCost(queue, k.pathCost + adjacency[j][nodeNumber], names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								int position = findPosition(queue, node);
								if(position == -1) {
									queue.add(node);
								} else {
									queue.add(position, node);
								}
							}
						} else if(visited[j] == true){
							if(checkVisitedPathCost(visitedQueue, k.pathCost + adjacency[j][nodeNumber], names[j])){
								node = new Node(count+1, names[j], k.id, k.depth + 1, k.pathCost + adjacency[j][nodeNumber], k);
								int position = findPosition(queue, node);
								if(position == -1) {
									queue.add(node);
								} else {
									queue.add(position, node);
								}
								
							}
						}
					}
				}
			}
		}
		
		reverseOutput += k.state;
		pathCost = k.pathCost;
		do{
			k = k.parent;
			reverseOutput += "-" + k.state;
		} while(k.parentId != 0);
		
		String component[] = reverseOutput.split("-");
		for(int i = component.length - 1; i >= 0; i--){
			if(i == 0){
				output += component[i];
			} else{
				output += component[i] + "-";
			}
		}
		
		FileWriter write = new FileWriter("output.txt");
		PrintWriter writer = new PrintWriter(write);
		if(flag == true){
			writer.println(expansion);
			writer.println(output);
			writer.println(pathCost);
			writer.close();
		} else {
			writer.println("NoPathAvailable");
			writer.close();
		}
	}
	
	public boolean nodeInQueues(ArrayList<Node> queue, ArrayList<Node> visitedQueue, String name){
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).state.equals(name)){
				return true;
			}
		}
		
		for(int i = 0; i < visitedQueue.size(); i++){
			if(visitedQueue.get(i).state.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkVisitedPathCost(ArrayList<Node> visitedQueue, int nodePathCost, String nodeName){
		for(int i = 0; i < visitedQueue.size(); i++){
			if(visitedQueue.get(i).state.equals(nodeName)){
				if(nodePathCost < visitedQueue.get(i).pathCost){
					visitedQueue.remove(i);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkVisitedDepthCost(ArrayList<Node> visitedQueue, int nodeDepthCost, String nodeName){
		for(int i = 0; i < visitedQueue.size(); i++){
			if(visitedQueue.get(i).state.equals(nodeName)){
				if(nodeDepthCost < visitedQueue.get(i).depth){
					visitedQueue.remove(i);
					return true;
				}
			}
		}
		return false;
	}
	
	public int findPosition(ArrayList<Node> queue, Node node){
		int position = -1;
		int j = 0;
		for(int i = 0; i < queue.size(); i++){
			if(node.pathCost <= queue.get(i).pathCost){
				if(node.pathCost == queue.get(i).pathCost){
					int compare = node.state.compareTo(queue.get(i).state);
					if(compare >= 0){
						j = i;
						break;
					}
				}
				return i;
			}
		}
		
		if(j != 0){
			while(node.pathCost == queue.get(j).pathCost){
				j++;
				if(j == queue.size()){
					break;
				}
			}
			return j;
		}
		return position;
	}
	
	public boolean checkQueuePathCost(ArrayList<Node> queue, int pathCost, String name){
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).state.equals(name)){
				if(pathCost < queue.get(i).pathCost){
					queue.remove(i);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkQueueDepthCost(ArrayList<Node> queue, int depth, String name){
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).state.equals(name)){
				if(depth < queue.get(i).depth){
					queue.remove(i);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkAdjacent(int source, int dest, int[][] adjacent){
		if(adjacent[source][dest] >= 1){
			return true;
		}
		return false;
	}
	
	public int findNodeNumber(String name, String[] names){
		for(int i = 0; i < names.length; i++){
			if(names[i].equals(name)){
				return i;
			}
		}
		return -1;
	}
	
	public static void main(String args[]) throws IOException{
		String inputFile = "input.txt";
		String line = null;
		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int problem = 0;
		String source = "", dest = "";
		int NO_OF_NODES = 0;
		int countOfLines = 1;
		String names[] = null;
		int nameCount = 0;
		int traverseName = 0;
		int traverseGraph = 0;
		String array[] = null;
		int v[] = null;
		int adj[][] = null;
		while((line = bufferedReader.readLine()) != null){
			if(countOfLines == 1){
				problem = Integer.parseInt(line);
			} else if(countOfLines == 2){
				source = line;
			} else if(countOfLines == 3){
				dest = line;
			} else if(countOfLines == 4){
				NO_OF_NODES = Integer.parseInt(line);
				names = new String[NO_OF_NODES];
				array = new String[NO_OF_NODES];
				adj = new int[NO_OF_NODES][NO_OF_NODES];
				v = new int[NO_OF_NODES];
			} else if(countOfLines == 5){
				nameCount = countOfLines;
			}
			
			if(countOfLines >= 5 && countOfLines < (nameCount + NO_OF_NODES)){
				names[traverseName] = line;
				v[traverseName] = traverseName + 1;
				traverseName++;
			}
			
			if((countOfLines >= (nameCount + NO_OF_NODES)) && nameCount != 0){
				array = line.split(" ");
				for(int i = 0; i < array.length; i++){
					adj[traverseGraph][i] = Integer.parseInt(array[i]);
				}
				traverseGraph++;
			}
			countOfLines++;
		}
		
		agent a = new agent();
		if(problem == 1){
			a.bfs(source, dest, names, adj);
		} else if(problem == 2){
			a.dfs(source, dest, names, adj);
		} else if(problem == 3){
			a.ucs(source, dest, names, adj);
		}
	}
}
