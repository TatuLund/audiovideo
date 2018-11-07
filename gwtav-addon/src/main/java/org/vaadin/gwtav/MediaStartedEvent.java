package org.vaadin.gwtav;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * The VideoEndedEvent is triggered when video is ended
 * 
 * @author Tatu Lund
 */
@SuppressWarnings("serial")
public class MediaStartedEvent extends CustomComponent.Event {

	public MediaStartedEvent(Component source) {
		super(source);
	}

}
