package com.jds.jn.logs.readers;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.logs.listeners.ReaderListener;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.session.Session;
import com.jds.jn.util.StringHexBuffer;
import com.jds.jn.util.Util;
import com.jds.jn.util.version_control.Program;
import com.jds.jn.util.version_control.Version;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  14:41:18/03.09.2010
 */
public class PLogReader extends AbstractReader
{
	private static final Logger _log = Logger.getLogger(PLogReader.class);
	private LineNumberReader _reader;

	@Override
	public void read(File file, ReaderListener listener) throws IOException
	{
		if (!file.exists())
		{
			_log.info("File not exists: " + file);
			listener.onFinish(null);
			return;
		}

		if (_isBusy)
		{
			_log.info("Reader is busy: " + _currentFile.getName());
			listener.onFinish(null);
			return;
		}

		_listener = listener;
		_isBusy = true;

		_currentFile = file;
		_reader = new LineNumberReader(new FileReader(file));

		read();
	}

	@Override
	protected void close() throws IOException
	{
		_reader.close();

		_isBusy = false;
	}

	@Override
	public boolean parseHeader() throws IOException
	{
		_session = new Session(ListenerType.Game_Server, Util.positiveRandom());
		_session.setVersion(new Version(Program.L2PHX, 3, 5, Version.STABLE, 0));
		return true;
	}

	@Override
	public void parsePackets() throws IOException
	{
		List<String> lines = new ArrayList<String>();
		String readLine = null;
		while ((readLine = _reader.readLine()) != null)
		{
			lines.add(readLine);
		}

		int i = 0;
		for(String line : lines)
		{
			StringHexBuffer buffer = new StringHexBuffer(line);
			byte type = buffer.nextByte();
			PacketType packetType;
			if(type == 1 || type == 3)
			{
				packetType = PacketType.SERVER;
			}
			else
			{
				packetType = PacketType.CLIENT;
			}

			long time = buffer.nextLong();
			int size = buffer.nextShort();
			byte[] data = new byte[size - 2];
			for(int $ = 0; $ < data.length; $++)
			{
				data[$] = buffer.nextByte();
			}

			CryptedPacket packet = new CryptedPacket(packetType, data, time, _session.getProtocol().getOrder());

			DecryptedPacket dp = new DecryptedPacket(packet, _session.getProtocol());

			_session.receiveQuitPacket(dp);

			int p = (int) ((100D * (i + 1)) / lines.size());
			MainForm.getInstance().getProgressBar().setValue(p);
			i++;
		}
	}

	@Override
	public String getFileExtension()
	{
		return "pLog";
	}

	@Override
	public String getReaderInfo()
	{
		return "L2Phx log file";
	}
}