import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class Node {
	public int row, column;
	public FinalLegalNode parent;
	public int depth;
	public int value;
	public int alpha;
	public int beta;
	public int rowChangeFactor;
	public int colChangeFactor;
	
	public Node(int newRow, int newCol, FinalLegalNode newParent, int newDepth, int newValue, int newAlpha, int newBeta, int newRowChangeFactor, int newColChangeFactor){
		row = newRow;
		column = newCol;
		parent = newParent;
		depth = newDepth;
		value = newValue;
		alpha = newAlpha;
		beta = newBeta;
		rowChangeFactor = newRowChangeFactor;
		colChangeFactor = newColChangeFactor;
	}
}

class FinalLegalNode{
	public int row;
	public int column;
	public FinalLegalNode parent;
	public int depth;
	public int value;
	public int alpha;
	public int beta;
	public int rowChangeFactor;
	public int colChangeFactor;
	public boolean terminate = false;
	
	public String nextState[][] = new String[8][];
	
	public FinalLegalNode(String nextPossibleState[][], int newRow, int newCol, FinalLegalNode newParent, int newDepth, int newValue, int newAlpha, int newBeta){
		for(int m = 0; m < nextPossibleState.length; m++){
			nextState[m] = Arrays.copyOf(nextPossibleState[m], nextPossibleState[m].length);
		}
		row = newRow;
		column = newCol;
		parent = newParent;
		depth = newDepth;
		value = newValue;
		alpha = newAlpha;
		beta = newBeta;
	}
}

public class agent {
	public String outputState[][] = new String[8][];
	String output = "";
	String traverseLog = "";
	
	public void greedy(String reversi[][],String player,String opponent, int weight[][], int problem) throws IOException{
		ArrayList<Node> legalNodes = new ArrayList<Node>();
		String nextPossibleState[][] = new String[reversi.length][];
		ArrayList<FinalLegalNode> finalLegalNodes = new ArrayList<FinalLegalNode>();
		
		legalNodes = legalMoves(reversi, player, opponent, null, 0, -999999999, -99999999, 999999999);
		finalLegalNodes = findFinalLegalNodes(reversi,legalNodes,weight, player, opponent, problem, true);
		
		Collections.sort(finalLegalNodes, new Comparator<FinalLegalNode>() {
			@Override
			public int compare(FinalLegalNode fnode1, FinalLegalNode fnode2) {
				// TODO Auto-generated method stub
				return fnode2.value - fnode1.value;
			}
		});
		
		for(int r = 0; r < 8; r++){
			for(int c = 0; c < 8; c++){
				output += finalLegalNodes.get(0).nextState[r][c];
			}
			output += "\n";
		}
		output = output.trim();
		
		String[] result = output.split("\n");
		
		FileWriter write = new FileWriter("output.txt");
		PrintWriter writer = new PrintWriter(write);
		for(int i = 0; i < result.length; i++){
			if(i == result.length - 1){
				writer.print(result[i]);
			} else{
				writer.println(result[i]);
			}
		}
		writer.close();
	}
	
	public void miniMax(String reversi[][],String player,String opponent,int weight[][],int cutOff,int problem) throws IOException{
		traverseLog += "Node,Depth,Value\n";
		FinalLegalNode root = new FinalLegalNode(reversi, -1, -1, null, 0, -999999999, -999999999, 999999999);
		root = maxValue(root, player, opponent, weight, cutOff, problem, player, opponent, false);
		
		traverseLog += "root," + root.depth + "," + root.value;
		for(int r = 0; r < 8; r++){
			for(int c = 0; c < 8; c++){
				output += outputState[r][c];
			}
			output += "\n";
		}
		traverseLog = traverseLog.replaceAll("999999999", "Infinity");
		output += traverseLog;
		output = output.trim();
		String[] result = output.split("\n");
		
		FileWriter write = new FileWriter("output.txt");
		PrintWriter writer = new PrintWriter(write);
		for(int i = 0; i < result.length; i++){
			if(i == result.length - 1){
				writer.print(result[i]);
			} else{
				writer.println(result[i]);
			}
		}
		writer.close();
	}
	
