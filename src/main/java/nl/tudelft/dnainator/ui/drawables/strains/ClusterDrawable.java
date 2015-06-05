package nl.tudelft.dnainator.ui.drawables.strains;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.MapChangeListener;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.ColorServer;
import nl.tudelft.dnainator.ui.drawables.Drawable;

/**
 * The {@link ClusterDrawable} class represents the mid level object in the viewable model.
 */
public class ClusterDrawable extends Group implements Drawable {
	private static final int SINGLE = 1;
	private static final int SMALL = 3;
	private static final int MEDIUM = 10;
	private static final double SINGLE_RADIUS = 2;
	private static final double SMALL_RADIUS = 4;
	private static final double MEDIUM_RADIUS = 5;
	private static final double LARGE_RADIUS = 6;
	private static final int PIETHRESHOLD = 10;
	private List<SequenceNode> clustered;
	private Set<String> sources;
	private Text label;
	private Pie pie;

	/**
	 * Construct a new mid level {@link ClusterDrawable} using the default graph.
	 * @param colorServer the {@link ColorServer} to bind to.
	 * @param clustered	the clustered {@link SequenceNode}s in this cluster.
	 */
	public ClusterDrawable(ColorServer colorServer, List<SequenceNode> clustered) {
		this.clustered = clustered;
		this.sources = clustered.stream()
				.flatMap(e -> Arrays.asList(e.getSource().split(",")).stream())
				.collect(Collectors.toSet());

		label = new Text(Integer.toString(clustered.size()));
		label.setStyle("-fx-font-size: 2pt");

		draw(colorServer);
	}

	private void draw(ColorServer colorServer) {
		double radius = getRadius();

		if (sources.size() > PIETHRESHOLD) {
			getChildren().add(new Circle(radius));
		} else {
			colorServer.addListener(this::onColorServerChanged);

			List<String> collect = sources.stream()
					.map(e -> colorServer.getColor(e))
					.filter(e -> e != null)
					.collect(Collectors.toList());
			pie = new Pie(radius, collect);
			getChildren().add(pie);
		}
		getChildren().add(label);
	}

	private double getRadius() {
		int nChildren = clustered.size();

		if (nChildren == SINGLE) {
			return SINGLE_RADIUS;
		} else if (nChildren <= SMALL) {
			return SMALL_RADIUS;
		} else if (nChildren <= MEDIUM) {
			return MEDIUM_RADIUS;
		} else {
			return LARGE_RADIUS;
		}
	}

	private void onColorServerChanged(
			MapChangeListener.Change<? extends String, ? extends String> change) {
		if (!sources.contains(change.getKey())) {
			return;
		} else if (change.wasAdded()) {
			addStyle(change.getValueAdded());
		} else if (change.wasRemoved()) {
			removeStyle(change.getValueRemoved());
		}
	}

	@Override
	public void addStyle(String style) {
		pie.getStyles().add(style);
	}

	@Override
	public void removeStyle(String style) {
		pie.getStyles().remove(style);
	}

	/**
	 * @return the {@link SequenceNode}s in this cluster.
	 */
	public List<SequenceNode> getClustered() {
		return clustered;
	}
}
