package controllers;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import media.*;

public class SidebarController {
	@FXML
	private ListView sideview_songs;

	private Model model;

	public SidebarController(Model model) {
		this.model = model;
	}

	@FXML
	public void initialize() {
		sideview_songs.getSelectionModel().selectedIndexProperty().addListener((a, b, c) -> {
			int index = a.getValue().intValue();
			if (index >= 0)
				model.getPlayer().setIndex(index);
		});

		sideview_songs.setItems(model.getPlayer().getObservableList());
	}
}
