package MyLocks;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public abstract class MCSLock implements Lock{

	class QNode {
		boolean locked;
		QNode next;
		QNode(){
			locked = true;
			next = null;
		}
	}
	
	AtomicReference<QNode> tailNode = new AtomicReference<QNode>(null);
	ThreadLocal<QNode> myNode;
	
	public MCSLock(){
		myNode = new ThreadLocal<QNode>(){
			protected QNode initialValue(){
				return new QNode();
			}
		};
	}
	
	public void lock(){
		// I want the CS so make myself the tailNode
		QNode pred = tailNode.getAndSet(myNode.get());
		// Add myself to the list of processes wanting CS
		if(pred != null){
			myNode.get().locked = true;
			pred.next = myNode.get();
			while(myNode.get().locked){
				Thread.yield();
			}
		}
	}
	
	public void unlock(){
		/* If there is another node in the list, set next to null and unlock myself
		 * Setting next to null to remove myself from the list
		 * Setting locked to false to show I dont want the CS
		 */
		if(myNode.get().next == null){
			/* 2 cases if I enter this if
			 * Case 1: No other node has inserted itself in the list, so tailNode is pointing to me. Set
			 * 		   the tailNode to null and return.
			 * Case 2: Another process hasn't changed my .next to it's node yet, so stall until it does so.
			 */
			if(tailNode.compareAndSet(myNode.get(), null)){
				return;
			}
			while(myNode.get().next == null){
				Thread.yield();
			}
		}
		myNode.get().next.locked = false;
		myNode.get().next = null;
	}

}
