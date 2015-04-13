import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class agent {
	public static ArrayList<String> facts = new ArrayList<String>();
	public static ArrayList<String> implications = new ArrayList<String>();
	public static ArrayList<String> newKB = new ArrayList<String>();
	public static ArrayList<String> knowledgeBase = new ArrayList<String>();
	public static ArrayList<String> backTrack = new ArrayList<String>();
	
	public void backwardChaining(StringBuffer query) throws IOException{
		for(String clause : knowledgeBase){
			if(clause.contains("=>")){
				implications.add(clause);
			} else {
				facts.add(clause);
			}
		}
		
		boolean value = backwardChainingOR(query.toString());
		FileWriter write = new FileWriter("output.txt");
		PrintWriter writer = new PrintWriter(write);
		writer.print(String.valueOf(value).toUpperCase());
		writer.close();
	}
	
	public boolean backwardChainingOR(String query){
		boolean value = false;
		String queryName = query.substring(0, query.indexOf("("));
		String parameters = query.substring(query.indexOf("(") + 1, query.indexOf(")"));
		
		if(facts.contains(query)){
			return true;
		} else {	//is an implication
			for(String clause : knowledgeBase){
				String lhs = "";
				String rhs = "";
				if(clause.contains("=>")){
					String[] splitClause = new String[2];
					splitClause = clause.split("=>");
					lhs = splitClause[0];
					rhs = splitClause[1];
				} else {
					rhs = clause;
				}
				if(rhs.contains(queryName)){
					String rhsParameters = rhs.substring(rhs.indexOf("(") + 1, rhs.indexOf(")"));
					String newClause = unification(parameters, rhsParameters, clause, query);
					if(!newClause.equals("")){
						newKB.add(0, newClause);
						value = backwardChainingAND(newClause);
						if(value == true){
							return value;
						}
					} else if(newClause.equals("")){
						value = false;
					}
				}
			}
		}
		return value;
	}
	
	public String unification(String parameters, String rhsParameters, String clause, String query){
		if(rhsParameters.contains(",") && parameters.contains(",")){	//2 parameters
			String[] parameter = new String[2];
			parameter = parameters.split(",");
			String[] rhsParameter = new String[2];
			rhsParameter = rhsParameters.split(",");
			String replaceBy1 = "", replaceBy2 = "", replace1 = "", replace2 = "";
			if((isConstant(parameter[0]) && isConstant(rhsParameter[0])) && (!(isConstant(parameter[1]) && isConstant(rhsParameter[1])))){	//1st parameters are constants and 2nd are not
				if(parameter[0].equals(rhsParameter[0])){
					String change = "";
					if(isConstant(parameter[1]) && (!isConstant(rhsParameter[1]))){
						replaceBy1 = parameter[1];
						replace1 = rhsParameter[1];
						change = clause;
					} else if((!isConstant(parameter[1])) && isConstant(rhsParameter[1])){
						replaceBy1 = rhsParameter[1];
						replace1 = parameter[1];
						change = query;
					}
					String newClause = ""; 
					if(change.equals(query)){
						newClause = query.replaceAll(replace1, replaceBy1);
					} else if(change.equals(clause)){
						newClause = clause.replaceAll(replace1, replaceBy1);
					}
					return newClause;
				}
			} else if((isConstant(parameter[1]) && isConstant(rhsParameter[1])) && (!(isConstant(parameter[0]) && isConstant(rhsParameter[0])))){	//2nd parameters constant and 1st are not
				if(parameter[1].equals(rhsParameter[1])){
					String change = "";
					if(isConstant(parameter[0]) && (!isConstant(rhsParameter[0]))){
						replaceBy1 = parameter[0];
						replace1 = rhsParameter[0];
						change = clause;
					} else if((!isConstant(parameter[0])) && isConstant(rhsParameter[0])){
						replaceBy1 = rhsParameter[0];
						replace1 = parameter[0];
						change = query;
					}
					String newClause = "";
					if(change.equals(query)){
						newClause = query.replaceAll(replace1, replaceBy1);
					} else if(change.equals(clause)){
						newClause = clause.replaceAll(replace1, replaceBy1);
					}
					return newClause;
				}
			} else if((!isConstant(parameter[0]) && !isConstant(rhsParameter[0])) || (!isConstant(parameter[1]) && !isConstant(rhsParameter[1]))){
				if((!isConstant(parameter[0]) && !isConstant(rhsParameter[0])) && (!(!isConstant(parameter[1]) && !isConstant(rhsParameter[1])))){
					replaceBy1 = parameter[0];
					replace1 = rhsParameter[0];
					String newClause = clause.replaceAll(replace1, replaceBy1);
					if(isConstant(parameter[1]) && (!isConstant(rhsParameter[1]))){
						replaceBy2 = parameter[1];
						replace2 = rhsParameter[1];
					} else if(isConstant(rhsParameter[1]) && (!isConstant(parameter[1]))){
						replaceBy2 = rhsParameter[1];
						replace2 = parameter[1];
					} else if(isConstant(rhsParameter[1]) && isConstant(parameter[1])){
						return "";
					}
					newClause = newClause.replaceAll(replace2, replaceBy2);
					return newClause;
				} else if((!isConstant(parameter[1]) && !isConstant(rhsParameter[1])) && (!(!isConstant(parameter[0]) && !isConstant(rhsParameter[0])))){
					replaceBy1 = parameter[1];
					replace1 = rhsParameter[1];
					String newClause = clause.replaceAll(replace1, replaceBy1);
					if(isConstant(parameter[0]) && (!isConstant(rhsParameter[0]))){
						replaceBy2 = parameter[0];
						replace2 = rhsParameter[0];
					} else if(isConstant(rhsParameter[0]) && (!isConstant(parameter[0]))){
						replaceBy2 = rhsParameter[0];
						replace2 = parameter[0];
					} else if(isConstant(rhsParameter[0]) && isConstant(parameter[0])){
						return "";
					}
					newClause = newClause.replaceAll(replace2, replaceBy2);
					return newClause;
				} else if((!isConstant(parameter[0]) && !isConstant(rhsParameter[0])) && (!isConstant(parameter[1]) && !isConstant(rhsParameter[1]))){
					replaceBy1 = parameter[0];
					replace1 = rhsParameter[0];
					replaceBy2 = parameter[1];
					replace2 = rhsParameter[1];
					String newClause = clause.replaceAll(replace1, replaceBy1);
					newClause = newClause.replaceAll(replace2, replaceBy2);
					return newClause;
				}
			} else if((isConstant(parameter[0]) && isConstant(rhsParameter[0])) && (isConstant(parameter[1]) && isConstant(rhsParameter[1]))){
				if(parameter[0].equals(rhsParameter[0]) && parameter[1].equals(rhsParameter[1])){
					return clause;
				} else {
					return "";
				}
			} else {
				//Generalized unification starts here
				if(isConstant(parameter[0]) && (!isConstant(rhsParameter[0]))){
					replaceBy1 = parameter[0];
					replace1 = rhsParameter[0];
				} else if(isConstant(rhsParameter[0]) && (!isConstant(parameter[0]))){
					replaceBy1 = rhsParameter[0];
					replace1 = parameter[0];
				}
				if(isConstant(parameter[1]) && (!isConstant(rhsParameter[1]))){
					replaceBy2 = parameter[1];
					replace2 = rhsParameter[1];
				} else if(isConstant(rhsParameter[1]) && (!isConstant(parameter[1]))){
					replaceBy2 = rhsParameter[1];
					replace2 = parameter[1];
				}
				String newClause = clause.replaceAll(replace1, replaceBy1);
				newClause = newClause.replaceAll(replace2, replaceBy2);
				return newClause;
				//Generalized unification ends here
			}
		} else if((!parameters.contains(",")) && (!rhsParameters.contains(","))){	//Only 1 parameter
			String replace = "", replaceBy = "";
			if(isConstant(parameters) && (!isConstant(rhsParameters))){
				replaceBy = parameters;
				replace = rhsParameters;
			} else if(isConstant(rhsParameters) && (!isConstant(parameters))){
				replaceBy = rhsParameters;
				replace = parameters;
			} else if(!isConstant(parameters) && !isConstant(rhsParameters)){
				replaceBy = parameters;
				replace = rhsParameters;
			}
			String newClause = clause.replaceAll(replace, replaceBy);
			return newClause;
		}
		return "";
	}
	
	public boolean isConstant(String check){
		if(!(check.equals("x") || check.equals("y") || check.equals("z"))){
			return true;
		}
		return false;
	}
	
	public boolean backwardChainingAND(String newQuery){
		boolean value = true;
		String[] splitClause = new String[2];
		splitClause = newQuery.split("=>");
		String lhs = splitClause[0];
		
		String premises[] = lhs.split("&");
		
		boolean values[] = new boolean[premises.length];
		for(int i = 0; i < values.length; i++){
			values[i] = false;
		}
		for(int i = 0; i < premises.length; i++){
			values[i] = backwardChainingOR(premises[i]);
			value = value & values[i];
			if(value == false){
				return value;
			}
		}
		return value;
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String inputFile = "input.txt";
		String line = null;
		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer query = null ;
		int NO_OF_LINES = 0;
		int countOfLines = 1;
		
		while((line = bufferedReader.readLine()) != null){
			if(countOfLines == 1){
				query = new StringBuffer(line);
			} else if(countOfLines == 2){
				NO_OF_LINES = Integer.parseInt(line);
			} else if(countOfLines >= 3){
				knowledgeBase.add(line);
			}
			countOfLines++;
		}
		
		agent a = new agent();
		a.backwardChaining(query);
	}
}
