package org.vaadin.gwtav;

import java.lang.reflect.Method;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.util.ReflectTools;

/**
 * Listener for {@link MediaPausedEvent}
 * 
 * @see GwtVideo#addMediaPausedListener(MediaPausedListener)
 * @see GwtAudio#addMediaPausedListener(MediaPausedListener)
 * 
 * @author Tatu Lund
 */
public interface MediaPausedListener extends ConnectorEventListener {
	Method MEDIA_PAUSED_METHOD = ReflectTools.findMethod(
			MediaPausedListener.class, "mediaPaused", MediaPausedEvent.class);
	public void mediaPaused(MediaPausedEvent event);
}

