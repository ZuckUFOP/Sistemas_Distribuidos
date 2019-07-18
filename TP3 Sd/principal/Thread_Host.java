package principal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Thread_Host implements Runnable{
	public String ip;
	public Map<Integer,Double> vector;
	Thread_Host(){
		vector= new HashMap<Integer,Double>();
	}
	
	@Override
	public void run() {
		
		ObjectOutputStream saida = null;
		Socket cliente = null;
		try {
			cliente = new Socket(ip, 12345);
			saida = new ObjectOutputStream(cliente.getOutputStream());
			System.out.println("Enviado:"+vector.size()  );
			saida.writeObject(vector);
			cliente.close();
			saida.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
