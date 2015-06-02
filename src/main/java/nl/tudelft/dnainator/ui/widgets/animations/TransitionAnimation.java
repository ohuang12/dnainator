package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.animation.Transition;
import javafx.scene.layout.Pane;

/**
 * A {@link CustomAnimation} which animates a transition.
 * It applies the animation onto a {@link Pane}.
 * The animation is carried out in {@link Direction}.
 */
public abstract class TransitionAnimation extends Transition implements CustomAnimation {
	
	protected Pane pane;
	protected double size;
	protected double duration;

	/**
	 * Construct a new {@link TransitionAnimation}.
	 * @param pane         The {@link Pane} which is animated.
	 * @param size        The length over which the pane will slide.
	 * @param duration     The duration of the animations.
	 */
	public TransitionAnimation(Pane pane, double size, double duration) {
		this.pane = pane;
		this.size = size;
		this.duration = duration;
		
		setupAnimation();
	}
	
	/**
	 * Plays the {@link TransitionAnimation}.
	 */
	@Override
	public void playAnimation() {
		this.play();
	}
	
	/**
	 * Stops the {@link TransitionAnimation}.
	 */
	@Override
	public void stopAnimation() {
		this.stop();
	}
}
