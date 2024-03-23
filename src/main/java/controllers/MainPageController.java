package controllers;

import java.io.*;
import java.util.prefs.Preferences;

import javafx.fxml.FXMLLoader;
import javafx.event.*;
import javafx.fxml.FXML;

import javafx.scene.Scene;

import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.scene.image.Image;
import media.Player;

import util.ResourceManager;

public class MainPageController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name, artist_name, album_name;

	@FXML
	private Button prev_button, pause_button, next_button;

	@FXML
	private MenuItem quit_button, load_directory, load_file;

	@FXML
	private Menu themes_picker;

	private final Stage currentStage;
	private final Player player;

	private Preferences userSettings;
	private String startupTheme, currentDirectory;
	private TrackInformationUpdater trackInfo;
	private ControlsUpdater controls;

	public MainPageController() {
		currentStage = new Stage();
		currentStage.setResizable(false);
		currentStage.setTitle("Music App");
		currentStage.getIcons().add(ResourceManager.getImage("/img/large/play.png"));

		userSettings = Preferences.userRoot().node(getClass().getName());
		startupTheme = userSettings.get("user-theme", "default");
		currentDirectory = userSettings.get("active-directory", "no directory set");
		player = new Player();
		if (!currentDirectory.equals("no directory set")) {
			player.setDirectory(new File(currentDirectory));
		}

		System.out.println("starting with theme: " + startupTheme);
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/basic.fxml"));
			loader.setController(this);

			Scene currentScene = new Scene(loader.load());

			// configure layout
			currentScene.getStylesheets().add(getClass().getResource("/themes/basic_config.css").toString());

			// configure color scheme - should not alter layout
			currentScene.getStylesheets().add(ResourceManager.getStyleSheet(startupTheme).toString());

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
		// load required resources
		ImageView play_icon = new ImageView();
		ImageView prev_icon = new ImageView(ResourceManager.getImage("/themes/" + startupTheme + "/prev.png"));
		ImageView next_icon = new ImageView(ResourceManager.getImage("/themes/" + startupTheme + "/next.png"));

		controls = new ControlsUpdater(prev_icon, play_icon, next_icon);
		controls.setPlayIcon(ResourceManager.getImage("/themes/" + startupTheme + "/play.png"));
		controls.setPauseIcon(ResourceManager.getImage("/themes/" + startupTheme + "/pause.png"));
		controls.togglePause(true);

		// prepare alternative themes
		controls.loadThemes(themes_picker);
		controls.addEvent(() -> {
			userSettings.put("user-theme", controls.getTheme());
		});

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

		load_file.setOnAction(event -> {
			FileChooser picker = new FileChooser();

			File picked = picker.showOpenDialog(currentStage);
			if (picked != null && picked.exists()) {
				System.out.println("loading file");
				player.setDirectory(picked);
				currentDirectory = picked.getAbsolutePath();
			}
		});

		load_directory.setOnAction(event -> {
			DirectoryChooser picker = new DirectoryChooser();

			File picked = picker.showDialog(currentStage);
			if (picked != null && picked.exists()) {
				System.out.println("loading directory");
				player.setDirectory(picked);
				currentDirectory = picked.getAbsolutePath();
			}
		});

		// exit safely
		quit_button.setOnAction(event -> quit());
		currentStage.setOnCloseRequest(event -> quit());
	}

	/**
	 * Ensure preferences are up to date
	 */
	private void writePreferences() {
		userSettings.put("user-theme", startupTheme);
		userSettings.put("active-directory", currentDirectory);
	}

	/**
	 * Ensures player is closed properly and exits stage.
	 */
	private void quit() {
		System.out.println("quitting");
		writePreferences();
		player.quit();
		currentStage.close();
	}
}
