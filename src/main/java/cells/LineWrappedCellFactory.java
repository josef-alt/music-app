package cells;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;

/**
 * Very basic line-wrapped cell factory for use anywhere that does not have a
 * custom cell factory.
 */
public class LineWrappedCellFactory implements Callback<ListView, ListCell> {
	@Override
	public ListCell call(ListView param) {
		return new TextFieldListCell<>() {
			{
				setMaxWidth(160);
				setPrefWidth(160);
				setWrapText(true);
			}
		};
	}
}