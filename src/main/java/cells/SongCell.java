package cells;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.*;
import javafx.scene.control.Label;

import media.Song;

/**
 * Custom cell class to display both the track name and the artist name in
 * the sidebar.
 */
public class SongCell extends ListCell<Song> {

	@FXML
	private Label titleLabel;

	@FXML
	private Label artistLabel;

	public SongCell() {
		loadFXML();

		titleLabel.setMaxWidth(160);
		titleLabel.setPrefWidth(160);
		titleLabel.setWrapText(true);

		artistLabel.setMaxWidth(160);
		artistLabel.setPrefWidth(160);
		artistLabel.setWrapText(true);
	}

	private void loadFXML() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/song_cell.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void updateItem(Song item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} else {
			titleLabel.setText(item.getTitle());
			artistLabel.setText(item.getArtist());

			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
	}
}