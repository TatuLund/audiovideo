package org.vaadin.gwtav.client;

import org.vaadin.gwtav.GwtVideo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.MediaBaseConnector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.AbstractEmbeddedState;
import com.vaadin.shared.ui.Connect;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(GwtVideo.class)
public class GwtVideoConnector extends MediaBaseConnector {

	Timer timer = null;
	
    // ServerRpc is used to send events to server. Communication implementation
    // is automatically created here
    GwtVideoServerRpc rpc = RpcProxy.create(GwtVideoServerRpc.class, this);

    public GwtVideoConnector() {
        
        // To receive RPC events from server, we register ClientRpc implementation 
        registerRpc(GwtVideoClientRpc.class, new GwtVideoClientRpc() {
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
	       		getRpc().initialData(getWidget().getDuration(),getWidget().getInitialPosition(),getWidget().getVideoWidth(),
	       				getWidget().getVideoHeight());
			}

			@Override
			public void setVolume(double volume) {
				getWidget().setVolume(volume);				
			}

			@Override
			public void pause() {
				getWidget().pause();
				getRpc().reportPosition(getWidget().getPosition());
			}

        });

		setTimer();	
		
		// This is not working for some reason
        getWidget().addEndedHandler(event -> {
        	getRpc().mediaEnded();
        });
        
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
    public GwtVideoWidget getWidget() {
        return (GwtVideoWidget) super.getWidget();
    }

    // We must implement getState() to cast to correct type
    @Override
    public GwtVideoState getState() {
        return (GwtVideoState) super.getState();
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
   		getRpc().initialData(getWidget().getDuration(),getWidget().getInitialPosition(),getWidget().getVideoWidth(),
   				getWidget().getVideoHeight());		
	}
	
	@Override
	protected String getDefaultAltHtml() {
        return "Your browser does not support the <code>video</code> element.";
	}
	
	private GwtVideoServerRpc getRpc() {
		return getRpcProxy(GwtVideoServerRpc.class);
	}

	public void videoEnded() {
		getRpc().mediaEnded();	
	}

	public void videoPaused() {
		getRpc().mediaPaused();		
	}

	public void videoStarted() {
		getRpc().mediaStarted();		
	}
}
