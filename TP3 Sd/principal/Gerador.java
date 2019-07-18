package principal;

import main.Gerador_0_10M;
import main.Gerador_qq_inteiro;

public class Gerador implements Runnable{
	private String name;
	private int tam;
	
	public void setName(String n){name =n;}
	public void setTam(int n){tam =n;}
	
	

	public void run() {
		
		Gerador_0_10M Gbinary = new Gerador_0_10M();
		Gbinary.name= name;
		Gbinary.gera1GB(tam);
		
	}

}
