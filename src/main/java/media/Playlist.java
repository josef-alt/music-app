package media;

import java.util.List;

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

	public ObservableList<Song> getSongs() {
		return songs;
	}

	public String toString() {
		return name;
	}
}
