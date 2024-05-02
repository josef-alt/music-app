package cells;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import media.Album;

public class AlbumCellFactory implements Callback<ListView<Album>, ListCell<Album>> {
	@Override
	public ListCell<Album> call(ListView<Album> param) {
		return new AlbumCell();
	}
}