package controllers;

import cells.*;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import media.*;
import util.ThemeSwitcher;

/**
 * Controls for the tabbed side-bar with the currently playing queue
 * and filters for artist/album/genre
 */
public class SidebarController {
	@FXML
	private ListView active_queue, artists_list, albums_list, genres_list, playlists_list;

	@FXML
	private Tab artists_tab, albums_tab, genres_tab, playlists_tab;

	@FXML
	private TabPane tab_pane;

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
		ImageView playlist_icon = new ImageView();
		switcher.addView(artist_icon, "artist", 20);
		switcher.addView(album_icon, "album", 20);
		switcher.addView(genre_icon, "genre", 20);
		switcher.addView(playlist_icon, "playlist", 20);

		artists_tab.setGraphic(artist_icon);
		albums_tab.setGraphic(album_icon);
		genres_tab.setGraphic(genre_icon);
		playlists_tab.setGraphic(playlist_icon);

		// link user selections to player
		active_queue.getSelectionModel().selectedIndexProperty().addListener((obs, old, sel) -> {
			int index = sel.intValue();
			if (index >= 0)
				model.getPlayer().setIndex(index);
		});

		active_queue.setItems(model.getPlayer().getQueue());
		active_queue.setPrefHeight(400);
		active_queue.setCellFactory(new SongCellFactory());

		// link auto-play to list view
		model.getPlayer().addListener(() -> {
			active_queue.getSelectionModel().select(model.getPlayer().getSong());
		});

		// TODO lots of very simliar code below
		artists_list.setPrefHeight(400);
		artists_list.setItems(model.getPlayer().getStats().getAllArtists());
		artists_list.getSelectionModel().selectedItemProperty().addListener((obs, current, selected) -> {
			if (selected != null) {
				model.getPlayer().playArtist((String) selected);
				Platform.runLater(() -> {
					tab_pane.getSelectionModel().select(0);
					artists_list.getSelectionModel().clearSelection();
				});
			}
		});

		albums_list.setPrefHeight(400);
		albums_list.setItems(model.getPlayer().getStats().getAllAlbums());
		albums_list.setCellFactory(new AlbumCellFactory());
		albums_list.getSelectionModel().selectedItemProperty().addListener((obs, current, selected) -> {
			if (selected != null) {
				model.getPlayer().playAlbum((Album) selected);
				Platform.runLater(() -> {
					tab_pane.getSelectionModel().select(0);
					albums_list.getSelectionModel().clearSelection();
				});
			}
		});

		genres_list.setPrefHeight(400);
		genres_list.setItems(model.getPlayer().getStats().getAllGenres());
		genres_list.getSelectionModel().selectedItemProperty().addListener((obs, current, selected) -> {
			if (selected != null) {
				model.getPlayer().playGenre((String) selected);
				Platform.runLater(() -> {
					tab_pane.getSelectionModel().select(0);
					genres_list.getSelectionModel().clearSelection();
				});
			}
		});

		playlists_list.setPrefHeight(400);
		playlists_list.setItems(model.getPlayer().getAllPlaylists());
		playlists_list.getSelectionModel().selectedItemProperty().addListener((obs, current, selected) -> {
			if (selected != null) {
				model.getPlayer().playPlaylist((Playlist) selected);
				Platform.runLater(() -> {
					tab_pane.getSelectionModel().select(0);
					playlists_list.getSelectionModel().clearSelection();
				});
			}
		});
	}
}
