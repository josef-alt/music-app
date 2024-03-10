import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainPage extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fl = new FXMLLoader(getClass().getResource("/basic.fxml"));
		Scene s = new Scene(fl.load(), 300, 400);

		MainPageController cont = fl.getController();
		s.setOnKeyPressed(cont::update);

		stage.setResizable(false);
		stage.setScene(s);
		stage.show();
	}
}
