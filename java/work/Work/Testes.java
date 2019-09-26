package Work;
import java.io.*;
import java.util.Scanner;
import java.util.*;

public class Testes {

	public static void main(String[] args) throws FileNotFoundException {
		BloomFilter l=new BloomFilter(80,3);
		
		l.insert("ola");
		l.insert("oi");
		l.insert("gold");
		l.insert("gold");
		l.insert("gold");
		l.insert("silver");
		l.insert("silver");
		l.insert("bronze");
		l.insert("triple");
		l.insert("his");
		l.insert("the");
		
		System.out.println("ola - "+l.search("ola"));
		System.out.println("oi - "+l.search("oi"));
		System.out.println("gold - "+l.search("gold"));
		System.out.println("silver - "+l.search("silver"));
		System.out.println("bronze- "+l.search("bronze"));
		System.out.println("llsdjfkw - "+l.search("llsdjfkw"));
		System.out.println("triple - "+l.search("triple"));
		System.out.println("his - "+l.search("his"));
		System.out.println("the - "+l.search("the"));
		
		//Bloomfilter com texto grande
		System.out.println("");
		BloomFilter c=new BloomFilter(7000*8,5);
		Set<String> uniqueWords=new TreeSet<>();
		Scanner sc = new Scanner(new FileReader("text2.txt"));
		int count=0;
		while (sc.hasNext()) {
			String word = sc.next();
			word=word.replaceAll("\\p{Punct}", "");
			String word2=word.toLowerCase();
			uniqueWords.add(word2);
			c.insert(word2);
			count++;
			
		}

		for (String unique : uniqueWords) {
			long value=c.search(unique);
			if(value > 100) {
				System.out.println(unique+" - "+value);
			}
		}
		System.out.println("word count: "+count);
		sc.close();
	}
	
	

}
