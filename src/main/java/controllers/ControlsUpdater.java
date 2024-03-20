package controllers;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import util.ResourceManager;

/**
 * Used to apply theme switches to application and buttons
 */
public class ControlsUpdater {
	private ImageView prev_button, play_pause, next_button;

	private Image play_icon, pause_icon;

	public ControlsUpdater(ImageView prev_button, ImageView play_pause, ImageView next_button) {
		this.prev_button = prev_button;
		this.play_pause = play_pause;
		this.next_button = next_button;
	}

	public void setPlayIcon(Image icon) {
		this.play_icon = icon;
	}

	public void setPauseIcon(Image icon) {
		this.pause_icon = icon;
	}

	/**
	 * Attempts to load installed themes from resources
	 */
	public void loadThemes(Menu themes_picker) {
		String[] themes = ResourceManager.loadThemes();

		for (String dir : themes) {
			MenuItem item = new MenuItem(dir);
			item.setOnAction(event -> setTheme(dir));
			themes_picker.getItems().add(item);
		}
	}

	/**
	 * Setters for icons because these may change with the theme.
	 * 
	 * TODO Styling Issue - Where to call setTheme(default)
	 * The default theme has to be loaded manually in the controller
	 * because getScene returns null.
	 */
	public void setTheme(String theme) {
		// apply new style sheet to scene
		Scene curr = prev_button.getScene();
		if (curr.getStylesheets().size() > 1)
			curr.getStylesheets().remove(1);
		prev_button.getScene().getStylesheets().add(ResourceManager.getStyleSheet(theme));

		// need to keep track of which icon is in use before loading new icons
		boolean paused = play_pause.getImage() == play_icon;

		// load new theme icons
		this.prev_button.setImage(ResourceManager.getImage("/themes/" + theme + "/prev.png"));
		this.next_button.setImage(ResourceManager.getImage("/themes/" + theme + "/next.png"));
		this.play_icon = ResourceManager.getImage("/themes/" + theme + "/play.png");
		this.pause_icon = ResourceManager.getImage("/themes/" + theme + "/pause.png");

		// make sure that the correct icon is put back after theme switch
		togglePause(paused);
	}

	/**
	 * Update Play/Pause icon
	 */
	void togglePause(boolean paused) {
		if (play_icon == null || pause_icon == null) {
			return;
		}

		if (paused) {
			play_pause.setImage(play_icon);
		} else {
			play_pause.setImage(pause_icon);
		}
	}
}
