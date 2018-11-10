package org.vaadin.gwtav;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.Constants;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

/**
 * Implementation of range request support when Content-Length is known. Used internally.
 * 
 * @author Tatu Lund
 *
 */
public class IOUtil {
    
    public static void writeResponse(VaadinRequest request, VaadinResponse response, DownloadStream stream, long rangeStart, long rangeEnd)
            throws IOException {
        if (stream.getParameter("Location") != null) {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", stream.getParameter("Location"));
            return;
        }

        // Download from given stream
        final InputStream data = stream.getStream();
        if (data == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (data != null) {

            OutputStream out = null;
            try {
                // Sets content type
                response.setContentType(stream.getContentType());

                // Sets cache headers
                response.setCacheTime(stream.getCacheTime());

                // Copy download stream parameters directly
                // to HTTP headers.
                final Iterator<String> i = stream.getParameterNames();
                if (i != null) {
                    while (i.hasNext()) {
                        final String param = i.next();
                        response.setHeader(param, stream.getParameter(param));                        
                    }
                }

                // Content-Disposition: attachment generally forces download
                String contentDisposition = stream.getParameter(DownloadStream.CONTENT_DISPOSITION);
                if (contentDisposition == null) {
                    contentDisposition = stream.getContentDispositionFilename(
                    		stream.getFileName());
                }

                response.setHeader(DownloadStream.CONTENT_DISPOSITION, contentDisposition);

                int bufferSize = stream.getBufferSize();
                if (bufferSize <= 0 || bufferSize > Constants.MAX_BUFFER_SIZE) {
                    bufferSize = Constants.DEFAULT_BUFFER_SIZE;
                }
                final byte[] buffer = new byte[bufferSize];
                int bytesRead = 0;

                // Calculate range that is going to be served if this was range request
                long bytesToWrite = -1;
                data.skip(rangeStart); // Skip to start offset of the request
                if (rangeStart > 0 || rangeEnd > 0) {
                	response.setStatus(206); // 206 response code needed since this is partial data
                	long contentLength = Long.parseLong(stream.getParameter("Content-Length"));
                	if (rangeEnd == -1) rangeEnd = contentLength-1;
                	response.setHeader("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+contentLength);
                	bytesToWrite = rangeEnd-rangeStart+1;
					response.setHeader("Content-Length", ""+bytesToWrite);
                }
                out = response.getOutputStream();
                
                long totalWritten = 0;
                while ((bytesToWrite == -1 || totalWritten < bytesToWrite) && (bytesRead = data.read(buffer)) > 0) {
                	// Check if this was last part, hence possibly less
                	if (bytesToWrite != -1) {
                		bytesRead = (int) Math.min(bytesRead, bytesToWrite - totalWritten);
                	}
               		out.write(buffer, 0, bytesRead);

                    totalWritten += bytesRead;
                    if (totalWritten >= buffer.length) {
                        // Avoid chunked encoding for small resources
                        out.flush();
                    }
                }
            } catch (EOFException ignore) {
        		// Browser aborts when it notices that range requests are supported
            	// Swallow e.g. Jetty
            } catch (IOException e) {
            	String name = e.getClass().getName();
            	if (name.equals("org.apache.catalina.connector.ClientAbortException")) {
            		// Browser aborts when it notices that range requests are supported
            		// Swallow e.g. Tomcat
            	} else {
            		throw e;
            	}
            } finally {
            	tryToCloseStream(out);
            	tryToCloseStream(data);
            }
        }
    }

    /**
     * Helper method that tries to close an output stream and ignores any
     * exceptions.
     *
     * @param out
     *            the output stream to close, <code>null</code> is also
     *            supported
     */
    public static void tryToCloseStream(OutputStream out) {
        try {
            // try to close output stream (e.g. file handle)
            if (out != null) {
                out.close();
            }
        } catch (IOException e1) {
            // NOP
        }
    }

    /**
     * Helper method that tries to close an input stream and ignores any
     * exceptions.
     *
     * @param in
     *            the input stream to close, <code>null</code> is also supported
     */
    public static void tryToCloseStream(InputStream in) {
        try {
            // try to close output stream (e.g. file handle)
            if (in != null) {
                in.close();
            }
        } catch (IOException e1) {
            // NOP
        }
    }

}
