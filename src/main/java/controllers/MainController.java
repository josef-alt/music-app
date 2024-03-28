package controllers;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import media.Player;
import util.ResourceManager;

/**
 * The top level controller for this application
 */
public class MainController {
	@FXML
	private MenuController menuController;

	@FXML
	private TrackInfoController trackInfoController;

	@FXML
	private PlayerControlsController playerControlsController;

	private final Preferences userSettings;
	private final Stage currentStage;
	private final Player player;

	public MainController() {
		currentStage = new Stage();
		currentStage.setResizable(false);
		currentStage.setTitle("Music App");
		currentStage.getIcons().add(ResourceManager.getImage("/img/large/play.png"));
		userSettings = Preferences.userRoot().node("music-app");
		player = new Player();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
			loader.setController(this);

			Scene currentScene = new Scene(loader.load());

			// configure layout
			currentScene.getStylesheets().add(getClass().getResource("/themes/basic_config.css").toString());

			// configure default/startup theme
			currentScene.getStylesheets().add(ResourceManager.getStyleSheet(playerControlsController.getTheme()));

			currentStage.setScene(currentScene);
		} catch (IOException e) {
			System.err.println("Failed to load fxml");
			e.printStackTrace();
		}

		// ensure directory is not set until all initialize functions have been run
		String currentDirectory = userSettings.get("active-directory", "no directory set");
		if (!currentDirectory.equals("no directory set")) {
			player.setDirectory(new File(currentDirectory));
		}
	}

	@FXML
	public void initialize() {
		// link sub controllers
		menuController.setPlayer(player);
		trackInfoController.setPlayer(player);
		playerControlsController.setPlayer(player);

		menuController.setParentController(this);
		trackInfoController.setParentController(this);
		playerControlsController.setParentController(this);

		// make sure all close events are handled the same way
		currentStage.setOnCloseRequest(event -> quit());
	}

	/**
	 * Set the theme via pass-through to PlayerControlsController
	 */
	public void setTheme(String theme) {
		playerControlsController.setTheme(theme);
	}

	public void showStage() {
		currentStage.showAndWait();
	}

	/**
	 * Ensure preferences are up to date
	 */
	private void writePreferences() {
		userSettings.put("user-theme", playerControlsController.getTheme());
		userSettings.put("active-directory", player.getDirectory());
	}

	/**
	 * Ensures player is closed properly and exits stage.
	 */
	public void quit() {
		writePreferences();
		player.quit();
		currentStage.close();
	}
}
