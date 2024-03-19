package util;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javafx.scene.control.*;
import javafx.scene.image.Image;

public class Resources {
	/**
	 * Retrieve full path for style sheets
	 */
	public static String getStyleSheet(String name) {
		return Resources.class.getResource("/themes/" + name + "/style.css").toString();
	}

	/**
	 * Retrieve image from resources
	 */
	public static Image getImage(String name) {
		return new Image(Resources.class.getResourceAsStream(name));
	}

	/**
	 * Attempts to load installed themes from resources
	 */
	public static String[] loadThemes() {
		ArrayList<String> themes = new ArrayList<>();
		try {
			File[] folders = (new File(Resources.class.getResource("/themes/").toURI())).listFiles();
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
