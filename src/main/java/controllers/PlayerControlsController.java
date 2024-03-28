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
import util.ResourceManager;

/**
 * Acts as the link between the user and the Player
 */
public class PlayerControlsController extends SubController {
	@FXML
	private Button prev_button, pause_button, next_button;

	private ImageView prev_view, pause_view, next_view;
	private Image play_icon, pause_icon;

	@FXML
	private Slider time_slider;

	private String activeTheme;

	/**
	 * Set up controls using startup theme for icons
	 */
	@FXML
	public void initialize() {
		activeTheme = Preferences.userRoot().node("music-app").get("user-theme", "default");
		play_icon = ResourceManager.getImage("/themes/" + activeTheme + "/play.png");
		pause_icon = ResourceManager.getImage("/themes/" + activeTheme + "/pause.png");

		prev_view = new ImageView(ResourceManager.getImage("/themes/" + activeTheme + "/prev.png"));
		pause_view = new ImageView(play_icon);
		next_view = new ImageView(ResourceManager.getImage("/themes/" + activeTheme + "/next.png"));

		prev_button.setGraphic(prev_view);
		pause_button.setGraphic(pause_view);
		next_button.setGraphic(next_view);
	}

	/**
	 * Link the shared Player instance and add required listeners
	 */
	@Override
	public void setPlayer(Player newPlayer) {
		super.setPlayer(newPlayer);

		player.addListener(() -> configureSlider());
		prev_button.setOnAction(event -> player.prevSong());
		pause_button.setOnAction(event -> togglePause(player.pause()));
		next_button.setOnAction(event -> player.nextSong());
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
		boolean paused = pause_view.getImage() == play_icon;

		// load new theme icons
		this.prev_view.setImage(ResourceManager.getImage("/themes/" + theme + "/prev.png"));
		this.next_view.setImage(ResourceManager.getImage("/themes/" + theme + "/next.png"));
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
			pause_view.setImage(play_icon);
		} else {
			pause_view.setImage(pause_icon);
		}
	}

	private double sliderNewVal = Double.NEGATIVE_INFINITY;
	private int current_mins = 0, current_secs = 0;
	private boolean sliderUpdateInProgress = false;

	/**
	 * Configure listeners to form link between slider and player
	 */
	public void configureSlider() {
		time_slider.setMax(player.getSong().getDuration());
		time_slider.setMin(0);

		int total_mins = (int) (time_slider.getMax() / 60);
		int total_secs = (int) (time_slider.getMax() - 60 * total_mins);

		Tooltip indicator = new Tooltip(String.format("%d:%02d / %d:%02d", 0, 0, total_mins, total_secs));
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
