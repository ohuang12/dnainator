package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.graph.impl.Neo4jBatchBuilder;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A JavaFX background service to load files into graphs.
 * <p>
 * Please note that none of the properties are initialized (beyond being
 * set to <code>null</code>) upon instantiation.
 * </p>
 */
public class GraphLoadService extends Service<Graph> {
	private static final String DB_PATH = "target\\db\\db";
	private ObjectProperty<File> nodeFile = new SimpleObjectProperty<>(this, "nodeFile");
	private ObjectProperty<File> edgeFile = new SimpleObjectProperty<>(this, "edgeFile");
	private ObjectProperty<String> database = new SimpleObjectProperty<>(this, "database");

	/**
	 * Construct a GraphLoadService with a default database path.
	 */
	public GraphLoadService() {
		setDatabase(DB_PATH);
	}

	/**
	 * Checks what paths are already in use and constructs an unique path,
	 * that does not collide with existing ones.
	 * @return unique path for the database.
	 */
	public String getNewPath(List<String> paths) {
		int i = 0;
		String newPath = DB_PATH;
		while (paths.contains(newPath)) {
			newPath = DB_PATH + (i++);
		}
		paths.add(newPath);
		return newPath;
	}
	
	/**
	 * @param f The node file to load.
	 */
	public final void setNodeFile(File f) {
		nodeFile.set(f);
	}

	/**
	 * @return The node file to load, if any.
	 */
	public final File getNodeFile() {
		return nodeFile.get();
	}

	/**
	 * @return The node file property.
	 */
	public ObjectProperty<File> nodeFileProperty() {
		return nodeFile;
	}

	/**
	 * @param f The edge file to load.
	 */
	public final void setEdgeFile(File f) {
		edgeFile.set(f);
	}

	/**
	 * @return The edge file to load, if any.
	 */
	public final File getEdgeFile() {
		return edgeFile.get();
	}

	/**
	 * @return The edge file property.
	 */
	public ObjectProperty<File> edgeFileProperty() {
		return edgeFile;
	}

	/**
	 * @param g	The database to use.
	 */
	public final void setDatabase(String g) {
		database.set(g);
	}

	/**
	 * @return The database to use, if any.
	 */
	public final String getDatabase() {
		return database.get();
	}

	/**
	 * @return The database property.
	 */
	public ObjectProperty<String> databaseProperty() {
		return database;
	}

	@Override
	protected Task<Graph> createTask() {
		return new Task<Graph>() {
			@Override
			protected Graph call() throws IOException, ParseException {
				GraphBuilder gb;
				gb = new Neo4jBatchBuilder(database.get());

				EdgeParser ep = new EdgeParserImpl(getEdgeFile());
				NodeParser np = new NodeParserImpl(getNodeFile());
				gb.constructGraph(np, ep);

				ep.close();
				np.close();

				return new Neo4jGraph(database.get());
			}
		};
	}
}
