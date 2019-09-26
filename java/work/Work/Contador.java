package Work;

public class Contador {
	private int contador;
	private float log;
	private boolean special;

	public Contador(boolean special) {
		this.contador = 0;
		this.log=1;
		this.special=special;
	}

	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
	}
	
	public void adicionar() {
		if(this.special==true) {
			if(Math.random() < (1/this.log)) {
				this.contador+=this.log;
				this.log=this.log+1;
			}
		}
		else {
			if(Math.random()<0.5) {
				this.contador+=2;
			}
		}
	}
	public void adicionarV2(int val) {
		for (int i =0; i < val; i++) {
			if(Math.random()<0.5) {
				this.contador+=2;
			}
		}
	}
	
}
