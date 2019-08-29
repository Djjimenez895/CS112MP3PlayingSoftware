# MP3 Playing Software

## Description
A simple MP3 software made with Core Java, JAudioTagger, JLayer, and Java Swing. The program allows the user to load a list a of songs into the software and then select which songs to play. Additionally, users can search for specific songs. The program is multi-threaded to allow for song searching, playing, and file browsing/loading to happen at any time. 

## Design
The MP3 software contains a handful of different files; the local song "database" object is represented as LinkedList
for the sake of experimentation. This project includes the following files:

1) SongNode.java - Specifies all the necessary attributes that a song will have such as a song title, artist, and the filename. 
Each song is represented as a "node" in the LinkedList. 
2) SongList.java - This file contains all the LinkedList functionality. It includes all the basic components of a LinkedList (head, tail, etc.) and specifies functionality such as inserting nodes. 
3) SongDatabase.java - SongDatabase.java is a way to store the song LinkedList and implement functionality such as: a) searching, adding songs to the GUI, and keeping track of the total number of songs.
4) MPlayerPanel.java - Establishes the GUI components and the multi-thread logic that allows the player to search, play, and load music all at the same time.

## Features
The MP3 Software contains a few basic features:
1) Load MP3 Files into the software - The load button allows a user to choose a directory to import music from. <br />
![screenshot](mp3_loading_screenshot.PNG)
2) Search functionality - Once a list of songs is loaded in, the user can search for a specific songs  using the search box. The software will search for any song name that starts with the word(s) that the user typed. <br />
![screenshot](searching_screenshot.PNG)
3) Play - This button plays the chosen MP3 <br />
![screenshot](play_button.PNG)
4) Stop - This stops the current song that is playing <br />
![screenshot](stop_button.PNG)
5) Exit - Exits the program <br />
![screenshot](exit_button.PNG)

## Limitations
Currently, this only works on MP3 files.

## Resources
1) Standard Core Java (compatible with Java 8 or higher)
2) JAudioTagger - JAudioTagger allows the program to get audio file data (tags) such as the title, artist, and etc.
3) JLayer (Java Zoom) - This library allows the program to play MP3s
4) Java Swing - Swing is used to create a simple GUI
