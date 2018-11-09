package org.vaadin.gwtav;

import java.lang.reflect.Method;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.util.ReflectTools;

/**
 * Listener for {@link MetadataLoadedEvent}
 * 
 * @see GwtVideo#addMetadataLoadedListener(MetadataLoadedListener)
 * @see GwtAudio#addMetadataLoadedListener(MetadataLoadedListener)
 * 
 * @author Tatu Lund
 */
public interface MetadataLoadedListener extends ConnectorEventListener {
	Method METADATA_LOADED_METHOD = ReflectTools.findMethod(
			MetadataLoadedListener.class, "metadataLoaded", MetadataLoadedEvent.class);
	public void metadataLoaded(MetadataLoadedEvent event);
}

