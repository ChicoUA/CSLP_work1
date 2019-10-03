package Work;
/*! Class that represents a Stochastic Counter */
public class Contador {
	private int contador;
	private float log; /*! defines the logarithmic value. Always starts at 1 */
	private boolean special;
	
	/**
	 * Constructor for the Stochastic Counter
	 * @param special defines if the counter will be logarithmic( special = true ) or if will be a counter with 50% chance of counting
	 */
	public Contador(boolean special) {
		this.contador = 0;
		this.log=1;
		this.special=special;
	}
	
	/**
	 * @return returns the value of the counter
	 */
	public int getContador() {
		return contador;
	}
	
	/**
	 * @param contador new value for the counter
	 */
	public void setContador(int contador) {
		this.contador = contador;
	}
	
	/**
	 * Function used to increase the counter.
	 * If special is true, then the percentage to count will be reduced logarithmically for each iteration. It starts at 1(100%), then 1/2(50%), 1/3(~33%) ...
	 * And the number to add to the counter will increase, first is 1, then 2, 3, 4 ...
	 * Else it will count only with a percentage of 50% and increase the counter by 2.
	 */
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
	
}
