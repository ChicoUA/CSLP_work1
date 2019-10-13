package Work;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
/*! Class used to test the class Contador */
public class testeContador {

	public static void main(String[] args) throws FileNotFoundException {
		Contador cont2=new Contador(false);
		for (int i = 0; i<1000000; i++) {
			cont2.adicionar();
		}
		System.out.println("contador 2 = "+cont2.getContador());
	
		Contador cont3=new Contador(true);
		for (int j = 0; j<1000000; j++) {
			cont3.adicionar();
		}
	
		System.out.println("contador 3 = "+cont3.getContador());
		
		Scanner sc = new Scanner(new FileReader("text2.txt"));
		int count=0;
		Contador cont4=new Contador(false);
		while (sc.hasNext()) {
			String word = sc.next();
			count++;
			cont4.adicionar();
			
		}

		System.out.println("word count: "+count);
		System.out.println("contador 4: "+cont4.getContador());
		

	}

}
