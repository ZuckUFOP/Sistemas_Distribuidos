package principal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Thread_Accept implements Runnable{
	public Socket csocket;
	public Map<Integer,Double> vet;
	
	Thread_Accept(Socket s){
		this.csocket=s;
		vet = new HashMap<Integer,Double>();
	}
	
	
	@Override
	public void run() {
		ObjectInputStream entrada=null;
		try {
		
			entrada = new ObjectInputStream(csocket.getInputStream());
			vet.putAll((HashMap<Integer,Double>)entrada.readObject());
			System.out.println("Recebido:"+vet.size());
			entrada.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("bbbb");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("aaa");
		}
		
		
	}

}
