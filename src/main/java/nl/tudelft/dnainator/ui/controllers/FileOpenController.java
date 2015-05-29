package nl.tudelft.dnainator.ui.controllers;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import nl.tudelft.dnainator.ui.services.GraphLoadService;
import nl.tudelft.dnainator.ui.services.NewickLoadService;
import nl.tudelft.dnainator.ui.views.PhylogeneticView;
import nl.tudelft.dnainator.ui.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;
import nl.tudelft.dnainator.ui.widgets.dialogs.ProgressDialog;

import java.io.File;

/**
 * Controls the file open pane on the left side of the application. It offers options
 * to open node, edge and newick files. FIXME: will be sliding in on command.
 */
public class FileOpenController {
	private static final String EDGE = ".edge.graph";
	private static final String NODE = ".node.graph";
	private static final String NEWICK = ".nwk";

	private static final int WIDTH = 550;
	private static final int ANIM_DURATION = 250;

	@FXML private GridPane fileOpenPane;
	@FXML private TextField nodeField;
	@FXML private TextField edgeField;
	@FXML private TextField newickField;
	@FXML private Label curNodeLabel;
	@FXML private Label curEdgeLabel;
	@FXML private Label curNewickLabel;
	@FXML private Button openButton;

	private GraphLoadService graphLoadService;
	private NewickLoadService newickLoadService;
	private FileChooser fileChooser;
	private ProgressDialog progressDialog;
	private ObjectProperty<PhylogeneticView> treeProperty;
	private SlidingAnimation animation;

	/*
	 * Sets up the services, filechooser and treeproperty.
	 */
	@FXML
	private void initialize() {
		fileChooser = new FileChooser();
		treeProperty = new SimpleObjectProperty<>(this, "tree");

		graphLoadService = new GraphLoadService();

		graphLoadService.setOnFailed(e ->
				new ExceptionDialog(fileOpenPane.getParent(), graphLoadService.getException(),
						"Error loading graph files!"));
		graphLoadService.setOnRunning(e -> progressDialog.show());
		graphLoadService.setOnSucceeded(e -> progressDialog.close());

		newickLoadService = new NewickLoadService();
		newickLoadService.setOnFailed(e ->
				new ExceptionDialog(fileOpenPane.getParent(), newickLoadService.getException(),
						"Error loading newick file!"));
		newickLoadService.setOnSucceeded(e -> treeProperty.setValue(
				new PhylogeneticView(newickLoadService.getValue())
		));

		animation = new SlidingAnimation(fileOpenPane, WIDTH, ANIM_DURATION);
				/*SlidingAnimation.Location.TOP_LEFT);*/

		bindOpenButtonDisabling();
	}

	/**
	 * Disables the openbutton when either no newick file or no node file is selected.
	 */
	private void bindOpenButtonDisabling() {
		Binding<Boolean> isFilesFilled = Bindings.and(newickField.textProperty().isEmpty(),
				nodeField.textProperty().isEmpty());
		openButton.disableProperty().bind(isFilesFilled);
	}

	/*
	 * If the node textfield is clicked, open the filechooser and if a file is selected, try
	 * to fill in the edge textfield as well.
	 */
	@FXML
	private void onNodeFieldClicked() {
		File nodeFile = selectFile("Node file", NODE);
		if (nodeFile != null) {
			graphLoadService.setNodeFile(nodeFile);
			nodeField.setText(graphLoadService.getNodeFile().getAbsolutePath());
			graphLoadService.setEdgeFile(openEdgeFile(nodeFile.getPath()));
			edgeField.setText(graphLoadService.getEdgeFile().getAbsolutePath());
		}
	}

	/*
	 * If the edge textfield is clicked, open the filechooser and if a file is selected, try
	 * to fill in the node textfield as well.
	 */
	@FXML
	private void onEdgeFieldClicked() {
		File edgeFile = selectFile("Edge file", EDGE);
		if (edgeFile != null) {
			graphLoadService.setEdgeFile(edgeFile);
			edgeField.setText(graphLoadService.getEdgeFile().getAbsolutePath());
			graphLoadService.setNodeFile(openNodeFile(edgeFile.getPath()));
			nodeField.setText(graphLoadService.getNodeFile().getAbsolutePath());
		}
	}

	/*
	 * If the newick textfield is clicked, open the filechooser and if a file is selected,
	 * fill in the newick textfield.
	 */
	@FXML
	private void onNewickFieldClicked() {
		File newickFile = selectFile("Newick file", NEWICK);
		if (newickFile != null) {
			newickLoadService.setNewickFile(newickFile);
			newickField.setText(newickLoadService.getNewickFile().getAbsolutePath());
		}
	}

	/*
	 * If the open button is clicked, open the files if selected and hide the pane. Clears the
	 * text fields and updates the current file labels if files are opened.
	 */
	@FXML
	private void onOpenAction() {
		progressDialog = new ProgressDialog(fileOpenPane.getParent());
		resetTextFields();
		animation.toggle();

		if (graphLoadService.getNodeFile() != null && graphLoadService.getEdgeFile() != null) {
			graphLoadService.restart();
			curNodeLabel.setText(graphLoadService.getNodeFile().getAbsolutePath());
			curEdgeLabel.setText(graphLoadService.getEdgeFile().getAbsolutePath());
		}

		if (newickLoadService.getNewickFile() != null) {
			newickLoadService.restart();
			curNewickLabel.setText(newickLoadService.getNewickFile().getAbsolutePath());
		}
	}

	/* Clears the files, textfields and hides the pane. */
	@FXML
	private void onCancelAction(ActionEvent actionEvent) {
		animation.toggle();
		graphLoadService.setNodeFile(null);
		graphLoadService.setEdgeFile(null);
		newickLoadService.setNewickFile(null);
		resetTextFields();
	}

	private void resetTextFields() {
		nodeField.clear();
		edgeField.clear();
		newickField.clear();
	}

	/**
	 * Sets up the {@link FileChooser} to use have the specified title and to use the
	 * given extension as a filter.
	 *
	 * @param title     The title of the {@link FileChooser}.
	 * @param extension The value to filter for the
	 *                  {@link javafx.stage.FileChooser.ExtensionFilter}.
	 * @return The selected file, or null if none is chosen.
	 */
	private File selectFile(String title, String extension) {
		fileChooser.setTitle(title);
		fileChooser.getExtensionFilters().setAll(
				new FileChooser.ExtensionFilter(title, "*" + extension));
		return fileChooser.showOpenDialog(fileOpenPane.getScene().getWindow());
	}

	/**
	 * @param path Creates an edge file from the path of a node file. This requires
	 *             the edge file to be in the same directory and to have the same name
	 *             as the node file.
	 * @return The edge file.
	 */
	private File openEdgeFile(String path) {
		return new File(path.substring(0, path.length() - NODE.length()).concat(EDGE));
	}

	/**
	 * @param path Creates a node file from the path of an edge file. This requires
	 *             the node file to be in the same directory and to have the same name
	 *             as the edge file.
	 * @return The edge file.
	 */
	private File openNodeFile(String path) {
		return new File(path.substring(0, path.length() - EDGE.length()).concat(NODE));
	}

	/**
	 * @return The {@link PhylogeneticView} property.
	 */
	public ObjectProperty<PhylogeneticView> treeProperty() {
		return treeProperty;
	}

	/**
	 * Toggles the pane, showing or hiding it with a sliding animation.
	 */
	public void toggle() {
		animation.toggle();
	}
}
