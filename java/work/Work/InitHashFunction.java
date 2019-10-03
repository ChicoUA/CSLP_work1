package Work;

import java.lang.Math;

public class InitHashFunction {
	private long a[];
	private long b[];
	private long p;
	private int k;//number of hash functions
	
	/**
	 * Constructor for the hash functions
	 * It creates two lists of random numbers with the size of the number of hash functions
	 * @param k number of hash functions
	 */
	public InitHashFunction(int k) {
		this.p = 1299721;//large prime number
		int i;
		this.a=new long[k];
		this.b=new long[k];
		this.k=k;
		for (i=0;i<=k-1;i++) {
			a[i]=(long)(Math.random()*p+1);
			b[i]=(long)(Math.random()*p+1);
		}
		
	}

	public long[] getA() {
		return a;
	}

	public void setA(long[] a) {
		this.a = a;
	}

	public long[] getB() {
		return b;
	}

	public void setB(long[] b) {
		this.b = b;
	}

	public long getP() {
		return p;
	}

	public void setP(long p) {
		this.p = p;
	}

	public long getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
	
	
}
