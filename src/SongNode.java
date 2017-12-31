
public class SongNode implements Comparable<SongNode>{
	private String artist; 
	private String title;
	private String filename;
	private SongNode next;
	
	//Constructor
	public SongNode(String a, String t, String fn, SongNode n){
		this.artist = a;
		this.title = t;
		this.next = n;
		this.filename = fn;
	}
	// Second Constructor
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
	
	//compareTo method
	@Override
	public int compareTo(SongNode o) {
		int i = 0;
		
		while(i < this.getTitle().length() && i < o.getTitle().length()){
			
			if (this.getTitle().charAt(i) == o.getTitle().charAt(i)){
				i++;
				continue; //that way it doesn't continue downwards and crash
			}
		
			if (this.getTitle().charAt(i) > o.getTitle().charAt(i)){
				
				return -1; //this means that the title of the first object is behind the second object
			}
			
			if (this.getTitle().charAt(i) < o.getTitle().charAt(i)){
				
				return 1; //this means that the title of the first object is in front of the second object
			}
		}
		
		return 0;
	}
}
