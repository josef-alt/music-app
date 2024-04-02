package controllers;

import java.net.*;
import java.util.*;

import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import media.Player;
import media.Song;

/**
 * Handles display information for the user interface
 */
public class TrackInfoController extends SubController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name, artist_name, album_name;

	/**
	 * Link the shared Player instance and add required listeners
	 */
	@Override
	public void setPlayer(Player p) {
		super.setPlayer(p);
		player.addListener(() -> setTrackInformation());
	}

	/**
	 * Update track information in user interface.
	 */
	void setTrackInformation() {
		if (player == null || player.getSong() == null) {
			return;
		}
		Song currentlyPlaying = player.getSong();

		if (currentlyPlaying.hasAlbum()) {
			album_name.setText(currentlyPlaying.getAlbum());
		} else {
			album_name.setText("Unknown Album");
		}

		if (currentlyPlaying.hasArtist()) {
			artist_name.setText(currentlyPlaying.getArtist());
		} else {
			artist_name.setText("Unknown Artist");
		}

		if (currentlyPlaying.hasTitle()) {
			song_name.setText(currentlyPlaying.getTitle());
		} else {
			song_name.setText(currentlyPlaying.getFile().getName());
		}

		// no check because it should never be null
		album_art.setImage(currentlyPlaying.getCover());
	}
}
