package org.vaadin.gwtav.client;

import com.google.gwt.media.dom.client.MediaError;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

// ServerRpc is used to pass events from client to server
public interface GwtAudioServerRpc extends ServerRpc {

    // Example API: Widget click is clicked
    public void notSupported();
    
    public void initialData(double duration, double initialPosition);
    
    public void reportPosition(double time);
    
    public void mediaEnded();

	public void mediaStarted();

	public void mediaPaused();

    public void mediaSeeked();
}
