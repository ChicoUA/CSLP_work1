package Work;

import java.util.ArrayList;

public class Signature {
	private String str;
	private String[] list;
	private long[] signature;
	private int k;
	private InitHashFunction inithash;
	
	public Signature(String str, int k, InitHashFunction inithash) {
		this.str = str;
		this.k = k;
		this.inithash=inithash;
		this.signature=new long[k];
	}

	public Signature(String[] list, int k, InitHashFunction inithash) {
		this.list = list;
		this.k = k;
		this.inithash=inithash;
		this.signature=new long[k];
	}

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
