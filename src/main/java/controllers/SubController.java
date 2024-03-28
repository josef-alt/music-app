package controllers;

import media.Player;

/**
 * Provide common fields to various controlers
 */
public class SubController {
	protected SepControl parent;
	protected static Player player;

	/**
	 * Link the shared Player instance
	 */
	public void setPlayer(Player p) {
		this.player = p;
	}

	/**
	 * Used to establish a link between nested controllers
	 */
	public void setParentController(SepControl parent) {
		this.parent = parent;
	}
}
