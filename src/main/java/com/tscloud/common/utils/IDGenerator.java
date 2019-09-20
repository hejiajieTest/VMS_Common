package com.tscloud.common.utils;

import java.io.PrintStream;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
//import org.apache.commons.lang.time.DateFormatUtils;

public class IDGenerator
{
  private static int COUNT = 0;

  private static IDGenerator generator = new IDGenerator();

  public static String getID()
  {
    UUID uid = UUID.randomUUID();
    String id = uid.toString();
    id = id.toUpperCase();
    id = id.replace("-", "");
    return id;
  }

  public static IDGenerator getGenerator()
  {
    return generator;
  }

  public static String getRandomChar()
  {
    int maxNum = 25;

    int count = 0;
    char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    StringBuffer pwd = new StringBuffer("");
    Random r = new Random();
    while (count < 32)
    {
      int i = Math.abs(r.nextInt(25));
      if ((i >= 0) && (i < str.length)) {
        pwd.append(str[i]);
        count++;
      }
    }
    return pwd.toString().toUpperCase();
  }

  public static synchronized String getUniqueID()
  {
    long millis = System.currentTimeMillis();
    return num2Str(millis, 15) + num2Str(++COUNT, 11);
  }

  public static synchronized String getDateUniqueID() {
    String uniqueStr = num2Str(17) + num2Str(++COUNT, 2);

    return uniqueStr;
  }

  public static String num2Str(long number, int width)
  {
    String numStr = String.valueOf(number);

    int len = numStr.length() - width;
    if (len > 0) {
      numStr = numStr.substring(len);
    }

    width -= numStr.length();
    StringBuffer zeroBuff = new StringBuffer();
    while (zeroBuff.length() < width) {
      zeroBuff.append("0");
    }
    return zeroBuff.toString() + numStr;
  }

  public static String num2Str(int width)
  {
	  String numStr ="";
    //String numStr = DateFormatUtils.format(new Date(), "HHmmssS");
    width -= numStr.length();
    StringBuffer zeroBuff = new StringBuffer();
    while (zeroBuff.length() < width) {
      zeroBuff.append("0");
    }
    return zeroBuff.toString() + numStr;
  }

  public static void main(String[] args)
  {
    for (int i = 0; i < 100; i++) {
      getGenerator(); getGenerator(); System.out.println(getRandomChar() + "   " + getID() + "     " + getUniqueID());
    }
  }

  public void test()
  {
    for (int i = 0; i < 500; i++) {
      Thread thread = new Thread(new IDgen());
      thread.start();
    }
  }
  class IDgen implements Runnable {
    IDgen() {
    }

    public void run() {
      System.out.println(Thread.currentThread().getName() + "--ID:" + 
        IDGenerator.getDateUniqueID());
    }
  }
}