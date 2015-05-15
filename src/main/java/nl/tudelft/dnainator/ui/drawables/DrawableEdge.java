package nl.tudelft.dnainator.ui.drawables;

import javafx.scene.shape.Line;
import nl.tudelft.dnainator.ui.widgets.EdgeContext;

/**
 * The drawable edge is the JavaFX counterpart of
 * {@link nl.tudelft.dnainator.core.Edge}.
 */
public class DrawableEdge extends Line {

	/**
	 * Instantiate a new DrawableEdge.
	 * @param src This edge's source node.
	 * @param dest This edge's destination node.
	 */
	public DrawableEdge(DrawableNode src, DrawableNode dest) {
		super(src.getCenterX(), src.getCenterY(), dest.getCenterX(), dest.getCenterY());

		getStyleClass().add("drawable-edge");
		setOnContextMenuRequested(e -> {
			EdgeContext.getInstance().show(DrawableEdge.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});
	}

}
