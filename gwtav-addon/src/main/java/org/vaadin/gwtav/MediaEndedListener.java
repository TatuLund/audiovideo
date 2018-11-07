package org.vaadin.gwtav;

import java.lang.reflect.Method;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.util.ReflectTools;

public interface MediaEndedListener extends ConnectorEventListener {
	Method MEDIA_ENDED_METHOD = ReflectTools.findMethod(
			MediaEndedListener.class, "mediaEnded", MediaEndedEvent.class);
	public void mediaEnded(MediaEndedEvent event);
}

