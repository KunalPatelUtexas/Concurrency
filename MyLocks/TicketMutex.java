package MyLocks;

import java.util.concurrent.atomic.*;

public class TicketMutex {
	AtomicInteger nextTicket = new AtomicInteger(0);
	AtomicInteger currentTicket = new AtomicInteger(0);
	
	public void lock(){ 
		int myticket = nextTicket.getAndIncrement();
		while(myticket != currentTicket.get()); // s k i p ( ) ;
		
	}
	
	public void unlock(){
		int temp = currentTicket.getAndIncrement();

	}
}