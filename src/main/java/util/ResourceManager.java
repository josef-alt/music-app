package util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class ResourceManager {
	/**
	 * Retrieve full path for style sheets
	 */
	public static String getStyleSheet(String name) {
		return ResourceManager.class.getResource("/themes/" + name + "/style.css").toString();
	}

	/**
	 * Retrieve image from resources
	 */
	public static Image getImage(String name) {
		return new Image(ResourceManager.class.getResourceAsStream(name));
	}

	/**
	 * Retrieve image from resources
	 */
	public static Image getImage(String name, int size) {
		return new Image(ResourceManager.class.getResourceAsStream(name), size, size, true, false);
	}

	/**
	 * Attempts to load installed themes from resources
	 */
	public static String[] loadThemes() {
		ArrayList<String> themes = new ArrayList<>();
		try {
			// loading from the classpath
			InputStream inputStream = ResourceManager.class.getResourceAsStream("/themes");
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				for (String theme = reader.readLine(); theme != null; theme = reader.readLine()) {
					// for now i'm going to assume there is nothing but themes and basic_config.css
					if (!theme.endsWith(".css")) {
						themes.add(theme);
					}
				}
			}

			// if that fails, load from the local file system
			if (themes.isEmpty()) {
				File themesFolder = new File("themes/");
				File[] folders = themesFolder.listFiles();
				if (folders != null) {
					for (File theme : folders) {
						if (theme.isDirectory()) {
							themes.add(theme.getName());
						}
					}
				}
			}
		} catch (IOException e) {
			// returning an empty array will be fine
			// just won't be able to switch theme later
			System.err.println("Error accessing themes: " + e.getMessage());
		}

		return themes.stream().toArray(String[]::new);
	}

	/**
	 * List of all custom playlists found
	 */
	public static String[] loadPlaylists() {
		ArrayList<String> playlists = new ArrayList<>();
		try {
			// loading from the classpath
			InputStream inputStream = ResourceManager.class.getResourceAsStream("/playlists");
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				for (String playlist = reader.readLine(); playlist != null; playlist = reader.readLine()) {
					playlists.add(playlist);
				}
			}

			// if that fails, load from the local file system
			if (playlists.isEmpty()) {
				File playlistFolder = new File("playlists/");
				File[] folders = playlistFolder.listFiles();
				if (folders != null) {
					for (File playlist : folders) {
						playlists.add(playlist.getName());
					}
				}
			}
		} catch (IOException e) {
			// returning an empty array will be fine
			// just won't be able to switch theme later
			System.err.println("Error accessing playlists: " + e.getMessage());
		}

		return playlists.stream().toArray(String[]::new);
	}
}
