package org.vaadin.gwtav.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.VideoElement;
import com.google.gwt.event.dom.client.EndedHandler;
import com.google.gwt.event.dom.client.ProgressHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.media.client.Audio;
import com.google.gwt.media.client.Video;
import com.google.gwt.media.dom.client.MediaError;
import com.vaadin.client.Util;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VMediaBase;

// Extend any GWT Widget
public class GwtAudioWidget extends VMediaBase {

	Audio audio = null;
	GwtAudioConnector connector = null;
	
    public GwtAudioWidget() {
    	audio = Audio.createIfSupported();
    	if (audio != null) {
    		setMediaElement(audio.getMediaElement());
            setStyleName("gwt-audio");     
            updateServerWhenMetadataLoaded(getElement());            
            addEndEventListener(getElement());
            addPlayEventListener(getElement());
            addPauseEventListener(getElement());
            addSeekedEventListener(getElement());
    	} else {
    		VConsole.log("Audio cannot be created");
    	}
        // State is set to widget in GwtVideoConnector
    }

    public void setVolume(double volume) {
    	audio.setVolume(volume);
    }
    
    public double getDuration() {
    	return audio.getDuration();
    }

    public double getInitialPosition() {
    	return audio.getInitialTime();
    }

    public boolean isPaused() {
    	return audio.isPaused();
    }
    
    public MediaError getError() {
    	return audio.getError();
    }
   
    public void addProgressHandler(ProgressHandler handler) {
    	audio.addProgressHandler(handler);
    }
            
    public void addEndedHandler(EndedHandler handler) {
    	audio.addEndedHandler(handler);
    }
    
    public void setPosition(double time) {
    	audio.setCurrentTime(time);
    }

    public double getPosition() {
    	return audio.getCurrentTime();
    }
    
    /**
     *
     */
    private native void updateServerWhenMetadataLoaded(Element el)
    /*-{
              var self = this;
              el.addEventListener('loadedmetadata', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtAudioWidget::updateInitialData()();
              }), false);

    }-*/;

    private native void addEndEventListener(Element el)
    /*-{
              var self = this;
              el.addEventListener('ended', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtAudioWidget::audioEnded()();
              }), false);

    }-*/;

    private native void addPlayEventListener(Element el)
    /*-{
              var self = this;
              el.addEventListener('play', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtAudioWidget::audioStarted()();
              }), false);

    }-*/;
    
    private native void addPauseEventListener(Element el)
    /*-{
              var self = this;
              el.addEventListener('pause', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtAudioWidget::audioPaused()();
              }), false);

    }-*/;
    
    private native void addSeekedEventListener(Element el)
    /*-{
              var self = this;
              el.addEventListener('seeked', $entry(function(e) {
                  self.@org.vaadin.gwtav.client.GwtAudioWidget::audioSeeked()();
              }), false);

    }-*/;    
    
    private void audioEnded() {
    	connector.audioEnded();
    }

    private void audioStarted() {
    	connector.audioStarted();
    }
    
    private void audioPaused() {
    	connector.audioPaused();
    }    

    private void audioSeeked() {
    	connector.audioSeeked();
    }
            
    private void updateInitialData() {
        // Send relevant metadata also to server
        connector.sendInitialData();
    }

	public void setConnector(GwtAudioConnector gwtAudioConnector) {
		connector = gwtAudioConnector;		
	}
    
}