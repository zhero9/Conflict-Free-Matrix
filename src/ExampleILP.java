import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;


public class ExampleILP {
	public static void solveMe(){
		try{
			 IloCplex cplex = new IloCplex();
			
			 // variables
			 IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x");
			 IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y");
			 
			 // expresions
			 
			 IloLinearNumExpr objective = cplex.linearNumExpr();
			 objective.addTerm(0.12, x);
			 objective.addTerm(0.15, y);
			 
			 // define objective
			 cplex.addMinimize(objective);
			 
			 // define constraints
			 List<IloRange> constraints = new ArrayList<IloRange>();
			 constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x),cplex.prod(60, y)),300));
			 constraints.add(cplex.addGe(cplex.sum(cplex.prod(12, x),cplex.prod(6, y)),36));
			 constraints.add(cplex.addGe(cplex.sum(cplex.prod(10, x),cplex.prod(30, y)),90));
			 IloLinearNumExpr num_expr = cplex.linearNumExpr();
			 num_expr.addTerm(2, x);
			 num_expr.addTerm(-1, y);
			 constraints.add(cplex.addEq(num_expr, 0));
			 num_expr = cplex.linearNumExpr();
			 num_expr.addTerm(1, y);
			 num_expr.addTerm(-1, x);
			 constraints.add(cplex.addLe(num_expr,8));
			 
			 
			 // solve
			 
			 if (cplex.solve()) {
				 System.out.println("Objective = "+cplex.getObjValue());
				 System.out.println("x :=" + cplex.getValue(x));
				 System.out.println("y :=" + cplex.getValue(y));
				 for (int i = 0; i< constraints.size(); i++){
					 System.out.println("Dual constraint "+(i+1)+" = " + cplex.getDual(constraints.get(i)));
					 System.out.println("Slack constraint "+(i+1)+" = " + cplex.getSlack(constraints.get(i)));
				 }
			 }else {
				 System.out.println("Optimal solution not found");
			 }
			 
			 cplex.end();
		} 
		catch (IloException exc) {
			System.err.println("Concert exception '" + exc + "' caught");
		}
	}
}
