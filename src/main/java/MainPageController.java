import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.stream.*;

import javax.imageio.*;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

public class MainPageController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name, artist_name, album_name;

	// this is just to test changing the image
	private static int x = -1;
	private static String[] samples = Stream.of(new File("E:/Media/Pictures/Staff Photos").listFiles())
			.filter(f -> !f.isDirectory()).map(File::toString).toArray(String[]::new);

	// cylce through sample images on key press
	public void update(int delta) {
		x += delta;
		if (x < 0) {
			x += samples.length;
		}

		String next = samples[x % samples.length];
		album_art.setImage(new Image("file:" + next));

		// not really important for now, just proof of concept for my own sanity
		song_name.setText(next.substring(next.lastIndexOf('\\') + 1));
	}

	@FXML
	public void handlePrevButton(ActionEvent e) {
		System.out.println("prev");
		update(-1);
	}

	@FXML
	public void handlePauseButton(ActionEvent e) {
		System.out.println("pause/play");

	}

	@FXML
	public void handleNextButton(ActionEvent e) {
		System.out.println("next");
		update(1);
	}

}
