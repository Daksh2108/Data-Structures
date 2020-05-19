package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;
import structures.Vertex.Neighbor;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {

	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;

		/**
		 * Next node in linked list
		 */
		public Node next;

		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;

	/**
	 * Number of nodes in the CLL
	 */
	private int size;

	/**
	 * Initializes this list to empty
	 */
	public PartialTreeList() {
		rear = null;
		size = 0;
	}

	/**
	 * Adds a new tree to the end of the list
	 * 
	 * @param tree Tree to be added to the end of the list
	 * 
	 */
	public void append(PartialTree tree) {
		Node ptr = new Node(tree);
		if (rear == null) {
			ptr.next = ptr;
		} else {
			ptr.next = rear.next;
			rear.next = ptr;
		}
		rear = ptr;
		size++;
	}

	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		/* COMPLETE THIS METHOD */

		//Creating empty list of partial tree 
		PartialTreeList L = new PartialTreeList();

		// Creating partial tree T containing only V
		for(int i=0; i<graph.vertices.length; i++) {

			PartialTree T = new PartialTree(graph.vertices[i]);

			//Connect meanHeap P to the partial Tree object T
			MinHeap<Arc> P = T.getArcs(); 

			// Now loading all arc into the P object


			Neighbor ptr = graph.vertices[i].neighbors;

			while (ptr != null) {
				Arc findArc = new Arc(graph.vertices[i],ptr.vertex,ptr.weight);
				//Loaded all the arc into the P
				P.insert(findArc);
				ptr = ptr.next;
			}
			L.append(T);
		}
		return L;
	}

	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 * 
	 * 
	 */    
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		//Creating arrayList
		ArrayList<Arc> resultList = new ArrayList();
		while(ptlist.size()>1) {

			PartialTree PQX=ptlist.remove();   // removing PTX and storing it in PQX(Step 3)
			Arc a;
			while (!PQX.getArcs().isEmpty()) {

				a = PQX.getArcs().deleteMin();  // removing highest priority arc from PQX (STEP 4)
				Vertex one=  a.getv1(); //Getting vertex1 
				Vertex two =  a.getv2();// Getting vertex 2
				//check if one and two are in PQX

				if(one.getRoot().parent.equals(PQX.getRoot()) && two.getRoot().parent.equals(PQX.getRoot()) ) {
					continue;
				}
				//if yes -> move to next arc
				//if no -> find tree that contains two say PQY, merge PQX and PQY

				PartialTree PQY = null;

				PQY = ptlist.removeTreeContaining(two);
				if (PQY == null) {
					//get partial tree for one
					PQY = ptlist.removeTreeContaining(one);
				}

				if (PQY == null)
					continue;

				//merge pqx and pqy
				PQX.merge(PQY);
				resultList.add(a);
				ptlist.append(PQX); 
				break;
			}
		}

		return resultList;
	}



	/**
	 * Removes the tree that is at the front of the list.
	 * 
	 * @return The tree that is removed from the front
	 * @throws NoSuchElementException If the list is empty
	 */
	public PartialTree remove() 
			throws NoSuchElementException {

		if (rear == null) {
			throw new NoSuchElementException("list is empty");
		}
		PartialTree ret = rear.next.tree;
		if (rear.next == rear) {
			rear = null;
		} else {
			rear.next = rear.next.next;
		}
		size--;
		return ret;

	}

	/**
	 * Removes the tree in this list that contains a given vertex.
	 * 
	 * @param vertex Vertex whose tree is to be removed
	 * @return The tree that is removed
	 * @throws NoSuchElementException If there is no matching tree
	 */
	public PartialTree removeTreeContaining(Vertex vertex)  // This method is taking in vertex
			throws NoSuchElementException {
		/* COMPLETE THIS METHOD */

		PartialTree deletedNode = null;
		Node ptr=this.rear.next;
		Node prev=rear;

		if(this.size==1) {


			if(rear.tree.getRoot().equals(vertex.getRoot())) {
				deletedNode = rear.tree;
				rear=null;
				size--;
				return deletedNode;

			}

		}


		while(ptr!=rear) {

			if(rear==null) {
				throw new NoSuchElementException("Partial Tree List is Empty");
			}
			if(ptr.tree.getRoot().equals(vertex.getRoot())) {  // if target vertex is found, now its time to delete
				deletedNode=ptr.tree;
				prev.next=ptr.next;//Connects prev to the ptr
				this.size--;
				break;
			}

			prev=ptr;
			ptr=ptr.next;

		}

		//A---> B--->C

		//vertex to be deleted is found at the rear 
		if(rear.tree.getRoot().equals(vertex.getRoot())) {
			deletedNode=rear.tree;
			ptr=rear.next;
			rear.next = null;
			rear=ptr;
			prev.next = rear;
			this.size--;
			return deletedNode;
		}

		if (deletedNode == null) {
			throw new NoSuchElementException("There is no matching tree");
		}
		
		return deletedNode;
	}
	/**
	 * Gives the number of trees in this list
	 * 
	 * @return Number of trees
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns an Iterator that can be used to step through the trees in this list.
	 * The iterator does NOT support remove.
	 * 
	 * @return Iterator for this list
	 */
	public Iterator<PartialTree> iterator() {
		return new PartialTreeListIterator(this);
	}

	private class PartialTreeListIterator implements Iterator<PartialTree> {

		private PartialTreeList.Node ptr;
		private int rest;

		public PartialTreeListIterator(PartialTreeList target) {
			rest = target.size;
			ptr = rest > 0 ? target.rear.next : null;
		}

		public PartialTree next() 
				throws NoSuchElementException {
			if (rest <= 0) {
				throw new NoSuchElementException();
			}
			PartialTree ret = ptr.tree;
			ptr = ptr.next;
			rest--;
			return ret;
		}

		public boolean hasNext() {
			return rest != 0;
		}

		public void remove() 
				throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

	}
}


