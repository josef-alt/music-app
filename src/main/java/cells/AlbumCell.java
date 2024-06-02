package cells;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

import media.Album;

/**
 * Custom cell class to display both the album name and the artist name in
 * the sidebar.
 */
public class AlbumCell extends ListCell<Album> {

	@FXML
	private Label albumNameLabel;

	@FXML
	private Label artistLabel;

	public AlbumCell() {
		loadFXML();

		albumNameLabel.setMaxWidth(160);
		albumNameLabel.setPrefWidth(160);
		albumNameLabel.setWrapText(true);

		artistLabel.setMaxWidth(160);
		artistLabel.setPrefWidth(160);
		artistLabel.setWrapText(true);
	}

	private void loadFXML() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/album_cell.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void updateItem(Album item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} else {
			albumNameLabel.setText(item.getAlbumName());
			artistLabel.setText(item.getAlbumArtist());

			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
	}
}