	public FinalLegalNode maxValue(FinalLegalNode f, String player, String opponent, int weight[][], int cutOff, int problem, String orgPlayer, String orgOpponent, boolean noSolution){
		if(f.depth == cutOff){
			f.value = evaluation(f.nextState, weight, orgPlayer, orgOpponent);
			return f;
		}
		 
		ArrayList<Node> legalNodes = legalMoves(f.nextState, player, opponent, f, f.depth, 999999999, -999999999, 999999999);
		ArrayList<FinalLegalNode> finalLegalNodes = findFinalLegalNodes(f.nextState,legalNodes,weight, player, opponent, problem, true);
		
		String name = generateName(f.row, f.column);
		
		if(finalLegalNodes.isEmpty()){
			int rootValue = f.value;
			if(noSolution == true){
				f.value = evaluation(f.nextState, weight, orgPlayer, orgOpponent);
				f.terminate = true;
				return f;
			} else if(noSolution == false){
				String temp = traverseLog;
				traverseLog += name + "," + f.depth + "," + f.value + "\n";
				FinalLegalNode pass = new FinalLegalNode(f.nextState, -2, -2, f, f.depth + 1, 999999999, -f.alpha, f.beta);
				pass.value = Max(pass, minValue(pass, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, true));
				f.value = Max(f, pass);
				if(f.depth == 0){
					if(f.value > rootValue){
						for(int m = 0; m < pass.nextState.length; m++){
							outputState[m] = Arrays.copyOf(pass.nextState[m], pass.nextState[m].length);
						}
					}
				}
				if(pass.terminate == true){
					traverseLog = temp;
					//traverseLog += name + "," + f.depth + "," + f.value + "\n";
					f.terminate = true;
					return f;
				}
				String childName = generateName(pass.row, pass.column);
				traverseLog += childName + "," + pass.depth + "," + pass.value + "\n";
			}
			return f;
		}
		
		for(int i = 0; i < finalLegalNodes.size(); i++){
			int rootValue = f.value;
			traverseLog += name + "," + f.depth + "," + f.value + "\n";
			FinalLegalNode child = finalLegalNodes.get(i);
			child.value = Max(child, minValue(child, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, false));
			f.value = Max(f, child);
			if(f.depth == 0){
				if(f.value > rootValue){
					for(int m = 0; m < finalLegalNodes.get(i).nextState.length; m++){
						outputState[m] = Arrays.copyOf(finalLegalNodes.get(i).nextState[m], finalLegalNodes.get(i).nextState[m].length);
					}
				}
			}
			String childName = generateName(child.row, child.column);
			traverseLog += childName + "," + child.depth + "," + child.value + "\n";
		}
		return f;
	}
	
	public FinalLegalNode minValue(FinalLegalNode f, String player, String opponent, int weight[][], int cutOff, int problem, String orgPlayer, String orgOpponent, boolean noSolution){
		if(f.depth == cutOff){
			f.value = evaluation(f.nextState,  weight, orgPlayer, orgOpponent);
			return f;
		}
		
		ArrayList<Node> legalNodes = legalMoves(f.nextState, player, opponent, f, f.depth, -999999999, -999999999, 999999999);
		ArrayList<FinalLegalNode> finalLegalNodes = findFinalLegalNodes(f.nextState,legalNodes,weight, player, opponent, problem, true);
		String name = generateName(f.row, f.column);
		
		if(finalLegalNodes.isEmpty()){
			if(noSolution == true){
				f.value = evaluation(f.nextState, weight, orgPlayer, orgOpponent);
				f.terminate = true;
				return f;
			} else if(noSolution == false){
				String temp = traverseLog;
				traverseLog += name + "," + f.depth + "," + f.value + "\n";
				FinalLegalNode pass = new FinalLegalNode(f.nextState, -2, -2, f, f.depth + 1, -999999999, -f.alpha, f.beta);
				pass.value = Min(pass, maxValue(pass, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, true));
				f.value = Min(f, pass);
				
				if(pass.terminate == true){
					traverseLog = temp;
					return f;
				}
				
				String childName = generateName(pass.row, pass.column);
				traverseLog += childName + "," + pass.depth + "," + pass.value + "\n";
			}
		}
		
		for(int i = 0; i < finalLegalNodes.size(); i++){
			traverseLog += name + "," + f.depth + "," + f.value + "\n";
			FinalLegalNode child = finalLegalNodes.get(i);
			child.value = Min(child, maxValue(child, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, false));
			f.value = Min(f, child);
			String childName = generateName(child.row, child.column);
			traverseLog += childName + "," + child.depth + "," + child.value + "\n";
		}
		
		return f;
	}
	
	
	public void alphaBetaPruning(String reversi[][], String player, String opponent, int weight[][], int cutOff, int problem) throws IOException{
		traverseLog += "Node,Depth,Value,Alpha,Beta\n";
		FinalLegalNode root = new FinalLegalNode(reversi, -1, -1, null, 0, -999999999, -999999999, 999999999);
		int alpha = -999999999, beta = 999999999;
		root.value = alpha;
		alpha = maxValuePruning(root, player, opponent, weight, cutOff, problem, player, opponent, alpha, beta, false);
		root.value = alpha;
		
		for(int r = 0; r < 8; r++){
			for(int c = 0; c < 8; c++){
				output += outputState[r][c];
			}
			output += "\n";
		}
		
		traverseLog = traverseLog.replaceAll("999999999", "Infinity");
		output += traverseLog;
		output = output.trim();
		String[] result = output.split("\n");
		
		FileWriter write = new FileWriter("output.txt");
		PrintWriter writer = new PrintWriter(write);
		for(int i = 0; i < result.length; i++){
			if(i == result.length - 1){
				writer.print(result[i]);
			} else{
				writer.println(result[i]);
			}
		}
		writer.close();
	} 
	
