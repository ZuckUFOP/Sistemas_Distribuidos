package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class Gerador_Randomico {

	public static void main(String[] args) {
		File arquivo = new File("output_10GB.txt");
		Random rand= new Random();
		try( OutputStream os = new FileOutputStream(arquivo) ){
		 int b; 
			for(int i=0;i<10000;i++){
				
				for(int j=0;j<100000;j++){
					for(int w=0;w<10;w++){
					  b =rand.nextInt(10000000);
					  os.write( b );
					  os.flush();
					}
				}
				  
			}
			os.close();
		}catch(IOException ex){
		  ex.printStackTrace();
		}
		
		
		
		
		
		
	}

}
