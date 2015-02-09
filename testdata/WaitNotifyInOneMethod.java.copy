
public class WaitNotifyInOneMethod 
{
	public synchronized void waitAndNotifyUsed() {
		while(condition()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		notify();
	}
	
	public synchronized void waitAndNotifyAllUsed() {
		while(condition()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		notifyAll();
	}
	
	public synchronized void waitOnly() {
		while(condition()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void notifyOnly() {
		notify();
	}
	
	public synchronized void notifyAllOnly() {
		notifyAll();
	}
	
	private boolean condition() {
		return true;
	}
}
