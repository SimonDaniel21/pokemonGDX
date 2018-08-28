package net.simondaniel.network;

import java.awt.BorderLayout;
import static java.nio.file.StandardCopyOption.*;
import java.awt.FlowLayout;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.simondaniel.Config;
import net.simondaniel.FileSystem;
import net.simondaniel.GameConfig;

import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class UpdateDialog extends JDialog implements Observer, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	static int i =  0;
	
	public static float downloadSize = 8.43252f;
	static float downloaded = 0;
	
	static Download download;
	
	static UpdateDialog ref;
	
	static String URL, CFG_URL, FILE_NAME;
	
	Thread thread;

	@Override
	public void run() {
		try {
			ref.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			ref.setVisible(true);
			//GameConfig config = Config.gameConfig;
			System.out.println(URL);
			download = new Download(URL, FILE_NAME);
			Download cfgDownload = new Download(CFG_URL, "stdConfig.txt");
			cfgDownload.finish();
			File cfg = new File("stdConfig.txt");
			File destination = FileSystem.loadFile("config.txt").file();
			Files.move(cfg.toPath(), destination.toPath(), REPLACE_EXISTING);
			Runnable r = new Runnable() {
				boolean started = false;
				@Override
				public void run() {
					try {
						while(true){
							if(!started && download.getSize() != -1){
								lblDownloadsize.setText(download.getPrefSize());
								started = true;
							}
							if(started){
								lblDownloaded.setText(download.getPrefDownloaded());
								progressBar.setValue(Math.round(download.getProgress()));
							}
							if(download.getStatus() == Download.COMPLETE){
								JOptionPane.showMessageDialog(ref, "Download finished!");
								ref.dispose();
								break;
							}
							Thread.sleep(80);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			Thread t = new Thread(r);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launch the application.
	 */
	
	
	static DecimalFormat format = new DecimalFormat("0.000");
	
	public static void updateProgress(){
		progressBar.setValue( Math.round(downloaded / downloadSize*100));
		lblDownloaded.setText(format.format(downloaded) + " MB");
	}

	public static JProgressBar progressBar;
	static JLabel lblDownloadsize;
	static JLabel lblDownloaded;
	/**
	 * Create the dialog.
	 */
	public UpdateDialog(String url, String cfgUrl, String fileName) {
		ref = this;
		URL = url;
		CFG_URL = cfgUrl;
		FILE_NAME = fileName;
		setTitle("Update");
		setBounds(100, 100, 409, 174);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			progressBar = new JProgressBar();
			progressBar.setBounds(12, 10, 385, 20);
			progressBar.setToolTipText("progress");
			progressBar.setStringPainted(true);
			contentPanel.add(progressBar);
		}
		{
			JLabel lblDownloadize_ = new JLabel("Downloadsize:");
			lblDownloadize_.setBounds(12, 42, 105, 15);
			contentPanel.add(lblDownloadize_);
		}
		{
			JLabel lblDownloaded_ = new JLabel("Downloaded:");
			lblDownloaded_.setBounds(12, 69, 105, 15);
			contentPanel.add(lblDownloaded_);
		}
		{
			lblDownloadsize = new JLabel("0 bytes");
			lblDownloadsize.setBounds(129, 42, 105, 15);
			contentPanel.add(lblDownloadsize);
		}
		{
			lblDownloaded = new JLabel("");
			lblDownloaded.setBounds(129, 69, 70, 15);
			contentPanel.add(lblDownloaded);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						ref.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		thread = new Thread(this);
		thread.start();
	}
	
	public void waitToFinish(){
		try {
			Thread.sleep(200);
			while(ref.isShowing()){
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
	}
}
