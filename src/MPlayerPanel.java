import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
/**
 *  The GUI Panel for the MPlayer project
 */

public class MPlayerPanel extends JPanel {
	//inner class player thread 
	class PlayerThread extends Thread {
	 	Player pl;
	 	PlayerThread(String filename){
	 		FileInputStream file;
	 		//System.out.println("test"); //somehow gets here
	 		//System.out.println(filename);
	 		File thefile = new File(filename);
	 		try {
	 			// filename here contains mp3 you want to play
	 			file = new FileInputStream(thefile);
	 			pl = new Player(file);
	 		} catch (FileNotFoundException e){
	 			System.out.println("test1"); //it gets here, so for some reason the file can't be found
	 			e.getMessage();
	 		} catch (JavaLayerException e){
	 			System.out.println("test2"); //it doesn't get here
	 			e.getMessage();
	 		}
	 	}
	 	
	 	public void run(){
	 		try{
	 			pl.play();
	 		} catch (Exception e){
	 			e.getMessage();
	 		}
	 	}
	 }
	
	private static final long serialVersionUID = 1L;
	String dataValues[][];
	private SongDatabase songDatabase; // the class that contains songs

	// panels
	JPanel topPanel, bottomPanel;
	JScrollPane centerPanel;
	Thread currThread = null;
	
	// buttons and search box
	JButton playButton, stopButton, exitButton, loadMp3Button;
	JTextField searchBox;
	JButton searchButton;
	SongDatabase results;
	int selectedSong = -1;
	JTable table = null;
	JTable Newtable = null;
	private final JFileChooser fc = new JFileChooser();

