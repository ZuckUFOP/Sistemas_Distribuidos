package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Filosofo
{
	public static ServerSocket servidor; //Hora servidor
	public static Socket cliente; //Hora cliente
	public static int numeroDeRodizios = 10; // Cada filósofo só pode comer 10 vezes.
	public static int numeroDeFilosofos = 3;
	public static int porta = 8080;
	public static int rodizioAtual = 1; //marcador de quantos rodízios este filósofo já participou
	public static String FilosofoEsquerda = null;
	public static String FilosofoAtual = null;
	public static String FilosofoDireita = null;
	public static boolean jantando = false;	//Filosofo jantando;
	public static boolean pensando = false;	//Filosofo pensando;
	public static boolean pedindo = false;	//Se filosofo à direita DESTE tá pedindo garfo;
	public static int garfo = 1; 
	public static int jantou = 0; //quantidade de vezes que um filósofo jantou
	public static Random tempo = new Random();
	public static String respostaPositiva = "Sim";
	public static String respostaNegativa = "Nao";

	//Pede o garfo do filósofo à e/d emprestado.
	public static boolean pedirGarfoEmprestado(String ip) throws UnknownHostException, IOException, ClassNotFoundException
	{
		if(jantando || pensando)
		{
			String resposta = null;
			cliente = new Socket(ip, porta);
			ObjectInputStream mensageiro = new ObjectInputStream(cliente.getInputStream());

			pedindo = true;
			pensando = false;
			jantando = false;

			System.out.println("Filosofo " + FilosofoAtual + " quer o garfo emprestado! \n");

			if(garfo == 0 || garfo == 1)
			{

				//System.out.println("entrou if");
				resposta = (String) mensageiro.readObject();
				System.out.println("Filosofo (" + FilosofoAtual +"): " + " pediu o garfo! \n"
						+"Resposta recebida: "+resposta);
				//System.out.println("depois da resp " + leitura.length());
				if(resposta.equals("Sim"))
				{
					System.out.println("Emprestou!!");
					garfo++;
					System.out.println("Tenho: " + garfo + " garfo agora!!");
					mensageiro.close();
					cliente.close();
					return true;
				}
				else
				{
					mensageiro.close();
					cliente.close();
					return false;
				}

			}
		}
		return false;
	}

	public static void Jantar() throws InterruptedException																//Filosofo jantando;
	{
		pedindo = false;
		pensando = false;
		jantando = true;
		System.out.println("Filosofo (" + FilosofoAtual +"): " + " esta jantando! \n");
		Thread.sleep(tempo.nextInt(5000) + 5);
		jantou++;

	}

	public static void Pensar() throws InterruptedException																//Filosofo pensando;
	{
		pedindo = false;
		pensando = true;
		jantando = false;
		System.out.println("Filosofo (" + FilosofoAtual +"): " + " esta pensando;\n");
		Thread.sleep(tempo.nextInt(5000) + 5);
	}

	//Permite que esse filósofo receba requisições diretamente de outros filósofos da rede
	public static void Server()																							//Servidor para o Sucessor;
	{
		try 
		{
			servidor = new ServerSocket(porta);
			System.out.println("Servidor ouvindo a porta " + porta);

			new Thread() {

				@Override
				public void run() {
					Socket filosofoPidao = null;
					ObjectOutputStream mensageiro = null;
					while (!servidor.isClosed()){
						//System.out.println("Thread");
						try {
							filosofoPidao = servidor.accept();
							//System.out.println("Thread2");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Filosofo: " + filosofoPidao.getInetAddress().getHostAddress() + " quer o garfo emprestado");
						try {
							mensageiro = new ObjectOutputStream(filosofoPidao.getOutputStream());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							mensageiro.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Quando eu não empresto meu garfo
						if (pedindo || jantando || (garfo == 0)){
							try {
								mensageiro.writeObject(respostaNegativa);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else //Quando eu empresto meu garfo.
						{
							garfo--;
							try {
								mensageiro.writeObject(respostaPositiva);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							mensageiro.close();
							filosofoPidao.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}.start();
			System.out.println("Sobremesa");
		}   
		catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}

	}

	//Quando eu me comporto como cliente do filosofo à minha esquerda (Filosofo pidao)
	public static void Client()
	{
		//System.out.println("While1");
		while(!servidor.isClosed()){
			System.out.println("Quantidade de Garfo: "+ garfo);
			try 
			{	
				//System.out.println("While3");
				Pensar();
				if (garfo == 2){
					Jantar();
				}
				else if (pedirGarfoEmprestado(FilosofoEsquerda) && garfo == 2){
					Jantar();
				}
				else 
				{
					if (garfo == 2)
						Jantar();
				}
				if(jantou == numeroDeRodizios)																		
				{
					//Encerra a conexao após último rodízio;
					servidor.close();
					System.out.println("Conexao encerrada com sucesso!");
				}
			}
			catch(Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
		//System.out.println("While1");
	} 
}
