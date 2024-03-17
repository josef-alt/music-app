package pages;

import controllers.MainPageController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainPage extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainPageController cont = new MainPageController();
		cont.showStage();
	}
}
