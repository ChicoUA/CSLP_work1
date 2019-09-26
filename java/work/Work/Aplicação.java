package Work;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Aplicação {

	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<String[]> ListasDeLivros = new ArrayList<>();
		ArrayList<Signature> ListaDeAssinaturas=new ArrayList<>();
		TreeSet<String> NomesDeLivros = new TreeSet<>();
		String[] Lists= {"L1", "L2", "L3","L4","L5","L6","L7","L8","L9","L10"};//Lista 5 serve de controlo(é igual à lista 1 com diferenças) e lista 10 é recolhida dos dados do bloomfilter. 
		int count=0;
		int k=50;
		long sum=0;
		BloomFilter b = new BloomFilter(8*1000,7);
		Contador counter=new Contador(false);
		InitHashFunction inithash=new InitHashFunction(k);
		
		//Leitura das Listas
		for(String i : Lists) {
			String[] Livros= new String[100];
			int index=0;
			Scanner sc = new Scanner(new FileReader(i));
			while(sc.hasNextLine()) {
				count++;
				counter.adicionar();//Incremento do Contador Estocástico
				String line=sc.nextLine();
				line=line.replaceAll("\\p{Punct}", "");
				line=line.toLowerCase();
				Livros[index]=line;
				index++;
				b.insert(line);//Inserir Elementos no Bloomfilter
				NomesDeLivros.add(line);
			}
			ListasDeLivros.add(Livros);
			sc.close();
		}
		
		//Procurar Elementos no BloomFilter
		int counthelp=0;
		Contador counter2=new Contador(false);
		for(String book : NomesDeLivros) {
			long value=b.search(book);
			sum+=value;
			counter2.adicionar();
			if(value>2) {
				System.out.println(book+" - "+value);
			}
		}
		
		System.out.println("");
		System.out.println("Line Count: "+count);
		System.out.println("BloomFilter Count: "+sum);
		System.out.println("Valor do Contador Estocástico (número de livros): "+counter.getContador());
		System.out.println("Unique Books: "+NomesDeLivros.size());
		System.out.println("Valor do Contador Estocástico (livros únicos: "+counter2.getContador());
		System.out.println("");
		
		//Listas Semelhantes
		for(String[] lista : ListasDeLivros) {
			Signature signature=new Signature(lista,k,inithash);
			signature.signatureMaker2();
			ListaDeAssinaturas.add(signature);			
		}
		
		
		for(int i=0;i<ListaDeAssinaturas.size();i++) {
			Signature sig1=ListaDeAssinaturas.get(i);
			int l1=i+1;
			for(int j=i+1;j<ListaDeAssinaturas.size();j++) {
				int l2=j+1;
				Signature sig2=ListaDeAssinaturas.get(j);
				double sim=sig1.distance(sig2);
				if(sim > 0.25) {
					System.out.println("L"+l1+" - "+"L"+l2+ " -> "+sim);
				}
			}
		}
		
	}

}
