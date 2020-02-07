/*
 * File: SongList.java
 * 
 * Description: SongList.java contains methods for a custom LinkedList object. 
 * Each individual "node" in the LinkedList is a SongNode object (See SongNode.java for details). 
 */
public class SongList {	
    private SongNode head;
    private SongNode tail;

    // Constructor
    public SongList(){
        head = null;
        tail = null;
    }

    // Getters and setters
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

    /*
    Method: insertAtFront(String artist, String title, String filename) <--- Double check args
    Argument(s): String artist - artist of song
                 String title - title of song
                 String filename - actual name of file in directory
    Return value(s): None

    Description: This method inserts a SongNode at the front of a LinkedList.
    Purpose: This method makes it easier to insert when we know that a node must go
    in the front of a LinkedList.
    */
    public void insertAtFront(String artist, String title, String filename){
        SongNode sn = new SongNode(artist, title, filename);

        if (head != null) {
            sn.setNext(head);
        } else {
            tail = sn;
        }

        head = sn;
    }

    /*
    Method: append(String artist, String title, String filename, SongNode n)
    Argument(s): String artist - artist of song
                 String title - title of song
                 String filename - actual name of file in directory
    Return value(s): None

    Description: This method adds a SongNode to the back of the LinkedList.
    Purpose: This method makes it easier to insert at the end when we know that
    the node must go at the end, while also adjusting the tail and possibly the head
    accordingly.
    */
    public void append(String artist, String title, String filename, SongNode n){
        SongNode sn = new SongNode(artist, title, filename, n);

        if (tail != null){
            tail.setNext(sn);
            tail = sn;
        } else {
            head = tail = sn; // If there isn't a tail, then we know the LinkedList is empty
        }
    }

    /*
    Method: printNodes()
    Argument(s): None
    Return value(s): None

    Description: This method prints all the nodes in the LinkedList to the console. More
    specifically, it prints the title and artist for each song
    Purpose: This is here for debugging purposes.
     */
    public void printNodes(){
        SongNode curr = head;

        while (curr != null){
            System.out.println(curr.getTitle() + ", " + curr.getArtist());
            curr = curr.next();
        }

        System.out.println();
    }

    /*
    Method: set(int node_index, String artist, String title, String filename)
    Argument(s): String artist - artist of song
                 String title - title of song
                 String filename - actual name of file in directory
    Return value(s): None

    Description: This method changes the value of a specific SongNode at a specific
    part of the LinkedList.
    Purpose:
     */
    public void set(int node_index, String artist, String title, String filename){
        SongNode curr = head; // Start of the LinkedList
        int c = 0; // Counter

        while (c < node_index && curr.next() != null && curr != null){
            c++;
            curr = curr.next();
        }

        if (c == node_index){
            curr.setArtist(artist);
            curr.setTitle(title);
            curr.setFilename(filename);
        }
    }
}
