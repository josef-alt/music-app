package pages;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import media.*;
import util.*;

/**
 * Just a bit of fun because I like to see a summary of my music collection from
 * time to time.
 */
public class StatsPage {
	@FXML
	private ListView stats_list;

	private Player player;

	public StatsPage(Stage parent, Player player) {
		Stage popupStage = new Stage();
		popupStage.setResizable(false);
		popupStage.initOwner(parent);
		popupStage.getIcons().add(ResourceManager.getImage("/img/large/play.png"));

		this.player = player;

		FXMLLoader load = new FXMLLoader(getClass().getResource("/fxml/stats.fxml"));
		load.setController(this);
		try {
			Scene sc = new Scene(load.load());
			sc.getStylesheets().addAll(parent.getScene().getStylesheets());
			popupStage.setScene(sc);
			popupStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (!newValue)
						popupStage.close();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		popupStage.show();
	}

	/**
	 * Utility function for converting a duration to hh:mm:ss format
	 * 
	 * @param duration in seconds
	 */
	private String durationToString(int duration) {
		int hours = duration / 3600;
		duration %= 3600;
		int minutes = duration / 60;
		duration %= 60;
		int seconds = duration % 60;

		if (hours > 0) {
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			return String.format("%02d:%02d", minutes, seconds);
		}
	}

	/**
	 * Create an HBox with consistent sizing and alignment for labeled fields.
	 */
	private Node createTwoColumnNode(String label, String value) {
		HBox row = new HBox();
		Label left = new Label(label);
		left.setMinWidth(180);
		Label right = new Label(value);
		right.setMinWidth(80);
		right.setAlignment(Pos.BASELINE_RIGHT);

		row.getChildren().add(left);
		row.getChildren().add(right);
		HBox.setHgrow(left, Priority.NEVER);
		HBox.setHgrow(right, Priority.NEVER);

		return row;
	}

	/**
	 * Creates a Label that is right aligned and sized to match the two column
	 * label/field nodes.
	 */
	private Node createFullWidthNode(String value) {
		Label row = new Label(value);
		row.setMaxWidth(260);
		row.setAlignment(Pos.BASELINE_RIGHT);
		return row;
	}

	@FXML
	public void initialize() {
		ArrayList<Node> elements = new ArrayList<>(10);
		Separator sep = new Separator();

		LibraryStats statistics = player.getStats();
		String title;

		// general library statistics
		elements.add(createTwoColumnNode("Songs", Integer.toString(statistics.getTotalSongs())));
		elements.add(createTwoColumnNode("Albums", Integer.toString(statistics.getTotalAlbums())));
		elements.add(createTwoColumnNode("Artists", Integer.toString(statistics.getTotalArtists())));
		elements.add(sep);

		// extrema
		Song shortest = statistics.getShortest();
		if (shortest != null) {
			elements.add(createTwoColumnNode("Shortest Song", durationToString(shortest.getDuration())));

			title = shortest.getTitle();
			elements.add(createFullWidthNode(title == null ? "Unknown Title" : title));
		}

		Song longest = statistics.getLongest();
		if (longest != null) {
			elements.add(createTwoColumnNode("Longest Song", durationToString(longest.getDuration())));

			title = longest.getTitle();
			elements.add(createFullWidthNode(title == null ? "Unknown Title" : title));
		}
		elements.add(sep);

		// most played?
		// average?
		// genres?

		stats_list.getItems().setAll(elements);
	}
}
