import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import classes_auxiliares.Data;

public class Writer {
	private int port=8080,numberOfSubject=3;
	private String ips [] = {"200.239.139.111","200.239.139.23","200.239.139.123"}; 

	public Writer(){
		Random generate = new Random();
		int i=0;
		boolean ok=false;
		Socket cliente;
		while (true)
		{
			ok=false;
			do
			{
				//i=generate.nextInt(numberOfSubject-1);//???
				i=0;
				for(;i<numberOfSubject;i++)
				{
					try {
						//System.out.println("200.239.139.23");
						if (i >= numberOfSubject) {
							ok = true;
							break;
						}
						System.out.println(ips[i]);
						cliente = new Socket(ips[i],port);
						//	System.out.println("Aki1");
						ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
						//		System.out.println("Aki2");
						//output.flush();
						output.writeInt(0);
						output.flush();
						output.writeObject(generateValues());
						output.flush();
						//		System.out.println("Aki3");
						//output.writeInt(0);
						output.close();
						cliente.close();
						ok=true;
					} catch (UnknownHostException e) {
						//			System.out.println("Aki4");
						if(i<numberOfSubject-1)
							i++;
						else 
							i=0;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//			System.out.println("Aki5");
						if(i<numberOfSubject-1)
							i++;
						else 
							i=0;
						//e.printStackTrace();
					}
				}
			}while(!ok);
		}
	}
	public ArrayList<Data> generateValues()
	{	
		Random generate = new Random();
		int numDatas = 1+ generate.nextInt(2);
		ArrayList<Data> datas = new ArrayList<Data>();
		for (int i=0; i < numDatas;i++)
			datas.add(new Data(generate.nextInt(4)+4, generate.nextInt(3)+1, generate.nextInt(300), generate.nextInt(150), generate.nextInt(4)));
		return datas;
	}



	public static void main(String args[])
	{
		new Writer();
	}
}