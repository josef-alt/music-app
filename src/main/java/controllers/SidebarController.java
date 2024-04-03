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
		sideview_songs.getSelectionModel().selectedIndexProperty().addListener((obs, old, sel) -> {
			int index = sel.intValue();
			if (index >= 0)
				model.getPlayer().setIndex(index);
		});

		sideview_songs.setItems(model.getPlayer().getObservableList());
		sideview_songs.setPrefHeight(425);
	}
}
