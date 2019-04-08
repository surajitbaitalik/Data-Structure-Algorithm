package com.sur.btree;


import java.util.Random;

//BinarySearchTree class
//
//CONSTRUCTION: with no initializer
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//void remove( x )       --> Remove x
//boolean contains( x )  --> Return true if x is present
//Comparable findMin( )  --> Return smallest item
//Comparable findMax( )  --> Return largest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//void printTree( )      --> Print tree in sorted order
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
* Implements an unbalanced binary search tree.
* Note that all "matching" is based on the compareTo method.
* @author Mark Allen Weiss
*/
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>
{
 /**
  * Construct the tree.
  */
   public BinarySearchTree( )
   {
      root = null;
   }

 /**
  * Insert into the tree; duplicates are ignored.
  * @param x the item to insert.
  */
   public void insert( AnyType x )
   {
      root = insert( x, root );
   }

 /**
  * Remove from the tree. Nothing is done if x is not found.
  * @param x the item to remove.
  */
   public void remove( AnyType x )
   {
      root = remove( x, root );
   }

 /**
  * Find the smallest item in the tree.
  * @return smallest item or null if empty.
 * @throws Exception 
  */
   public AnyType findMin( ) throws Exception
   {
      if( isEmpty( ) )
         throw new Exception( );
      return findMin( root ).element;
   }

 /**
  * Find the largest item in the tree.
  * @return the largest item of null if empty.
 * @throws Exception 
  */
   public AnyType findMax( ) throws Exception
   {
      if( isEmpty( ) )
         throw new Exception( );
      return findMax( root ).element;
   }

 /**
  * Find an item in the tree.
  * @param x the item to search for.
  * @return true if not found.
  */
   public boolean contains( AnyType x )
   {
      return contains( x, root );
   }

 /**
  * Make the tree logically empty.
  */
   public void makeEmpty( )
   {
      root = null;
   }

 /**
  * Test if the tree is logically empty.
  * @return true if empty, false otherwise.
  */
   public boolean isEmpty( )
   {
      return root == null;
   }

 /**
  * Print the tree contents in sorted order.
  */
   public void printTree( )
   {
      if( isEmpty( ) )
         System.out.println( "Empty tree" );
      else
       
         printTree( root) ;
   }

 /**
  * Internal method to insert into a subtree.
  * @param x the item to insert.
  * @param t the node that roots the subtree.
  * @return the new root of the subtree.
  */
   private BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
   {
      if( t == null )
         return new BinaryNode<>( x, null, null );
     
      int compareResult = x.compareTo( t.element );
         
      if( compareResult < 0 )
         t.left = insert( x, t.left );
      else if( compareResult > 0 )
         t.right = insert( x, t.right );
      else
         ;  // Duplicate; do nothing
      return t;
   }

 /**
  * Internal method to remove from a subtree.
  * @param x the item to remove.
  * @param t the node that roots the subtree.
  * @return the new root of the subtree.
  */
   private BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
   {
      if( t == null )
         return t;   // Item not found; do nothing
         
      int compareResult = x.compareTo( t.element );
         
      if( compareResult < 0 )
         t.left = remove( x, t.left );
      else if( compareResult > 0 )
         t.right = remove( x, t.right );
      else if( t.left != null && t.right != null ) // Two children
      {
         t.element = findMin( t.right ).element;
         t.right = remove( t.element, t.right );
      }
      
      else
         t = ( t.left != null ) ? t.left : t.right;
      return t;
   }
/*This method is used to find the node by passing it's element*/
 
   private BinaryNode<AnyType> findNode(AnyType x,BinaryNode<AnyType> t)
   {
    
      if(t.element.equals(x))
      {
         return t;
      }
      int compareResult=x.compareTo(t.element);
      if(compareResult>0)
      {
         return findNode(x,t.right);
      }
      else 
         return findNode(x,t.left);
   }
 /**
  * Internal method to find the smallest item in a subtree.
  * @param t the node that roots the subtree.
  * @return node containing the smallest item.
  */
   private BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
   {
      if( t == null )
         return null;
      else if( t.left == null )
         return t;
      return findMin( t.left );
   }

 /**
  * Internal method to find the largest item in a subtree.
  * @param t the node that roots the subtree.
  * @return node containing the largest item.
  */
   private BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
   {
      if( t != null )
         while( t.right != null )
            t = t.right;
   
      return t;
   }

 /**
  * Internal method to find an item in a subtree.
  * @param x is item to search for.
  * @param t the node that roots the subtree.
  * @return node containing the matched item.
  */
   private boolean contains( AnyType x, BinaryNode<AnyType> t )
   {
      if( t == null )
         return false;
         
      int compareResult = x.compareTo( t.element );
         
      if( compareResult < 0 )
         return contains( x, t.left );
      else if( compareResult > 0 )
         return contains( x, t.right );
      else
         return true;    // Match
   }

 /**
  * Internal method to print a subtree in sorted order.
  * @param t the node that roots the subtree.
  */
 
 
 
   private void printTree( BinaryNode<AnyType> t )
   {
   
      if( t != null )
      {
       
         System.out.println( t.element );
         printTree( t.left );
         printTree( t.right );
      }
   
   }
 
   public int height()
   {
      return height(root);
   }
 /**
  * Internal method to compute height of a subtree.
  * @param t the node that roots the subtree.
  */
   private int height( BinaryNode<AnyType> t )
   {
      if( t == null )
         return -1;
      else
         return 1 + Math.max( height( t.left ), height( t.right ) );    
   }
 
 //all methods of project2
 //.a) nodeCount Recursively traverses the tree and returns the count of nodes.
 
   public void nodeCount()
   {
      int count=nodeCount(root);
      System.out.println("no. of nodes are >>>"+count);
    
   }

   private int nodeCount(BinaryNode<AnyType> t)
   {
    
      if( t==null)
      {
         return 0; 
      }else {
         return nodeCount(t.left)+1+nodeCount(t.right);
      }
    
    
   }
 
 //.b)isFull Returns true if the tree is full.  A full tree has every node 
 //as either a leaf or a parent with two children.
   public boolean isFull()
   {
      return isFull(root);
   }
 
   private boolean isFull(BinaryNode<AnyType> t)
   {
      if(t==null)
      {
         return true;
      }else if(t.left==null && t.right==null)
      {
         return true;
      }
      else if (t.left!=null && t.right!=null)
      {
         return (isFull(t.left)&&isFull(t.right));
      }else
         return false;
    
   }
 
 /*c) compareStructure 
 Compares the structure of current tree to another tree and returns
 true if they match.*/
   public boolean compareStructure(BinarySearchTree<AnyType> t)
   {
      return compStructure(t.root,root);
   }
   private boolean compStructure(BinaryNode<AnyType> nodeA,BinaryNode<AnyType> nodeB)
   {
      if(nodeA==null && nodeB==null)
      {
         return true;
      }
      else if(nodeA==null ||nodeB==null){
         return false;
      
      }else 
         return compStructure(nodeA.left,nodeB.left)&&compStructure(nodeA.right,nodeB.right);
   }
 
 /*d) equals
 Compares the current tree to another tree and returns true
   if they are identical. 
 */
   public boolean equal(BinarySearchTree<AnyType> t)
   {
      return equals(root,t.root);
   }
 
   private boolean equals(BinaryNode<AnyType> nodeA,BinaryNode<AnyType>nodeB)
   {
      if(nodeA==null && nodeB==null)
      {
         return true;
      }
      else  if(nodeA!=null && nodeB!=null)
      {
      
         if(nodeA.element.equals(nodeB.element)&&equals(nodeA.left,nodeB.left)&& equals(nodeA.right,nodeB.right))
            return true;
         else 
            return false;
      }else 
         return false;
   
    
   }
 /*e) copy
 Creates and returns a new tree that is a copy of the original tree.*/
   public BinarySearchTree<AnyType> copyTree()
   {
      BinaryNode<AnyType> rootnew=copyNode(root);
      BinarySearchTree<AnyType> newTree=new BinarySearchTree<AnyType>();
      newTree.root=rootnew;
      return newTree;
   
   }

   private BinaryNode<AnyType> copyNode(BinaryNode<AnyType>t)
   {
      if(t==null)
      {
         return null;
      }
      BinaryNode<AnyType> newNode=new BinaryNode<AnyType>(t.element, null, null);
      newNode.left=copyNode(t.left);
      newNode.right=copyNode(t.right);
      return newNode;
   }

/* f) mirror
 Creates and returns a new tree that is a mirror image of the original tree.
 For example, for the tree on the left, the tree on the right is returned:*/
   public BinarySearchTree<AnyType> mirrorTree()
   {
      BinaryNode<AnyType> rootnew=mirrorTree(root);
      BinarySearchTree<AnyType> mirrorTree=new BinarySearchTree<AnyType>();
      mirrorTree.root=rootnew;
      return mirrorTree;
   
   }
 
   private BinaryNode<AnyType> mirrorTree(BinaryNode<AnyType>t)
   {
      if(t==null)
      {
         return null;
      }
      BinaryNode<AnyType> newNode=new BinaryNode<AnyType>(t.element, null, null);
      newNode.right=mirrorTree(t.left);
      newNode.left=mirrorTree(t.right);
      return newNode;
   }
 
 /*g)isMirror 
 Returns true if the tree is a mirror of the passed tree.*/
 
   public boolean isMirror(BinarySearchTree<AnyType> t)
   {
      return isMirror(root,t.root);
   }
   private boolean isMirror(BinaryNode<AnyType> t1,BinaryNode<AnyType>t2)
   {
      BinaryNode<AnyType> temp=mirrorTree(t2);
      if(equals(root,temp))
      {
         return true;
      }
      else 
         return false;
   }
 
/*h) rotateRight
Performs a single rotation on the node having the passed value.
If a RotateRight on 100 is performed:*/
   public void rotateRight(AnyType x)
   {
      if(!contains(x,root))
      {
         System.out.println("The element does not belong to this tree");	
      }else
         root=rotateRight(root,x);
   }
   private BinaryNode<AnyType> rotateRight(BinaryNode<AnyType> t,AnyType x)
   {
      try {
         if(t.left!=null) {
            BinaryNode<AnyType> t1=findNode(x, t);
            BinaryNode<AnyType> t2=t1.left;
            t1.left=t2.right;
            t2.right=t1;
            if(t2.right.element.equals(root.element))
            {
               return t2;
            }
            else 
            {
            
               return replaceNode(x, root,t2);	
            
            }
         
         }
         
         else
            return root;
      }catch(Exception e)
      {
         System.out.println("right rotation is not possible as left subtree is null");
         return root;
      }
   }



   private BinaryNode<AnyType> replaceNode( AnyType x, BinaryNode<AnyType> t ,BinaryNode<AnyType>t2)
   {
      int compareResult = x.compareTo( t.element );
        
      if( compareResult < 0 )
         t.left = replaceNode( x, t.left,t2 );
      else if( compareResult > 0 )
         t.right = replaceNode( x, t.right,t2 );
      else
      {
         return new BinaryNode<>( t2.element, t2.left, t2.right );
      }
      return t;
   }	

/*i) rotateLeft 
As above but left rotation.*/
   public void rotateLeft(AnyType x)
   {
      if(!contains(x,root) )
      {
         System.out.println("The element does not belong to this tree");	
      }else
         root=rotateLeft(root,x);
   }
   private BinaryNode<AnyType> rotateLeft(BinaryNode<AnyType> t,AnyType x)
   {
      try{
         if(t.right!=null)
         
         {	
            BinaryNode<AnyType> t1=findNode(x, t);
            BinaryNode<AnyType> t2=t1.right;
            t1.right=t2.left;
            t2.left=t1;
            if(t2.left.element.equals(root.element))
            {
               return t2;
            }
            else 
            {
            
               return replaceNode(x, root,t2);	
            
            }
         }else {
            System.out.println("left rotation is not possible");
            return root;
         }
      }catch(Exception e)
      {
         System.out.println("left rotation is not possible as right subtree is null");
         return root;
      }
   
   
   
   }

/*j) printLevels - performs a level-by-level printing of the tree.*/
   public void printLevel()
   {
      printLevel(root);
   
   }

   private void printLevel(BinaryNode<AnyType> t)
   {
      for(int i=0;i<=height(t);i++)
      {
         levelPrinter(t, i);	
      }
   }

   private void levelPrinter(BinaryNode<AnyType> t, int level)
   {
      if(t==null)
      {
         return ;
      }else if(level==0) {
         System.out.println(t.element);
      }else {
         levelPrinter(t.left, level-1);
         levelPrinter(t.right, level-1);
      }
   
   }

 // Basic node stored in unbalanced binary search trees
   private static class BinaryNode<AnyType>
   {
         // Constructors
      BinaryNode( AnyType theElement )
      {
         this( theElement, null, null );
      }
   
      BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
      {
         element  = theElement;
         left     = lt;
         right    = rt;
      }
   
      AnyType element;            // The data in the node
      BinaryNode<AnyType> left;   // Left child
      BinaryNode<AnyType> right;  // Right child
   }


   /** The tree root. */
   private BinaryNode<AnyType> root;


     //   k) main - demonstrate in your main method that all of your new methods work.
   public static void main( String [ ] args ) throws Exception
   {
      BinarySearchTree<Integer> t1 = new BinarySearchTree<>( );
      BinarySearchTree<Integer> t2 = new BinarySearchTree<>( );
      t1.insert(100);
      t1.insert(70);
      t1.insert(120);
      t1.insert(90);
      t1.insert(96);
      t1.insert(80);
      t1.insert(50);
      t1.insert(115);
      t1.insert(125);
      t1.insert(123);
      t1.insert(130);
    //test case a) nodecount
      t1.nodeCount();
    //test case b) isFull
      System.out.println("Is tree t1 full?>>>" +t1.isFull());
    //c) compareStructure 
      t2.insert(100);
      t2.insert(70);
      t2.insert(120);
      t2.insert(90);
      t2.insert(96);
      t2.insert(80);
      t2.insert(50);
      t2.insert(115);
      t2.insert(125);
      t2.insert(123);
      t2.insert(130);
      System.out.println("Are the structure of both trees t1 and t2 similar?>>"+t1.compareStructure(t2));
    //d) equals
      System.out.println("Are the both trees t1 and t2 equal?>>> "+t1.equal(t2));
    //e) copy
      BinarySearchTree<Integer>t3=t1.copyTree();
      System.out.println("Is the newly copied t3 is equal with original tree>>"+t1.equal(t3));
    //f)mirror  ...to test this i will print the pre order of the both trees t2 and t4
      BinarySearchTree<Integer> t4=t2.mirrorTree();
      System.out.println("print t2 first");
      t2.printTree();
      System.out.println("print t4 Second");
      t4.printTree();
      //g)isMirror
      System.out.println("Is t4 mirror of t2?>>>"+t2.isMirror(t4));
      
      //h) rotateRight
      t2.rotateRight(100);
      System.out.println("After right rotation of t2 on 100");
      t2.printTree();
      //i)rotateLeft 
      t2.rotateLeft(70);
      System.out.println("After left rotation of t2 on 70");
      t2.printTree();
      // j) printLevels
      System.out.println("printing the tree by level wise");
      t2.printLevel();

     
   }
}