package com.sur.disjoint;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Surajit
 *
 */
public class MazeGeneration {

	private int horizontal;
	private int vertical;
	protected boolean[][] hWalls;
	protected boolean[][] vWalls;
	LinkedHashMap<Integer, String> lhm1=new LinkedHashMap<>();
	LinkedHashMap<Integer, String> lhm2=new LinkedHashMap<>();
	public DisjSets disjoin;
	public MazeGeneration(int x,int y)
	{
		horizontal=x;
		vertical=y;
		disjoin=new DisjSets(x*y);
		
		if ((x < 1) || (y < 1) || ((x == 1) && (y == 1))) {
		      return;                                    
		    }

		    // all horizontal wall will be created first,
		   //then it will be removed based on the operation
		    if (x > 1) {
		      hWalls = new boolean[x+1][y+1];
		      for (int i = 0; i< x+1; i++) {
		        for (int j = 0; j < y+1; j++) {
		        	//if(j!=y)
		          hWalls[i][j] = true;
		        }
		      }
		    }
		 // all vertical wall will be created first,
	     //then it will be removed based on the operation
		    if (y > 1) {
		      vWalls = new boolean[x+1][y+1];
		      for (int i = 0; i < x+1; i++) {
		        for (int j = 0; j < y+1; j++) {
		        	if(!(i==0 && j==0 ) )
		        	{
		        		if((i==x-1 && j==y)) {
		        			
		        			 vWalls[i][j] =false;
		        		}else
		        		 vWalls[i][j] = true;	
		        	}
		         
		        }
		      }
	}
	}
	
 public String toString() {
				    
String s="";
for(int i=0;i<horizontal+1;i++)
{
	for(int j=0;j<vertical+1;j++)
	{
		if(j!=vertical) {
			if(hWalls[i][j])
			{
				s=s+"+-+";
			}else
				s=s+"   ";
			
		}
		}
			
		
		
	s=s+"\n";
	if(i<vertical && i<horizontal) {
	for(int k=0;k<vertical+1;k++)
	{
		if(vWalls[i][k] )
		{
			s=s+"|  ";
		}else {
			s=s+"   ";
		}	
	}
	}
	s=s+"\n";
	
}
				   
				    return s + "\n";
				  }
 
 //size of disjointSet
 private int sizeOfDisjoint(DisjSets dis)
 {
	 int disjointelement=0;
	 int[] arraydis=dis.getS();
	 for(int i=0;i<arraydis.length;i++)
	 {
		 if(arraydis[i]==-1) {
			 disjointelement++;
		 }
	 }
	 return disjointelement;
 }
 
