package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Ceia extends Filosofo{
	public static void main(String[] args) throws InterruptedException, UnknownHostException, IOException, ClassNotFoundException 
	{
		Socket primeiro = new Socket("200.239.139.111", 8080);
		ObjectInputStream mensageiro = new ObjectInputStream(primeiro.getInputStream());
		FilosofoEsquerda = (String) mensageiro.readObject();
		FilosofoAtual = (String) mensageiro.readObject();
		FilosofoDireita = (String) mensageiro.readObject();
		System.out.println("Filosofo da Esquerda: " + FilosofoEsquerda + 
				           " Atual: "+ FilosofoAtual +
				           " Filosofo da Direita: "+ FilosofoDireita);
		mensageiro.close();
		primeiro.close();
		Server();
		Client();
	}
}
