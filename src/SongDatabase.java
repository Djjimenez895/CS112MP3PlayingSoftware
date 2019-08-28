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

/*
 * File: SongDatabase.java
 * 
 * Description: SongDatabase.java contains methods that allow us to store nodes
 * in the SongList LinkedList object and perform operations such as adding nodes and 
 * searching for specific nodes.  
 */
public class SongDatabase {
	SongDatabase results; // used later to get the search results a user looked up
	private static SongList slist = new SongList(); // LinkedList
	
	public SongDatabase(){
		slist = new SongList();
	}
	
	/*
	Method: insertInorder(SongNode n)
	Argument(s): SongNode n - the SongNode object to be added to the LinkedList
	Return Value(s): None
	
	Description: This method takes a SongNode object and puts it into the SongList in the 
	appropriate order based on the compareTo method (See SongNode.java). 
	Nodes pointers will be adjusted as necessary.
	
	This method handles three different types of cases for insertion: 
	1) When there's nothing in the LinkedList.
	2) When there's only one item in the LinkedList (i.e., the head node is also the tail node) 
	3) When there are at least 2 nodes in the LinkedList. 
	 */
	public static void insertInOrder(SongNode n){
		SongNode h = slist.head();
		SongNode t = slist.tail();
		SongNode ncurr; // this will be used to find the element after current later
		
		if (slist.head() == null){ // this means nothing is in the LinkedList
			n.setNext(slist.head()); // Sets next to null since slist.head() == null
			slist.setHead(n); // Update head and tail for LinkedList
			slist.setTail(n);
			h = slist.head(); // Updates the original pointers from the beginning of the method
			t = slist.tail();
		}
		
		if (h == t && !n.getTitle().equals(h.getTitle())){ //this means that the head and the tail are the same, but the node
			// we're trying to insert is different than the head node's title (prevents duplicates) 
			if (n.compareTo(h) == -1){ // meaning the SongNode belongs behind the head (based on compareTo method)
				slist.setTail(n); // Makes the node that we're trying to insert the tail
				t = slist.tail();
				slist.head().setNext(n); // Updates the head node's pointer
			} else if (n.compareTo(h) == 1){ // meaning the SongNode belongs in front of the head
				slist.setHead(n);
				h = slist.head();
			}
		} else { // Case for when there are 2 or more items in the LinkedList
			SongNode curr = slist.head();
			// Situation considered when curr is the head
			if (curr.getTitle().equals(slist.head().getTitle()) && n.compareTo(curr) == 1){ // this means curr is the head and that n should be in front of curr
				n.setNext(slist.head());
				slist.setHead(n); // sets node n to be the head
			}
			
			while (curr.next() != null){ // Null check so we can safely check current node and the next node
				if (n.compareTo(curr) == -1 && n.compareTo(curr.next()) == 1){ // this means that n should be between curr and curr.next
					SongNode temp = curr.next(); // Adjust pointers
					curr.setNext(n);
					n.setNext(temp);		
				}
					
				if (curr.next() != null){ // Null check before moving forward	
					curr = curr.next(); 
				}	
			}
		}
	}
	
	/*
	Method: insertAtFront(String artist, String title, String filename) 
	Argument(s): String artist - artist of song
			     String title - title of song
			     String filename - actual name of file in directory
	Return Value(s): void
	
	Description: This method takes the arguments for a new SongNode, creates a new SongNode object, and then
	puts the node at the front of the SongDatabase. 
	 */
	public void insertAtFront (String artist, String title, String filename){
		SongNode sn = new SongNode(artist, title, filename); //creates the node to be added
		if (slist.head() != null) {
			sn.setNext(slist.head()); // makes the next pointer for sn the head of the database
		}
		else {
			slist.setTail(sn);
		}
		slist.setHead(sn); // sets the head to sn
	}