	public int maxValuePruning(FinalLegalNode f,String player,String opponent,int weight[][],int cutOff,int problem,String orgPlayer,String orgOpponent, int newAlpha, int newBeta, boolean noSolution){
		int alpha = newAlpha;
		int beta = newBeta;
		String name = generateName(f.row, f.column);
		
		if(f.depth == cutOff){
			f.value = evaluation(f.nextState, weight, orgPlayer, orgOpponent);
			traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
			return f.value;
		}
		
		ArrayList<Node> legalNodes = legalMoves(f.nextState, player, opponent, f, f.depth, -999999999, -999999999, 999999999);
		ArrayList<FinalLegalNode> finalLegalNodes = findFinalLegalNodes(f.nextState,legalNodes,weight, player, opponent, problem, true);
		
		if(finalLegalNodes.isEmpty()){
			int rootValue = f.value;
			if(noSolution == true){
				f.value = evaluation(f.nextState, weight, orgPlayer, orgOpponent);
				f.terminate = true;
				return f.value;
			} else if(noSolution == false){
				String temp = traverseLog;
				traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
				
				FinalLegalNode pass = new FinalLegalNode(f.nextState, -2, -2, f, f.depth + 1, 999999999, -f.alpha, f.beta);
				f.value = MaxAlpha(f.value, minValuePruning(pass, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, alpha, beta, true));
				f.value = MaxAlpha(f.value, pass.value);
				
				if(f.depth == 0){
					if(f.value > rootValue){
						for(int m = 0; m < pass.nextState.length; m++){
							outputState[m] = Arrays.copyOf(pass.nextState[m], pass.nextState[m].length);
						}
					}
				}
				
				if(pass.terminate == true){
					traverseLog = temp;
					traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
					return f.value;
				}
				
				if(f.value >= beta){
					traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
					return f.value;
				}
				alpha = MaxAlpha(alpha, f.value);
			}
			traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
			return alpha;
		}
		
		for(int i = 0; i < finalLegalNodes.size(); i++){
			int rootValue = f.value;
			traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n"; 
			FinalLegalNode child = finalLegalNodes.get(i);
			child.value = 999999999;
			f.value = MaxAlpha(f.value, minValuePruning(child, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, alpha, beta, false));
			f.value = MaxAlpha(f.value, child.value);
			
			if(f.depth == 0){
				if(f.value > rootValue){
					for(int m = 0; m < finalLegalNodes.get(i).nextState.length; m++){
						outputState[m] = Arrays.copyOf(finalLegalNodes.get(i).nextState[m], finalLegalNodes.get(i).nextState[m].length);
					}
				}
			}
			
			if(f.value >= beta){
				traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
				return f.value;
			}
			alpha = MaxAlpha(alpha, f.value);
		}
		traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
		return alpha;
	}
	
