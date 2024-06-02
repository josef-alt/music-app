package pages;

import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import util.ResourceManager;

/**
 * Handles the about page.
 * Should inherit the stylings from the main stage
 * Should automatically close when losing focus
 */
public class AboutPage {
	public AboutPage(Stage parent) {
		Stage popupStage = new Stage();
		popupStage.setResizable(false);
		popupStage.initOwner(parent);
		popupStage.getIcons().add(ResourceManager.getImage("/img/large/play.png"));

		FXMLLoader load = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
		try {
			Scene sc = new Scene(load.load());
			sc.getStylesheets().addAll(parent.getScene().getStylesheets());
			popupStage.setScene(sc);
			popupStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (!newValue)
						popupStage.close();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		popupStage.show();
	}
}
