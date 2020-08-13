package sxb180026;

import java.lang.reflect.Array;

/* Starter code for LP3 */

//Change this to netid of any member of team

/*
 *
 * @author Surajit Baitalik (sxb180026)

 */

import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;






/*Skeleton for skip list implementation.*/

/**
 * @author Surajit
 *
 * @param <T>
 */
public class SkipList<T extends Comparable<? super T>> {
	static final int maxLevel = 32;
	
	

	@SuppressWarnings("unchecked")
	static class Entry<E> {
		E element;
		Entry<E>[] next;
		int height;
		int[] width;

		public Entry(E x, int lev) {
			element = x;
			next = new Entry[lev];
			height = lev;
			width = new int[lev];
		}
	}
	
	public int achievedLevel;// this level is to track the exact max level of the SkipList
	private Entry<T> head, tail;
	private int size;
	private Entry<T> last; // to track last non null node
	private Predecessor[] predecessor;
	Random rand;
	

	/**
	 * Constructor of SkipList
	 */
	public SkipList() {
		head = new Entry<T>(null, maxLevel + 1);//the upper most level is kept empty with no node
		tail = new Entry<T>(null, maxLevel + 1);
		size=0;
		achievedLevel=1;
		rand = new Random();

		/* connect each level of head with tail */
		for (int i = 0; i <=maxLevel; i++) {
			head.next[i] = tail;
			head.width[i] = 1; // as tail is 1 distance away from head
			
		}

	}
	 
	 /**
	 * This class will store predecessor details with their width
	 *
	 */
	public class Predecessor {
	        private Entry<T> node;
	        private int totalWidth;

	        public Predecessor(Entry<T> node, int totalWidth) {
	            this.node = node;
	            this.totalWidth = totalWidth;
	        }
	    }
	
	
	
	

	/**
	 * Add x to list. If x already exists, reject it. Returns true if new node is
	 * 
	 * @param x
	 * @return true if x is added
	 */
	public boolean add(T x) {
		if (null==x || contains(x)) {

			return false;
		}
		int height = chooseHeight();
		Entry<T> node = new Entry<T>(x, height+1);
		   int distance = 0;
	        for (int i = 0; i <= maxLevel; i++) {
	        	//will create predecessor of each level  
	            Entry<T> pred = i < predecessor.length ? predecessor[i].node : head;
	            if (i <= height) {
	            	node.next[i] = pred.next[i];
	            	pred.next[i] = node;
	            	node.width[i] = Math.max(pred.width[i]-distance , 1);
	                pred.width[i] = distance+ 1;

	                distance += predecessor[i].totalWidth;
	            } else {
	                pred.width[i]++;
	            }
	        }
		if (node.next[0].element == null) {
			last = node;
		}
		size++;
		return true;
	}

	/**
	 * helper method to assign height to a newly added node
	 * 
	 * @return height
	 */
	public int chooseHeight() {
		int height = 1 + Integer.numberOfLeadingZeros(rand.nextInt());
		height = Math.min(height, achievedLevel+1);// to allow maxLevel to grow gradually--for better performance
		// achievedLevel+ 1
		achievedLevel=Math.max(height,achievedLevel );
		return height; 
		
	}

	/**
	 * helper method to get predecessor
	 * 
	 * @param x
	 * @return predecessor array
	 */
	@SuppressWarnings("unchecked")
	public  void findPred(T x) {
		predecessor =(Predecessor[])Array.newInstance(Predecessor.class, Math.min(achievedLevel+2, maxLevel+1));
	        Entry<T> p = head;
	        for (int i = Math.min(maxLevel, achievedLevel+1 ) ; i >= 0; i--) {
	            int jump = 0;
	            while (p.next[i].element!= null && x.compareTo(p.next[i].element) > 0) {
	            	jump += p.width[i];// distance we have to traverse in each level
	                p = p.next[i];
	            }
	            predecessor[i] = new Predecessor(p, jump);
	        }
	}

	/**
	 * Find smallest element that is greater or equal to x
	 * 
	 * @param x
	 * @return
	 */
	public T ceiling(T x) {
		if (contains(x)) {
			return x;
		}

		return predecessor[0].node.next[0].element;
	}

	/**
	 * Check if the element is in the list or not
	 * 
	 * @param x
	 * @return True/False
	 */
	public boolean contains(T x) {
		
		findPred(x);  // it will update the Predecessor Array 
		Entry<T> cursor=predecessor[0].node;//cursor is pointing to the same or previous node 
		
		return cursor.next[0].element != null ? cursor.next[0].element.equals(x) : false;
	}

