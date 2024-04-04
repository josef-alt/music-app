package controllers;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import media.Player;
import pages.*;
import util.*;

/**
 * Handles the menu bar and everything there in
 */
public class MenuController {
	@FXML
	private MenuItem quit_button, load_directory, load_file, about_button, stats_button, shuffle, inorder;

	@FXML
	private Menu themes_picker;

	private ThemeSwitcher switcher;
	private Model model;

	public MenuController(Model model, ThemeSwitcher switcher) {
		this.model = model;
		this.switcher = switcher;
	}

	@FXML
	public void initialize() {
		loadThemes();

		// TODO seems that these have to be set up separately
		load_file.setOnAction(event -> {
			FileChooser picker = new FileChooser();

			File picked = picker.showOpenDialog(null);
			if (picked != null && picked.exists()) {
				model.getPlayer().setDirectory(picked);
			}
		});

		load_directory.setOnAction(event -> {
			DirectoryChooser picker = new DirectoryChooser();

			File picked = picker.showDialog(null);
			if (picked != null && picked.exists()) {
				model.getPlayer().setDirectory(picked);
			}
		});

		shuffle.setOnAction(event -> model.getPlayer().shuffle());
		inorder.setOnAction(event -> model.getPlayer().inorder());

		Stage parentStage = model.getMainController().getStage();
		about_button.setOnAction(event -> new AboutPage(parentStage));
		stats_button.setOnAction(event -> new StatsPage(parentStage, model.getPlayer()));

		quit_button.setOnAction(event -> model.getMainController().quit());
	}

	/**
	 * Attempts to load installed themes from resources
	 */
	public void loadThemes() {
		String[] themes = ResourceManager.loadThemes();

		for (String dir : themes) {
			MenuItem item = new MenuItem(dir);
			item.setOnAction(event -> switcher.update(dir));
			themes_picker.getItems().add(item);
		}
	}
}
