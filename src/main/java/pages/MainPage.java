package pages;

import javafx.application.Application;
import javafx.stage.Stage;

import controllers.MainController;

public class MainPage extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainController cont = new MainController();
		cont.showStage();
	}
}
