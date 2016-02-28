import java.util.List;
import java.util.ArrayList;
import ilog.cplex.*;
import ilog.concert.*;


public class ILP {

	static boolean[][] matrix;
	static Poset D;
	static int m,n;

	public ILP(boolean [][] M){
		matrix = M;
		D = new Poset(M);
		m = M.length; // # of rows
		n = M[0].length;  // # of columns
	}

	public int solveOpt(){
		try{
			IloCplex cplex = new IloCplex();

			// variables
			IloNumVar[][] x = new IloNumVar[m][];
			for(int i = 0; i < m; i++)
				x[i] = cplex.boolVarArray(n);

			IloNumVar[][] y = new IloNumVar[n][];
			for(int i = 0; i< n; i++)
				y[i] = cplex.boolVarArray(D.d.outEdges.elementAt(i).size());

			// objective
			IloLinearNumExpr objective = cplex.linearNumExpr();

			for (int i = 0; i< m; i++){
				for(int j = 0; j< n; j++){
					if (matrix[i][j]){
						objective.addTerm(1, x[i][j]); //checked
						//System.out.print("+x"+i+","+j);
					}
				}
			}

			// define objective
			cplex.addMaximize(objective);

			// define constraints
			List<IloRange> constraints = new ArrayList<IloRange>();
			IloLinearNumExpr num_expr = cplex.linearNumExpr();
			for(int s = 0; s< n; s++){ 										// for all s \in V
				if (D.d.outEdges.elementAt(s).size() > 0){ 						// N+(s) != emptySet
					num_expr = cplex.linearNumExpr(); 							// clear old num_expr
					for(int t = 0; t< D.d.outEdges.elementAt(s).size(); t++){  // sum_{t \in N+(s)}
						num_expr.addTerm(1, y[s][t]); 
						//System.out.println(s+","+t);
					}
					//System.out.println(num_expr.toString());
					constraints.add(cplex.addEq(num_expr, 1));		 			// first constraint
				}
			}

			for (int r = 0;  r  < m ; r++){
				for(int s = 0; s < n ; s++){
					if(matrix[r][s]){
						num_expr = cplex.linearNumExpr();
						num_expr.addTerm(1, x[r][s]);
						//System.out.print("x"+r+","+s);
						for(int t = 0; t < n; t++){
							for(int index_s = 0; index_s < D.d.outEdges.elementAt(t).size(); index_s++){
								if (D.d.outEdges.elementAt(t).elementAt(index_s).equals((new Integer(s))) && matrix[r][t]){ 
									num_expr.addTerm(-1, y[t][index_s]);
									//System.out.print("-y"+t+","+index_s);
								}
							}
						}
						constraints.add(cplex.addLe(num_expr, 0));
						//System.out.println();
					}
				}
			}

			cplex.setOut(null);

			int value = -1;
			if(cplex.solve()){	 
				value = (int) (numberOfOnes(matrix)-cplex.getObjValue());
				//System.out.println("Objective ILP = "+value);
				//System.out.println(cplex.getObjValue());
				//for(int i = 0; i < m; i++){}
					//System.out.println("x["+i+"] = "+cplex.getValues(x[i]));
				//for(int i = 0; i< n; i++){
					//for(int j = 0; j< D.d.outEdges.elementAt(i).size(); j++){
						//System.out.print(" y["+i+","+D.d.outEdges.elementAt(i).elementAt(j)+"] = " +cplex.getValues(y[i])[j]);
					//}
					//System.out.println();
				//}
					
				
			} else {
				System.out.println("Optimal solution not found");
			}
			return value;

		}
		catch (IloException exc) {
			System.err.println("Concert exception '" + exc + "' caught");
			return -1;
		}
	}

	public static int numberOfOnes(boolean[][] m){
		int i = 0;
		for(int j = 0; j< m.length; j++){
			for(int k = 0; k < m[0].length; k++)
				if(m[j][k]) i++;
		}
		return i;
	}

}
