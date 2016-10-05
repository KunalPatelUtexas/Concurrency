package MyLocks;
import java.util.concurrent.locks.Lock;


public abstract class Bakery implements Lock { 
	int N;
	boolean [] choosing ; // inside doorway
	int [] number ;

	public Bakery (int numProc) { 
		N = numProc;
		choosing = new boolean [N];
		number = new int [N];
		for(int j = 0; j < N; j++) {
			choosing[j] = false;
			number[j] = 0;
		}
	}
	
	public void requestCS ( int i ) {
		// step 1: doorway : choose a number
		// Find max number and give this process the value max + 1
		choosing [i] = true;
		for (int j = 0; j < N; j++){
			if(number[j] > number[i]){
				number[i] = number[j];
			}
		}
		number[i]++;
		choosing[i] = false;
		// step 2: check if my number is the smallest
		for(int j = 0; j < N; j++) {
			// process j in doorway, wait for it to exit
			while(choosing[j]); 
			/* wait until:
			 1. other processes have number 0. This means they aren't in line to access the CS. If they want to access
			    the CS after process i has already scanned past them, they will get a number bigger than process i's
			    and get in line behind it
			 2. other processes have a number greater than process i's. That means they are in line behind this process to
			    access the CS.
			*/
			while ((number[j] != 0) && (( number[j] < number[i]) || ((number[j] == number[i]) && j < i )));
		}
	}
	
	public void releaseCS ( int i ) { // exit protocol
		number[i] = 0 ;
	}

}
