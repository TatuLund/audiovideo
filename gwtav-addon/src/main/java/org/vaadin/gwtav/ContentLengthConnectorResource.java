package org.vaadin.gwtav;

import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;

/**
 * This is a utility and helper class for the cases where you know the content length, but for
 * example want to use StreamResource. Good example is reading a blob from database and serving
 * that as StreamResource. StreamResouce does not by nature know Content-Length. 
 * 
 * Content-Length is necessary in order to serve range requests.
 * 
 * @see GwtAudio#setPosition(double)
 * @see GwtVideo#setPosition(double)
 * 
 * @author Tatu Lund
 *
 */
@SuppressWarnings("serial")
public class ContentLengthConnectorResource implements ConnectorResource {
	private long contentLength;
	private ConnectorResource resource;
	
	/**
	 * Constructor 
	 * 
	 * NOTE: Unexpected errors can happen if contentLength does not match real one
	 * 
	 * @param resource A resource to be wrapped, e.g. StreamResource
	 * @param contentLength The real Content-Length
	 */
	public ContentLengthConnectorResource(ConnectorResource resource, long contentLength) {
		this.resource = resource;
		this.contentLength = contentLength;
	}

    @Override
    public DownloadStream getStream() {
    	DownloadStream ds = resource.getStream();
        ds.setParameter("Content-Length",
                String.valueOf(contentLength));
    	return ds;
    }

	@Override
	public String getMIMEType() {
		return resource.getMIMEType();
	}

	@Override
	public String getFilename() {
		return resource.getFilename();
	}

}
