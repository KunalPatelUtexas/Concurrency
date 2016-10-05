package MyLocks;

import java.util.concurrent.locks.Lock;

public abstract class PetersonAlgorithm implements Lock{
	int N;
	int [] gate;
	int [] last;
	
	public PetersonAlgorithm(int NumberOfProcesses){
		N = NumberOfProcesses;
		gate = new int[N];
		last = new int[N];
		for (int j = 0; j < N; j++){
			gate[j] = 0;
			last[j] = 0;
		}
	}
	
	public void requestCS(int i){
		for ( int k = 1; k < N; k++) { 
			gate [i] = k ;
			last[k] = i ;
			for (int j = 0; j < N; j++) { 
				// busy wait
				while ((j != i) && // there is some other process
						(gate[j] >= k) && // that is ahead or at the same level
						(last[k] == i )); // and I am the last to update last[k]
						 					
			}
		}
	}
	
	public void releaseCS(int i){
		gate[i] = 0;
	}

}
