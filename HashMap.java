/*
 * Author: Henry Zhu
 * Building a HashMap that resizes based on load factor
 */

import java.util.ArrayList;

public class HashMap {
	private ArrayList<Bucket> data;
	private double loadFactor = 0.75;
	private int initialCapacity = 16;
	private int numBuckets = 0;
	
	public HashMap() {
		this.data = new ArrayList<Bucket>();
		
		// Fill the list up to the initial capacity
		for (int i = 0; i < initialCapacity; i++) {
			this.data.add(null);
		}
	}
	
	public void remove(String key) {
		/*
		 * get hash code of key
		 * index is hash code % list's size
		 * let head be the bucket @ the index
		 * let previous be null
		 * while the head is not null:
		 *     if the head's key = key of bucket we want to delete:
		 *         Case 1 (B1 -> B2 (to delete) -> B3):
		 *         previous = B1, so set B1.next = B3
		 *         end result: B1 -> B3
		 *         Case 2 (B1 -> B2 (to delete) -> null):
		 *         previous = B1, so set B1.next = null
		 *         Case 3 (B1 (to delete) -> B2):
		 *         previous = null, set list[index] = B2
		 *         Case 4 (B1 (to delete) -> null):
		 *         set list[index] = null;
		 *         
		 *         return
		 *     else:
		 *         previous becomes head
		 *         head becomes head.next
		 */
		int hashCode = Math.abs(key.hashCode());
		int index = hashCode % this.data.size();
		
		Bucket head = this.data.get(index);
		Bucket previous = null;
		while (head != null) {
			if (head.key.equals(key)) { // Evaluates Cases 1 and 2
				if (previous != null) {
					if (head.next != null) { // Case 1
						previous.next = head.next;
					} else { // Case 2
						previous.next = null;
					}
				} else { // Evaluates Case 3 and 4
					if (head.next != null) { // Case 3
						this.data.set(index, head.next);
					} else { // Case 4
						this.data.set(index, null); 
					}
				}
				
				numBuckets--;
				
				return;
			} else {
				previous = head;
				head = head.next;
			}
		}
	}
	
	public Integer get(String key) {
		/*
		 * get the hash code of the key
		 * index is the hash code % list's size
		 * begin the search:
		 *     let head = the bucket at the index
		 *     while the head i snot null:
		 *         if the head's key = the key we're looking for:
		 *             return head.value
		 *         if not?
		 *             head becomes head.next
		 * return -1
		 */
		int hashCode = Math.abs(key.hashCode());
		int index = hashCode % this.data.size();

		Bucket head = this.data.get(index);
		while (head != null) {
			if (head.key.equals(key)) {
				return head.value;
			} else {
				head = head.next;
			}
		}
		
		return -1;
	}
	
	public void add(String key, Integer value) {
		/*
		 * get hash code of the key
		 * index is hash code % list's size
		 * check if key already exists
		 *     if key exists: update the bucket
		 *     if key doesn't exist:
		 *         if index is already occupied: use chaining
		 *         if index isn't already occupied, place the bucket at the index
		 */
		int hashCode = Math.abs(key.hashCode());
		int index = hashCode % this.data.size();
		
		Bucket bucket = this.data.get(index);
		
		boolean alreadyExists = false;
		
		// See if the key already exists in the HashMap
		for (int i = 0; i < this.data.size(); i++) {
			if (this.data.get(i) != null && this.data.get(i).key.equals(key)) {
				this.data.set(i, new Bucket(key, value));
				alreadyExists = true;
			}
		}
		
		if (!alreadyExists) {
			// If the spot is occupied, implement the chaining algorithm (uses a LinkedList)
			if (this.data.get(index) != null) {
				Bucket head = this.data.get(index);
				Bucket previous = head;
				
				while (head != null) {
					previous = head;
					head = head.next;
				}
				
				previous.next = new Bucket(key, value);
			}
			else {
				bucket = new Bucket(key, value);
				this.data.set(index, bucket);
			}
		}
		
		// Check to see if the capacity of the list needs to be increased.
		numBuckets++;
		
		if (new Double((1.0 * numBuckets) / this.data.size()).compareTo(loadFactor) >= 0) {
			while (new Double((1.0 * numBuckets) / this.data.size()).compareTo(loadFactor) >= 0) {
				// Increase the size of the list until the load factor is satisfied
				this.data.add(null);
			}
		}
	}
	
	public void printMap() {
		/*
		 * Go through every element in the list
		 * if element is null:
		 *    print null
		 * if element is a bucket:
		 *    set head to equal the element
		 *    while the head is not null:
		 *       print the head's value
		 *       if head.next is not null
		 *           print "-> " 
		 *       set head = head.next
		 */
		for (int i = 0; i < this.data.size(); i++) {
			if (this.data.get(i) == null) {
				System.out.println("[" + i + "] = null");
			} else {
				Bucket head = this.data.get(i);
				
				String text = "[" + i + "] = ";
				
				while (head != null) {
					text += ("(" + head.key + ", " + head.value + ")");
					
					if (head.next != null) {
						text += " -> ";
					}
					
					head = head.next;
				}
				
				System.out.println(text);
			}
		}
	}
}