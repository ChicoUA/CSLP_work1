package Work;
public class BloomFilter{
	private int n;
	private int k;
	private InitHashFunction initHash;
	private long b[];

	public BloomFilter(int n, int k) {
		this.n = n;
		this.k = k;
		this.b=new long[n];
		this.initHash=new InitHashFunction(k);
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
	
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
			if(temp<occurences) {
				occurences=temp;
			}
		}
		return occurences;
	}
	
}
