package com.tscloud.common.framework.logcache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tscloud.common.utils.StringUtils;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogCache
{
  protected Logger log = LoggerFactory.getLogger(getClass());

  private static LoadingCache<String, String> logCache = null;
  private static LogCache localCacheTool = null;

  public static synchronized LogCache init()
  {
    if (localCacheTool == null) {
      localCacheTool = new LogCache();
    }
    if (logCache == null) {
      initLogCache();
    }
    return localCacheTool;
  }

  private static LoadingCache<String, String> initLogCache()
  {
    if (logCache == null)
    {
      logCache = CacheBuilder.newBuilder()
        .concurrencyLevel(8)
        .initialCapacity(10)
        .maximumSize(100000L)
        .build(new CacheLoader()
      {
        public String load(String key) throws Exception
        {
          String str = new String();
          return str;
        }

		@Override
		public Object load(Object paramK) throws Exception {
			 String str = new String();
	          return str;
		} } );
    }
    return logCache;
  }

  public String getValue(String key)
  {
    String msg = null;
    try {
      msg = (String)logCache.get(key);
    } catch (ExecutionException e) {
      this.log.error("获取缓存数据失败！", e);
    }
    return msg;
  }

  public void putValue(String key, String message)
  {
    try
    {
      Object object = logCache.get(key);
      if (object == null) {
        logCache.put(key, message);
      } else {
        logCache.invalidate(key);
        logCache.put(key, message);
      }
    } catch (ExecutionException e) {
      this.log.error("获取缓存数据失败！", e);
    }
  }

  public void delValue(Object key)
  {
    logCache.invalidate(key);
  }

  public static void main(String[] org)
  {
    String jStr = "{\"objCondition\":{\"name\":\"\",\"type\":\"\",\"orgId\":\"1\"},\"pageNumber\":1,\"pageSize\":10}";
    jStr = jStr.replaceAll("\"", "'");
    System.out.print(jStr);
  }

  public List<String> getList(String pattern)
  {
    List list = new ArrayList();
    if (StringUtils.isBlank(pattern)) {
      return list;
    }

    Map<String,String> keys = logCache.asMap();
    if ((keys == null) || (keys.isEmpty())) {
      return list;
    }

    for (String key : keys.keySet()) {
      if (key.contains(pattern)) {
        list.add(getValue(key) + "|" + key);
      }
    }
    return list;
  }
}