

public class SongList {
	
	private SongNode head; 
	private SongNode tail;
	//Constructor
	public SongList(){
		head = null;
		tail = null;
	}
	//Getters and setters
	public SongNode head(){
		return this.head;
	}
	
	public SongNode tail(){
		return this.tail;
	}
	
	public void setHead(SongNode n){
		head = n;
	}
	
	public void setTail(SongNode n){
		tail = n;
	}
	
	// inserts a SongNode at the front of a LinkedList
	public void insertAtFront (String a, String b, String c){
		
		SongNode sn = new SongNode(a, b, c);
		if (head != null) {
			sn.setNext(head);
		}
		else {
			tail = sn;
		}
		head = sn;
	}
	
	//adds a SongNode to the back of a LinkedList
	public void append (String a, String b, String c, SongNode n){
		SongNode sn = new SongNode(a, b, c, n);
		if (tail != null){
			tail.setNext(sn);
			tail = sn;
		} else {
			head = tail = sn;
		}
	}
	
	//prints all the nodes in the LinkedList
	public void printNodes(){
		SongNode curr = head;
		while (curr != null){
			System.out.println(curr.getTitle() + ", " + curr.getArtist());
			curr = curr.next();
		}
		System.out.println();
	}
	
	
	//Changes the values of a SongNode at a specific part of the LinkedList
	public void set (int i, String a, String b, String d){
		SongNode curr = head;
		int c = 0;
		
		while (c < i && curr.next() != null && curr != null){
			c++;
			curr = curr.next();
		}
		if (c == i){
			curr.setArtist(a);
			curr.setTitle(b);
			curr.setFilename(d);
		}
	}
	
	
	
	
	
	
	
		
	
	
}
