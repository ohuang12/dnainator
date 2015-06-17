package nl.tudelft.dnainator.javafx.widgets;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.tudelft.dnainator.javafx.views.StrainView;

/**
 * A widget that shows several {@link Control}s to interact with the {@link StrainView}.
 */
public class StrainControl extends VBox {
	private static final double PADDING = 10;
	private static final double WIDTH = 200;
	private static final String NODE = "Jump to node...";
	private static final String RANK = "Jump to rank...";
	private static final String ANNOTATION = "Jump to annotation...";
	private static final String ERROR = "Invalid input!";
	private static final String INVALIDPROMPT = "jump-invalid-prompt";
	private StrainView strainView;
	private TextField jumpTo;
	private String previousInput;
	private Collection<String> attachedAnnotations;

	/**
	 * Instantiates a new {@link StrainControl}.
	 * @param strainView The {@link StrainView} to interact with.
	 */
	public StrainControl(StrainView strainView) {
		this.strainView = strainView;
		setPadding(new Insets(PADDING));
		setSpacing(PADDING);
		setMaxWidth(WIDTH);
		setPickOnBounds(false);
		jumpTo = new TextField();

		// Wait for strain view to be created.
		Platform.runLater(this::createJumpToNode);
		getChildren().add(jumpTo);
	}

	private void createJumpToNode() {
		setupTextField(NODE);
		jumpTo.setOnAction(e -> jump(NODE, bool -> bool, strainView::gotoNode));
	}
	
	private void createJumpToRank() {
		setupTextField(RANK);
		jumpTo.setOnAction(e -> jump(RANK, bool -> bool, strainView::gotoRank));
	}

	private void createJumpToAnnotation() {
		setupTextField(ANNOTATION);
		jumpTo.setOnAction(e -> jump(ANNOTATION, bool -> !bool, this::gotoAnnotation));
	}

	private void jump(String reset, Predicate<Boolean> predicate, Consumer<String> function) {
		String inputText = jumpTo.getCharacters().toString();
		if (predicate.test(isPositiveInteger(inputText))) {
			resetPromptText(reset);
			function.accept(inputText);
		} else {
			promptInvalid();
		}
	}

	private void gotoAnnotation(String inputText) {
		if (previousInput != null && previousInput.equals(inputText)
				&& attachedAnnotations != null && !attachedAnnotations.isEmpty()) {
			gotoNextAnnotationNode();
			return;
		}
		previousInput = inputText;
		attachedAnnotations = strainView.getAnnotatedNodeIDs(inputText);
		gotoNextAnnotationNode();
	}

	private void gotoNextAnnotationNode() {
		String next = attachedAnnotations.iterator().next();
		attachedAnnotations.remove(next);
		strainView.gotoRank(next);
	}
	
	private void setupTextField(String name) {
		if (jumpTo.getPromptText().equals(name) && jumpTo.isVisible()) {
			jumpTo.setVisible(!jumpTo.isVisible());
		} else {
			jumpTo.clear();
			jumpTo.setPrefColumnCount(name.length());
			resetPromptText(name);
			jumpTo.setVisible(true);
			requestFocus();
		}
	}
	
	private void resetPromptText(String name) {
		jumpTo.getStyleClass().remove(INVALIDPROMPT);
		jumpTo.setPromptText(name);
	}

	private void promptInvalid() {
		jumpTo.clear();
		jumpTo.setPromptText(ERROR);
		jumpTo.getStyleClass().add(INVALIDPROMPT);
	}
	
	private boolean isPositiveInteger(String s) {
		try {
			return Integer.parseInt(s) >= 0;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	/**
	 * Toggle the jump to node text field.
	 */
	public void toggleJumpNode() {
		createJumpToNode();
	}
	
	/**
	 * Toggle the jump to rank text field.
	 */
	public void toggleJumpRank() {
		createJumpToRank();
	}
	
	/**
	 * Toggle the jump to rank text field.
	 */
	public void toggleJumpAnnotation() {
		createJumpToAnnotation();
	}
}