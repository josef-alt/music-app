package controllers;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.prefs.Preferences;

import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import media.Player;
import util.*;

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

	private final Stage currentStage;
	private final Player player;

	public MainController() {
		currentStage = new Stage();
		currentStage.setResizable(false);
		currentStage.setTitle("Music App");
		currentStage.getIcons().add(ResourceManager.getImage("/img/large/play.png"));

		boolean shuffle = Boolean.parseBoolean(PreferenceManager.getShuffled());
		player = new Player(shuffle);
		String currentDirectory = PreferenceManager.getDirectory();
		if (!currentDirectory.equals(PreferenceManager.DEFAULT)) {
			player.setDirectory(new File(currentDirectory));
		}

		Model model = new Model(player, this);
		ThemeSwitcher themeSwitcher = new ThemeSwitcher();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/window.fxml"));
			loader.setController(this);
			loader.setControllerFactory(type -> {
				if (type.equals(MenuController.class))
					return new MenuController(model, themeSwitcher);
				if (type.equals(TrackInfoController.class))
					return new TrackInfoController(model);
				if (type.equals(PlayerControlsController.class)) {
					playerControlsController = new PlayerControlsController(model, themeSwitcher);
					return playerControlsController;
				}
				if (type.equals(SidebarController.class))
					return new SidebarController(model, themeSwitcher);
				throw new IllegalArgumentException("Unexpected controller type: " + type);
			});

			// configure layout
			Scene currentScene = new Scene(loader.load());
			currentScene.getStylesheets().add(getClass().getResource("/themes/basic_config.css").toString());
			themeSwitcher.setStylesheets(currentScene.getStylesheets());
			currentStage.setScene(currentScene);
		} catch (IOException e) {
			System.err.println("Failed to load fxml");
			e.printStackTrace();
		}

		player.notifyListeners();
		themeSwitcher.update(PreferenceManager.getTheme());
	}

	@FXML
	public void initialize() {
		// make sure all close events are handled the same way
		currentStage.setOnCloseRequest(event -> quit());
		currentStage.setOnHidden(event -> quit());
	}

	public void showStage() {
		currentStage.showAndWait();
	}

	/**
	 * Ensure preferences are up to date
	 */
	private void writePreferences() {
		PreferenceManager.setDirectory(player.getDirectory());
		PreferenceManager.setShuffle(player.getShuffled());
	}

	public Stage getStage() {
		return currentStage;
	}

	/**
	 * Ensures player is closed properly and exits stage.
	 */
	public void quit() {
		System.out.println("quit button");
		writePreferences();
		player.quit();
		currentStage.close();
	}
}
