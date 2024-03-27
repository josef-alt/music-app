package controllers;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.*;
import media.*;
import util.ResourceManager;

/**
 * Used to apply theme switches to application and buttons
 */
public class ControlsUpdater {
	private ImageView prev_button, play_pause, next_button;

	private Image play_icon, pause_icon;

	private String activeTheme;

	private boolean sliderUpdateInProgress = false;

	private double sliderNewVal = Double.NEGATIVE_INFINITY;

	public ControlsUpdater(ImageView prev_button, ImageView play_pause, ImageView next_button, String active) {
		this.prev_button = prev_button;
		this.play_pause = play_pause;
		this.next_button = next_button;
		this.activeTheme = active;
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

		activeTheme = theme;
	}

	public String getTheme() {
		return activeTheme;
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

	// user for slider tooltip display
	private int current_mins = 0, current_secs = 0;

	/**
	 * Configure listeners to form link between slider and player
	 */
	public void configureSlider(Slider time_slider, Player player) {
		time_slider.setMax(player.getSong().getDuration());
		time_slider.setMin(0);

		int total_mins = (int) (time_slider.getMax() / 60);
		int total_secs = (int) (time_slider.getMax() - 60 * total_mins);

		Tooltip indicator = new Tooltip(
				String.format("%d:%02d / %d:%02d", 0, 0, total_mins, total_secs));
		indicator.setShowDelay(Duration.seconds(0.5));
		time_slider.setTooltip(indicator);

		// automatic slider increment
		player.addTimeListener((obs, old_val, new_val) -> {
			if (!sliderUpdateInProgress) {
				time_slider.setValue(new_val.toSeconds());
				current_mins = (int) (time_slider.getValue() / 60);
				current_secs = (int) (time_slider.getValue() - 60 * current_mins);
				indicator.setText(
						String.format("%d:%02d / %d:%02d", current_mins, current_secs, total_mins, total_secs));
			}
		});

		time_slider.valueChangingProperty().addListener((obs, old_progress, new_progress) -> {
			sliderUpdateInProgress = new_progress;
			if (!sliderUpdateInProgress && sliderNewVal != Double.NEGATIVE_INFINITY) {
				player.seek(sliderNewVal);
			}
		});

		// user led slider increment
		time_slider.valueProperty().addListener((obs, old_time, new_time) -> {
			int change = Math.abs(new_time.intValue() - old_time.intValue());
			if (change > 10) {
				if (sliderUpdateInProgress) {
					sliderNewVal = new_time.intValue();
				} else {
					player.seek(new_time.intValue());
				}
			}
		});
	}
}
