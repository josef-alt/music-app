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
import util.*;

public class MainPageController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name, artist_name, album_name;

	@FXML
	private Button prev_button, pause_button, next_button;

	@FXML
	private MenuItem quit_button;

	@FXML
	private Menu themes_picker;

	private final Stage currentStage;
	private final Player player;

	private Preferences userSettings;
	private TrackInformationUpdater trackInfo;
	private ControlsUpdater controls;

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
			currentScene.getStylesheets().add(ResourceManager.getStyleSheet("default").toString());

			currentStage.setScene(currentScene);
		} catch (IOException e) {
			System.err.println("Failed to load fxml");
			e.printStackTrace();
		}
	}

	public void showStage() {
		currentStage.showAndWait();
	}

	/**
	 * Configures button handlers and loads icons.
	 */
	@FXML
	public void initialize() {
		// prepare alternative themes
		loadThemes();

		// load required resources
		ImageView play_icon = new ImageView();
		ImageView prev_icon = new ImageView(ResourceManager.getImage("/themes/default/prev.png"));
		ImageView next_icon = new ImageView(ResourceManager.getImage("/themes/default/next.png"));

		controls = new ControlsUpdater(prev_icon, play_icon, next_icon);
		controls.setPlayIcon(ResourceManager.getImage("/themes/default/play.png"));
		controls.setPauseIcon(ResourceManager.getImage("/themes/default/pause.png"));
		controls.togglePause(true);

		trackInfo = new TrackInformationUpdater(album_art, artist_name, album_name, song_name);
		trackInfo.setTrackInformation(player.getSong());

		// set up icons
		prev_button.setGraphic(prev_icon);
		pause_button.setGraphic(play_icon);
		next_button.setGraphic(next_icon);

		// configure controls
		prev_button.setOnAction(event -> player.prevSong());
		pause_button.setOnAction(event -> controls.togglePause(player.pause()));
		next_button.setOnAction(event -> player.nextSong());

		// update user interface
		player.addEvent(() -> trackInfo.setTrackInformation(player.getSong()));

		// exit safely
		quit_button.setOnAction(event -> quit());
	}

	/**
	 * Attempts to load installed themes from resources
	 */
	private void loadThemes() {
		String[] themes = ResourceManager.loadThemes();

		for (String dir : themes) {
			MenuItem item = new MenuItem(dir);
			item.setOnAction(event -> controls.setTheme(dir));
			themes_picker.getItems().add(item);
		}
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
