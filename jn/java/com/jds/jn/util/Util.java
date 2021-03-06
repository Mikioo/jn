package com.jds.jn.util;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Random;

import com.jds.nio.buffer.NioBuffer;

/**
 * @author Ulysses R. Ribeiro
 */
public class Util
{
	public static Random _random = new Random();
	private static final SimpleDateFormat PACKET_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss SSSS");

	public static String formatPacketTime(long d)
	{
		return PACKET_TIME_FORMAT.format(d);
	}

	public static int positiveRandom()
	{
		int i = _random.nextInt();
		if(i < 0)
			i = -i;
		return i;
	}
	/**
	 * Returns the unsigned value of a byte
	 *
	 * @param b witch u want to convert
	 * @return
	 */
	public static int byteToUInt(byte b)
	{
		return b & 0xFF;
	}


	public static String zeropad(String number, int size)
	{
		if (number.length() >= size)
		{
			return number;
		}
		return repeat("0", size - number.length()) + number;
	}


	/**
	 * Returns the hex dump of the given byte array
	 * as 16 bytes per line
	 *
	 * @param b the byte array
	 * @return A string with the hex dump
	 */
	public static String hexDump(byte[] b)
	{
		if (b == null)
		{
			return "";
		}
		StringBuffer buf = new StringBuffer();
		int size = b.length;
		for (int i = 0; i < size; i++)
		{
			if ((i + 1) % 16 == 0)
			{
				buf.append(zeropad(Integer.toHexString(byteToUInt(b[i])).toUpperCase(), 2));
				buf.append("\n");
			}
			else
			{
				buf.append(zeropad(Integer.toHexString(byteToUInt(b[i])).toUpperCase(), 2));
				buf.append(" ");
			}
		}
		return buf.toString();
	}

	public static String repeat(String str, int repeat)
	{
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < repeat; i++)
		{
			buf.append(str);
		}

		return buf.toString();
	}

	public static int parseNumber(byte[] data)
	{
		int size = data.length;
		int output = 0;
		int i = 0;
		while (byteToUInt(data[i]) == 0)
		{
			size--;
		}
		for (; i < size; i++)
		{
			output = output + byteToUInt(data[i]) * 256 ^ i;
		}
		return output;
	}

	public static String printData(byte[] data, int len)
	{
		StringBuilder result = new StringBuilder();

		int counter = 0;

		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				result.append(fillHex(i, 4) + ": ");
			}

			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");

				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}

				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}

			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}

			result.append("\n");
		}


		return result.toString();
	}

	public static String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);

		for (int i = number.length(); i < digits; i++)
		{
			number = "0" + number;
		}

		return number;
	}

	public static String toAnsci(byte[] data, int from, int to)
	{
		StringBuilder result = new StringBuilder();

		for (int i = from; i < to; i++)
		{
			int t1 = data[i];
			if (t1 > 0x1f && t1 < 0x80)
			{
				result.append((char) t1);
			}
			else
			{
				result.append('.');
			}
		}
		return result.toString();
	}

	public static String printData(ByteBuffer blop)
	{
		return printData(blop.array());
	}

	public static String printData(NioBuffer blop)
	{
		return printData(blop.array());
	}


	public static String printData(byte[] blop)
	{
		return printData(blop, blop.length);
	}
}
