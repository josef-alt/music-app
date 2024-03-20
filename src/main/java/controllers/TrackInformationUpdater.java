package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import media.Song;

/**
 * Extracted logic for displaying track meta-data on user interface
 */
public class TrackInformationUpdater {
	private ImageView album_art;

	private Label song_name, artist_name, album_name;
	
	public TrackInformationUpdater(ImageView album_art, Label artist_name, Label album_name, Label song_name) {
		this.album_art = album_art;
		this.artist_name = artist_name;
		this.album_name = album_name;
		this.song_name = song_name;
	}

	/**
	 * Update track information in user interface.
	 */
	void setTrackInformation(Song currentlyPlaying) {
		if (currentlyPlaying == null) {
			return;
		}

		if (currentlyPlaying.hasAlbum()) {
			album_name.setText(currentlyPlaying.getAlbum());
		}

		if (currentlyPlaying.hasArtist()) {
			artist_name.setText(currentlyPlaying.getArtist());
		}

		if (currentlyPlaying.hasTitle()) {
			song_name.setText(currentlyPlaying.getTitle());
		}

		// no check because it should never be null
		album_art.setImage(currentlyPlaying.getCover());
	}
}
