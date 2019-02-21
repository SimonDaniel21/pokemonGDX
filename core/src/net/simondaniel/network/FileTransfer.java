package net.simondaniel.network;

import java.util.Arrays;

import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.server.Response.FileTransferS;

public class FileTransfer {
	
	private static final int frameSize = 1024 + 512;
	private static final int MAX_FILE_SIZE = 1024 * 1024; // 1mb

	public static void sendFile(Connection c, FileHandle fh, String dest) {

		if(!fh.exists()) {
			System.err.println("file " + fh + "doesnt exist");
			return;
		}
		long fileSize = fh.length();
		if(fileSize > MAX_FILE_SIZE) {
			System.err.println("file " + fh + "(" + fileSize + ") is bigger than the maximum send size of " + MAX_FILE_SIZE);
			return;
		}
		
		try {
			FileTransferS p = new FileTransferS();
			
			byte[] buffer = fh.readBytes();
			
			// number of packets that are going to be sent + 1
			int startIndex = (buffer.length-1) / frameSize;
			// counts down to 0 (end of file)
			int index = startIndex;
			// start byte in original buffer of frame
			int startPointer = 0;
			// end byte in original buffer of frame
			int endPointer = buffer.length % frameSize;
			
			// first dataBuffer to be send, only buffer which size does not have to be the framesize
			byte[] initialBuffer = Arrays.copyOfRange(buffer, startPointer, endPointer);
			
			p.data = initialBuffer;
			p.fileName = dest;
			p.index = index;
			
			c.sendTCP(p);
			
			p.fileName = null;
			index--;
			
			while(index >= 0) {
				startPointer = endPointer;
				endPointer += frameSize;
				byte[] frameBuffer = Arrays.copyOfRange(buffer, startPointer, endPointer);
				p.data = frameBuffer;
				p.index = index;
				c.sendTCP(p);
				index--;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void receivedFileFrame(FileTransferS frame) {

		System.out.println("index " + frame.index);
		
		if(currentlyDownloaded == null && frame.fileName == null) {
			System.err.println("the first packet didnt specify the fileName!");
			return;
		}
		if(currentlyDownloaded != null && frame.fileName != null) {
			System.err.println("only the first packet can specify the fileName!");
			return;
		}
		
		if(frame.fileName == null) {
			receiveFrame(frame);
		}
		else {
			startReceive(frame);
		}
		if(frame.index == 0) {
			endReceive();
		}
	}
	
	private static void startReceive(FileTransferS frame) {
		currentlyDownloaded = frame.fileName;
		int fileSize = (frame.index * frameSize) + frame.data.length;
		buffer = new byte[fileSize];
		System.arraycopy(frame.data, 0, buffer, pointer, frame.data.length);
		pointer += frame.data.length;
	}
	
	private static void receiveFrame(FileTransferS frame) {
		System.arraycopy(frame.data, 0, buffer, pointer, frameSize);
		pointer += frameSize;
	}
	
	private static void endReceive() {
		FileHandle fh = net.simondaniel.aux.FileSystem.loadFile(currentlyDownloaded);
		fh.writeBytes(buffer, false);
		System.err.println("wrote Image " + buffer.length);
		currentlyDownloaded = null;
		buffer = null;
		pointer = 0;
	}
	
	private static byte[] buffer;
	private static String currentlyDownloaded;
	
	private static int pointer;
}
