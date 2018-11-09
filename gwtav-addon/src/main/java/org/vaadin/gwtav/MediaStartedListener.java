package org.vaadin.gwtav;

import java.lang.reflect.Method;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.util.ReflectTools;

/**
 * Listener for {@link MediaStartedEvent}
 * 
 * @see GwtVideo#addMediaStartedListener(MediaStartedListener)
 * @see GwtAudio#addMediaStartedListener(MediaStartedListener)
 * 
 * @author Tatu Lund
 */
public interface MediaStartedListener extends ConnectorEventListener {
	Method MEDIA_STARTED_METHOD = ReflectTools.findMethod(
			MediaStartedListener.class, "mediaStarted", MediaStartedEvent.class);
	public void mediaStarted(MediaStartedEvent event);
}

