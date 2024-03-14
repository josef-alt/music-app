package controllers;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import javax.imageio.*;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.*;

import javafx.application.*;
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

	@FXML
	private MenuItem quit_button;

	private final Stage currentStage;
	private MediaPlayer player;
	private File currentFile;
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

	private ImageView play_icon, prev_icon, next_icon;
	private Image play, pause;
	@FXML
	public void initialize() {
		paused = true;

		play = new Image(getClass().getResourceAsStream("/img/play-50.png"));
		pause = new Image(getClass().getResourceAsStream("/img/pause-50.png"));
		prev_icon = new ImageView(new Image(getClass().getResourceAsStream("/img/prev-50.png")));
		play_icon = new ImageView(play);
		next_icon = new ImageView(new Image(getClass().getResourceAsStream("/img/next-50.png")));

		prev_button.setStyle("-fx-background-color: transparent;");
		prev_button.setGraphic(prev_icon);
		pause_button.setStyle("-fx-background-color: transparent;");
		pause_button.setGraphic(play_icon);
		next_button.setStyle("-fx-background-color: transparent;");
		next_button.setGraphic(next_icon);

		prev_button.setOnAction(event -> {
			handlePrevButton(event);
		});
		pause_button.setOnAction(event -> {
			handlePauseButton(event);
		});
		next_button.setOnAction(event -> {
			handleNextButton(event);
		});

		quit_button.setOnAction(event -> {
			quit();
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

		currentFile = new File(next);
		Media mediaElement = new Media(currentFile.toURI().toString());
		if (player != null)
			player.dispose();
		player = new MediaPlayer(mediaElement);

		player.play();
		if (paused)
			player.pause();

		setTrackInformation();
	}

	private void setTrackInformation() {
		try {
			AudioFile af = AudioFileIO.read(currentFile);
			Tag tag = af.getTag();

			if (tag.hasField(FieldKey.ALBUM)) {
				album_name.setText(tag.getFirst(FieldKey.ALBUM));
			}

			Image image;
			if (tag.hasField(FieldKey.COVER_ART)) {
				image = new Image(new ByteArrayInputStream(tag.getFirstArtwork().getBinaryData()), 250, 250, false,
						false);
			} else {
				image = new Image(getClass().getResourceAsStream("/img/cd.png"));
			}
			album_art.setImage(image);

			if (tag.hasField(FieldKey.ARTIST)) {
				artist_name.setText(tag.getFirst(FieldKey.ARTIST));
			}

			if (tag.hasField(FieldKey.TITLE)) {
				song_name.setText(tag.getFirst(FieldKey.TITLE));
			}

		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		if (paused) {
			play_icon.setImage(play);
		} else {
			play_icon.setImage(pause);
		}
	}

	@FXML
	public void handleNextButton(ActionEvent e) {
		System.out.println("next");
		update(1);
	}

	private void quit() {
		System.out.println("quitting");
		if (player != null) {
			if (player.getStatus() == Status.PLAYING) {
				player.stop();
			}
		}

		currentStage.close();
	}
}
