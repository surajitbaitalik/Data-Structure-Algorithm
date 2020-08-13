1. Skip Lists
Implement the following operations of skip lists.  
add(x): Add a new element x to the list. If x already exists in the skip list, replace it and return false. Otherwise, insert x into the skip list and return true.
contains(x): Does list contain x?
remove(x): Remove x from the list. If successful, removed element is returned. Otherwise, return null.
size(): Return the number of elements in the list.
isEmpty(): Is the list empty?
ceiling(x): Find smallest element that is greater or equal to x. (optional)
first(): Return first element of list. (optional)
floor(x): Find largest element that is less than or equal to x. (optional)
get(n): Return element at index n of list. First element is at index 0. Call either getLinear or getLog. (optional)
getLinear(n): O(n) algorithm for get(n). (optional)
getLog(n): O(log n) expected time algorithm for get(n). This method is optional, but code it correctly to earn an EC. Need to implement get(n) to if you implement this.
iterator(): Iterator for going through the elements of list in sorted order. (optional)
last(): Return last element of list. (optional)
rebuild(): Reorganize the elements of the list into a perfect skip list. A search operation in a perfect skip list, will emulate binary search in a sorted array. (optional)