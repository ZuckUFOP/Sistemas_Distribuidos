package principal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class main {

	public static Map<Integer, Double> vector;
	public static List<String> ip;
	public static String ip_Servidor;
	public static int nMaquinas;

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		int resposta;
		// Configuracao do Server ou Host
		resposta = JOptionPane.showConfirmDialog(null, "Configurar Servidor?");
		ip_Servidor = JOptionPane.showInputDialog(null, "Informe o ip do servidor");
		// Se sim configuramos o servidor
		if (resposta == JOptionPane.YES_OPTION) {
			// n representa o numero de maquinas
			int n = 0;
			ip = new ArrayList<String>();
			// List Thread_S guarda os sockes dos hosts
			List<Thread_Servidor> Thread_S = new ArrayList<Thread_Servidor>();
			// Abrindo servidor na porta 12345
			ServerSocket ssock = new ServerSocket(12345);
			System.out.println("Listening");

			int i = 0;
			Socket sock = null;
			// Aguarda a conexao dos hosts
			while (true) {
				sock = ssock.accept();
				System.out.println("Connected " + sock.getInetAddress().getHostAddress());
				ip.add(sock.getInetAddress().getHostAddress().toString());
				// apos se conectar ele guarda o socket no list Thread_S
				Thread_Servidor t = new Thread_Servidor(0);
				t.csocket = sock;
				Thread_S.add(t);
				if (i == n)
					break;
				i++;
			}
			// Envia os ips para todas as maquinas
			ObjectOutputStream saida = null;
			for (int j = 0; j <= i; j++) {
				saida = new ObjectOutputStream(Thread_S.get(j).csocket.getOutputStream());
				saida.writeObject(ip);
				saida.close();
			}

			sock.close();
			ssock.close();

		} else {

			// Apenas para Host
			ip = new ArrayList<String>();
			//Conecta ao servidor e espera o envio do list de ips
			try {
				
				Socket cliente = new Socket(ip_Servidor, 12345);
				System.out.println("O cliente se conectou ao servidor!");

				ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

				ip = (ArrayList<String>) entrada.readObject();

				// JOptionPane.showMessageDialog(null,"Data recebida do servidor:"
				// + data_atual.toString());
				System.out.println(ip.toString());
				System.out.println(ip.size());
				entrada.close();
				cliente.close();
				nMaquinas = ip.size();
				ordInterna();

			} catch (Exception e) {

				System.out.println("Erro: " + e.getMessage());

			}

		}

	}

	public static void ordInterna() throws IOException {
		Runtime runTime = Runtime.getRuntime();
		List<Gerador> threads = new LinkedList<Gerador>();
		List<Ordenador> threads_Ordena = new LinkedList<Ordenador>();
		List<Thread> t = new LinkedList<Thread>();
		List<Thread> t1 = new LinkedList<Thread>();
		int test, tam = 0;
		long tempoInicial, tempoFinal;
		int nucleos = runTime.availableProcessors();

		System.out.println("Numero de Threads: " + nucleos);
		System.out.println("Nucleos do Processador: " + runTime.availableProcessors());
		System.out.println("Memoria livre: " + runTime.freeMemory() + " bytes");
		System.out.println("Memoria total: " + runTime.totalMemory() + " bytes");

		// -----------------Geracao de arquivo ---------------------------
/*
		
		  for(int ti=0;ti<3;ti++){ if(ti==0) tam=2; if(ti==1) tam=5; if(ti==2)
		  tam=10; tempoInicial = System.currentTimeMillis(); for(int
		 i=0;i<nucleos;i++){
		
		  threads.add(new Gerador());
		  threads.get(i).setName(String.valueOf(i)+String.valueOf(ti));
		  threads.get(i).setTam(tam); t.add(new Thread(threads.get(i)));
		  t.get(i).start(); }
		  
		  
		  
		  while(true){ test=0; for(int i=0;i<nucleos;i++)
		  if(!t.get(i).isAlive()){test+=1;}
		  
		 if(test ==nucleos ) break; }
		  
		 t.clear(); tempoFinal = System.currentTimeMillis();
		System.out.println("\nTempo = " + (tempoFinal - tempoInicial)/1000.0
		  + "s"); }
		 */
		// --------------------fim da Geracao----------------------------

		// -----------------------Ordenacao--------------------------------------
		nucleos=nMaquinas;
		for (int ti = 0; ti < 3; ti++) {
			tempoInicial = System.currentTimeMillis();

			for (int i = 0; i < nucleos; i++) {

				threads_Ordena.add(new Ordenador());
				threads_Ordena.get(i).setName(String.valueOf(i) + String.valueOf(ti));
				threads_Ordena.get(i).setNucleos(nucleos);
				threads_Ordena.get(i).ind = i + 1;
				t.add(new Thread(threads_Ordena.get(i)));
				if (ti == 0)
					threads_Ordena.get(i).tam = 31107;
				if (ti == 1)
					threads_Ordena.get(i).tam = 31107;
				if (ti == 2)
					threads_Ordena.get(i).tam = 31107;
				t.get(i).start();
			}

			while (true) {
				test = 0;
				for (int i = 0; i < nucleos; i++)
					if (!t.get(i).isAlive()) {
						test += 1;
					}

				if (test == nucleos)
					break;
			}
			// threads_Ordena.clear();
			t.clear();
			tempoFinal = System.currentTimeMillis();
			//System.out.println("\nTempo = " + (tempoFinal - tempoInicial) / 1000.0 + "s");
		}

		// ----------------------Fim Ordenacao------------------------------------

		for (int i = 0; i < nucleos; i++) {
			for (int j = 0; j < nucleos; j++) {
				threads_Ordena.get(i).vectorOrd.get(j).putAll(threads_Ordena.get(j).vector.get(i));
				threads_Ordena.get(j).vector.get(i).remove(j);
			}
		}
		tempoInicial = System.currentTimeMillis();

		for (int i = 0; i < nucleos; i++) {
			threads_Ordena.get(i).flag = 1;
			t1.add(new Thread(threads_Ordena.get(i)));
			t1.get(i).start();
		}

		while (true) {
			test = 0;
			for (int i = 0; i < nucleos; i++)
				if (!t1.get(i).isAlive()) {
					test += 1;
				}

			if (test == nucleos)
				break;
		}
		tempoFinal = System.currentTimeMillis();
		//System.out.println("\nTempo = " + (tempoFinal - tempoInicial) / 1000.0 + "s");
		t1.clear();
		// ------------------------------------Thread Accept--------------------------------
		//Thread que fica rodando esperando receber os vetores ordenados de outras maquinas
		Thread_Servidor thread_accept = null;
		Thread s = null;
		
		try {
			thread_accept = new Thread_Servidor(nMaquinas);
			s = new Thread(thread_accept);
			s.start();
		} catch (Exception e) {

			System.out.println("Erro: " + e.getMessage());

		}

		// ----------------------------------------------------------------------------------

		// ----------------------Envio via Rede------------------------------------------------------
		tempoInicial = System.currentTimeMillis();
		//apos obter todos os vetores eles sao enviados para suas respectivas maquinas
		System.out.println("---------------------------------");
		try {

			for (int i = 0; i < nMaquinas; i++) {
				Thread_Host h = new Thread_Host();
				h.ip=ip.get(i);
				h.vector.putAll(threads_Ordena.get(i).Ordenado);
				Thread thread = new Thread(h);
				thread.start();

			
			}

		} catch (Exception e) {

			System.out.println("Erro: " + e.getMessage());

		}
	
		

		vector = new HashMap<Integer,Double>();
		//parte onde juntarems os vetores que foram enviados pela rede em um vetor map so
	 	double tag;
		int ind = 0;
		
		for(int i=0;i<ip.size();i++){
			if(ip.get(i) == InetAddress.getLocalHost().getHostAddress() ){
				ind=i;
				break;
			}
		}
		while(s.isAlive()){}
		tempoFinal = System.currentTimeMillis();
		System.out.println("\nTempo = " + (tempoFinal - tempoInicial) / 1000.0 + "s");
		System.out.println("---------------------------------");
		//aguarda receber todos os vetores 
		
		 tag=10000000/nMaquinas;

			for(int i=(int) ((ind)*tag);i<(ind+1)*tag;i++){
				
				vector.put(i, 0.0);
				for(int j=0;j<nMaquinas;j++){
					
					if(thread_accept.vector.get(j).containsKey(i)){
						vector.put(i, vector.get(i)+thread_accept.vector.get(j).get(i));
						thread_accept.vector.get(j).remove(i);
					}
				}
				if(vector.get(i)==0)
					vector.remove(i);
				
			}
		  
		  
		 
			System.out.println(vector.size());
		
		//System.out.println(vector.size());
		
		
		// ------------------------------------------------------------------------------------------

		threads_Ordena.clear();
	}

}
