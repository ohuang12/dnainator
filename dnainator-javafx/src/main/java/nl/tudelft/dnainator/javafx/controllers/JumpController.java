package nl.tudelft.dnainator.javafx.controllers;

import javafx.scene.control.TextField;
import nl.tudelft.dnainator.javafx.widgets.animations.DownSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;


public class JumpController {
	private static final int HEIGHT = 250;
	private static final int ANIM_DURATION = 250;
	private SlidingAnimation animation;
	@SuppressWarnings("unused") @FXML private VBox jumpTo;
	@SuppressWarnings("unused") @FXML private TextField searchField;
	
	@SuppressWarnings("unused") @FXML 
	private void jumpNodeAction(ActionEvent a) {
		toggle();
	}
	
	@SuppressWarnings("unused") @FXML 
	private void onKeyPressed(KeyEvent e) {
		KeyCode key = e.getCode();
		
		if (key == KeyCode.ENTER) {
			String text = searchField.getText();
			System.out.println(text);
		}
	}


	@SuppressWarnings("unused") @FXML
	private void initialize() {
		animation = new DownSlideAnimation(jumpTo, HEIGHT, ANIM_DURATION, Position.TOP);
	}
	
	/**
	 * Toggles the pane, showing or hiding it with a sliding animation.
	 */
	public void toggle() {
		animation.toggle();
	}
}
