import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.stream.*;

import javax.imageio.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

public class MainPageController {
	@FXML
	private ImageView album_art;

	@FXML
	private Label song_name;

	@FXML
	private Label artist_name;

	@FXML
	private Label album_name;

	// this is just to test changing the image
	private static int x = 0;
	private static String[] samples = Stream.of(new File("E:/Media/Pictures/Staff Photos").listFiles())
			.filter(f -> !f.isDirectory()).map(File::toString).toArray(String[]::new);

	// cylce through sample images on key press
	public void update(KeyEvent ev) {
		String next = samples[x++ % samples.length];
		album_art.setImage(new Image("file:" + next));
		
		// not really important for now, just proof of concept for my own sanity
		song_name.setText(next.substring(next.lastIndexOf('\\') + 1));
	}
}