	//method will randomly do find and union operation
	public void generateFindUnion(DisjSets dis,int horizontal,int vertical)
	{
		int counter=0;
		int currentElement=0;
		int nextElement=0;
		int endpoint=dis.getS().length-1;
		//second approach
		Random rand=new Random();
		while(sizeOfDisjoint(dis)>1)
		{
			
			int cell1=rand.nextInt(horizontal*vertical);
			int cell2=rand.nextInt(horizontal*vertical);
			 if(dis.find(cell1)!=dis.find(cell2) ) {
				if(Math.abs(cell1-cell2)==1 && (cell1/vertical==cell2/vertical)) {
					
					if(cell1>cell2)
					{
						dis.union(dis.find(cell2), dis.find(cell1));
					lhm1.put(cell2, "horizontal");	
					}else if(cell2>cell1)
					{
						dis.union(dis.find(cell1), dis.find(cell2));
						lhm1.put(cell1,"horizontal");
					}else {
						//donothing
					}
				}
				else if(Math.abs(cell1-cell2)==vertical) {
					if(cell1>cell2)
					{
						dis.union(dis.find(cell2), dis.find(cell1));
					lhm1.put(cell2, "vertical");	
					}else if(cell2>cell1)
					{
						dis.union(dis.find(cell1), dis.find(cell2));
						lhm1.put(cell1,"vertical");
					}else {
						//do nothing
					}
					
				}
				
			//check whether nodes with same parent has the connectivity	
			}else {
               if(Math.abs(cell1-cell2)==1 && (cell1/vertical==cell2/vertical)) {
					
					if(cell1>cell2)
					{
						
					lhm2.put(cell2, "horizontal");	
					}else if(cell2>cell1)
					{
					
						lhm2.put(cell1,"horizontal");
					}else {
						//donothing
					}
				}
				else if(Math.abs(cell1-cell2)==vertical) {
					if(cell1>cell2)
					{
						
					lhm2.put(cell2, "vertical");	
					}else if(cell2>cell1)
					{
						
						lhm2.put(cell1,"vertical");
					}else {
						//do nothing
					}
			}
		}
			 }
		
		
	}	
	//update the maze walls
	private void updateWalls()
	{
		Set<Integer> keys1=lhm1.keySet();
		for(Integer key:keys1)
		{
			if("horizontal".equals(lhm1.get(key)))
			{
				int row=key/vertical;
				int col=key%vertical+1;
				vWalls[row][col]=false;
			}
			else if("vertical".equals(lhm1.get(key)))
			{
				int row=key/vertical+1;
				int col=key%vertical;
				hWalls[row][col]=false;
			}
		}
		//lhm1.clear();
		//update for all same parent wall
		Set<Integer> keys2=lhm2.keySet();
		for(Integer key:keys1)
		{
			if("horizontal".equals(lhm2.get(key)))
			{
				int row=key/vertical;
				int col=key%vertical+1;
				vWalls[row][col]=false;
			}
			else if("vertical".equals(lhm2.get(key)))
			{
				int row=key/vertical+1;
				int col=key%vertical;
				hWalls[row][col]=false;
			}
		}
		
		
	}
	//cross check the path created by previous methods
	public void crossCheck(DisjSets dis)
	{
		
		//check all elements if they have same parent then they must be connected alternate cells
		for(int i=vertical;i<dis.getS().length-1;)
		{
			//random horizontal check
			if((i/vertical==0)||(i/vertical==vertical-1))
			{
				int tmp=i;
				for(int j=0;j<vertical-1;j++)
				{	if(tmp!=dis.getS().length-1) {
				  if(dis.find(tmp)==dis.find(tmp+1))
				  lhm1.put(tmp, "horizontal");
				  tmp=tmp+1;
				}
				}
				//random vertical check 
				i=i+10;
			}else
			if((i/vertical!=0)&&(i/vertical <horizontal-1)){
				 if(dis.find(i)==dis.find(i+vertical))
					  lhm2.put(i, "vertical");
					  
					}
			i=i+10;
				
			}
		updateWalls();
		}
		
		
		
		
	
	//creating path from left top to right bottom
	public void createMaze()
	{
		generateFindUnion(disjoin,horizontal,vertical);
		updateWalls();
		crossCheck(disjoin);
	}
	
	
	public static void main(String[] args) {
	try {	
	Scanner sc=new Scanner(System.in);
	System.out.println("Please enter the row and column no. separated by , Row and Column would be minimum 20*20");
	String[] scStr=sc.next().split(",");
	if(Integer.parseInt(scStr[0])<20 && Integer.parseInt(scStr[1]) <20)
	{
		System.out.println("Please read the instruction carefully for minimum  requirement of inputs.");
	}
	else {
	    System.out.println("your input is:"+"Row>>"+scStr[0]+"  Coloumn>>"+scStr[1]);
	
		MazeGeneration mg=new MazeGeneration(Integer.parseInt(scStr[0]),Integer.parseInt(scStr[1]));
		System.out.println("maze with initial state>>>");
		System.out.println(mg);
		
		mg.createMaze();
		System.out.println("maze after path creation>>>");		
        System.out.println(mg);
	}
	}catch(Exception e)
	{
		System.out.println("Please read the instruction for inputs carefully!");
	}
	

}
}