	/**
	 * @return first element of list
	 */
	public T first() {

		return head.next[0].element;
	}

	/**
	 * Find largest element that is less than or equal to x
	 * 
	 * @param x
	 * @return element
	 */
	public T floor(T x) {
		if (contains(x)) {
			return x;
		}
		return predecessor[0].node.element;
	}

	/**
	 * 
	 * @param n
	 * @return Return element at index n of list,first element is at index 0
	 */
	public T get(int n) {
		if (n >= size) {
			
			return null;
		}
		//return getLinear(n); // taking too much time for sk-t13.txt test cases
		return getLog(n);
	}

	/**
	 * O(n) algorithm for get(n)
	 * SkipList Iterator has been used here.
	 *  @param n
	 * @return element at index n
	 */
	public T getLinear(int n) {
		SkipListIterator<T> iterator = new SkipListIterator<T>(0);
		int index =0;
        T element = null;
        while(iterator.hasNext() && index<=n)
        {	element=iterator.next();
        	index++;
        }
        return element;
    }

		
	

	/**
	 * Optional operation: Eligible for EC. O(log n) expected time for get(n).
	 * 
	 * @param n
	 * @return element at index n
	 */
	public T getLog(int n) {
		 int span = 0;
	        Entry<T> node = head;
	        for (int i = achievedLevel; i >= 0; i--) {
	            while (node.next[i] != null && span + node.width[i] <= n) {
	            	span += node.width[i];
	                node = node.next[i];
	            }
	        }
	        return  node.next[0]!=null?node.next[0].element:null;
	}

	/**
	 * @return true if it is empty else false
	 */
	public boolean isEmpty() {

		return size == 0;
	}

	/**
	 * Iterate through the elements of list in sorted order
	 */
	public Iterator<T> iterator() {

		return new SkipListIterator<>(maxLevel);
	}

	/**
	 * find and return the last element of the Skiplist
	 * 
	 * @return last element
	 */
	public T last() {

		return last!=null?last.element:null;
	}

	/**
	 * Reorganize the elements of the list into a perfect skip list
	 * 
	 */
	public void rebuild() {
		
		
		

	}

	/**
	 * Remove x from list.
	 * 
	 * @param x
	 * @return Removed element else null if x not in list
	 */
	public T remove(T x) {
		if (!contains(x))
            return null;

        Entry<T> node = predecessor[0].node.next[0];
        if (node.next[0].element == null) {
            this.last = predecessor[0].node;
        }
        for (int i = 0; i <= maxLevel; i++) {
            Entry<T> pred = i < predecessor.length ? predecessor[i].node : head;
            if (pred.next[i].element!= null && pred.next[i].element.equals(x)) {
            	pred.width[i] += node.width[i] - 1;
            	pred.next[i] = node.next[i];
            } else {
            	pred.width[i]--;
            }

        }
        size=size--;
        return (T) node.element;
	}

	/**
	 * @return size of SkipList
	 */
	public int size() {
		return size;
	}
	
    /**
     * Iterator implemented for the SkipList
     */

 
	private class SkipListIterator<T> implements Iterator<T> {
 
        Entry<T> iter;
        int level;

        @SuppressWarnings("unchecked")
		SkipListIterator(int level) {
            this.level = level;
            iter =  (Entry<T>) head.next[level];
        }

        @Override
        public boolean hasNext() {
            if (iter != null && iter.element != null) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            Entry<T> node = null;
            if (hasNext()) {
                node = iter;
                iter = iter.next[level];
            }
            return node == null ? null : node.element;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }
    }
	
	

	public static void main(String args[]) {
		SkipList<Double> skipList = new SkipList<>();
		skipList.add(Double.valueOf(20));
		skipList.add(Double.valueOf(15));
		skipList.add(Double.valueOf(25));
		skipList.add(Double.valueOf(30));
		skipList.add(Double.valueOf(10));
		skipList.add(Double.valueOf(40));
		
		System.out.println("get element at index 2>>" + skipList.get(2));
		System.out.println("ceilling of 12>>" + skipList.floor(Double.valueOf(16)));
		System.out.println("Last element>>" + skipList.last());
		System.out.println("is Empty()?>>" + skipList.isEmpty());
		
		
		

	}

}
