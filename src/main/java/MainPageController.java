import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.stream.*;

import javax.imageio.*;

import javafx.fxml.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

public class MainPageController {
	@FXML
	private ImageView album_art;

	// this is just to test changing the image
	private static int x = 0;
	private static String[] samples = Stream.of(new File("E:/Media/Pictures/Staff Photos").listFiles())
			.filter(f -> !f.isDirectory()).map(File::toString).toArray(String[]::new);

	// cylce through sample images on key press
	public void setImage(KeyEvent ev) {
		album_art.setImage(new Image("file:" + samples[x++ % samples.length]));
	}
}
