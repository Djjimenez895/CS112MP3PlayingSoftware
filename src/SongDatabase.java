import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class SongDatabase {
	SongDatabase results; //used later to get the search results a user looked up
	private static SongList slist = new SongList(); 
	
	public SongDatabase(){
		slist = new SongList();
	}
	
	public static void insertInOrder(SongNode n){
		//Takes a SongNode and put it into the SongList in the appropriate order
		//nodes will shift to make room for where the node should be.
		SongNode h = slist.head();
		SongNode t = slist.tail();
		SongNode ncurr; //this will be used to find the element after current later
		
		if (slist.head() == null){ //this means nothing is in the LinkedList
			n.setNext(slist.head());
			slist.setHead(n);
			slist.setTail(n);
			h = slist.head();
			t = slist.tail();
		}
		
		if (h == t && !n.getTitle().equals(h.getTitle())){ //this means that that the head and the tail are the same, but the node
			// is different than what's inside the linkedlist at the moment
			//System.out.println("test!"); //it gets here
			if (n.compareTo(h) == -1){ //meaning the SongNode belongs behind the head
				slist.setTail(n); //makes the tail the node that we're currently looking at
				t = slist.tail();
				slist.head().setNext(n); //sets the element after the head to n since I know that there is only a head and tail in this case
				// Now it works properly
				//System.out.println("test1!"); //Here for testing purposes
			} else if (n.compareTo(h) == 1){ //meaning the SongNode belongs infront of the head
				slist.setHead(n);
				h = slist.head();
				//System.out.println("test2!"); //here for testing purposes
			}
		} else { //this means any other situation beyond there being
			// nothing in the Linkedlist or there only being a head and a tail
			SongNode curr = slist.head();
			//Situation considered when curr is the head
			if (curr.getTitle().equals(slist.head().getTitle()) && n.compareTo(curr) == 1){ //this means curr is the head and that n should be in front of curr
				n.setNext(slist.head());
				slist.setHead(n); //this means that n would become the head
			}
			
			while (curr.next() != null){ //meaning the element that is the pointer is not null and the element in front of that isn't null
				if (n.compareTo(curr) == -1 && n.compareTo(curr.next()) == 1){ //this means that n should be behind curr, but in front of curr.next()
					//so the element is in the middle of these two elements
					SongNode temp = curr.next();
					curr.setNext(n);
					n.setNext(temp);		
				}
					
				if (curr.next() != null){ //this ensures that it doesn't crash	
					curr = curr.next(); //moves curr downwards so I can move down the LinkedList
				}	
			}
		}
	}
	
	//inserts a SongNode at the front of a SongDatabase
	public void insertAtFront (String a, String b, String c){
		SongNode sn = new SongNode(a, b, c); //creates the node to be added
		if (slist.head() != null) {
			sn.setNext(slist.head()); //makes the next pointer for sn the head of the database
		}
		else {
			slist.setTail(sn);
		}
		slist.setHead(sn); //sets the head to sn
	}

	public static void printFilesAndFolders(Path path){
		if (!Files.isDirectory(path)){ //checks if it's a directory or not so the program won't crash
			System.out.println("Path is not a directory. ");
			return;
		} try {
			DirectoryStream<Path> listing = Files.newDirectoryStream(path); //creates a directory stream
			for (Path file : listing) { //traverses the path objects in the directory stream
				if (!Files.isDirectory(file)){
					String fno = file.getFileName().toString(); //	
					String fn = file.toString(); //stores info from the file
					
					try {//creates an AudioFile out of the found string
						AudioFile f = AudioFileIO.read(new File(fn)); //makes an audio file out of fn 
						Tag tag = f.getTag(); //creates a tag
						String artist = tag.getFirst(FieldKey.ARTIST); //gets the artist 
						String title = tag.getFirst(FieldKey.TITLE); //gets the title
						SongNode newSong = new SongNode(artist, title, fn); //makes a new SongNode object
						SongDatabase.insertInOrder(newSong); //puts the song into the SongDatabase
									
					//catch exceptions so the program won't crash
					} catch (CannotReadException e) {
						//e.printStackTrace();
					} catch (TagException e) {
						//e.printStackTrace();
					} catch (ReadOnlyFileException e) {		
						//e.printStackTrace();
					} catch (InvalidAudioFrameException e) {
						//e.printStackTrace();
					} 	
				}
				
				if (Files.isDirectory(file)){ //prints the names of subfolders			
					printFilesAndFolders(file);//recursively goes through the file if file is a directory
				}
			}
		} catch (IOException e){
			System.out.println("Could not get the list of files/subfolders inside this folder");
		}			
	}
	
	// finds a song in the LinkedList
	public String findSong(int n){
		int c = 0;
		SongNode curr = slist.head();
		
		while (c != n && curr != null){
			curr = curr.next();
			c++;
		}

		File newfile = new File(curr.getFilename());
		String thefile = curr.getFilename();
		
		return thefile;
	}
	
	//prints all the songs in the LinkedList
	public void printAllsongs(){
		//this method should print all the songs in the SongDatabase
		SongNode curr = slist.head();
		while (curr != null){
			System.out.println(curr.getTitle() + ", " + curr.getArtist());
			curr = curr.next();
		}
		System.out.println();
	}
	
	//Gets the size of the SongDatabase
	public int getDatabasesize(){
		int c = 0; //counter
		SongNode curr = SongDatabase.slist.head();
		while (curr != null){
			c++;
			curr = curr.next();
		}
		
		return c;
	}
	
	// Adds SongNodes to the 2D Array that is used for the JTable
	public void addTotable(String t[][]){ //takes a 2D array as an argument
		SongNode curr = slist.head();
		//System.out.println(t.length);
		int k = 0;
		int l = 1;
		
		// loops through the first column and adds all of the titles to it
		for (int i = 0; i < t.length && curr != null; i++){
			t[i][k] = curr.getTitle();
			curr = curr.next();
		}
		
		//reassigns curr to the head
		curr = slist.head();
		
		// loops through the second column and adds artists to it
		for (int i = 0; i < t.length && curr !=null; i++){
			t[i][l] = curr.getArtist();
			curr = curr.next();
		}
	}
	
	//searches for the song(s) that match the user's criteria
	public void Search(String n, SongDatabase b){
		SongNode curr = b.slist.head(); //stores the head of the SongDatabase given in the parameters
		//System.out.println(curr.getTitle()); 
		while (curr != null){
			//System.out.println(curr.getTitle());
			//compares the beginning of the current node's song title to the string given in the parameters
			if (curr.getTitle().toLowerCase().substring(0, n.length()-1).equals(n.toLowerCase())){
				this.insertInOrder(curr);
			}
			curr = curr.next(); //moves through the LinkedList
		}
	}

	//inserts a SongNode at the back of the LinkedList
	public void append (String a, String b, String c, SongNode n){
		SongNode sn = new SongNode(a, b, c, n); //creates a new SongNode
		//sets the tail to the new SongNode
		if (slist.tail() != null){ 
			slist.tail().setNext(sn);
			slist.setTail(sn);
		} else {
			slist.setHead(sn);
			slist.setTail(sn);
		}
	}
	
	//gets the head of the SongDatabase
	public SongNode head() {
		return slist.head();
	}
}
