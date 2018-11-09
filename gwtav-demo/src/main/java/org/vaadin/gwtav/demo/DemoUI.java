package org.vaadin.gwtav.demo;

import org.apache.commons.io.FileUtils;
import org.vaadin.gwtav.GwtAudio;
import org.vaadin.gwtav.GwtVideo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.PreloadMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("demo")
@Title("GwtVideo Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

    	TabSheet tabsheet = new TabSheet();
    	
        // Initialize our new UI component
        final VerticalLayout layout1 = videoDemo();
        final VerticalLayout layout2 = audioDemo();
        
        tabsheet.addTab(layout1,"Video");
        tabsheet.addTab(layout2,"Audio");
        tabsheet.setSizeFull();
        
        setContent(tabsheet);
    }

	private VerticalLayout videoDemo() {
		final GwtVideo video = new GwtVideo("A Video");
		video.setPreload(PreloadMode.NONE);
		
        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.addComponent(video);
        layout.setComponentAlignment(video, Alignment.MIDDLE_CENTER);
        // Replace valid file here
//        File videoFile = new File("C:/Users/Tatu/test.mp4");        
        File videoFile = new File("C:/Users/Tatu/big_buck_bunny.mp4");        
        if (videoFile != null) { 
        	FileResource videoResource = new FileResource(videoFile);
        	video.setSource(videoResource);
//        	video.setSource(getStreamResource(videoFile,"video.mp4","video/mp4"));
//        	video.setSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        } else {
        	System.out.println("File not found");
        }

        HorizontalLayout controls = new HorizontalLayout();

        Slider slider = new Slider();
        slider.setMax(0.0d);
        slider.setMin(0.0d);
        slider.addValueChangeListener(event -> {
        	video.setPosition(slider.getValue());
        });
        
        Slider volume = new Slider();
        volume.setMax(100.0d);
        volume.setMin(0.0d);
        volume.addValueChangeListener(event -> {
        	video.setVolume(volume.getValue()/100.0d);
        });
        
        Label posLabel = new Label("Position: "+video.getPosition()+"/"+video.getVideoLength());
        Button playBtn = new Button(VaadinIcons.PLAY.getHtml());
        playBtn.setCaptionAsHtml(true);
        playBtn.addClickListener(event -> {
        	video.play();
        });
        Button pauseBtn = new Button(VaadinIcons.PAUSE.getHtml());
        pauseBtn.setCaptionAsHtml(true);
        pauseBtn.addClickListener(event -> {
        	video.pause();
        	posLabel.setValue("Position: "+video.getPosition()+"/"+video.getVideoLength());
        });
        Button stopBtn = new Button(VaadinIcons.STOP.getHtml());
        stopBtn.setCaptionAsHtml(true);
        stopBtn.addClickListener(event -> {
        	video.stop();
        });        
        Button muteBtn = new Button(VaadinIcons.MUTE.getHtml());
        muteBtn.setCaptionAsHtml(true);
        muteBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        muteBtn.addClickListener(event -> {
        	video.setMuted(!video.isMuted());
        	if (video.isMuted()) muteBtn.setStyleName(ValoTheme.BUTTON_QUIET);
        	else muteBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        });        
        Button loopBtn = new Button(VaadinIcons.RECYCLE.getHtml());
        loopBtn.setCaptionAsHtml(true);
        loopBtn.setStyleName(ValoTheme.BUTTON_QUIET);
        loopBtn.addClickListener(event -> {
        	video.setLoop(!video.isLoop());
        	if (video.isLoop()) loopBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        	else loopBtn.setStyleName(ValoTheme.BUTTON_QUIET);
        });        

        controls.addComponents(posLabel,playBtn,stopBtn,pauseBtn,muteBtn,loopBtn,new Label("Pos: "),slider,new Label("Vol: "),volume);
        controls.setStyleName(ValoTheme.LAYOUT_CARD);
        controls.setCaption("Controls");
        layout.addComponent(controls);
        layout.setComponentAlignment(controls, Alignment.MIDDLE_CENTER);       
        
        video.addMediaEndedListener(event -> {
        	Notification.show("Video ended");
        });

        video.addMediaPausedListener(event -> {
        	Notification.show("Video paused");
        });

        video.addMediaStartedListener(event -> {
        	Notification.show("Video started");
        });

        video.addMediaSeekedListener(event -> {
        	posLabel.setValue("Position: "+video.getPosition()+"/"+video.getVideoLength());        	
        });
        
        video.addMetadataLoadedListener(event -> {
        	slider.setMax(video.getVideoLength());
        	Notification.show("Video ready - Length: "+video.getVideoLength()+"s Width: "+video.getVideoWidth()+"px Height: "+video.getVideoHeight()+"px");
        	posLabel.setValue("Position: "+video.getPosition()+"/"+video.getVideoLength());
        });
		return layout;
	}

	public static ByteArrayInputStream reteriveByteArrayInputStream(File file) {
	    try {
			return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}      
	
	public static StreamResource getStreamResource(File file, String fileName, String mimeType) {
		StreamResource.StreamSource source = (StreamResource.StreamSource) () -> {
			return reteriveByteArrayInputStream(file);
		};
		StreamResource stream  = new StreamResource(source,fileName);
		stream.setMIMEType(mimeType);
		return stream;
	}
	

	private VerticalLayout audioDemo() {
		final GwtAudio audio = new GwtAudio("An Audio");

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.addComponent(audio);
        layout.setComponentAlignment(audio, Alignment.MIDDLE_CENTER);
        // Replace valid file here
        File audioFile = new File("C:/Users/Tatu/test.mp3");        
        if (audioFile != null) { 
        	FileResource audioResource = new FileResource(audioFile);
        	audio.setSource(audioResource);
//        	audio.setSource(getStreamResource(audioFile,"audio.mp3","audio/mpeg"));
//        	audio.setSource("http://file-examples.com/wp-content/uploads/2017/11/file_example_WAV_10MG.wav");
        } else {
        	System.out.println("File not found");
        }

        HorizontalLayout controls = new HorizontalLayout();

        Slider slider = new Slider();
        slider.setMax(0.0d);
        slider.setMin(0.0d);
        slider.addValueChangeListener(event -> {
        	audio.setPosition(slider.getValue());
        });

        Slider volume = new Slider();
        volume.setMax(100.0d);
        volume.setMin(0.0d);
        volume.addValueChangeListener(event -> {
        	audio.setVolume(volume.getValue()/100.0d);
        });
        
        Label posLabel = new Label("Position: "+audio.getPosition()+"/"+audio.getAudioLength());
        Button playBtn = new Button(VaadinIcons.PLAY.getHtml());
        playBtn.setCaptionAsHtml(true);
        playBtn.addClickListener(event -> {
        	audio.play();
        });
        Button pauseBtn = new Button(VaadinIcons.PAUSE.getHtml());
        pauseBtn.setCaptionAsHtml(true);
        pauseBtn.addClickListener(event -> {
        	audio.pause();
        	posLabel.setValue("Position: "+audio.getPosition()+"/"+audio.getAudioLength());
        });
        Button stopBtn = new Button(VaadinIcons.STOP.getHtml());
        stopBtn.setCaptionAsHtml(true);
        stopBtn.addClickListener(event -> {
        	audio.stop();
        });        
        Button muteBtn = new Button(VaadinIcons.MUTE.getHtml());
        muteBtn.setCaptionAsHtml(true);
        muteBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        muteBtn.addClickListener(event -> {
        	audio.setMuted(!audio.isMuted());
        	if (audio.isMuted()) muteBtn.setStyleName(ValoTheme.BUTTON_QUIET);
        	else muteBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        });        
        Button loopBtn = new Button(VaadinIcons.RECYCLE.getHtml());
        loopBtn.setCaptionAsHtml(true);
        loopBtn.setStyleName(ValoTheme.BUTTON_QUIET);
        loopBtn.addClickListener(event -> {
        	audio.setLoop(!audio.isLoop());
        	if (audio.isLoop()) loopBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        	else loopBtn.setStyleName(ValoTheme.BUTTON_QUIET);
        });        

        controls.addComponents(posLabel,playBtn,stopBtn,pauseBtn,muteBtn,loopBtn,new Label("Pos: "),slider,new Label("Vol: "),volume);
        controls.setStyleName(ValoTheme.LAYOUT_CARD);
        controls.setCaption("Controls");
        layout.addComponent(controls);
        layout.setComponentAlignment(controls, Alignment.MIDDLE_CENTER);       
        
        audio.addMediaSeekedListener(event -> {
        	posLabel.setValue("Position: "+audio.getPosition()+"/"+audio.getAudioLength());        	
        });
        
        audio.addMetadataLoadedListener(event -> {
        	slider.setMax(audio.getAudioLength());
        	Notification.show("Audio ready - Length: "+audio.getAudioLength()+"s");
        	posLabel.setValue("Position: "+audio.getPosition()+"/"+audio.getAudioLength());
        });
		return layout;
	}

}
