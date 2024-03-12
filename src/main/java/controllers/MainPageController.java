package controllers;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import javax.imageio.*;

import javafx.beans.binding.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.*;
import javafx.stage.*;

public class MainPageController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name, artist_name, album_name;

	@FXML
	private Button prev_button, pause_button, next_button;

	private final Stage currentStage;
	private MediaPlayer player;
	private Media mediaElement;
	private boolean paused;

	public MainPageController() {
		currentStage = new Stage();
		currentStage.setResizable(false);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/basic.fxml"));
			loader.setController(this);
			currentStage.setScene(new Scene(loader.load()));

			System.out.println("finished constructor");
		} catch (IOException e) {
			System.out.println("error loading");
			e.printStackTrace();
		}
	}

	public void showStage() {
		currentStage.showAndWait();
	}

	@FXML
	public void initialize() {
		paused = true;
		prev_button.setOnAction(event -> {
			handlePrevButton(event);
		});
		pause_button.setOnAction(event -> {
			handlePauseButton(event);
		});
		next_button.setOnAction(event -> {
			handleNextButton(event);
		});
	}

	// this is just to test changing the image
	private static int x = -1;
	private static String[] samples = Stream.of(new File("E:/Media/Music/with art/").listFiles())
			.filter(f -> !f.isDirectory()).map(File::toString).toArray(String[]::new);

	// cylce through sample images on key press
	public void update(int delta) {
		x += delta;
		if (x < 0) {
			x += samples.length;
		}

		String next = samples[x % samples.length];
		// not really important for now, just proof of concept for my own sanity
		song_name.setText(next.substring(next.lastIndexOf('\\') + 1));

		mediaElement = new Media(new File(next).toURI().toString());
		if (player != null)
			player.dispose();
		player = new MediaPlayer(mediaElement);

		player.play();
		if (paused)
			player.pause();

		setAlbumArt();
	}

	private void setAlbumArt() {
		Image image = new Image(getClass().getResourceAsStream("/img/cd.png"));
		album_art.setImage(image);
	}

	@FXML
	public void handlePrevButton(ActionEvent e) {
		System.out.println("prev");
		update(-1);
	}

	@FXML
	public void handlePauseButton(ActionEvent e) {
		System.out.println("pause/play");

		if (player == null) {
			System.out.println("no player");
		} else {
			if (player.getStatus() == Status.READY) {
				System.out.println("begin song");
				player.play();
				paused = false;
			} else if (player.getStatus() == Status.PAUSED) {
				System.out.println("hit play");
				player.play();
				paused = false;
			} else {
				System.out.println("hit pause");
				player.pause();
				paused = true;
			}
		}
	}

	@FXML
	public void handleNextButton(ActionEvent e) {
		System.out.println("next");
		update(1);
	}
}
