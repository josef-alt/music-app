package controllers;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.prefs.*;
import java.util.stream.*;

import javax.imageio.*;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.*;

import javafx.application.*;
import javafx.beans.binding.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.*;
import javafx.stage.*;
import media.*;

public class MainPageController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name, artist_name, album_name;

	@FXML
	private Button prev_button, pause_button, next_button;

	@FXML
	private MenuItem quit_button;

	private final Stage currentStage;
	private final Player player;

	private Preferences userSettings;

	public MainPageController() {
		currentStage = new Stage();
		currentStage.setResizable(false);

		player = new Player();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/basic.fxml"));
			loader.setController(this);

			Scene currentScene = new Scene(loader.load());

			// configure layout
			currentScene.getStylesheets().add(getClass().getResource("/themes/basic_config.css").toString());

			// configure color scheme - should not alter layout
			currentScene.getStylesheets().add(getClass().getResource("/themes/1500.css").toString());

			currentStage.setScene(currentScene);

			// ensure that the first track is displayed properly
			setTrackInformation();

			System.out.println("finished constructor");
		} catch (IOException e) {
			System.err.println("Failed to load fxml");
			e.printStackTrace();
		}
	}

	public void showStage() {
		currentStage.showAndWait();
	}

	private ImageView play_icon, prev_icon, next_icon;
	private Image play, pause;

	@FXML
	/**
	 * Configures button handlers and loads icons.
	 */
	public void initialize() {
		// load required resources
		play = new Image(getClass().getResourceAsStream("/img/50/play.png"));
		pause = new Image(getClass().getResourceAsStream("/img/50/pause.png"));

		prev_icon = new ImageView(new Image(getClass().getResourceAsStream("/img/50/prev.png")));
		play_icon = new ImageView(play);
		next_icon = new ImageView(new Image(getClass().getResourceAsStream("/img/50/next.png")));

		// set up icons
		prev_button.setGraphic(prev_icon);
		pause_button.setGraphic(play_icon);
		next_button.setGraphic(next_icon);

		// configure controls
		prev_button.setOnAction(event -> player.prevSong());
		pause_button.setOnAction(event -> {
			player.pause();
			if (player.isPaused()) {
				play_icon.setImage(play);
			} else {
				play_icon.setImage(pause);
			}
		});
		next_button.setOnAction(event -> player.nextSong());

		// update user interface
		player.addEvent(() -> setTrackInformation());

		// exit safely
		quit_button.setOnAction(event -> quit());
	}

	/**
	 * Update track information in user interface.
	 */
	private void setTrackInformation() {
		System.out.println("event " + player.getSong());

		Song currentlyPlaying = player.getSong();
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

	/**
	 * Ensures player is closed properly and exits stage.
	 */
	private void quit() {
		System.out.println("quitting");
		player.quit();
		currentStage.close();
	}
}
