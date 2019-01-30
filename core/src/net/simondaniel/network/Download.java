package net.simondaniel.network;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

// This class downloads a file from a URL.
public class Download extends Observable implements Runnable {

	// Max size of download buffer.
	private static final int MAX_BUFFER_SIZE = 1024;

	// These are the status names.
	public static final String STATUSES[] = { "Downloading", "Paused", "Complete", "Cancelled", "Error" };

	// These are the status codes.
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;

	private URL url; // download URL
	private String filename;
	private int size; // size of download in bytes
	private int downloaded; // number of bytes downloaded
	private int status; // current status of download
	
	Thread thread;

	// Constructor for Download.
	public Download(URL url, String filename) {
		this.url = url;
		size = -1;
		downloaded = 0;
		status = DOWNLOADING;
		this.filename = filename;
		// Begin the download.
		download();
	}
	
	public Download(URL url){
		this(url, getFileName(url));
	}
	
	public Download(String url) throws MalformedURLException{
		this(new URL(url));
	}
	
	public Download(String url, String filename) throws MalformedURLException{
		this(new URL(url), filename);
	}

	public Download(String url, FileHandle fh) throws MalformedURLException {
		this(url, fh.path());
	}

	// Get this download's URL.
	public String getUrl() {
		return url.toString();
	}
	DecimalFormat df = new DecimalFormat("#.##");     
	
	public DataUnit getPrefUnit(){
		if(size > DataUnit.GB.bytes * 4) return DataUnit.GB;
		if(size > DataUnit.MB.bytes * 4) return DataUnit.MB;
		if(size > DataUnit.KB.bytes * 4) return DataUnit.KB;
		return DataUnit.BT;
	}

	// Get this download's size.
	public int getSize() {
		return size;
	}
	
	// Get this download's size.
	public String getPrefSize() {
		DataUnit u = getPrefUnit();
		
		return (df.format(size / (double)u.bytes)) + " " + u.name;
	}
	
	public String getPrefDownloaded() {
		DataUnit u = getPrefUnit();
		
		return (df.format(downloaded / (double)u.bytes)) + " " + u.name;
	}
	
	

	// Get this download's progress.
	public float getProgress() {
		return ((float) downloaded / size) * 100;
	}

	// Get this download's status.
	public int getStatus() {
		return status;
	}

	// Pause this download.
	public void pause() {
		status = PAUSED;
		stateChanged();
	}

	// Resume this download.
	public void resume() {
		status = DOWNLOADING;
		stateChanged();
		download();
	}

	// Cancel this download.
	public void cancel() {
		status = CANCELLED;
		stateChanged();
	}

	// Mark this download as having an error.
	private void error() {
		status = ERROR;
		stateChanged();
	}

	// Start or resume downloading.
	private void download() {
		thread = new Thread(this);
		thread.start();
	}

	// Get file name portion of URL.
	private static String getFileName(URL url) {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}

	// Download file.
	@Override
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;

		try {
			// Open connection to URL.
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Specify what portion of file to download.
			connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

			// Connect to server.
			connection.connect();

			// Make sure response code is in the 200 range.
			if (connection.getResponseCode() / 100 != 2) {
				error();
			}

			// Check for valid content length.
			int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				error();
			}

			/*
			 * Set the size for this download if it hasn't been already set.
			 */
			if (size == -1) {
				size = contentLength;
				stateChanged();
			}

			// Open file and seek to the end of it.
			file = new RandomAccessFile(filename, "rw");
			file.seek(downloaded);

			stream = connection.getInputStream();
			while (status == DOWNLOADING) {
				/*
				 * Size buffer according to how much of the file is left to
				 * download.
				 */
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[size - downloaded];
				}

				// Read from server into buffer.
				int read = stream.read(buffer);
				if (read == -1)
					break;

				// Write buffer to file.
				file.write(buffer, 0, read);
				downloaded += read;
				stateChanged();
			}

			/*
			 * Change status to complete if this point was reached because
			 * downloading has finished.
			 */
			if (status == DOWNLOADING) {
				status = COMPLETE;
				stateChanged();
			}
		} catch (Exception e) {
			error();
		} finally {
			// Close file.
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
				}
			}

			// Close connection to server.
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// Notify observers that this download's status has changed.
	private void stateChanged() {
		setChanged();
		notifyObservers();
	}
	
	enum DataUnit{
		BT(1, "byte"), KB(1000, "KB"), MB(1000*1000, "MB"), GB(1000*1000*1000, "GB");
		
		long bytes;
		final String name;
		
		private DataUnit(long bytes, String s){
			this.bytes = bytes;
			this.name = s;
		}
	}
	
	public void finish(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String downloadString(String url) {
		String content = null;
		try {
			Download d = new Download(url, "temp");
			d.finish();
			FileHandle fh = Gdx.files.local("temp");
			content = fh.readString();
			fh.delete();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return content;
	}
}