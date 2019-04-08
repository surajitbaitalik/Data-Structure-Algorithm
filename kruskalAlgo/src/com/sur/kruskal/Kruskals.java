package com.sur.kruskal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;





/**
 * @author Surajit
 *
 */
public class Kruskals {
    //map to store user defined keys  and source
	static Map<Integer,String> vertexMap=new HashMap<>();
	//map to store vertex name and their values of vertexmap
	static Map<String,Integer> mapVertex=new HashMap<>();
	//map to feed disjoint sets and binary heap
	static Map<String,List<Vertex>> mapdis=new HashMap<>();
	static Map<String,String> tempMap=new LinkedHashMap<>();
	static int NUM_VERTICES=0;
	@SuppressWarnings("resource")
	public static void loadFile() throws IOException
	{
		File file=new File("assn9_data.csv");
		BufferedReader br=new BufferedReader(new FileReader(file));
		String str;
		int i=0;
		
		while((str =br.readLine())!=null)
		{
			String[] starr=str.split(",");
			
			
			vertexMap.put(i, starr[0]);
			tempMap.put(starr[0], str.substring( starr[0].length()+1));
				
			i++;
			
		}
		
	}
	public static void loadMapVertex(Map<Integer,String> vm)
	{
		for(Integer key:vm.keySet())
		{
			
			mapVertex.put(vm.get(key),key);
			
		}
		
	}
	public static void generateEdge()
	{
		
		for(String key:tempMap.keySet()) {
			
			String[] value=tempMap.get(key).split(",");
			for(int i=0;i<value.length;i=i+2)
			{
				if(mapdis.containsKey(value[i+1]))
				{
					List<Vertex> lv = mapdis.get(value[i+1]);
					lv.add(new Vertex(mapVertex.get(key),mapVertex.get(value[i])));
					mapdis.put(value[i+1], lv);
					
				}else {
					List<Vertex> lv=new ArrayList<>();
					lv.add(new Vertex(mapVertex.get(key),mapVertex.get(value[i])));
					mapdis.put(value[i+1], lv);
				}
				
			}
			
		}
	}
	
	//Kruskal Theorem for minimum spanning tree
	public static  void kruskal(DisjSets disj,BinaryHeap<Integer> h,Map<String,List<Vertex>> mapdis)
	{
	    int edgesAccepted = 0;
	    int u, v;
        int edge;
        int distance=0;
        System.out.println("edges are>>");
	    while (edgesAccepted < NUM_VERTICES-1)
	    {
	    	edge = h.deleteMin( ); 
	    	List<Vertex> liv=mapdis.get(edge+"");
	    	for(Vertex ver:liv)
	    	{
	    		u=ver.source;
	    		v=ver.destination;
	    		if(disj.find(u)!=disj.find(v))
	    		{
	    			distance=distance+edge;
	    			edgesAccepted++;
	    			
	    			disj.union(disj.find(u), disj.find(v));
	    			String source=vertexMap.get(u);
	    			String destination=vertexMap.get(v);
	    			System.out.println(source+".....>"+destination+" "+".........distance...."+edge);
	    			
	    		}
	    		
	    	}
	        
	   }
	    
	    System.out.println("Sum of all distances>>>"+distance);
	} 

	public static class Vertex {
		int source;
		int destination;
		public Vertex(int source,int destination)
		{
			this.source=source;
			this.destination=destination;
			
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Vertex [source=" + source + ", destination=" + destination + "]";
		}

		}
	
	
	
	
	
public static void main(String[] args) {
try {
	
	loadFile();
	loadMapVertex(vertexMap);
	generateEdge();
	NUM_VERTICES=vertexMap.size();
	DisjSets disj=new DisjSets(NUM_VERTICES);
	BinaryHeap<Integer> h = new BinaryHeap<>( );
	
	
for(String key:mapdis.keySet())
{
	h.insert(Integer.parseInt(key));
}
	kruskal(disj,h,mapdis);
	
	
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		
	}

}
