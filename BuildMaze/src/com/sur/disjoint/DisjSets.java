package com.sur.disjoint;

public class DisjSets {
	private int [ ] s;
	
	/**
	 * Construct the disjoint sets object.
	 * @param numElements the initial number of disjoint sets.
	 */
	public DisjSets( int numElements )
	 { 
		s = new int [ numElements ];
		for( int i = 0; i < s.length; i++ )
		s[ i ] = -1;
		
		
	 }
	
	/**
	 * Union two disjoint sets.
	 * For simplicity, we assume root1 and root2 are distinct
	 * and represent set names.
	 * @param root1 the root of set 1.
	 * @param root2 the root of set 2.
	 */
	 public void union( int root1, int root2 )
	{ 
		 
		 s[ root2 ] = root1;
		 
	}
	 
	 /**
	  * Perform a find.
	  * Error checks omitted again for simplicity.
	  * @param x the element being searched for.
	  * @return the set containing x.
	  */
	 public int find( int x )
	 { 
		 if( s[ x ] < 0 )
			 return x;
			  else
			  return find( s[ x ] );
		 
	 }
	 
	
 public int[] getS() {
		return s;
	}

	public void setS(int[] s) {
		this.s = s;
	}

}
