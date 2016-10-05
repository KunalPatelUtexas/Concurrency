package MyLocks;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public abstract class CLHLock implements Lock {
	class Node {
		boolean locked;
	}
	
	AtomicReference<Node> tailNode;
	ThreadLocal<Node> myNode;
	ThreadLocal<Node> pred;

	public CLHLock(){
		tailNode = new AtomicReference<Node>(new Node());
		tailNode.get().locked = false;
		myNode = new ThreadLocal<Node>(){
			protected Node initialValue(){
				return new Node();
			}
		};
		pred = new ThreadLocal<Node>();
	}
	
	public void lock(){
		// I want to access the CS so lock
		myNode.get().locked = true;
		// Attach myself to the end of the linked list and make myself the tailNode
		pred.set(tailNode.getAndSet(myNode.get()));
		// wait until the node ahead of me finishes with the CS
		while(pred.get().locked){
			Thread.yield();			// while waiting, reduce the thread's CPU usage
		}
	}
	
	public void unlock(){
		// I am done so unlock myself
		myNode.get().locked = false;
		/* If I dont do this, the next lock may spin if I make another request. I make myself my predecessor so 
		   I can access the CS again in the future */
		myNode.set(pred.get()); // reusing predecessor node for future use
	}
}