	/*
	Method: printFilesAndFolders(Path path)
	Argument(s): Path path - path to print all files and folders from
	Return Value(s): None
	
	Description: This method prints all the files and folders in a given path.
	 */
	public static void printFilesAndFolders(Path path){
		if (!Files.isDirectory(path)){ // checks if it's a directory or not so the program won't crash
			System.out.println("Path is not a directory. ");
			return;
		} try {
			DirectoryStream<Path> listing = Files.newDirectoryStream(path); // creates a directory stream
			for (Path file : listing) { // traverses the path objects in the directory stream
				if (!Files.isDirectory(file)){
					String fno = file.getFileName().toString(); 
					String fn = file.toString(); // stores info from the file
					
					try { // creates an AudioFile out of the found string
						AudioFile f = AudioFileIO.read(new File(fn)); // makes an audio file out of fn 
						Tag tag = f.getTag(); // creates a tag
						String artist = tag.getFirst(FieldKey.ARTIST); // gets the artist 
						String title = tag.getFirst(FieldKey.TITLE); // gets the title
						SongNode newSong = new SongNode(artist, title, fn); // makes a new SongNode object
						SongDatabase.insertInOrder(newSong); // puts the song into the SongDatabase
									
					// catch exceptions
					} catch (CannotReadException e) {
						e.printStackTrace();
					} catch (TagException e) {
						e.printStackTrace();
					} catch (ReadOnlyFileException e) {		
						e.printStackTrace();
					} catch (InvalidAudioFrameException e) {
						e.printStackTrace();
					} 	
				}
				
				if (Files.isDirectory(file)){ // prints the names of subfolders			
					printFilesAndFolders(file); // recursively goes through the file if file is a directory
				}
			}
		} catch (IOException e){
			System.out.println("Could not get the list of files/subfolders inside this folder");
		}			
	}
	
	/* 
	Method: findSong(int node_index)
	Argument(s): int node_index - finds the song at the given index (n)
	Return value(s): String foundFile - name of the file at the given index
	
	Description: This method traverses the SongDatabase and finds the song at the specified 
	node_index. The method returns the filename. 
	 */
	public String findSong(int node_index){
		int c = 0;
		SongNode curr = slist.head();
		
		while (c != node_index && curr != null){
			curr = curr.next();
			c++;
		}

		File newfile = new File(curr.getFilename()); // Unnecessary?
		String foundFile = curr.getFilename();
		return foundFile;
	}
	
	/*
	Method: printAllSongs()
	Argument(s): None
	Return Value(s): None
	
	Description: This prints all the songs in the SongDatabase to the console. 
	 */
	public void printAllsongs(){
		SongNode curr = slist.head();
		while (curr != null){
			System.out.println(curr.getTitle() + ", " + curr.getArtist());
			curr = curr.next();
		}
		System.out.println();
	}
	
	/*
	Method: getDatabaseSize() 
	Argument(s): None
	Return Value(s): None
	
	Description: This method gets the size of the database by traversing all the nodes. 
	 */
	public int getDatabasesize(){
		int total = 0; // counter
		SongNode curr = SongDatabase.slist.head();
		while (curr != null){
			total++;
			curr = curr.next();
		}
		
		return total;
	}
	
	/*
	Method: addToTable(String t[][])
	Argument(s): String t[][] - 2D that represents the different attributes of the files that we need to show (title and artist) 
	Return Value(s): None
	
	Description: addToTable adds SongNode to the 2D Array so that the songs can be represented in the JTable. 
	 */
	public void addTotable(String t[][]){ 
		SongNode curr = slist.head();
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
	
	/*
	Method: Search(String n, SongDatabase b) 
	Argument(s): String n - The prefix/song name that the user is searching for.
			     SongDatabase b - the Database to check for the song
	Return Value(s): None
	
	Description: The search method looks for any song name that contains String n and then inserts it into the beginning 
	of the LinkedList as a search result. 
	 */
	public void Search(String n, SongDatabase b){
		SongNode curr = b.slist.head(); // stores the head of the SongDatabase given in the parameters
		while (curr != null){
			//compares the beginning of the current node's song title to the string given in the parameters
			if (curr.getTitle().toLowerCase().substring(0, n.length()-1).equals(n.toLowerCase())){
				this.insertInOrder(curr);
			}
			curr = curr.next();
		}
	}

	/* 
	Method: append(String a, String b, String C, SongNode n)
	Argument(s): String artist - artist of song
			     String title - title of song
			     String filename - actual name of file in directory
			     SongNode nextNode - next song node
	Return Value(s): None
	
	Description: Appends a SongNode to the LinkedList
	 */
	public void append(String artist, String title, String filename, SongNode nextNode){
		SongNode sn = new SongNode(artist, title, filename, nextNode); //creates a new SongNode
		//sets the tail to the new SongNode
		if (slist.tail() != null){ 
			slist.tail().setNext(sn);
			slist.setTail(sn);
		} else {
			slist.setHead(sn);
			slist.setTail(sn);
		}
	}
	
	/*
	Method: head()
	Argument(s): None
	Return Value(s): SongNode slist.head() - head of the LinkedList
	
	Description: This returns the head of the LinkedList
	 */
	public SongNode head() {
		return slist.head();
	}
}
