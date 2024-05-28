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
public class TrackInfoController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name, artist_name, album_name;

	private Model model;

	public TrackInfoController(Model model) {
		this.model = model;
	}

	/**
	 * Set up player listeners
	 */
	@FXML
	public void initialize() {
		model.getPlayer().addListener(() -> setTrackInformation());
	}

	/**
	 * Update track information in user interface.
	 */
	void setTrackInformation() {
		// if no song is playing, load up default values
		System.out.println("update");
		if (model.getPlayer() == null || model.getPlayer().getSong() == null) {
			song_name.setText("No Song Loaded");
			artist_name.setText("No Artist");
			album_name.setText("No Album");
			album_art.setImage(Song.getDefaultCover());
			return;
		}
		Song currentlyPlaying = model.getPlayer().getSong();

		if (currentlyPlaying.hasAlbum()) {
			album_name.setText(currentlyPlaying.getAlbum().getAlbumName());
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
