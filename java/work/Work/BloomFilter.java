package Work;

public class BloomFilter{
	private int n;
	private int k;
	private InitHashFunction initHash;
	private long b[];
	
	/**
	 * Constructor of the Bloomfilter
	 * 
	 * @param n represents the size of the bloomfilter
	 * @param k represents the number of hash functions that will be applied to the values that are saved in the bloomfilter
	 */
	public BloomFilter(int n, int k) {
		this.n = n;
		this.k = k;
		this.b=new long[n];
		this.initHash=new InitHashFunction(k);
	}
	
	/**
	 * @return returns the size of the bloomfilter
	 */

	public int getN() {
		return n;
	}
	
	/**
	 * 
	 * @param n new value of the size of the bloomfilter
	 */
	public void setN(int n) {
		this.n = n;
	}

	/**
	 * 
	 * @return returns the number of hash functions used in the bloomfilter
	 */
	public int getK() {
		return k;
	}

	/**
	 * 
	 * @param k new number of hash functions for the bloomfilter
	 */
	public void setK(int k) {
		this.k = k;
	}
	
	/**
	 * Function that inserts a new value into the bloomfilter. It creates a key by multiplying the integer value of each letter by its position in the word.
	 * Then creates a number of codes based on the number of hash functions. The code is calculated by multiplying the key by the value 'a' of the hash function,
	 * then adds the value of 'b', finally makes two modulus, one by a large prime number, and another by the size of the bloomfilter so it can give a number between 0
	 * and the size of the bloomfilter.
	 * Then increments the number of occurrences of that number inside the bloomfilter
	 * @param var value that is to be saved into the bloomfilter
	 */
	public void  insert(String var) {
		int key=0;
		int i;
		long code;
		for (i=0;i<var.length();i++) {
			key+=(int)var.charAt(i)*(i+1);
		}
		for(i=0;i<this.k;i++) {
			code=((this.initHash.getA()[i] * key + this.initHash.getB()[i]) % this.initHash.getP()) % this.n;
			this.b[(int)code]+=1;
		}
	}
	/**
	 * Function to get the number of occurrences, of a word, inside the bloomfilter.
	 * It creates the code following the same logic used in fucntion insert(String var).
	 * Since there is different values, depending on the number of hash functions and there are different strings that can give the same code while using a hash function, 
	 * so the correct number is the lowest one
	 * @param var word that is to be searched
	 * @return returns the number of occurences of that word inside of the bloomfilter
	 */
	public long search(String var) {
		long key=0;
		long code,temp;
		int i;
		long occurences=1000000;
		for (i=0;i<var.length();i++) {
			key+=(long)var.charAt(i)*(i+1);
		}
		for(i=0;i<this.k;i++) {
			code=((this.initHash.getA()[i] * key + this.initHash.getB()[i]) % this.initHash.getP()) % this.n;
			temp=this.b[(int)code];
			if(temp<occurences) { // to change the number of occurrences to the lowest
				occurences=temp;
			}
		}
		return occurences;
	}
	
}
