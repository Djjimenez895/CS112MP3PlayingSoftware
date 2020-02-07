import java.awt.Dimension;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFrame;

/*
 * File: Driver.java
 * Description: The driver creates a GUI object (MPlayerPanel) for the Java application and also establishes the path from which MP3 files will be read. Compile and run this file
 * to start the application.
*/

public class Driver {
    public static void main(String[] args) {
    Path path = Paths.get("mp3"); // Path where the files will come from
    JFrame frame = new JFrame ("Mp3 player");
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

    SongDatabase songs  = new SongDatabase();
    songs.printFilesAndFolders(path);
    MPlayerPanel panel  = new MPlayerPanel(songs); // Panel that will display all the buttons and list of songs
    panel.setPreferredSize(new Dimension(600,400));

    frame.add (panel);
    frame.pack();
    frame.setVisible(true);
    }
}
