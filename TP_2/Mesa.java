package test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Mesa{
	public static int porta = 8080;
	private static int numeroDeFilosofos = 5;
	public static String IPFilosofos[] = new String[numeroDeFilosofos];												//Vetor de filosofos;
	private static Vector<Socket> filosofos = new Vector<Socket>();
	private static Vector<ObjectOutputStream> conversasIP = new Vector<ObjectOutputStream>();
	
	public static void Server() throws IOException
	{
		//Abrindo servidor
		ServerSocket servidor = new ServerSocket(porta);
		System.out.println("Servidor ouvindo a porta " + porta);
		
		//Startando filosofos em modo cliente
		while (filosofos.size() < numeroDeFilosofos){
			
			filosofos.add(servidor.accept());
			IPFilosofos[filosofos.size()-1] = filosofos.elementAt(filosofos.size()-1).getLocalAddress().getHostAddress();
			conversasIP.add(new ObjectOutputStream (filosofos.elementAt(filosofos.size()-1).getOutputStream()));
			conversasIP.get(filosofos.size()-1).flush();
		}
		
		//Lógica de atribuição dos IPs
		for (int i = 1; i < filosofos.size();i++){
			if (i == 1){
				//maquina 1 recebe antecessor na ultima posicao
				conversasIP.get(i-1).writeObject(IPFilosofos[filosofos.size()-1]);
				conversasIP.get(i-1).flush();
				
				//maquina 1 recebe seu ip
				conversasIP.get(i-1).writeObject(IPFilosofos[i-1]);
				conversasIP.get(i-1).flush();
				
				//maquina 1 recebe Sucessor
				conversasIP.get(i-1).writeObject(IPFilosofos[i]);
				conversasIP.get(i-1).flush();
			}
			
			//maquina i-1 recebe antecessor na posicao i-2
			conversasIP.get(i-1).writeObject(IPFilosofos[i-2]);
			conversasIP.get(i-1).flush();
			
			//maquina i-1 recebe seu ip
			conversasIP.get(i-1).writeObject(IPFilosofos[i-1]);
			conversasIP.get(i-1).flush();
			
			//maquina i-1 recebe Sucessor na posicao i
			conversasIP.get(i-1).writeObject(IPFilosofos[i]);
			conversasIP.get(i-1).flush();
		}
		//ultima maquina recebe penultima
		conversasIP.get(filosofos.size()-1).writeObject(IPFilosofos[filosofos.size()-2]);
		conversasIP.get(filosofos.size()-1).flush();
		
		//ultima maquina recebe seu ip
		conversasIP.get(filosofos.size()-1).writeObject(IPFilosofos[filosofos.size()-1]);
		conversasIP.get(filosofos.size()-1).flush();
		
		//ultima maquina recebe ip da primeira
		conversasIP.get(filosofos.size()-1).writeObject(IPFilosofos[0]);
		conversasIP.get(filosofos.size()-1).flush();
		
		//fechar todos os filosofos clientes
		for(int i = 0; i < filosofos.size(); i++){
			conversasIP.get(i).close();
			filosofos.get(i).close();
		}
		servidor.close();
	}
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		new Mesa();
		Mesa.Server();
	}
}