	MPlayerPanel(SongDatabase songCol) {
		this.songDatabase = songCol;
		this.setLayout(new BorderLayout());
		// Create panels: top, center, bottom
		
		// Create the top panel that has the Load mp3 button, the textfield and
		// the Search button to search for a song
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));

		// create buttons
		loadMp3Button = new JButton("Load mp3");
		searchBox = new JTextField(5);
		searchButton = new JButton("Search");
		exitButton = new JButton("Exit");
		playButton = new JButton("Play");
		stopButton = new JButton("Stop");

		// add a listener for each button
		loadMp3Button.addActionListener(new ButtonListener());
		exitButton.addActionListener(new ButtonListener());
		playButton.addActionListener(new ButtonListener());
		stopButton.addActionListener(new ButtonListener());
		searchButton.addActionListener(new ButtonListener());

		// add buttons and the textfield to the top panel
		topPanel.add(loadMp3Button);
		topPanel.add(searchBox);
		topPanel.add(searchButton);

		this.add(topPanel, BorderLayout.NORTH); // add the top panel to this
												// panel (MPlayer panel)
		// create the bottom panel and add three buttons to it
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 3));
		bottomPanel.add(playButton);
		bottomPanel.add(stopButton);
		bottomPanel.add(exitButton);

		this.add(bottomPanel, BorderLayout.SOUTH);

		// the panel in the center that shows mp3 songs
		centerPanel = new JScrollPane();
		this.add(centerPanel, BorderLayout.CENTER);

		// file chooser (opens another window that allows the user to select a folder with files)
		// Set the default directory to the current directory
		fc.setCurrentDirectory(new File("."));
	}

	/** An inner listener class for buttons **/
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == loadMp3Button) {
				System.out.println("Load mp3 button");
				
				// read all the mp3 files from mp3 directory
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Select a directory with mp3 songs");
				int returnVal = fc.showOpenDialog(MPlayerPanel.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					songDatabase = new SongDatabase(); //Makes a new SongDatabase
					centerPanel.getViewport().removeAll(); //removes everything fromt he centerPanel
					File dir = fc.getSelectedFile(); //creates a file from fc
					// dir is the directory with the mp3 files that the user
					// selected
					Path ndir = dir.toPath(); //Directory changed into a path
					//System.out.println(ndir.toString());
					songDatabase.printFilesAndFolders(ndir);
					// ^ Calls the method that recursively traverses
					// the directory to find all .mp3 files
					
					String columnNames[] = {"Title", "Artist"}; //column names for the titles
					int n = songDatabase.getDatabasesize(); //stores the size of the database
					//System.out.println(n);
					String dataValues[][] = new String[n][2]; //n rows and 2 columns
				
					songDatabase.addTotable(dataValues); //adds the nodes to the 2d Array
					table = new JTable(dataValues, columnNames); //makes a table with the dataValues and columnNames[]
					centerPanel.getViewport().add(table); //puts the table on the centerPanel
					updateUI(); //updates ui

				}
			} else if (e.getSource() == playButton) {

				if (table == null){ //prevents a crash
					 return;
				}
				
				if (currThread != null){ //checks to see if something is already playing and
					//stops currThread if something is
					currThread.stop();
				}
				
				selectedSong = table.getSelectedRow(); //gets the song that the user selected
				
				if (selectedSong != -1){ //ensures that the user actually picked a song, meaning that selectedSong doesn't have its
					// default values of -1
					System.out.println("selected Song = " + selectedSong + " ");
					// if songDatabase is empty, the SongDatabase results will be used and the 
					// thread will run that database instead
					if (songDatabase.head() == null){
						String sst = results.findSong(selectedSong);
						currThread = new PlayerThread(sst);
						currThread.start();
					} else {
						// Finds the selected song in the SongDatabase and plays it
						String sst = songDatabase.findSong(selectedSong); //selected song's title (filename)
						System.out.println(sst);
						currThread = new PlayerThread(sst);
						currThread.start();
					}
				}
			} else if (e.getSource() == stopButton) {
				//stops playing the song if its currently playing
				if (currThread != null){ //meaning it's being used at the moment
					currThread.stop(); //stops the thread
				}
			} else if (e.getSource() == exitButton) {
				System.exit(0); //exits the program
			}

			else if (e.getSource() == searchButton) {
				SongNode curr = songDatabase.head();
				SongDatabase results = new SongDatabase();
				
				if (table != null){ //ensures that the user can't search if nothing is in the table
					//System.out.println(curr);
					centerPanel.getViewport().removeAll(); //clears the centerPanel
					String s = searchBox.getText(); //gets the string from search box
					//System.out.println(s);
					int n = s.length(); //finds the length of that string
					// if the search box is empty when clicked and the table isn't empty, then it'll 
					// display the original list of songs
					if (searchBox.getText().equals("") && table != null){
						File dir = fc.getSelectedFile();
						System.out.println(dir.toString());
						// dir is the directory with the mp3 files that the user
						// selected
						Path ndir = dir.toPath(); 
						songDatabase.printFilesAndFolders(ndir);
						
						String columnNames[] = {"Title", "Artist"}; //column names for the titles
						int b = songDatabase.getDatabasesize(); 
						//System.out.println(n);
						String dataValues[][] = new String[b][2]; //n rows and 2 columns
					
						songDatabase.addTotable(dataValues); //adds nodes from the songDatabase to the table
						table = new JTable(dataValues, columnNames); //creates a table based on the 2D array dataValues and columnNames
						centerPanel.getViewport().add(table); //adds the table to the panel
						updateUI(); //updates ui
					} else{
						while (curr != null){ //prevents a crash
							if (n <= curr.getTitle().length()){ //gets the length of the current node's title
							// if the current title starts with the string the user looked up, then the node will be added to the SongDatabase results
								if (curr.getTitle().toLowerCase().substring(0, n).startsWith(s.toLowerCase())){
									results.append(curr.getArtist(), curr.getTitle(), curr.getFilename(), null);
								}
							}
							
							curr = curr.next(); //moves through the LinkedList
						}
						
						String cN[] = {"Title", "Artist"}; //column names for the titles
						int f = results.getDatabasesize(); //size of results
						String dV[][] = new String[f][2]; //f rows and 2 columns
						SongNode ocurr = results.head(); //stores the head of results
						//two counters
						int k = 0;
						int l = 1;
						
						//goes through column 0 and adds the titles of songs from the database to the 2d array
						for (int i = 0; i < dV.length && ocurr != null; i++){
							dV[i][k] = ocurr.getTitle();
							//System.out.println(curr.getTitle());
							ocurr = ocurr.next();
						}	
						
						ocurr = results.head(); //reassigns the head of results to ocurr
						//goes through column 1 and adds the artists of songs from the database to the 2d array
						for (int i = 0; i < dV.length && ocurr !=null; i++){
							dV[i][l] = ocurr.getArtist();
							ocurr = ocurr.next();
						}
						//makes a table out of dV and cN
						table = new JTable(dV, cN);
						centerPanel.getViewport().add(table); //add the table to the centerPanel
						updateUI(); //updates the UI
					}	
				}
			} // actionPerformed
		}		
	} // ButtonListener
}
