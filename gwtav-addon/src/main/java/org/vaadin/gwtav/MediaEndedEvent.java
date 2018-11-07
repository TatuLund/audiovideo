package org.vaadin.gwtav;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * The VideoEndedEvent is triggered when video is ended
 * 
 * @author Tatu Lund
 */
@SuppressWarnings("serial")
public class MediaEndedEvent extends CustomComponent.Event {

	public MediaEndedEvent(Component source) {
		super(source);
	}

}
