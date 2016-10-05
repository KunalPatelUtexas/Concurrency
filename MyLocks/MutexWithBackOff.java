package MyLocks;

import java.util.concurrent.atomic.*;

public class MutexWithBackOff{ 
	AtomicBoolean isOccupied = new AtomicBoolean (false);
	public void lock (){
		while(true){
			while(isOccupied.get()){}
			if (!isOccupied.getAndSet(true)){
				return;
			} else {
				int timeToSleep = calculateDuration();
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private int calculateDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void unlock(){
		isOccupied.set(false); 
	}
}