	public int minValuePruning(FinalLegalNode f,String player,String opponent,int weight[][],int cutOff,int problem,String orgPlayer,String orgOpponent, int newAlpha, int newBeta, boolean noSolution){
		int alpha = newAlpha;
		int beta = newBeta;
		String name = generateName(f.row, f.column);
		
		if(f.depth == cutOff){
			f.value = evaluation(f.nextState, weight, orgPlayer, orgOpponent);
			traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
			return f.value;
		}
		
		ArrayList<Node> legalNodes = legalMoves(f.nextState, player, opponent, f, f.depth, -999999999, -999999999, 999999999);
		ArrayList<FinalLegalNode> finalLegalNodes = findFinalLegalNodes(f.nextState,legalNodes,weight, player, opponent, problem, true);
		
		if(finalLegalNodes.isEmpty()){
			if(noSolution == true){
				f.value = evaluation(f.nextState, weight, orgPlayer, orgOpponent);
				f.terminate = true;
				return f.value;
			} else if(noSolution == false){
				String temp = traverseLog;
				traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
				
				FinalLegalNode pass = new FinalLegalNode(f.nextState, -2, -2, f, f.depth + 1, -999999999, -f.alpha, f.beta);
				f.value = MinBeta(f.value, maxValuePruning(pass, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, alpha, beta, true));
				f.value = MinBeta(f.value, pass.value);
				
				if(pass.terminate == true){
					traverseLog = temp;
					traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
					return f.value;
				}
				
				if(f.value <= alpha){
					traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
					return f.value;
				}
				beta = MinBeta(beta, f.value);
			}
			traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
			return beta;
		}
		
		for(int i = 0; i < finalLegalNodes.size(); i++){
			FinalLegalNode child = finalLegalNodes.get(i);
			traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
			child.value = -999999999;
			f.value = MinBeta(f.value, maxValuePruning(child, opponent, player, weight, cutOff, problem, orgPlayer, orgOpponent, alpha, beta, false));
			f.value = MinBeta(f.value, child.value);
			
			if(f.value <= alpha){
				traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
				return f.value;
			}
			beta = MinBeta(beta, f.value);
		}
		traverseLog += name + "," + f.depth + "," + f.value + "," + alpha + "," + beta + "\n";
		return beta;
	}
	
	public int MinBeta(int a, int b){
		if(a <= b){
			return a;
		} else if(b <= a){
			return b;
		}
		return a;
	}
	
	public int MaxAlpha(int a, int b){
		if(a > b){
			return a;
		} else if(b > a){
			return b;
		}
		return a;
	}
	
	public int MaxAlpha(FinalLegalNode f1, FinalLegalNode f2){
		if(f1.alpha >= f2.value){
			return f1.alpha;
		} else if(f2.value >= f1.alpha){
			return f2.value;
		}
		return 0;
	}
	
	public int MinBeta(FinalLegalNode f1, FinalLegalNode f2){
		if(f1.beta <= f2.value){
			return f1.beta;
		} else if(f2.value <= f1.beta){
			return f2.value;
		}
		return 0;
	}
	
	public int Max(FinalLegalNode f1, FinalLegalNode f2){
		if(f1.value >= f2.value){
			return f1.value;
		} else if(f2.value >= f1.value){
			return f2.value;
		}
		return 0;
	}
	
	public int Min(FinalLegalNode f1, FinalLegalNode f2){
		if(f1.value <= f2.value){
			return f1.value;
		} else if(f2.value <= f1.value){
			return f2.value;
		}
		return 0;
	}
	
	public int evaluation(String nextState[][], int weight[][], String orgPlayer, String orgOpponent){
		int weightPlayer = 0;
		int weightOpponent = 0;
		String player = orgPlayer;
		String opponent = orgOpponent;
		for(int r = 0; r < 8; r++){
			for(int c = 0; c < 8; c++){
				if(nextState[r][c].equalsIgnoreCase(player)){
					weightPlayer += weight[r][c];
				} else if(nextState[r][c].equalsIgnoreCase(opponent)){
					weightOpponent += weight[r][c];
				}
			}
		}
		int totalWeight = weightPlayer - weightOpponent; 
		return totalWeight;
	}
	
	public String generateName(int row, int column){
		String name = "";
		if(row == -1 && column == -1){
			return "root";
		}
		if(row == -2 && column == -2){
			return "pass";
		}
		if(column == 0){
			name = "a";
		} else if(column == 1){
			name = "b";
		} else if(column == 2){
			name = "c";
		} else if(column == 3){
			name = "d";
		} else if(column == 4){
			name = "e";
		} else if(column == 5){
			name = "f";
		} else if(column == 6){
			name = "g";
		} else if(column == 7){
			name = "h";
		}
		name += row+1;
		
		return name;
	}
	
	public boolean isDepthEven(int depthCount){
		if(depthCount % 2 == 0){
			return true;
		}
		return false;
	}
	
	public boolean isDepthOdd(int depthCount){
		if(depthCount % 2 != 0){
			return true;
		}
		return false;
	}
	
