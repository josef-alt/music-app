package util;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Handles theme switching for all components.
 */
public class ThemeSwitcher {
	private class Mapping {
		private ImageView view;
		private String name;
		private int size;

		public Mapping(ImageView v, String n, int s) {
			view = v;
			name = n;
			size = s;
		}
	}

	private ArrayList<Mapping> icons;
	private ObservableList<String> stylesheets;
	private ArrayList<Runnable> listen;
	private Image play, pause;

	public ThemeSwitcher() {
		icons = new ArrayList<>();
		listen = new ArrayList<>();
	}

	public void setStylesheets(ObservableList<String> sheets) {
		this.stylesheets = sheets;
	}

	public void addListener(Runnable r) {
		listen.add(r);
	}

	public void addView(ImageView view, String iconName, int size) {
		icons.add(new Mapping(view, iconName, size));
	}

	/**
	 * Update the theme application wide.
	 */
	public void update(String theme) {
		// update icons
		play = ResourceManager.getImage(String.format("/themes/%s/play.png", theme), 50);
		pause = ResourceManager.getImage(String.format("/themes/%s/pause.png", theme), 50);
		listen.forEach(Runnable::run);
		for (Mapping icon : icons) {
			icon.view.setImage(
					ResourceManager.getImage(String.format("/themes/%s/%s.png", theme, icon.name), icon.size));
		}

		// update stylesheets
		if (stylesheets != null) {
			if (stylesheets.size() > 1) {
				stylesheets.remove(1);
			}
			stylesheets.add(ResourceManager.getStyleSheet(theme));
		}

		// update preferences
		PreferenceManager.setTheme(theme);
	}

	public Image getPlayIcon() {
		return play;
	}

	public Image getPauseIcon() {
		return pause;
	}
}
