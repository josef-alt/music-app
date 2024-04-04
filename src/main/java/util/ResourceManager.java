package util;

import java.io.File;
import java.net.URISyntaxException;

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
			File[] folders = (new File(ResourceManager.class.getResource("/themes/").toURI())).listFiles();
			for (File theme : folders) {
				if (!theme.isDirectory()) {
					continue;
				}
				themes.add(theme.getName());
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return themes.stream().toArray(String[]::new);
		}
	}
}
