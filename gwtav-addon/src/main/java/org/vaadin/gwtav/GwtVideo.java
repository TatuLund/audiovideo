package org.vaadin.gwtav;

import org.vaadin.gwtav.client.GwtVideoClientRpc;
import org.vaadin.gwtav.client.GwtVideoServerRpc;
import org.vaadin.gwtav.client.GwtVideoState;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.PreloadMode;
import com.vaadin.ui.AbstractMedia;

/**
 * GwtVideo is an improved version of Video component with some additional API and Events
 *  
 * @author Tatu Lund
 */
@SuppressWarnings("serial")
public class GwtVideo extends AbstractMedia {

	private double position = 0.0d;
	private double videoLength;
	private double initialPosition;
	private int videoWidth;
	private int videoHeight;
	private double volume = -1.0d;
	
	/**
	 * Default constructor
	 */
	public GwtVideo() {
		this(null,"");
	}

	
	/**
	 * Create a new GwtVideo using given resource
	 * 
	 * @param resource A resource
	 */
	public GwtVideo(Resource resource) {
		this(resource,"");
	}

	/**
	 * Create a new GwtVideo using given caption
	 * 
	 * @param caption A caption as a String
	 */
	public GwtVideo(String caption) {
		this(null,caption);
	}	
	
	/**
	 * Create a new GwtVideo using given resource and caption
	 * 
	 * @param resource A resource
	 * @param caption A caption as a String
	 */
    public GwtVideo(Resource resource, String caption) {

    	GwtVideo component = this;
    	
        // To receive events from the client, we register ServerRpc
        registerRpc(new GwtVideoServerRpc() {

			@Override
			public void notSupported() {
				throw new IllegalStateException("GWT Video is not supported in this browser");				
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
			public void initialData(double duration, double initialPos, int width, int height) {
				videoLength = duration;
				initialPosition = initialPos;
				videoWidth = width;
				videoHeight = height;
				fireEvent(new MetadataLoadedEvent(component));
			}

			@Override
			public void mediaPaused() {
				fireEvent(new MediaPausedEvent(component));
				
			}

			@Override
			public void mediaStarted() {
				fireEvent(new MediaStartedEvent(component));				
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
     * Get the duration of the video

     * Note: This gives real value only after {@link org.vaadin.gwtav.MetadataLoadedEvent} has
     * been received.
     * 
     * @see org.vaadin.gwtav.MetadataLoadedEvent
     * @see org.vaadin.gwtav.MetadataLoadedListener
     * @see GwtVideo#addMetadataLoadedListener(MetadataLoadedListener)
     *  
     * @return Length of the video in seconds 
     */
    public double getVideoLength() {
    	return videoLength;
    }
    
    /**
     * Get the starting position offset
     * 
     * Note: This gives real value only after {@link org.vaadin.gwtav.MetadataLoadedEvent} has
     * been received.
     * 
     * @see org.vaadin.gwtav.MetadataLoadedEvent
     * @see org.vaadin.gwtav.MetadataLoadedListener
     * @see GwtVideo#addMetadataLoadedListener(MetadataLoadedListener)
     *  
     * @return The starting position offset in seconds
     */
    public double getInitialPosition() {
    	return initialPosition;
    }
    
    /**
     * Get the height
     * 
     * Note: This gives real value only after {@link org.vaadin.gwtav.MetadataLoadedEvent} has
     * been received.
     * 
     * @see org.vaadin.gwtav.MetadataLoadedEvent
     * @see org.vaadin.gwtav.MetadataLoadedListener
     * @see GwtVideo#addMetadataLoadedListener(MetadataLoadedListener)
     *  
     * @return The height of the video in pixels
     */
    public int getVideoHeight() {
    	return videoHeight;
    }
    
    /**
     * Get the width
     * 
     * Note: This gives real value only after {@link org.vaadin.gwtav.MetadataLoadedEvent} has
     * been received.
     * 
     * @see org.vaadin.gwtav.MetadataLoadedEvent
     * @see org.vaadin.gwtav.MetadataLoadedListener
     * @see GwtVideo#addMetadataLoadedListener(MetadataLoadedListener)
     *  
     * @return The width of the video in pixels
     */
    public int getVideoWidth() {
    	return videoWidth;
    }
    
    /**
     * A convenience method to set source of the media to be a url
     *  
     * @param url The url where the media is streamed from
     */
    public void setSource(String url) {
    	ExternalResource resource = new ExternalResource(url);
    	setSource(resource);
    }
    
    /**
     * Set the new position
     * 
     * Note: This works only if the media is streamed from external url
     * 
     * @param time Position offset in seconds
     */
    public void setPosition(double time) {
    	getRpc().setPosition(time);
    }
    
    /**
     * Get the current playback position. The position is submitted to the server
     * as set by {@link GwtVideo#setReportingInterval(int)}
     * 
     * @see GwtVideo#setReportingInterval(int) 
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
    protected GwtVideoState getState() {
        return (GwtVideoState) super.getState();
    }
    
    private GwtVideoClientRpc getRpc() {
    	return getRpcProxy(GwtVideoClientRpc.class);
    }

	/**
	 * Add a new MetadataLoadedListener
	 * The MetadataLoadedEvent event is fired when Metadata of the media has loaded and media is
	 * ready to play
	 *  
	 * @param listener A MetadataLoadedListener to be added
	 */
	public Registration addMetadataLoadedListener(MetadataLoadedListener listener) {
		return addListener(MetadataLoadedEvent.class, listener, MetadataLoadedListener.METADATA_LOADED_METHOD);
	}
	
	/**
	 * Add a new MediaEndedListener
	 * The MediaEndedEvent event is fired when media playback is ended, i.e. media reaches its end
	 *  
	 * @param listener A MediaEndedListener to be added
	 */
	public Registration addMediaEndedListener(MediaEndedListener listener) {
		return addListener(MediaEndedEvent.class, listener, MediaEndedListener.MEDIA_ENDED_METHOD);
	}
	
	/**
	 * Add a new MediaStartedListener
	 * The MediaStartedEvent event is fired when media playback is started either by user or programmatically
	 *  
	 * @param listener A MediaStartedListener to be added
	 */
	public Registration addMediaStartedListener(MediaStartedListener listener) {
		return addListener(MediaStartedEvent.class, listener, MediaStartedListener.MEDIA_STARTED_METHOD);
	}
	
	/**
	 * Add a new MediaPausedListener
	 * The MediaPausedEvent event is fired when media playback is paused either by user or programmatically
	 *  
	 * @param listener A MediaPausedListener to be added
	 */
	public Registration addMediaPausedListener(MediaPausedListener listener) {
		return addListener(MediaPausedEvent.class, listener, MediaPausedListener.MEDIA_PAUSED_METHOD);
	}

}