	public ArrayList<FinalLegalNode> findFinalLegalNodes(String reversi[][],ArrayList<Node> legalNodes, int weight[][], String player,String opponent, int problem, boolean isEven){
		String nextPossibleState[][] = new String[reversi.length][];
		ArrayList<FinalLegalNode> finalLegalNodes = new ArrayList<FinalLegalNode>();
		
		for(int i = 0; i < legalNodes.size(); i++){
			for(int m = 0; m < reversi.length; m++){
				nextPossibleState[m] = Arrays.copyOf(reversi[m], reversi[m].length);
			}
			
			int newRow = legalNodes.get(i).row;
			int newCol = legalNodes.get(i).column;
			
			nextPossibleState[newRow][newCol] = player;
			newRow += legalNodes.get(i).rowChangeFactor;
			newCol += legalNodes.get(i).colChangeFactor;
			
			while(newRow <= 7 && newRow >= 0 && newCol <= 7 && newCol >= 0){
				if(nextPossibleState[newRow][newCol].equalsIgnoreCase(opponent)){
					nextPossibleState[newRow][newCol] = player;
					newRow += legalNodes.get(i).rowChangeFactor;
					newCol += legalNodes.get(i).colChangeFactor;
				} else {
					break;
				}
			}
			boolean flag = false;
			FinalLegalNode fnode = new FinalLegalNode(nextPossibleState, legalNodes.get(i).row, legalNodes.get(i).column, legalNodes.get(i).parent, legalNodes.get(i).depth, legalNodes.get(i).value, legalNodes.get(i).alpha, legalNodes.get(i).beta);
			if(finalLegalNodes.isEmpty()){
				finalLegalNodes.add(fnode);
			} else {
				for(int j = 0; j < finalLegalNodes.size(); j++){
					if(j == finalLegalNodes.size()){
						break;
					}
					if(finalLegalNodes.get(j).row == fnode.row && finalLegalNodes.get(j).column == fnode.column){
						flag = true;
						for(int r = 0; r < 8; r++){
							for(int c = 0; c < 8; c++){
								if((finalLegalNodes.get(j).nextState[r][c].equalsIgnoreCase(player) && fnode.nextState[r][c].equalsIgnoreCase(opponent))){
									finalLegalNodes.get(j).nextState[r][c] = player;
								} else if((finalLegalNodes.get(j).nextState[r][c].equalsIgnoreCase(opponent) && fnode.nextState[r][c].equalsIgnoreCase(player))){
									finalLegalNodes.get(j).nextState[r][c] = player;
								}
							}
						}
					}
					
				}
				if(flag == false){
					finalLegalNodes.add(fnode);
				}
			}
		}
		for(int i = 0; i < finalLegalNodes.size(); i++){
			int weightPlayer = 0;
			int weightOpponent = 0;
			
			for(int r = 0; r < 8; r++){
				for(int c = 0; c < 8; c++){
					if(finalLegalNodes.get(i).nextState[r][c].equalsIgnoreCase(player)){
						weightPlayer += weight[r][c];
					} else if(finalLegalNodes.get(i).nextState[r][c].equalsIgnoreCase(opponent)){
						weightOpponent += weight[r][c];
					}
				}
			}
			if(problem == 1){
				finalLegalNodes.get(i).value = weightPlayer - weightOpponent;
			}
		}
		return finalLegalNodes;
	}
	
