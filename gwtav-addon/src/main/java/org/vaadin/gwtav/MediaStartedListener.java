package org.vaadin.gwtav;

import java.lang.reflect.Method;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.util.ReflectTools;

public interface MediaStartedListener extends ConnectorEventListener {
	Method MEDIA_STARTED_METHOD = ReflectTools.findMethod(
			MediaStartedListener.class, "mediaStarted", MediaStartedEvent.class);
	public void mediaStarted(MediaStartedEvent event);
}

