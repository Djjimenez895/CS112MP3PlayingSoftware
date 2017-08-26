import java.awt.Dimension;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
// Davin Jimenez
// Project 4 MP3 Player
public class Driver {
	public static void main(String[] args) {
		//Driver main
		  Path path = Paths.get("mp3"); //Path where the files will come from
		  JFrame frame = new JFrame ("Mp3 player");
	      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	      SongDatabase songs  = new SongDatabase();
	      songs.printFilesAndFolders(path);
	     
	      MPlayerPanel panel  = new MPlayerPanel(songs);
	      panel.setPreferredSize(new Dimension(600,400));
	      frame.add (panel);
	      frame.pack();
	      frame.setVisible(true);
	      
	    
	}
}