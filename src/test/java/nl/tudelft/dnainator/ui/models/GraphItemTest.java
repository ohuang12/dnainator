package nl.tudelft.dnainator.ui.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.graph.Graph;

import nl.tudelft.dnainator.ui.ColorServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test the GraphItem of the model.
 * Also test its abstract base classes.
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphItemTest {
	@Mock private Graph graph;
	@Mock private Node node;
	@Mock private Group child, content;
	private GraphItem gi;

	/**
	 * Initialize the graphitem.
	 */
	@Before
	public void setup() {
		child = new Group();
		content = new Group();
		gi = new GraphItem(new ColorServer(), graph, content, child);
	}

	/**
	 * Test construction of a graphitem.
	 * Should not query the model for anything yet.
	 */
	@Test
	public void testConstruction() {
		verify(graph, never()).getRank(anyInt());
		verify(graph, never()).getRanks();
	}

	/**
	 * Test whether this item is in a specified viewport.
	 * The drawable we're testing currently extends to x = 40000, y = 20.
	 */
	@Test
	public void testInViewPort() {
		// CHECKSTYLE.OFF: MagicNumber
		Bounds viewport = new Rectangle(640, 480).getBoundsInLocal();
		assertEquals(true, gi.isInViewport(viewport));

		content.setTranslateX(0);
		content.setTranslateY(-1);
		assertEquals(false, gi.isInViewport(viewport));

		content.setTranslateX(0);
		content.setTranslateY(480);
		assertEquals(true, gi.isInViewport(viewport));

		content.setTranslateX(0);
		content.setTranslateY(481);
		assertEquals(false, gi.isInViewport(viewport));

		content.setTranslateX(640);
		content.setTranslateY(0);
		assertEquals(true, gi.isInViewport(viewport));

		content.setTranslateX(641);
		content.setTranslateY(0);
		assertEquals(false, gi.isInViewport(viewport));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Toggle visibility of this item.
	 */
	@Test
	public void testToggle() {
		// CHECKSTYLE.OFF: MagicNumber
		// TODO
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test whether this item is updated correctly, given a specified viewport.
	 * The drawable we're testing currently extends to x = 40000, y = 20.
	 */
	@Test
	public void testUpdate() {
		// CHECKSTYLE.OFF: MagicNumber
		gi.update(new Rectangle(20000, 10000).getBoundsInLocal());
		assertTrue(content.isVisible());
		verify(graph, never()).getRank(anyInt());

		gi.update(new Rectangle(5000, 2500).getBoundsInLocal());
		assertFalse(content.isVisible());
		verify(graph, Mockito.atLeastOnce()).getRank(anyInt());

		gi.update(new Rectangle(1000, 500).getBoundsInLocal());
		assertFalse(content.isVisible());
		verify(graph, Mockito.atLeastOnce()).getRank(anyInt());

		gi.update(new Rectangle(20000, 10000).getBoundsInLocal());
		assertTrue(content.isVisible());
		verify(graph, Mockito.atLeastOnce()).getRank(anyInt());
		// CHECKSTYLE.ON: MagicNumber
	}
}
