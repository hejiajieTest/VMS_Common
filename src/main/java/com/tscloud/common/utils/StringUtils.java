package com.tscloud.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtils
{
  private static final Log log = LogFactory.getLog(StringUtils.class);

  public static String GBKToUTF(String str) { String utfStr = null;
    try {
      utfStr = new String(str.getBytes("GBK"), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return utfStr; }

  public static String UTFToGBK(String str)
  {
    String utfStr = null;
    try {
      utfStr = new String(str.getBytes("UTF-8"), "GBK");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return utfStr;
  }

  public static String ISOToGBK(String str) {
    String utfStr = null;
    try {
      utfStr = new String(str.getBytes("ISO8859_1"), "GBK");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return utfStr;
  }

  public static String getPasWordStr(String password) {
    String passStr = "";
    MessageDigest digester = null;
    try {
      digester = MessageDigest.getInstance("SHA");
      digester.update(password.getBytes("utf-8"));
      byte[] bytes = digester.digest();
      //passStr = new String(Hex.encodeHex(bytes));
    } catch (Exception e) {
      log.error("解密失败！", e);
    }
    return passStr;
  }

  public static Map<String, String> strToMap(String str)
  {
    Map map = new HashMap();
    if (!isBlank(str)) {
      String strObj = str.substring(1, str.length() - 1);
      String[] obj = strObj.split(",");
      for (String value : obj) {
        String[] key = value.split(":");
        map.put(key[0], key[1]);
      }
    }
    return map;
  }

  public static String mapToStr(Map<String, String> map)
  {
    String str = "";
    if (map != null) {
      for (Map.Entry obj : map.entrySet()) {
        String key = (String)obj.getKey();
        String value = (String)obj.getValue();
        str = new StringBuilder().append(str).append(key).append(":").append(value).append(",").toString();
      }
    }
    if (str.length() > 0) {
      str = new StringBuilder().append("{").append(str.substring(0, str.length() - 1)).append("}").toString();
    }
    return str;
  }

  public static boolean isBlank(String s)
  {
    return s == null;
  }

  public static String join(String[] s, String seperator)
  {
    return join(s, seperator, true, true);
  }

  public static String join(String[] s, String seperator, boolean ignoreBlank, boolean ignoreNull)
  {
    if ((s == null) || (seperator == null))
      throw new NullPointerException();
    StringBuilder result = new StringBuilder(256);
    for (String s_ : s) {
      if ((ignoreNull) && (s_ == null))
        continue;
      if ((ignoreBlank) && (s_.trim().length() == 0))
        continue;
      result.append(s_);
      result.append(seperator);
    }
    int i = result.length();
    if (i > 0) {
      return result.substring(0, i - seperator.length());
    }
    return "";
  }

  public static String camelToCapital(String s)
  {
    String pattern = "[A-Z]*[a-z0-9]+|[A-Z0-9]+";
    Pattern p = Pattern.compile("[A-Z]*[a-z0-9]+|[A-Z0-9]+");
    Matcher m = p.matcher(s);
    String r = null;
    while (m.find()) {
      if (r != null) {
        r = new StringBuilder().append(r).append("_").append(m.group().toUpperCase()).toString(); continue;
      }
      r = m.group().toUpperCase();
    }
    return r;
  }

  public static String capitalToCamel(String s)
  {
    String[] tokens = s.split("_");
    String r = tokens[0].toLowerCase();
    for (int i = 1; i < tokens.length; i++)
    {
      r = new StringBuilder().append(r).append(tokens[i].substring(0, 1))
        .append(tokens[i].substring(1)
        .toLowerCase()).toString();
    }
    return r;
  }

  public static String substring(String target, int maxBytes)
  {
    return substring(target.trim(), Charset.defaultCharset().name(), maxBytes, "...");
  }

  public static String substring(String target, String charset, int maxBytes, String append)
  {
    try
    {
      int count = getBytes(target, charset).length;
      if (count <= maxBytes) {
        return target;
      }
      int bytesCount = 0;
      char[] replace = new char[getBytes(append, charset).length];
      int j = 0;
      int bound = maxBytes - getBytes(append, charset).length;
      for (int i = 0; i < target.length(); i++) {
        char c = target.charAt(i);
        bytesCount = c > 'ÿ' ? bytesCount + 2 : bytesCount + 1;
        if (bytesCount > maxBytes) {
          return target.substring(0, i - j).concat(append);
        }
        if (bytesCount > bound)
          replace[(j++)] = c;
      }
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
    throw new RuntimeException("Unreachable!");
  }

  private static byte[] getBytes(String s, String charset) throws UnsupportedEncodingException
  {
    return s.getBytes(charset);
  }

  public static String trim(String str) {
    str = str.replace('　', ' ');
    return str.trim();
  }

  public static boolean isNotBlank(String[] strs)
  {
    boolean isNotNull = true;
    if ((strs == null) || (strs.length == 0)) {
      return false;
    }

    for (String str : strs) {
      if (isBlank(str)) {
        isNotNull = false;
        break;
      }
    }
    return isNotNull;
  }

  public static boolean contains(String orig, String targ) {
    return orig.toLowerCase().contains(targ.toLowerCase());
  }

  public static boolean equals(String str, String tar)
  {
    return (isNotBlank(new String[] { str, tar })) && (str.toLowerCase().equals(tar.toLowerCase()));
  }

  public static String substringAfterLast(String str, String separator) {
    if (!isNotBlank(new String[] { str, separator })) {
      return "";
    }
    int pos = str.lastIndexOf(separator);
    return (pos != -1) && (pos != str.length() - separator.length()) ? str.substring(pos + separator.length()) : "";
  }
}