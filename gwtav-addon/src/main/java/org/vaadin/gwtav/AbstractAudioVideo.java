package org.vaadin.gwtav;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Registration;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.ui.AbstractMedia;

/**
 * AbstractAudioVideo is an improved version of AbstractMedia component with implementation
 * of the new events, and support for serving ConnectorResources from given position. 
 * 
 * @see GwtVideo#setPosition(double)
 * @see GwtAudio#setPosition(double)
 *  
 * @author Tatu Lund
 */
@SuppressWarnings("serial")
public abstract class AbstractAudioVideo extends AbstractMedia {


    private Logger getLogger() {
        return Logger.getLogger(AbstractAudioVideo.class.getName());
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

	/**
	 * Add a new MediaSeekedListener
	 * The MediaSeekedEvent event is fired when user has changed position of the slider
	 *  
	 * @param listener A MediaSeekedListener to be added
	 */
	public Registration addMediaSeekedListener(MediaSeekedListener listener) {
		return addListener(MediaSeekedEvent.class, listener, MediaSeekedListener.MEDIA_SEEKED_METHOD);
	}

    @Override
    public boolean handleConnectorRequest(VaadinRequest request,
            VaadinResponse response, String path) throws IOException {

    	// Handle Range header
        long rangeStart = 0;
        long rangeEnd = -1;
        String header = request.getHeader("Range");
        if (header != null) {        	
        	String[] split = header.substring(6, header.length()).split("-");
        	rangeStart = Long.parseLong(split[0]);
        	if (split.length == 2) {
        		rangeEnd = Long.parseLong(split[1]);
        	}
        }
          	
    	Matcher matcher = Pattern.compile("(\\d+)(/.*)?").matcher(path);

        if (!matcher.matches()) {
            return super.handleConnectorRequest(request, response, path);
        }

        DownloadStream stream;

        VaadinSession session = getSession();
        session.lock();
        try {
            List<URLReference> sources = getState().sources;

            int sourceIndex = Integer.parseInt(matcher.group(1));

            if (sourceIndex < 0 || sourceIndex >= sources.size()) {
                getLogger().log(Level.WARNING,
                        "Requested source index {0} is out of bounds",
                        sourceIndex);
                return false;
            }

            URLReference reference = sources.get(sourceIndex);
            ConnectorResource resource = (ConnectorResource) ResourceReference
                    .getResource(reference);
           	stream = resource.getStream();
        } finally {
            session.unlock();
        }

        // If stream has Content-Length we can support range requests, add response header
        if (stream.getParameter("Content-Length") != null) response.setHeader("Accept-Ranges", "bytes");
        IOUtil.writeResponse(request, response, stream, rangeStart, rangeEnd);
        return true;
    }

    
}
