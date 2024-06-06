package controllers;

import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;

import media.Player;
import util.ThemeSwitcher;

/**
 * Acts as the link between the user and the Player
 */
public class PlayerControlsController {
	@FXML
	private Button prev_button, pause_button, next_button;

	private ImageView prev_view, pause_view, next_view;
	private Image play_icon, pause_icon;

	@FXML
	private Slider time_slider;

	private ThemeSwitcher switcher;
	private Model model;

	public PlayerControlsController(Model model, ThemeSwitcher switcher) {
		this.model = model;
		this.switcher = switcher;
	}

	/**
	 * Set up controls using startup theme for icons
	 */
	@FXML
	public void initialize() {
		prev_view = new ImageView();
		pause_view = new ImageView(switcher.getPlayIcon());
		next_view = new ImageView();
		switcher.addView(prev_view, "prev", 50);
		switcher.addView(next_view, "next", 50);
		switcher.addListener(() -> togglePause());

		prev_button.setGraphic(prev_view);
		pause_button.setGraphic(pause_view);
		next_button.setGraphic(next_view);

		model.getPlayer().addListener(() -> {
			togglePause();
			configureSlider();
		});
		prev_button.setOnAction(event -> model.getPlayer().prevSong());
		pause_button.setOnAction(event -> {
			model.getPlayer().pause();
			togglePause();
		});
		next_button.setOnAction(event -> model.getPlayer().nextSong());
	}

	/**
	 * Update Play/Pause icon
	 */
	void togglePause() {
		if (model.getPlayer().isPaused()) {
			pause_view.setImage(switcher.getPlayIcon());
		} else {
			pause_view.setImage(switcher.getPauseIcon());
		}
	}

	private double sliderNewVal = Double.NEGATIVE_INFINITY;
	private int current_mins = 0, current_secs = 0;
	private boolean sliderUpdateInProgress = false;

	/**
	 * Configure listeners to form link between slider and player
	 */
	public void configureSlider() {
		if (model.getPlayer() == null || model.getPlayer().getSong() == null) {
			System.err.println("failed to configure slider");
			return;
		}

		time_slider.setMax(model.getPlayer().getSong().getDuration());
		time_slider.setMin(0);
		time_slider.setValue(0);

		int total_mins = (int) (time_slider.getMax() / 60);
		int total_secs = (int) (time_slider.getMax() - 60 * total_mins);

		Tooltip indicator = new Tooltip(String.format("%d:%02d / %d:%02d", 0, 0, total_mins, total_secs));
		indicator.setShowDelay(Duration.seconds(0.5));
		time_slider.setTooltip(indicator);

		// automatic slider increment
		model.getPlayer().addTimeListener((obs, old_val, new_val) -> {
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
				model.getPlayer().seek(sliderNewVal);
			}
		});

		// user led slider increment
		time_slider.valueProperty().addListener((obs, old_time, new_time) -> {
			int change = Math.abs(new_time.intValue() - old_time.intValue());
			if (change > 10) {
				if (sliderUpdateInProgress) {
					sliderNewVal = new_time.intValue();
				} else {
					model.getPlayer().seek(new_time.intValue());
				}
			}
		});
	}
}
