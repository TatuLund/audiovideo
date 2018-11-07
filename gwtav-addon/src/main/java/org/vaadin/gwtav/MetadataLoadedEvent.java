package org.vaadin.gwtav;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * The MetadataLoadedEvent is triggered when video metadata is available
 * 
 * @author Tatu Lund
 */
@SuppressWarnings("serial")
public class MetadataLoadedEvent extends CustomComponent.Event {

	public MetadataLoadedEvent(Component source) {
		super(source);
	}

}
