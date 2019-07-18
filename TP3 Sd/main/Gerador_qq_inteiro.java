package main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

public final class Gerador_qq_inteiro {

	/**
	 * @param args
	 */
	public String name;
	public Gerador_qq_inteiro(){
		gera1GB();
	}
	
	public void gera1GB(){
		try{
			//file 1GB = 1.000MB = 1.000.000KB = 1.000.000.000B = 250 milhoes de ints de 32 bits
						
			//numero -2bi a +2bi
			//+- 2.000.000.000
			Random r = new Random();
			String primeiraParte, segundaParte, terceiraParte, quartaParte;
			File f = new File("out"+name+".bin");
			OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
			for(int k=0; k<1000; k++){
				byte[] buf = new byte[1000000];
				for(int i=0; i<1000000; i=i+4){
					primeiraParte = String.valueOf(r.nextInt(2));
					segundaParte = String.valueOf(r.nextInt(1000));
					terceiraParte = String.valueOf(r.nextInt(1000));
					quartaParte = String.valueOf(r.nextInt(1000));
					int tamanhoNum = r.nextInt(4);
					byte[] bytes = new byte[4];
					switch (tamanhoNum){
					case 0:{
						int posNeg = r.nextInt(2);
						if(posNeg==0)
							bytes = ByteBuffer.allocate(4).putInt(new Integer(quartaParte).intValue()).array();
						else bytes = ByteBuffer.allocate(4).putInt(- new Integer(quartaParte).intValue()).array();
						break;
					}
					case 1:{
						int posNeg = r.nextInt(2);
						if(posNeg==0)
							bytes = ByteBuffer.allocate(4).putInt(new Integer(terceiraParte+quartaParte).intValue()).array();
						else bytes = ByteBuffer.allocate(4).putInt(- new Integer(terceiraParte+quartaParte).intValue()).array();
						break;
					}
					case 2:{
						int posNeg = r.nextInt(2);
						if(posNeg==0)
							bytes = ByteBuffer.allocate(4).putInt(new Integer(segundaParte+terceiraParte+quartaParte).intValue()).array();
						else bytes = ByteBuffer.allocate(4).putInt(- new Integer(segundaParte+terceiraParte+quartaParte).intValue()).array();
						break;
					}
					case 3:{
						int posNeg = r.nextInt(2);
						if(posNeg==0)
							bytes = ByteBuffer.allocate(4).putInt(new Integer(primeiraParte+segundaParte+terceiraParte+quartaParte).intValue()).array();
						else bytes = ByteBuffer.allocate(4).putInt(- new Integer(primeiraParte+segundaParte+terceiraParte+quartaParte).intValue()).array();
						break;
					}
					}
					buf[i] = bytes[0];
					buf[i+1] = bytes[1];
					buf[i+2] = bytes[2];
					buf[i+3] = bytes[3];
					//System.err.println(ByteBuffer.wrap(bytes).getInt());
				}
				out.write(buf);
				out.flush();
				buf=null;
			}
			
			out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
