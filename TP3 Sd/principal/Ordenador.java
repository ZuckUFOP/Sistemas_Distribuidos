package principal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Ordenador implements Runnable{

	private String name;
	private int nucleos;
	public int tam;
	public int flag;
	public int ind;
	public List<Map<Integer,Integer>> vector;
	public List<Map<Integer,Integer>> vectorOrd;
	public Map<Integer,Double> Ordenado;
	public void setNucleos(int n){nucleos=n;}
	public void setName(String n){name =n;}
	public int tag;
	@Override
	public void run() {
		if(flag==0){
		File arquivo = new File("out"+name+".bin");
		vector = new ArrayList<Map<Integer,Integer>>();
		vectorOrd = new ArrayList<Map<Integer,Integer>>();
		Map<Integer,Integer> v;
		Map<Integer,Integer> v1;
		for(int i=0;i<nucleos;i++){
			v=new HashMap<Integer,Integer>();
			vector.add(v);
			v1=new HashMap<Integer,Integer>();
			vectorOrd.add(v1);
			
		}
		tag=10000000;
		tag/=nucleos;
		
		try{
			
			  InputStream in = new BufferedInputStream(new FileInputStream(arquivo));
			  byte[] bit = new byte[524288];  
			  while(in.read(bit)==524288){
				IntBuffer intBuff = ByteBuffer.wrap(bit).order(ByteOrder.BIG_ENDIAN).asIntBuffer();	 
				int [] arrays = new int[intBuff.remaining()];
				intBuff.get(arrays);
				
				for(int j=0;j<tam;j++){
					
				for(int i=0;i<nucleos;i++){
					
					if(arrays[j]<tag*(i+1)){	
						if(vector.get(i).containsKey(arrays[j])){
							vector.get(i).put(arrays[j],vector.get(i).get(arrays[j])+1);
						break;
							
						}else{
							vector.get(i).put(arrays[j],1);
							break;
							}
						}
				}
				
				}
				
			  }
			
			  in.close();	
			 // System.out.println("Thread:"+name+ " |Tamanho:"+vector.get(0).size());
			  

			  
		}catch (Exception e){}
		
		}else{
			Ordenado=new HashMap<Integer,Double>();
			tag=10000000/nucleos;

			for(int i=(ind-1)*tag;i<ind*tag;i++){
				
				Ordenado.put(i, 0.0);
				for(int j=0;j<nucleos;j++){
					
					if(vectorOrd.get(j).containsKey(i)){
						Ordenado.put(i, Ordenado.get(i)+vectorOrd.get(j).get(i));
						vectorOrd.get(j).remove(i);
					}
				}
				if(Ordenado.get(i)==0)
					Ordenado.remove(i);
				
			}
			//System.out.println("Thread:"+name+"|Tamanho:"+Ordenado.size());
			
		
			
		}
		
	}
	
	public void print(){
		tag=10000000/nucleos;
		for(int i=(ind-1)*tag;i<ind*tag;i++){
			if(Ordenado.containsKey(i)){
				if(Ordenado.get(i)>100)
				System.out.println("Thread:"+name+"|indice:"+i+"|quantidade:"+Ordenado.get(i));
				
			
			}

		}
		
	}
	
}
