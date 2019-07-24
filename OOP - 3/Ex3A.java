package OOP_matala3;

/**
 * calculate prime numbers
 * @author user
 *
 */
public class Ex3A implements Runnable {
	static long number;
	static Boolean ans=null;
	/**
	 * @param n natural number
	 * @param maxTime maximum time to check if the natural number is prime 
	 * @return boolean if the natural number is prime or not, throws exception if not
	 * @throws RuntimeException
	 */
	public boolean isPrime(long n, double maxTime) throws RuntimeException{
		
		number = n;
		long max_time = (long)(maxTime*1000);
		Thread t = new Thread (this);
		
		t.setDaemon(true); //to make sure the main stops if it throws exception
		
		long start=System.currentTimeMillis();
		t.start();
		long end=System.currentTimeMillis();
		
		while((end-start)<max_time){
			if (ans!=null)
				return ans;
			end=System.currentTimeMillis();
		}
		throw new RuntimeException();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ans=Ex3_tester.isPrime(number);
	}
}

