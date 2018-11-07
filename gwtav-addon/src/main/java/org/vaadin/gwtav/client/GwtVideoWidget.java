package org.vaadin.gwtav.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.EndedHandler;
import com.google.gwt.event.dom.client.ProgressHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.media.client.Video;
import com.google.gwt.media.dom.client.MediaError;
import com.vaadin.client.Util;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VMediaBase;

// Extend any GWT Widget
public class GwtVideoWidget extends VMediaBase {

	Video video = null;
	GwtVideoConnector connector = null;
	private String url;
	
    public GwtVideoWidget() {
    	video = Video.createIfSupported();
    	if (video != null) {
    		setMediaElement(video.getMediaElement());
            setStyleName("gwt-video");     
            updateDimensionsWhenMetadataLoaded(getElement());
            addEndEventListener(getElement());
            addPlayEventListener(getElement());
            addPauseEventListener(getElement());
    	} else {
    		VConsole.log("Video cannot be created");
    	}
        // State is set to widget in GwtVideoConnector
    }

    public double getDuration() {
    	return video.getDuration();
    }

    public void setVolume(double volume) {
    	video.setVolume(volume);
    }
    
    public double getInitialPosition() {
    	return video.getInitialTime();
    }

    public boolean isPaused() {
    	return video.isPaused();
    }
    
    public MediaError getError() {
    	return video.getError();
    }
   
    public int getVideoHeight() {
    	return video.getVideoHeight();
    }
    
    public int getVideoWidth() {
    	return video.getVideoWidth();
    }
    
    public void addProgressHandler(ProgressHandler handler) {
    	video.addProgressHandler(handler);
    }
            
    public void addEndedHandler(EndedHandler handler) {
    	video.addEndedHandler(handler);
    }
    
//    @Override
//    public void addSource(String sourceUrl, String sourceType) {
//    	super.addSource(sourceUrl, sourceType);
//    	this.url = sourceUrl;    	
//    }
    
    public void setPosition(double time) {
//    	int offset = (int) time;
//    	String urlWithTime = url+"#t="+offset;
//    	SourceElement element = (SourceElement) video.getElement().getFirstChildElement(); 
//    	element.setSrc(urlWithTime);
//    	load();
    	video.setCurrentTime(time);
    }

    public double getPosition() {
    	return video.getCurrentTime();
    }
    
    /**
     * Registers a listener that updates the dimensions of the widget when the
     * video metadata has been loaded.
     *
     * @param el
     */
    private native void updateDimensionsWhenMetadataLoaded(Element el)
    /*-{
              var self = this;
              el.addEventListener('loadedmetadata', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtVideoWidget::updateElementDynamicSize(II)(el.videoWidth, el.videoHeight);
              }), false);

    }-*/;

    private native void addEndEventListener(Element el)
    /*-{
              var self = this;
              el.addEventListener('ended', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtVideoWidget::videoEnded()();
              }), false);

    }-*/;

    private native void addPlayEventListener(Element el)
    /*-{
              var self = this;
              el.addEventListener('play', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtVideoWidget::videoStarted()();
              }), false);

    }-*/;
    
    private native void addPauseEventListener(Element el)
    /*-{
              var self = this;
              el.addEventListener('pause', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtVideoWidget::videoPaused()();
              }), false);

    }-*/;
    
    
    private void videoEnded() {
    	connector.videoEnded();
    }

    private void videoStarted() {
    	connector.videoStarted();
    }
    
    private void videoPaused() {
    	connector.videoPaused();
    }
    
    
    /**
     * Updates the dimensions of the widget.
     *
     * @param w
     * @param h
     */
    private void updateElementDynamicSize(int w, int h) {
        getElement().getStyle().setWidth(w, Unit.PX);
        getElement().getStyle().setHeight(h, Unit.PX);
        Util.notifyParentOfSizeChange(this, true);
        // Send relevant metadata also to server
        connector.sendInitialData();
    }

	public void setConnector(GwtVideoConnector gwtVideoConnector) {
		connector = gwtVideoConnector;		
	}
    
}