package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {

	T result;
	boolean complete;


	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future()
	{
		result = null;
		complete = false;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * 	       
     */
	public synchronized T get() {
		while (!isDone()&&!Thread.currentThread().isInterrupted()) {
			try {
				this.wait();
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		return this.result;
	}
	
	/**
     * Resolves the result of this Future object.
     */
	public synchronized void resolve (T result) {
		this.result = result;
		complete = true;
		this.notifyAll();
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		return this.complete;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
	public T get(long timeout, TimeUnit unit) {
		long start = System.currentTimeMillis();
		long end = start + timeout * 100;
		while (!isDone()) {
			try {
				unit.sleep(10);
			} catch (InterruptedException ex) {
			}
			if (System.currentTimeMillis() > end)
				break;
		}
		return result;
	}

}



