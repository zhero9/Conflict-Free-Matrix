import java.util.Arrays;


public class Greedy {

	static boolean[][] matrix;
	Poset D;
	static int m,n;

	public Greedy(boolean [][] M){
		matrix = M;
		D = new Poset(M);
		m = M.length; // # of rows
		n = M[0].length;  // # of columns
	}

	public Digraph solve(){
		Digraph branching = new Digraph(n);
		for (int t = 0; t < n; t++){
			for(int j  = 0; j < D.d.inEdges.elementAt(t).size(); j++){
				int s = D.d.inEdges.elementAt(t).elementAt(j); // We have edge s->t ;
				D.d.setWeight(s, t, weightOfEdge(s,t));
			}
		}

		for(int s = 0; s< n; s++){
			if(D.d.outEdges.elementAt(s).size() > 0){
				branching.addEdge(s, maxWeightIndex(s));
			}
		}

		return branching;
	}

	public short weightOfEdge (int s,int t){
		boolean[] tmp = new boolean[m];
		for(int j = 0; j< m; j++ ){
			tmp[j] = matrix[j][t];
		}
		for(int j = 0; j < D.d.inEdges.elementAt(t).size(); j++){
			if (!D.d.inEdges.elementAt(t).elementAt(j).equals(new Integer(s))){
				for(int k = 0; k < m; k++){
					tmp[k] = tmp[k] && !matrix[k][D.d.inEdges.elementAt(t).elementAt(j)]; //
				}
			}
		}

		short count = 0;
		for(int j = 0; j<m ; j++){
			if(tmp[j]) count++;
		}

		return count;
	}

	public int maxWeightIndex(int s){
		int i= -1;
		short max = -1;
		for (int j = 0; j< n; j++){
			if(D.d.adjMatrix[s][j] > max){
				max = D.d.adjMatrix[s][j];
				i = j;
			} else if(D.d.adjMatrix[s][j] == max && i>-1 && sizeOfSet(i) > sizeOfSet(j)){
				i = j;
			}
		}
		return i;
	}

	public static int evaluateBranching(Digraph b){
		int[] uncovered = new int[n];
		for(int i = 0; i< n; i++){
			if(b.inEdges.elementAt(i).size() == 0){
				uncovered[i] = sizeOfSet(i);
			}else{
				uncovered[i] = sizeOfSet(i) - sizeOfSet(disjunctionOfInEdges(i,b));
				//System.out.println(" "+sizeOfSet(disjunctionOfInEdges(i,b)));
			}

		}
		return Arrays.stream(uncovered).sum();
	}

	public static boolean[] disjunctionOfInEdges (int s, Digraph b){
		boolean[] tmp = new boolean[m];
		for(int j = 0; j < b.inEdges.elementAt(s).size(); j++){
			for(int k = 0; k < m; k++){
				tmp[k] = tmp[k] || matrix[k][b.inEdges.elementAt(s).elementAt(j)];
			}
		}
		return tmp;
	}

	public static int sizeOfSet(int s){
		int size = 0;
		for(int i =0; i< m; i++){
			if(matrix[i][s]) size++;
		}
		return size;
	}

	public static int sizeOfSet(boolean [] s){
		int size = 0;
		for(int i = 0; i< s.length; i++){
			if(s[i]) size++;
		}
		return size;
	}
}
