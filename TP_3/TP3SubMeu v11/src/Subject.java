

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import classes_auxiliares.Data;
import classes_auxiliares.IP;

public class Subject {
	//Variaveis
	private ArrayList<IP> ipServer, ipObserver;
	//private ArrayList<IP> ipWriters;
	//private Map<IP,ArrayList<String>> ipObservers;
	private boolean editavel=true;
	//Criando ArrayList de pontos para cada frame
	private Map<Integer,ArrayList<Data>> frame;
	private Map<Integer,ArrayList<ArrayList<Data>>> frameEstavel;

	private ServerSocket subject; // enviar ArrayList de Data para backup
	//private ServerSocket obsServer;//servers dos observers
	//private ServerSocket reconnecting;//server de reconexao
	//private ServerSocket writers;//Receber dados dos writers
	// Receber ArrayList de Data
	//private Socket client;
	private int porta = 8080,portObserver = 5050;
	//private int serverPort = 12345;
	private int Sucessor=1;
	private int qntFrames;
	public String resposta1 = "Sim", resposta2 = "Nao";
	private int qntEstaveis = 0;

	//Construtor
	public Subject(int frames)
	{
		int i,j,k;
		qntFrames = frames;
		frame = new HashMap<Integer, ArrayList<Data>>();
		frameEstavel = new HashMap<Integer,ArrayList<ArrayList<Data>>>();
		for (i = 0; i < frames; i++)
		{
			frame.put(i, new ArrayList<Data>());
			frameEstavel.put(i, new ArrayList<ArrayList<Data>>());
			for (j=0; j < 300;j++)
			{
				frameEstavel.get(i).add(new ArrayList<Data>());
				for (k=0; k<150;k++)
				{
					frameEstavel.get(i).get(j).add(new Data(1, 0, j, k, i));
				}
			}
		}

		//criando conexao com a mesa
		/*
				Socket first;
				try {
					System.out.println("Aq1");
					first = new Socket("200.239.138.122", porta); // setar ip e porta da mesa
					System.out.println("Aq2");
					ObjectInputStream input = new ObjectInputStream(first.getInputStream());
					try {
						System.out.println("Aq3");
						ipServer= (ArrayList<IP>) input.readObject();
						System.out.println("Aq4"+ipServer);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					input.close();
					first.close();
					MaquinaAtual = 1;
					Sucessor = 2;
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 */
		ipObserver = new ArrayList<>();
		ipObserver.add(new IP("200.239.139.125",true));
		//Adicionar ips observer
		ipServer = new ArrayList<IP>();
		ipServer.add(new IP("200.239.139.123",true));
		ipServer.add(new IP("200.239.139.111",true));
		ipServer.add(new IP("200.239.139.23",true));
		System.out.println("porta"+porta);
		receiveData(porta);
		int sum;
		while(!subject.isClosed()) {
			sum=0;
			//System.out.println("entrou no stateStable");
			for (i=0;i<qntFrames;i++) 
			{
				sum+=frame.get(i).size();
				//System.out.println("for");
			}
			//System.out.println("sum: "+sum);
			if (sum >=10) 
			{
				this.stateStable();
				//System.out.println("entrou no if stateStable");
				sum=0;
			}
		}
	}

	public Subject(ArrayList<IP> recebido, int frames) {
		int i,j,k;
		qntFrames = frames;
		frame = new HashMap<Integer, ArrayList<Data>>();
		frameEstavel = new HashMap<Integer,ArrayList<ArrayList<Data>>>();
		for (i = 0; i < frames; i++)
		{
			frame.put(i+1, new ArrayList<Data>());
			frameEstavel.put(i, new ArrayList<ArrayList<Data>>());
			for (j=0; j < 300;j++)
			{
				frameEstavel.get(i+1).add(new ArrayList<Data>());
				for (k=0; k<150;k++)
				{
					frameEstavel.get(i+1).get(j).add(new Data(1, 0, j, k, i));
				}
			}
		}
		ipServer = new ArrayList<IP>(recebido);
		//MaquinaAtual = 0;
		Sucessor = 1;
		receiveData(porta);
		int sum;
		while(!subject.isClosed()) {
			sum=0;
			for (i=0;i<qntFrames;i++) {
				sum+=frame.get(i).size();
			}
			if (sum >=10) {
				this.stateStable();
			}
		}
	}

