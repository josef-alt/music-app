package controllers;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;

import media.Player;
import pages.*;
import util.ResourceManager;

/**
 * Handles the menu bar and everything there in
 */
public class MenuController extends SubController {
	@FXML
	private MenuItem quit_button, load_directory, load_file, about_button;

	@FXML
	private Menu themes_picker;

	@FXML
	public void initialize() {
		loadThemes();

		// TODO seems that these have to be set up separately
		load_file.setOnAction(event -> {
			FileChooser picker = new FileChooser();

			File picked = picker.showOpenDialog(null);
			if (picked != null && picked.exists()) {
				player.setDirectory(picked);
			}
		});

		load_directory.setOnAction(event -> {
			DirectoryChooser picker = new DirectoryChooser();

			File picked = picker.showDialog(null);
			if (picked != null && picked.exists()) {
				player.setDirectory(picked);
			}
		});
		about_button.setOnAction(event -> new AboutPage(null));

		quit_button.setOnAction(event -> parent.quit());
	}

	/**
	 * Attempts to load installed themes from resources
	 */
	public void loadThemes() {
		String[] themes = ResourceManager.loadThemes();

		for (String dir : themes) {
			MenuItem item = new MenuItem(dir);
			item.setOnAction(event -> parent.setTheme(dir));
			themes_picker.getItems().add(item);
		}
	}
}
