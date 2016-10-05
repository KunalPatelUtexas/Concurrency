package MyLocks;

import java.util.concurrent.atomic.AtomicInteger;

//pseudo-code for Anderson Lock
public class AndersonLock {
	
	AtomicInteger tailSlot = new AtomicInteger(0);
	boolean[] Available;
	ThreadLocal<Integer> mySlot ; // initialize to 0
	int NumberOfProcesses;

	public AndersonLock (int numProc) { // constructor
		// all Available false except Available[0]
		NumberOfProcesses = numProc;
		Available = new boolean[numProc];
		mySlot = new ThreadLocal<Integer>();
		mySlot.set(0);
		for(int i = 1; i < numProc; i++){
			Available[i] = false;
		}
		
	}
	
	public void lock(){ 
		// place myself in the circular array and increment the tail
		mySlot.set(tailSlot.getAndIncrement() % NumberOfProcesses);
		// wait until the slot-1 gets unlocked, it will set me to true and i will exit the busy wait
		while(!Available[mySlot.get()]);
	}
	
	public void unlock(){
		// I am done with the CS
		Available[mySlot.get()] = false;
		// Let the next person in the circular lock have a turn into the CS
		Available[(mySlot.get()+1) % NumberOfProcesses] = true;

	}
	


}