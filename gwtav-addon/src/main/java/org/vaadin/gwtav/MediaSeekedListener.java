package org.vaadin.gwtav;

import java.lang.reflect.Method;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.util.ReflectTools;

/**
 * Listener for {@link MediaSeekedEvent}
 * 
 * @see GwtVideo#addMediaSeekedListener(MediaSeekedListener)
 * @see GwtAudio#addMediaSeekedListener(MediaSeekedListener)
 * 
 * @author Tatu Lund
 */
public interface MediaSeekedListener extends ConnectorEventListener {
	Method MEDIA_SEEKED_METHOD = ReflectTools.findMethod(
			MediaSeekedListener.class, "mediaSeeked", MediaSeekedEvent.class);
	public void mediaSeeked(MediaSeekedEvent event);
}

