package org.vaadin.gwtav.client;

import com.vaadin.shared.communication.ClientRpc;

// ClientRpc is used to pass events from server to client
// For sending information about the changes to component state, use State instead
public interface GwtAudioClientRpc extends ClientRpc {

    public void setPosition(double time);
    
    public void stop();

    public void pause();
    
    public void requestInitialData();
	
    public void setVolume(double volume);

	public void setControlsList(String controlsList);

}