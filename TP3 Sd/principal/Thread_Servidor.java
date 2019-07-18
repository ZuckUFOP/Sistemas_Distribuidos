package principal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Thread_Servidor implements Runnable{
	public Socket csocket;
	public int flag;
	List<Map<Integer,Double>> vector;
	
	Thread_Servidor(int flag) {
	      this.flag=flag;
	   }
	@Override
	public void run() {
		vector = new ArrayList<Map<Integer,Double>>();
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		List<Thread_Accept> thread_accept=null;
		List<Thread> t1=new ArrayList<Thread>();
		Thread_Accept ta=null;
		Thread t=null;
		csocket =null;
		int i=0,test=0;
		try {
			ServerSocket accept = new ServerSocket(12345);
			thread_accept=new ArrayList<Thread_Accept>();
			while (true) {
				csocket = accept.accept();
				ta= new Thread_Accept(csocket);
				thread_accept.add(ta);
				t = new Thread(thread_accept.get(i));
				t1.add(t);
				t1.get(i).start();
				i++;
				if(i==flag)
					break;
			   }
		
		accept.close();
		
			
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("cccc");
		}
		while (true) {
			test = 0;
			for (int j = 0; j < i; j++)
				if (!t1.get(j).isAlive()) {
					test += 1;
				}

			if (test == i)
				break;
		}
		
		
		for(int j=0;j<i;j++){
			map.putAll(thread_accept.get(j).vet);
			vector.add(map);
		}
		thread_accept.clear();
		
		
		
	}

}
