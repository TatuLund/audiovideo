package org.vaadin.gwtav;

import org.vaadin.gwtav.client.GwtAudioClientRpc;
import org.vaadin.gwtav.client.GwtAudioServerRpc;
import org.vaadin.gwtav.client.GwtAudioState;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.PreloadMode;

/**
 * GwtAudio is an improved version of Audio component with some additional API and Events
 *  
 * @author Tatu Lund
 */
@SuppressWarnings("serial")
public class GwtAudio extends AbstractAudioVideo {

	private double position = 0.0d;
	private double audioLength;
	private double initialPosition;
	private double volume = -1.0d;

	/**
	 * Default constructor
	 */
	public GwtAudio() {
		this(null,"");
	}

	
	/**
	 * Create a new GwtAudio using given resource
	 * 
	 * @param resource A resource
	 */
	public GwtAudio(Resource resource) {
		this(resource,"");
	}

	/**
	 * Create a new GwtAudio using given caption
	 * 
	 * @param caption A caption as a String
	 */
	public GwtAudio(String caption) {
		this(null,caption);
	}	
	
	/**
	 * Create a new GwtAudio using given resource and caption
	 * 
	 * @param resource A resource
	 * @param caption A caption as a String
	 */
    public GwtAudio(Resource resource, String caption) {

    	GwtAudio component = this;
    	
        // To receive events from the client, we register ServerRpc
        registerRpc(new GwtAudioServerRpc() {

			@Override
			public void notSupported() {
				throw new IllegalStateException("GWT Audio is not supported in this browser");				
			}

			@Override
			public void reportPosition(double time) {
				position = time;
				
			}

			@Override
			public void mediaEnded() {
				fireEvent(new MediaEndedEvent(component));
			}

			@Override
			public void mediaPaused() {
				fireEvent(new MediaPausedEvent(component));
				
			}

			@Override
			public void mediaStarted() {
				fireEvent(new MediaStartedEvent(component));				
			}
        	
			@Override
			public void initialData(double duration, double initialPos) {
				audioLength = duration;
				initialPosition = initialPos;
				fireEvent(new MetadataLoadedEvent(component));
			}

			@Override
			public void mediaSeeked() {
				fireEvent(new MediaSeekedEvent(component));				
			}
        	
        });
        
        getState().autoplay = true;
        getState().preload = PreloadMode.METADATA;
        getState().loop = false;
        getState().showControls = true;
        getState().caption = caption;

        setSource(resource);

    }

    /**
     * Get the duration of the audio. 
     * 
     * Note: This gives real value only after {@link org.vaadin.gwtav.MetadataLoadedEvent} has
     * been received.
     * 
     * @see org.vaadin.gwtav.MetadataLoadedEvent
     * @see org.vaadin.gwtav.MetadataLoadedListener
     * @see GwtAudio#addMetadataLoadedListener(MetadataLoadedListener)
     * 
     * @return Length of the audio in seconds 
     */
    public double getAudioLength() {
    	return audioLength;
    }

    /**
     * Get starting offset of the audio in seconds 
     * 
     * Note: This gives real value only after {@link org.vaadin.gwtav.MetadataLoadedEvent} has
     * been received.
     * 
     * @see org.vaadin.gwtav.MetadataLoadedEvent
     * @see org.vaadin.gwtav.MetadataLoadedListener
     * @see GwtAudio#addMetadataLoadedListener(MetadataLoadedListener)
     * 
     * @return Starting offset of the audio in seconds 
     */
    public double getInitialPosition() {
    	return initialPosition;
    }
    
    /**
     * Set the new position
     * 
     * @param time Position offset in seconds
     */
    public void setPosition(double time) {
    	getRpc().setPosition(time);
    }
    
    /**
     * Set audio volume of the media, 0 = muted, 1 = loudest
     * 
     * @param volume The volume
     */
    public void setVolume(double volume) {
    	getRpc().setVolume(volume);
    	this.volume = volume;
    }
    
    /**
     * Get currently set volume, or -1 if the default value
     * 
     * @return The currently set volume
     */
    public double getVolume() {
    	return volume;
    }
    
    
    /**
     * Get the current playback position. The position is submitted to the server
     * as set by {@link GwtAudio#setReportingInterval(int)}
     * 
     * @see GwtAudio#setReportingInterval(int) 
     * 
     * @return The current playback position
     */
    public double getPosition() {
    	return position;
    }

    /**
     * Stop the playback of the media
     */    
    public void stop() {
    	getRpc().stop();
    }
    
    /**
     * Pause the playback. The position is reported back to server.
     * 
     * @see GwtAudio#getPosition()
     */
    public void pause() {
    	getRpc().pause();
    }
    
    
    /**
     * Set reporting interval how often client send playback position of the media
     * to the server. Default 250ms.
     * 
     * @param millis Reporting interval in milliseconds
     */
    public void setReportingInterval(int millis) {
    	getState().reportingInterval = millis;
    }
    
    // We must override getState() to cast the state to GwtVideoState
    @Override
    protected GwtAudioState getState() {
        return (GwtAudioState) super.getState();
    }
    
    private GwtAudioClientRpc getRpc() {
    	return getRpcProxy(GwtAudioClientRpc.class);
    }

}