	public ArrayList<Node> legalMoves(String reversi[][], String player, String opponent, FinalLegalNode parent, int depth,int value,int alpha, int beta){
		ArrayList<Node> legalNodes = new ArrayList<Node>();
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(reversi[i][j].equalsIgnoreCase("*")){
					for(int k = 0; k < 8; k++){
						if(k == 0){
							int rowChangeFactor = -1;
							int colChangeFactor = -1;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						} else if(k == 1){
							int rowChangeFactor = -1;
							int colChangeFactor = 0;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						} else if(k == 2){
							int rowChangeFactor = -1;
							int colChangeFactor = 1;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						} else if(k == 3){
							int rowChangeFactor = 0;
							int colChangeFactor = 1;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						} else if(k == 4){
							int rowChangeFactor = 1;
							int colChangeFactor = 1;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						} else if(k == 5){
							int rowChangeFactor = 1;
							int colChangeFactor = 0;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						} else if(k == 6){
							int rowChangeFactor = 1;
							int colChangeFactor = -1;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						} else if(k == 7){
							int rowChangeFactor = 0;
							int colChangeFactor = -1;
							if(findLegalSolution(player, opponent, i, j, reversi, rowChangeFactor, colChangeFactor)){
								Node node = new Node(i, j, parent, depth + 1, value, alpha, beta, rowChangeFactor, colChangeFactor);
								legalNodes.add(node);
							}
						}
					}
				}
			}
		}
		return legalNodes;
	}
	
	public boolean findLegalSolution(String player, String opponent, int row, int column, String reversi[][], int rowChangeFactor, int colChangeFactor){
		int newRow = row, newCol = column;
		newRow = row + rowChangeFactor;
		newCol = column + colChangeFactor;
		
		if(!((newRow < 0) || (newRow > 7) || (newCol < 0) || (newCol > 7))){
			if(reversi[newRow][newCol].equalsIgnoreCase(opponent)){

				newRow += rowChangeFactor;
				newCol += colChangeFactor;
				while(newRow <= 7 && newRow >= 0 && newCol <= 7 && newCol >= 0){
					if(reversi[newRow][newCol].equalsIgnoreCase("*")){
						break;
					} else if(reversi[newRow][newCol].equalsIgnoreCase(player)){
						return true;
					} else if(reversi[newRow][newCol].equalsIgnoreCase(opponent)){
						newRow += rowChangeFactor;
						newCol += colChangeFactor;
					}
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String inputFile = "input.txt";
		String line = null;
		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int problem = 0;
		int countOfLines = 1;
		String player = "";
		int cutOff = 0;
		String reversi[][] = new String[8][8];
		while((line = bufferedReader.readLine()) != null){
			if(countOfLines == 1){
				problem = Integer.parseInt(line);
			} else if(countOfLines == 2){
				player = line;
			} else if(countOfLines == 3){
				cutOff = Integer.parseInt(line);
			} else {
				for(int i = 0; i < 8; i++){
					reversi[countOfLines - 4][i] = line.substring(i, i+1);
				}
			}
			countOfLines++;
		}
		
		int weight[][] = new int[8][8];
		weight[0][0] = 99; weight[0][1] = -8; weight[0][2] = 8; weight[0][3] = 6; weight[0][4] = 6; weight[0][5] = 8; weight[0][6] = -8; weight[0][7] = 99;
		weight[1][0] = -8; weight[1][1] = -24; weight[1][2] = -4; weight[1][3] = -3; weight[1][4] = -3; weight[1][5] = -4; weight[1][6] = -24; weight[1][7] = -8;
		weight[2][0] = 8; weight[2][1] = -4; weight[2][2] = 7; weight[2][3] = 4; weight[2][4] = 4; weight[2][5] = 7; weight[2][6] = -4; weight[2][7] = 8;
		weight[3][0] = 6; weight[3][1] = -3; weight[3][2] = 4; weight[3][3] = 0; weight[3][4] = 0; weight[3][5] = 4; weight[3][6] = -3; weight[3][7] = 6;
		weight[4][0] = 6; weight[4][1] = -3; weight[4][2] = 4; weight[4][3] = 0; weight[4][4] = 0; weight[4][5] = 4; weight[4][6] = -3; weight[4][7] = 6;
		weight[5][0] = 8; weight[5][1] = -4; weight[5][2] = 7; weight[5][3] = 4; weight[5][4] = 4; weight[5][5] = 7; weight[5][6] = -4; weight[5][7] = 8;
		weight[6][0] = -8; weight[6][1] = -24; weight[6][2] = -4; weight[6][3] = -3; weight[6][4] = -3; weight[6][5] = -4; weight[6][6] = -24; weight[6][7] = -8;
		weight[7][0] = 99; weight[7][1] = -8; weight[7][2] = 8; weight[7][3] = 6; weight[7][4] = 6; weight[7][5] = 8; weight[7][6] = -8; weight[7][7] = 99;
		
		String opponent = "";
		if(player.equalsIgnoreCase("X")){
			opponent = "O";
		} else if(player.equalsIgnoreCase("O")){
			opponent = "X";
		}
		agent a = new agent();
		
		if(problem == 1){
			a.greedy(reversi, player, opponent, weight, problem);
		} else if(problem == 2){
			a.miniMax(reversi, player, opponent, weight, cutOff, problem);
		} else if(problem == 3){
			a.alphaBetaPruning(reversi, player, opponent, weight, cutOff, problem);
		}
	}
}
