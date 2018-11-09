package org.vaadin.gwtav;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * The VideoEndedEvent is triggered when video is ended
 * 
 * @author Tatu Lund
 */
@SuppressWarnings("serial")
public class MediaSeekedEvent extends CustomComponent.Event {

	public MediaSeekedEvent(Component source) {
		super(source);
	}

}
