package com.hty.sound4pi.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
/**
 * mp3播放工具
 * @author Hetianyi
 */
public class Mp3Player {
	//停止播放信号
	private boolean signal = true;
	//暂停播放信号
	private boolean pause = false;
	
	public void play(String filename) throws FileNotFoundException {
		play(new FileInputStream(filename));
	}
	
	public void stop() {
		signal = false;
		begin();
	}
	
	public void pause() {
		pause = true;
	}
	
	public void begin() {
		pause = false;
	}
	
	public void play(InputStream ips) {
		try {
			AudioInputStream in = new MpegAudioFileReader()
					.getAudioInputStream(ips);
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			// Play now.
			rawplay(decodedFormat, din);
			din.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException, InterruptedException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (signal && nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1)
					nBytesWritten = line.write(data, 0, nBytesRead);
				while(pause) {
					Thread.sleep(100);
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	private SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}
}
