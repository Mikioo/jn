package com.jds.jn.util;

import java.io.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21 лист 2009
 * Time: 10:37:01
 */
public class FileUtils
{
	public static void copy(String fileNameSource, String fileNameDecs) throws IOException
	{
		File fileDecs = new File(fileNameDecs);
		File fileSource = new File(fileNameSource);
		if (!fileSource.exists())
		{
			throw new IllegalArgumentException("Source file is not exists. File " + fileNameSource);
		}
		if (fileDecs.exists())
		{
			fileDecs.delete();
		}
		fileDecs.createNewFile();

		InputStream outReal = new FileInputStream(fileSource);
		byte[] data = new byte[outReal.available()];
		outReal.read(data);
		outReal.close();


		OutputStream in = new FileOutputStream(fileDecs);
		in.write(data);
		in.close();
	}

}