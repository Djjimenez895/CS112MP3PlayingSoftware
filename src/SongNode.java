/*
 *  File: SongNode.java
 *  
 *  Description: This file contains all the data needed to store a specific song.
 *  Each song is represented as a node because they will all be connected as a LinkedList (See SongList.java)
 */
public class SongNode implements Comparable<SongNode>{
	private String artist; 
	private String title;
	private String filename;
	private SongNode next;
	
	// Constructor, takes the next SongNode as an argument
	public SongNode(String a, String t, String fn, SongNode n){
		this.artist = a;
		this.title = t;
		this.next = n;
		this.filename = fn;
	}
	
	// Second Constructor, sets this.next to null
	public SongNode(String a, String t, String fn){
		this.artist = a;
		this.title = t;
		this.filename = fn;
		this.next = null;
	}
	
	// Getters and Setters
	public String getArtist(){
		return this.artist;
	}
	
	public String getTitle(){
		return this.title;
	}

	public String getFilename(){
		return this.filename;
	}
	
	public SongNode next() {
		return next;
	}

	public void setNext(SongNode other) {
		next = other;
	}

	public void setArtist(String o){
		this.artist = o;
	}

	public void setTitle(String o){
		this.title = o;
	}
	
	public void setFilename(String f){
		this.filename = f;
	}
	
	public String toString(){
		return this.artist + this.title + this.filename;
	}
	
	/* 
	Method: compareTo(SongNode o)
	Argument(s): SongNode o - the other SongNode you want to compare to.
	Return value(s)s: -1 -> "this" object is behind the SongNode o object
	 			    1 -> "this" object is in front of the SongNode o object
	
	Description: This compareTo method takes the current object (this) and compares the ASCII 
	values of each individual character to the characters of the other object (SongNode o). 
	Purpose: This method is used to determine whether a song is in front of or behind the current
	song (this). 
	*/
	@Override
	public int compareTo(SongNode o) {
		int i = 0;
		
		while(i < this.getTitle().length() && i < o.getTitle().length()){
			if (this.getTitle().charAt(i) == o.getTitle().charAt(i)){
				i++;
				continue; // continue so that it doesn't go downwards and make the other comparisons
			}
		
			if (this.getTitle().charAt(i) > o.getTitle().charAt(i)){
				return -1; // this means that the title of the first object is behind the second object
			}
			
			if (this.getTitle().charAt(i) < o.getTitle().charAt(i)){
				return 1; // this means that the title of the first object is in front of the second object
			}
		}
		
		return 0;
	}
}
