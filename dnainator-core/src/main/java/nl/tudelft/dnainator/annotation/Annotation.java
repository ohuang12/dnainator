package nl.tudelft.dnainator.annotation;

import java.util.Collection;

/**
 * An interface for gene annotations.
 */
public interface Annotation {

	/**
	 * @return the name of the gene
	 */
	String getGeneName();

	/**
	 * @return the coordinates of the gene.
	 */
	Range getRange();

	/**
	 * @return the start coordinate of the gene.
	 */
	default int getStart() {
		return getRange().getX();
	}

	/**
	 * @return the end coordinate of the gene.
	 */
	default int getEnd() {
		return getRange().getY();
	}

	/**
	 * @return whether this gene is a mutation
	 */
	boolean isMutation();

	/**
	 * @return whether this gene is sense or note (positive or negative)
	 */
	boolean isSense();

	/**
	 * @return the nodes that are annotated by this annotation.
	 */
	Collection<String> getAnnotatedNodes();
}
