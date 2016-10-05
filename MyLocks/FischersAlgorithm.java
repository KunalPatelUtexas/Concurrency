package MyLocks;

import java.util.concurrent.locks.Lock;

public abstract class FischersAlgorithm implements Lock { 
	int N;
	int turn;
	int delta ;

	public FischersAlgorithm(int numProc) {
		N = numProc;
		turn = -1;	//turn = -1 when nobody is in the CS
		delta = 5;
	}
	
	public void requestCS ( int i ) {
		while (true) {
			while ( turn != -1); // wait for the door to open, somebody is in the CS
			turn = i ; // write my id on turn
			try { // Assume that delta is bigger than time to update turn
				Thread.sleep(delta);	// this delay makes the thread wait to check turn before returning
			}
			catch ( InterruptedException e ){};
			if ( turn == i ) return;
		}
	}
	
	public void releaseCS(int i){ 
		turn = -1;
	}

}
