package Work;
import java.io.*;
import java.util.*;
public class Módulo3 {

	public static void main(String[] args) throws FileNotFoundException {
		int k=100;
		InitHashFunction inithash=new InitHashFunction(k);
		ArrayList<Signature> listadeassinaturas=new ArrayList<>();
		
		
		String f1="Aveiro é Nosso!";
		String f2="Aveiro é Nosso!";
		String f3="Coimbra é bonita?";
		String f4="ola eu sou o Francisco gosto de gelado e adeus. Vamos divertirmos e comer muitos doces e o meu nome é Francisco. Era uma vez um cavaleiro chamado Arthur e que era muito corajoso e honroso. Comi muito gelado hoje mas esqueci de avisar os meus pais, adeus adeus adeus  e e e e";
		String f5="adeus eu era o Francisco gostava de milho e ola. Comi divertirmos e comer muitos doces e o adeus nome é João. Era uma vez tres cavaleiros chamado Lancelot e gosto era muito corajoso e honroso. Comi muito gelado hoje mas esqueci de avisar os telefone pais, adeus adeus adeus  e e e e";
		String f6="aveiro é nosso!";
		
		Signature sig1=new Signature(f1,k,inithash);
		Signature sig2=new Signature(f2,k,inithash);
		Signature sig3=new Signature(f3,k,inithash);
		Signature sig4=new Signature(f4,k,inithash);
		Signature sig5=new Signature(f5,k,inithash);
		Signature sig6=new Signature(f6,k,inithash);
		
		sig1.signatureMaker1(2);
		sig2.signatureMaker1(2);
		sig3.signatureMaker1(2);
		sig4.signatureMaker1(3);
		sig5.signatureMaker1(3);
		sig6.signatureMaker1(2);
		
		listadeassinaturas.add(sig1);
		listadeassinaturas.add(sig2);
		listadeassinaturas.add(sig3);
		listadeassinaturas.add(sig4);
		listadeassinaturas.add(sig5);
		listadeassinaturas.add(sig6);
		
		for(int i=0; i<listadeassinaturas.size(); i++) {
			int l1=i+1;
			Signature signature1=listadeassinaturas.get(i);
			for(int j = i+1; j < listadeassinaturas.size(); j++) {
				int l2=j+1;
				Signature signature2=listadeassinaturas.get(j);
				System.out.println("f"+l1+" - "+"f"+l2+" -> "+signature1.distance(signature2));
			}
		}
		
		//textos grandes
		Scanner sc = new Scanner(new FileReader("text2.txt"));
		StringBuilder str1 = new StringBuilder();
		while(sc.hasNextLine()) {
	
			String line=sc.nextLine();
			str1.append(line);
		}
		String text1=str1.toString();
		sc.close();
		
		sc = new Scanner(new FileReader("text1"));
		str1 = new StringBuilder();
		while(sc.hasNextLine()) {
	
			String line=sc.nextLine();
			str1.append(line);
		}
		String text2=str1.toString();
		
		sc = new Scanner(new FileReader("text3"));
		str1 = new StringBuilder();
		while(sc.hasNextLine()) {
	
			String line=sc.nextLine();
			str1.append(line);
		}
		String text3=str1.toString();
		
		Signature t1=new Signature(text1,k,inithash);
		Signature t2=new Signature(text2,k,inithash);
		Signature t3=new Signature(text3,k,inithash);
		
		t1.signatureMaker1(10);
		t2.signatureMaker1(10);
		t3.signatureMaker1(10);
		System.out.println("");
		System.out.println("t1 - t2 -> "+t1.distance(t2));
		System.out.println("t1 - t3 -> "+t1.distance(t3));
		
	}
	
}
