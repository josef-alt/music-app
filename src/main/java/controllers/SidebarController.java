package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import util.ThemeSwitcher;

/**
 * Controls for the tabbed side-bar with the currently playing queue
 * and filters for artist/album/genre
 */
public class SidebarController {
	@FXML
	private ListView sideview_songs;

	@FXML
	private Tab artists_tab, albums_tab, genres_tab;

	private Model model;
	private ThemeSwitcher switcher;

	public SidebarController(Model model, ThemeSwitcher switcher) {
		this.model = model;
		this.switcher = switcher;
	}

	@FXML
	public void initialize() {
		// set up tab icons and link to theme switcher
		ImageView artist_icon = new ImageView();
		ImageView album_icon = new ImageView();
		ImageView genre_icon = new ImageView();
		switcher.addView(artist_icon, "artist", 20);
		switcher.addView(album_icon, "album", 20);
		switcher.addView(genre_icon, "genre", 20);

		artists_tab.setGraphic(artist_icon);
		albums_tab.setGraphic(album_icon);
		genres_tab.setGraphic(genre_icon);

		// link user selections to player
		sideview_songs.getSelectionModel().selectedIndexProperty().addListener((obs, old, sel) -> {
			int index = sel.intValue();
			if (index >= 0)
				model.getPlayer().setIndex(index);
		});

		sideview_songs.setItems(model.getPlayer().getObservableList());
		sideview_songs.setPrefHeight(400);

		// link auto-play to list view
		model.getPlayer().addListener(() -> {
			sideview_songs.getSelectionModel().select(model.getPlayer().getSong());
		});
	}
}