	//Funcoes relevantes
	//Recebe os dados (canal de comunicacão de recepicao
	public void receiveData(int porta) // servidor
	{
		try {
			subject = new ServerSocket(porta);
			System.out.println("Servidor ouvindo a porta: "+porta);
			new Thread()
			{
				public void run()
				{
					//String ipCliente;
					//int porta;
					Socket cliente;
					while(!subject.isClosed())
					{
						if (!editavel)
							continue;
						try {
							System.out.println("esperando cliente");
							cliente = subject.accept();
							System.out.println("Cliente conectado: "+cliente.getInetAddress().getHostAddress());
							//ipCliente = cliente.getInetAddress().getHostAddress();
							//porta = client.getPort();
							//System.out.println("vai alocar Input");
							ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
							//System.out.println("syso");
							try {
								int times = input.readInt();
								ArrayList<Data> dataReceived= (ArrayList<Data>) input.readObject();
								//System.out.println("leu arquivo em arraylist");
								//System.out.println("salvou int");
								for(Data it : dataReceived)
								{
									//System.out.println("adicionando em frames corretos");
									switch(it.getFramePos())
									{
									case 0:
										//Adicionando valores ao frame 1
										frame.get(0).add((Data) it);
										//if(frame1.frame.size()==50){} // Realizar envio para arquivo de backup xD
										break;
									case 1:
										//Adicionando valores ao frame 2
										frame.get(1).add((Data) it);
										break;
									case 2:
										//Adicionando valores ao frame 3
										frame.get(2).add((Data) it);
										break;
									case 3:
										//Adicionando valores ao frame 4
										frame.get(3).add((Data) it);
										break;
									default:
										System.out.println("Frame Invalido!");
										break;
									}
								}
								//System.out.println(ipServer.get(Sucessor).getIp()+ipServer.get(Sucessor).isState());
								if (ipServer.get(Sucessor).isState() && times < Subject.this.ipServer.size()-1)
								{
									System.out.println("replicando pra prox ip e chamando sendData");
									new Thread(){
										public void run() {
											sendData(ipServer.get(Sucessor).getIp(), porta, dataReceived, times);
										}
									}.start();
								}
								if (times == 0) 
								{
									new Thread(){
										public void run() {
											sendDataObserver(dataReceived);
										}
									}.start();
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}.start();	
		} catch (UnknownHostException e) {
			//Chamar troca de server para servidor de backup 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	//Envia os dados
	public void sendData(String ipServer, int port,ArrayList<Data> pointsCollections, int times)
	{

		times++;
		//System.out.println("times "+times);
		Socket cliente;
		try {
			times++;
			cliente = new Socket(this.ipServer.get(Sucessor).getIp(),port);
			ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
			output.writeInt(times);
			output.flush();			
			output.writeObject(pointsCollections);
			output.flush();
			cliente.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				times++;
				cliente = new Socket(this.ipServer.get(Sucessor+1).getIp(), port);
				ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
				output.writeInt(times);
				output.flush();				
				//ystem.out.println("alocou output e deu flush");
				output.writeObject(pointsCollections);
				output.flush();
				cliente.close();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		}

	}

	//enviar dados p/ observer
	public void sendDataObserver(ArrayList<Data> pointsCollections)
	{
		
		try {
			for(int it=0; it < ipObserver.size();it++)
			{
				Socket cliente;
				cliente = new Socket(ipObserver.get(it).getIp(),portObserver);
				ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
				new Thread()
				{
					public void run()
					{
						while(!cliente.isClosed())
						{
							try 
							{
								output.writeObject(pointsCollections);
								output.flush();
								output.close();
								cliente.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
						}
					}
				}.start();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Atualiza o estado do frame virtual
	public void stateStable()
	{
		int i, j;
		Map<Integer, ArrayList<Data>> auxframe = new HashMap<Integer, ArrayList<Data>>(frame);
		//System.out.println("CHAMOU STATESTABLE");

		System.out.println("vai enviar ao observer: ");
		for (i=0; i<qntFrames; i++)
		{
			//sendDataObserver(auxframe.get(i));
			System.out.println("Enviou!");
			System.out.println("auxFrame: "+auxframe.toString());
			for(Data data : auxframe.get(i))
			{
				for(j = 0; j < data.getSize();j++)
				{
					if (data.getPosX()+j<=299)
						frameEstavel.get(data.getFramePos()).get(data.getPosX()+j).get(data.getPosY()).setColor(data.getColor());
					else
						frameEstavel.get(data.getFramePos()).get(0 + (300-data.getPosX()+j)).get(data.getPosY()).setColor(data.getColor());
				}
			}
		}
		//System.out.println("mandou");
		qntEstaveis++;
		editavel = false;
		//System.out.println("vai entrar no for");
		for(i=0; i < qntFrames; i++)
		{
			frame.get(i).clear();
		}
		//System.out.println("limpou frames");
		editavel = true;
	}

	public static void main(String args[])
	{
		new Subject(4);
	}
}

