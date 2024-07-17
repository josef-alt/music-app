package media;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Basic play list structure to be expanded later
 */
public class Playlist {
	private String name;
	private ObservableList<Song> songs;

	public Playlist(String name, List<Song> songs) {
		this.name = name;
		this.songs = FXCollections.observableArrayList(songs);
	}

	/**
	 * 	Reads a playlist from a local m3u file
	 * 	Supported directives:
	 * 		#PLAYLIST
	 * 		#EXTINF
	 * 		#EXTM3U
	 */
	public static Playlist fromM3u(String playlist, Library library) {
		List<Song> songs = new ArrayList<>();
		
		// default playlist name because #PLAYLIST tag is optional
		String playlistName = playlist.substring(playlist.indexOf(".m3u"));

		try {
			String playlistPath = "playlists/" + playlist;
			File jarPath = new File(Playlist.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			FileInputStream stream = new FileInputStream(new File(jarPath.getParent() + File.separator + playlistPath));

			Scanner reader = new Scanner(new String(stream.readAllBytes()));
			String header = reader.nextLine();
			if (header.contains("#EXTM3U")) {
				while (reader.hasNext()) {
					String entry = reader.nextLine();
					
					// determine directive
					if (entry.startsWith("#EXTINF:")) {
						/*	Directive format
						 * 	EXTINF:duration,Artist - Title
						 * 	Path
						 */

						// TODO: probably don't need all of these
						String directive = entry.substring(0, 8);
						String duration = entry.substring(8, entry.indexOf(',', 8));
						String[] songInfo = entry.substring(entry.indexOf(',', 8) + 1).split(" - ");
						String artist = songInfo[0];
						String title = songInfo[1];
						String filePath = reader.nextLine();

						// TODO: find song in library instead of re-reading
						Song newSong = new Song(new File(filePath));
						if (newSong.readSuccessfully())
							songs.add(newSong);
					} else if (entry.startsWith("#PLAYLIST:")) {
						/*	Directive format
						 * 	#PLAYLIST:playlist name
						 */
						playlistName = entry.split(":")[1];
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Playlist(playlistName, songs);
	}

	public ObservableList<Song> getSongs() {
		return songs;
	}

	public String toString() {
		return name;
	}
}
