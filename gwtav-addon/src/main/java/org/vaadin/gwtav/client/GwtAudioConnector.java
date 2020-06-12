package org.vaadin.gwtav.client;

import org.vaadin.gwtav.GwtAudio;

import com.google.gwt.user.client.Timer;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.MediaBaseConnector;
import com.vaadin.shared.ui.Connect;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(GwtAudio.class)
public class GwtAudioConnector extends MediaBaseConnector {

	Timer timer = null;
	
    // ServerRpc is used to send events to server. Communication implementation
    // is automatically created here
    GwtAudioServerRpc rpc = RpcProxy.create(GwtAudioServerRpc.class, this);

    public GwtAudioConnector() {
        
        // To receive RPC events from server, we register ClientRpc implementation 
        registerRpc(GwtAudioClientRpc.class, new GwtAudioClientRpc() {
			@Override
			public void setPosition(double time) {
				getWidget().setPosition(time);				
			}

			@Override
			public void stop() {
				getWidget().setPosition(0.0d);
				getWidget().pause();
				getRpc().reportPosition(getWidget().getPosition());
			}

			@Override
			public void requestInitialData() {
	       		getRpc().initialData(getWidget().getDuration(),getWidget().getInitialPosition());
			}

			@Override
			public void setVolume(double volume) {
				getWidget().setVolume(volume);				
			}

			@Override
			public void setControlsList(String controlsList) {
				getWidget().setControlsList(controlsList);				
			}

			@Override
			public void pause() {
				getWidget().pause();
				getRpc().reportPosition(getWidget().getPosition());
			}

        });

		setTimer();	
		
        getWidget().setConnector(this);
    }

    // Set timer to report position to server side
	private void setTimer() {
		timer = new Timer() {
			@Override
			public void run() {
				if (!getWidget().isPaused()) getRpc().reportPosition(getWidget().getPosition());
			}
		};
		timer.scheduleRepeating(getState().reportingInterval);
	}

    @Override
    public void onUnregister() {
    	super.onUnregister();
    	if (timer != null) timer.cancel();
    }
    
    // We must implement getWidget() to cast to correct type 
    // (this will automatically create the correct widget type)
    @Override
    public GwtAudioWidget getWidget() {
        return (GwtAudioWidget) super.getWidget();
    }

    // We must implement getState() to cast to correct type
    @Override
    public GwtAudioState getState() {
        return (GwtAudioState) super.getState();
    }


    // Whenever the state changes in the server-side, this method is called
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("reportingInterval")) {
        	if (timer != null) timer.cancel();
        	setTimer();
        }
    }

	public void sendInitialData() {
   		getRpc().initialData(getWidget().getDuration(),getWidget().getInitialPosition());		
	}
	
	@Override
	protected String getDefaultAltHtml() {
        return "Your browser does not support the <code>audio</code> element.";
	}
	
	private GwtAudioServerRpc getRpc() {
		return getRpcProxy(GwtAudioServerRpc.class);
	}

	public void audioEnded() {
		getRpc().mediaEnded();	
	}

	public void audioPaused() {
		getRpc().mediaPaused();		
	}

	public void audioStarted() {
		getRpc().mediaStarted();		
	}

	public void audioSeeked() {
		getRpc().reportPosition(getWidget().getPosition());
		getRpc().mediaSeeked();
	}
}
