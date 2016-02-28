import java.util.Vector;

public class Main {
	
	public static boolean[][] gen (int k){
		boolean[][] matrix = new boolean[k][k-1+k-2];
		
		for(int i = 1; i < k; i++){
			for(int j = 0; j< k-2; j++){
				if(i+j < k){
					matrix[i][j] = true;
				}
			}
		}
		
		for(int i = 0; i < k; i++){
			for(int j = k-2; j < 2*k-3; j++){
				if(i+j <= 2*k-2){
					matrix[i][j] = true;
				}
			}
		}
		return matrix;
	}
	

	public static void main(String[] args){
		RandMatrix generator = new RandMatrix(4);
		
		boolean[][] test; // = {{false,true,true,false},{true,true,true,true},{false,true,false,true}};
		Vector<Integer> badExamplesSq = new Vector<Integer>();
		Vector<Integer> badExamplesLS = new Vector<Integer>();

		for(int i = 0; i < 1000; i++){
			//test = generator.makeMatrix(i+17000);
			//test = generator.completePoset(i+1);
			test = generator.randMatrix(5,22);
			
			ILP lp = new ILP(test);
			double opt = lp.solveOpt();
			
			Greedy greedy = new Greedy(test);
			Digraph greedyBranching = greedy.solve();
			double greedyVal = Greedy.evaluateBranching(greedyBranching);
			
			SequentialGreedy sequential = new SequentialGreedy(test);
			Digraph seqBranching = sequential.solve();
			int seqVal = sequential.value();
			seqBranching.printAdjList();
			System.out.println("Factor SG    = "+ (seqVal/opt));
			
			LocalSearch local = new LocalSearch(test,seqBranching);
			double localVal = Greedy.evaluateBranching(local.branching);
			local.branching.printAdjList();
			System.out.println("Factor local = "+ (localVal/opt));
			System.out.println();
			/*
			if((seqVal/opt) >= 1.2 ){
				badExamplesSq.add(i);
				System.out.println("added "+i+" in SQ");
			}
			
			if((localVal/opt) >= 1.1 ){
				badExamplesLS.add(i);
				System.out.println("added "+i+" in LS");
			}
							RandMatrix.printMatrix(test);
				//System.out.println("Solution for complete poset on "+(i+1)+" elemets");
				//System.out.println("Solution by ILP          : " + opt);
				//System.out.println("Solution by greedy       : " + greedyVal);
				//System.out.println("Solution by SG           : " + seqVal);
				//System.out.println("Solution by Local Search : " + localVal);
				seqBranching.printAdjList();
				//greedy.D.printHight();
				System.out.println("Factor SG    = "+ (seqVal/opt));
				local.branching.printAdjList();
				System.out.println("Factor local = "+ (localVal/opt));
				//System.out.println("Factor greedy= "+ (greedyVal/opt));
				System.out.println();
			
			*/
			
			double trashold = 1.2;
			if ((seqVal == localVal) && (localVal/opt) >= 1.1){
				System.out.println("Break");
				break;
				
			} 
		}	
	}// ************************************ END OF MAIN ****************************************************
} 
