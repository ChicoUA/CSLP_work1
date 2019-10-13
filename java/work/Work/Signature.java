package Work;

import java.util.ArrayList;

public class Signature {
	private String str;
	private String[] list;
	private long[] signature;
	private int k;
	private InitHashFunction inithash;
	/**
	 * Constructor when it receives a string
	 * @param str string to be transformed into a signature
	 * @param k size of the signature
	 * @param inithash the hash functions used to determine the signature
	 */
	public Signature(String str, int k, InitHashFunction inithash) {
		this.str = str;
		this.k = k;
		this.inithash=inithash;
		this.signature=new long[k];
	}
	
	/**
	 * Constructor when it receives a list
	 * @param list list of strings to be transformed into a signature
	 * @param k size of the signature
	 * @param inithash the hash functions used to determine the signature
	 */
	public Signature(String[] list, int k, InitHashFunction inithash) {
		this.list = list;
		this.k = k;
		this.inithash=inithash;
		this.signature=new long[k];
	}
	/**
	 * Getter for the signature
	 * @return returns the signature
	 */
	public long[] getSignature() {
		return signature;
	}

	public void setSignature(long[] signature) {
		this.signature = signature;
	}
	
	
	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
	
	

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String[] getList() {
		return list;
	}

	public void setList(String[] list) {
		this.list = list;
	}

	public InitHashFunction getInithash() {
		return inithash;
	}

	public void setInithash(InitHashFunction inithash) {
		this.inithash = inithash;
	}

	/**
	 * Function that creates a signature, given a String.
	 * First, it splits the string into shingles.
	 * Second, it uses each shingle, one each iteration, and creates a key, by getting the value of each letter and multiplying by the position of that letter in the shingle and adds to the key.
	 * Third, in that same iteration, it uses the key, and the values of the hash function and creates the code (code = (a * key + b) % p).
	 * Finally, the smallest code is the one saved inside the signature.
	 * This process is repeated for each hash function, the more functions, the bigger is the signature. 
	 * @param len size of the shingles
	 */
	public void signatureMaker1(int len) {
		
		int i;
		long code;
		
		String shingle;
		long[] signatures=new long[this.getK()];
		
		ArrayList<String> shingles=new ArrayList<String>();
		for(i=0;i<this.str.length()-len-1;i++) {
			shingle=this.getStr().substring(i, i+len);
			shingles.add(shingle);
		}
		int signature[]=new int[shingles.size()-1];
		
		for(i=0;i<this.getK();i++) {
			
			long sig=2000000;
			for(int j=0;j<shingles.size();j++) {
				int key=0;
				shingle=shingles.get(j);
				
				for (int n=0;n<shingle.length();n++) {
					key+=(int)shingle.charAt(n)*(n+1);
				}
				code=((this.getInithash().getA()[i] * key + this.getInithash().getB()[i]) % this.getInithash().getP());
				
				if(code<sig) {
					sig=code;
			    }
			}
			
			signatures[i]=sig;
		}
		this.signature=signatures;	
	}
	/**
	 * Function that creates a signature, given a list of Strings.
	 * First, it uses each String, one each iteration, and creates a key, by getting the value of each letter and multiplying by the position of that letter in the string and adds to the key.
	 * Second, in that same iteration, it uses the key, and the values of the hash function and creates the code (code = (a * key + b) % p).
	 * Finally, the smallest code is the one saved inside the signature.
	 * This process is repeated for each hash function, the more functions, the bigger is the signature. 
	 */
	public void signatureMaker2() {
		
		int i;
		long code;
		long[] signatures=new long[this.getK()];
		
		
		for(i=0;i<this.getK();i++) {
			
			long sig=2000000;
			for(String book : this.getList()) {
				int key=0;
				
				for (int n=0;n<book.length();n++) {
					key+=(int)book.charAt(n)*(n+1);
				}
				code=((this.getInithash().getA()[i] * key + this.getInithash().getB()[i]) % this.getInithash().getP());
				
				if(code<sig) {
					sig=code;
			    }
			}
			
			signatures[i]=sig;
		}
		this.signature=signatures;		
	}
	/**
	 * Function to compare two signatures.
	 * It compares, for the same position, the elements of both signatures and, if they are equal, increments the variable equal.
	 * Finally, to obtain the value of similarity, it divides equal by the size of the signature.
	 * @param sig other signature used for comparation
	 * @return returns the value of similarity between to signatures
	 */
	public double distance(Signature sig) {
		double equal=0;
		long[] sig2 = sig.getSignature();
		
		for (int i=0; i<this.getK();i++) {
			if(this.getSignature()[i]==sig2[i]) {
				equal+=1;
			}
		}
		
		double sim=(equal/this.getK());
		return sim;
	}
	
	
}
