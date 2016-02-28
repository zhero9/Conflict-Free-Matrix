import java.util.Random;


public class LocalSearch {

	static boolean[][] matrix;
	static Poset D;
	static int m,n;
	Digraph branching;

	public LocalSearch(boolean [][] M, Digraph b){
		matrix = M;
		D = new Poset(M);
		m = M.length; // # of rows
		n = M[0].length;  // # of columns
		this.branching = b;
		improve();
	}

	public LocalSearch(boolean [][] M){
		matrix = M;
		D = new Poset(M);
		m = M.length; // # of rows
		n = M[0].length;  // # of columns
		this.branching = new Digraph(n);
		randomMaxBranching();
		improve();
	}

	public void improve(){
		//branching.printAdjList();
		for(int i = 0; i<n; i++){
			if( branching.outEdges.elementAt(i).size() > 0){
				for(int jj = 0;jj < D.d.outEdges.elementAt(i).size(); jj++){
					int s1 = branching.outEdges.elementAt(i).elementAt(0);
					int s2 = D.d.outEdges.elementAt(i).elementAt(jj);
					if(coverageInB(i, s1) < coverageInB(i, s2)	&& (s1 != s2)){
						branching.deleteOutNeighbours(i);
						branching.addEdge(i, s2);
						System.out.println("Changed "+i+","+s1+" with "+i+","+s2);
						i = 0;
					}
				}
			} 
		}
	}

	public short coverageInB(int s, int t){
		boolean[] tmp = new boolean[m];
		for(int j = 0; j< m; j++ ){
			tmp[j] = matrix[j][t];
		}
		for(int j = 0; j < branching.inEdges.elementAt(t).size(); j++){
			if (!branching.inEdges.elementAt(t).elementAt(j).equals(new Integer(s))){
				for(int k = 0; k < m; k++){
					tmp[k] = tmp[k] && !matrix[k][branching.inEdges.elementAt(t).elementAt(j)]; //
				}
			}
		}

		short count = 0;
		for(int j = 0; j<m ; j++){
			if(tmp[j]) count++;
		}
		return count;
	}

	public void randomMaxBranching(){
		Random gen = new Random();
		for(int i = 0; i<n ; i++){
			if(D.d.outEdges.elementAt(i).size()> 0){
				int j = gen.nextInt(D.d.outEdges.elementAt(i).size());
				branching.addEdge(i, D.d.outEdges.elementAt(i).elementAt(j));
			}
		}
	}
}
