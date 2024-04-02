package controllers;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import media.Player;

/**
 * Value object for properties shared between the controllers
 */
public class Model {
	private Property<Player> player;
	private Property<MainController> mainController;

	public Model(Player player, MainController main) {
		this.player = new SimpleObjectProperty<Player>(player);
		this.mainController = new SimpleObjectProperty<MainController>(main);
	}

	public Player getPlayer() {
		return player.getValue();
	}

	public MainController getMainController() {
		return mainController.getValue();
	}
}
