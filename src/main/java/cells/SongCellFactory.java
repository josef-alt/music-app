package cells;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import media.Song;

public class SongCellFactory implements Callback<ListView<Song>, ListCell<Song>> {
	@Override
	public ListCell<Song> call(ListView<Song> param) {
		return new SongCell();
	}
}