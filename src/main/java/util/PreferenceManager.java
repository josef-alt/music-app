package util;

import java.util.prefs.Preferences;

/**
 * Static access to preferences object to avoid creating multiple Preferences
 * objects, each of which would need a node path set. Also lock down which
 * keys are accessible.
 */
public class PreferenceManager {
	private static final Preferences userSettings = Preferences.userRoot().node("music-app");
	public static final String DEFAULT = "PreferenceManager NO VALUE SET";

	public static void setDirectory(String dir) {
		userSettings.put("active-directory", dir);
	}

	public static String getDirectory() {
		return userSettings.get("active-directory", DEFAULT);
	}

	public static void setTheme(String theme) {
		userSettings.put("user-theme", theme);
	}

	public static String getTheme() {
		return userSettings.get("user-theme", DEFAULT);
	}

	public static void setShuffle(boolean value) {
		userSettings.put("shuffle", Boolean.toString(value));
	}

	public static String getShuffled() {
		return userSettings.get("shuffle", DEFAULT);
	}
}
