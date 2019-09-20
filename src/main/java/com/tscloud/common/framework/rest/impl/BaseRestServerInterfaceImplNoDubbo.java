package com.tscloud.common.framework.rest.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tscloud.common.framework.domain.TrackableEntity;
import com.tscloud.common.framework.logcache.LogCache;
import com.tscloud.common.framework.rest.BaseRestServerInterface;
import com.tscloud.common.framework.rest.view.JsonViewObject;
import com.tscloud.common.framework.rest.view.Page;
import com.tscloud.common.framework.service.IBaseInterfaceService;
import com.tscloud.common.utils.BeanObjectToMap;
import com.tscloud.common.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRestServerInterfaceImplNoDubbo<Entity extends TrackableEntity>
  implements BaseRestServerInterface
{
  protected Logger log;
  protected JsonViewObject jsonView;

  @Context
  protected HttpServletResponse response;

  @Context
  protected HttpServletRequest request;
  protected String modelName;
  protected String systemName;
  protected String userName;

  public BaseRestServerInterfaceImplNoDubbo()
  {
    this.log = LoggerFactory.getLogger(getClass());
    this.jsonView = new JsonViewObject();

    getBaseInterface();
  }

  protected void addOperationLog(String operation, String paramJson, boolean status, String msg)
  {
    String name = this.modelName;
    String sysName = this.systemName;
    String createDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

    String jsonStr = new StringBuilder().append("{'name':'").append(name).append("','status':'")
      .append(String.valueOf(status))
      .append("','description':'").append(operation).append("===============").append(msg).append("','createDate':'").append(createDate).append("','operator':'")
      .append(StringUtils.isBlank(this.userName)
       ? "admin" : this.userName).append("','param':'").append(paramJson).append("'}").toString();

    LogCache cache = LogCache.init();

    UUID uid = UUID.randomUUID();
    String logKey = new StringBuilder().append(sysName).append(".sysLog").append(uid.toString()).toString();

    cache.putValue(logKey, jsonStr);
  }

  protected void addOperationLog(String optCotent, String param, boolean status)
  {
    JSONObject data = new JSONObject();
    data.put("name", this.modelName);
    data.put("description", optCotent);
    data.put("status", Boolean.valueOf(status));
    data.put("operator", StringUtils.isBlank(this.userName) ? "admin" : this.userName);
    data.put("createDate", new Date());
    data.put("param", param);
    LogCache cache = LogCache.init();
    UUID uid = UUID.randomUUID();
    String logKey = new StringBuilder().append(this.systemName).append(".sysLog").append(uid.toString()).toString();
    cache.putValue(logKey, data.toJSONString());
  }

  public String getPage(String jsonStr)
  {
    String result = "";
    Page page = null;
    try {
      Map mapBean = new HashMap();
      if (!StringUtils.isBlank(jsonStr)) {
        page = (Page)JSON.parseObject(jsonStr, Page.class);
        if (page != null) {
          TrackableEntity entity = null;
          String objCondition = page.getObjCondition().toString();
          if ((StringUtils.isNotBlank(new String[] { objCondition })) && (!"{}".equalsIgnoreCase(objCondition))) {
            entity = (TrackableEntity)JSON.parseObject(objCondition, getEntityClass());
          }
          mapBean = BeanObjectToMap.convertBean(entity);
        }
      } else {
        page = new Page();
      }
      page = getBaseInterface().findByPage(page, mapBean);
      result = JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[] { SerializerFeature.WriteMapNullValue });
      this.jsonView.successPack(result);
      addOperationLog("分页查询", jsonStr, true);
    } catch (Exception e) {
      this.jsonView.failPack(e);
      addOperationLog("分页查询", jsonStr, false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl getPage is error,{jsonStr:").append(jsonStr).append("}").toString(), e);
    }
    result = JSON.toJSONString(this.jsonView);
    return result;
  }

  public String getAll()
  {
    String result = "";
    try {
      List list = getBaseInterface().findAll();
      result = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
      addOperationLog("查询所有数据", "", true);
      this.jsonView.successPack(result);
    } catch (Exception e) {
      this.jsonView.failPack(e);
      addOperationLog("查询所有数据", "", false);
      this.log.error("BaseRestServerInterfaceImpl getAll is error", e);
    }
    result = JSON.toJSONString(this.jsonView);
    return result;
  }

  public String getByWhere(String jsonStr)
  {
    String result = "";
    try {
      TrackableEntity entity = (TrackableEntity)JSON.parseObject(jsonStr, getEntityClass());
      Map mapBean = BeanObjectToMap.convertBean(entity);
      List list = getBaseInterface().findByMap(mapBean);
      if (list != null) {
        result = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
        addOperationLog("条件查询", jsonStr, true);
      }
      this.jsonView.successPack(result);
    } catch (Exception e) {
      this.jsonView.failPack(e);
      addOperationLog("条件查询", jsonStr, false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl getByWhere is error，{jsonStr:").append(jsonStr).append("}").toString(), e);
    }
    result = JSON.toJSONString(this.jsonView);
    return result;
  }

  public String getById(String id)
  {
    String result = "";
    try {
      if (StringUtils.isNotBlank(new String[] { id })) {
        TrackableEntity entity = getBaseInterface().findById(id);
        if (entity != null) {
          result = JSON.toJSONStringWithDateFormat(entity, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
          addOperationLog("条件查询", new StringBuilder().append("id=").append(id).toString(), true);
        }
        this.jsonView.successPack(result);
      }
    } catch (Exception e) {
      this.jsonView.failPack(e);
      addOperationLog("条件查询", new StringBuilder().append("id=").append(id).toString(), false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl getById is error,{id:").append(id).append("}").toString(), e);
    }
    result = JSON.toJSONString(this.jsonView);
    return result;
  }

  public String getByName(String name)
  {
    String result = "";
    Map map = new HashMap();
    map.put("name", name);
    try {
      List list = getBaseInterface().findByMap(map);
      if ((list != null) && (!list.isEmpty())) {
        result = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
        addOperationLog("按名称查询", new StringBuilder().append("name=").append(name).toString(), true);
      }
      this.jsonView.successPack(result);
    } catch (Exception e) {
      this.jsonView.failPack(e);
      addOperationLog("按名称查询", new StringBuilder().append("name=").append(name).toString(), false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl getByName is error,{Name:").append(name).append("}").toString(), e);
    }
    result = JSON.toJSONString(this.jsonView);
    return result;
  }

  public String deleteById(String id)
  {
    boolean flag = false;
    try {
      flag = getBaseInterface().deleteById(id);
      this.jsonView.successPack(JSON.toJSONString(Boolean.valueOf(flag)));
      addOperationLog("删除数据", new StringBuilder().append("id=").append(id).toString(), true);
    } catch (Exception e) {
      this.jsonView.failPack(JSON.toJSONString(Boolean.valueOf(flag)));
      addOperationLog("删除数据", new StringBuilder().append("id=").append(id).toString(), false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl deleteById is error,{Id:").append(id).append("}").toString(), e);
    }
    return JSON.toJSONString(this.jsonView);
  }

  public String deleteByIds(String ids)
  {
    boolean flag = true;
    int count = 0;
    try {
      if (ids != null) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
          flag = getBaseInterface().deleteById(id);
          if (!flag) {
            continue;
          }
          count++;
        }
      }

      if (count > 0) {
        this.jsonView.successPack("true", "删除数据成功!");
        addOperationLog("删除数据", new StringBuilder().append("ids=").append(ids).toString(), false);
      } else {
        this.jsonView.successPack("false", "删除数据");
        addOperationLog("删除数据", new StringBuilder().append("ids=").append(ids).toString(), false);
      }
    } catch (Exception e) {
      this.jsonView.failPack("false", "删除数据失败!");
      addOperationLog("删除数据", new StringBuilder().append("id=").append(ids).toString(), false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl deleteByIds is error,{Id:").append(ids).append("}").toString(), e);
    }
    return JSON.toJSONString(this.jsonView);
  }

  public String save(String jsonStr)
  {
    String result = "";
    try {
      TrackableEntity entity = (TrackableEntity)JSON.parseObject(jsonStr, getEntityClass());
      if (entity != null) {
        result = getBaseInterface().save((Entity) entity);
        this.jsonView.successPack(result);
        addOperationLog("保存数据", new StringBuilder().append("jsonStr=").append(jsonStr).toString(), true);
      }
    } catch (Exception e) {
      String message = e.getMessage();
      if (StringUtils.isBlank(message)) message = "保存数据失败！";
      this.jsonView.failPack("false", message);
      addOperationLog("保存数据", new StringBuilder().append("jsonStr=").append(jsonStr).toString(), false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl save is error,{jsonStr:").append(jsonStr).append("},").append(e.getMessage()).toString(), e);
    }
    result = JSON.toJSONString(this.jsonView);
    return result;
  }

  public String update(String jsonStr)
  {
    String result = "";
    try {
      TrackableEntity entity = (TrackableEntity)JSON.parseObject(jsonStr, getEntityClass());
      if (entity != null) {
        result = String.valueOf(getBaseInterface().update((Entity) entity));
        this.jsonView.successPack(result);
        addOperationLog("更新数据", new StringBuilder().append("jsonStr=").append(jsonStr).toString(), true);
      }
    } catch (Exception e) {
      String message = e.getMessage();
      if (StringUtils.isBlank(message)) message = "更新数据失败！";
      this.jsonView.failPack("false", message);
      addOperationLog("更新数据", new StringBuilder().append("jsonStr=").append(jsonStr).toString(), false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl update is error,{jsonStr:").append(jsonStr).append("},").append(e.getMessage()).toString(), e);
    }
    result = JSON.toJSONString(this.jsonView);
    return result;
  }

  public String getTreeData(String orgId)
  {
    String result = "";
    try {
      Map mapBean = new HashMap();
      mapBean.put("orgId", orgId);
      List list = getBaseInterface().findByMap(mapBean);
      if (list != null) {
        result = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
      }
      this.jsonView.successPack(result);
      addOperationLog("条件查询", orgId, true);
    } catch (Exception e) {
      this.jsonView.failPack(e);
      addOperationLog("条件查询", orgId, false);
      this.log.error(new StringBuilder().append("BaseRestServerInterfaceImpl getTreeData is error，orgId:").append(orgId).append("").toString(), e);
    }
    return JSON.toJSONString(this.jsonView);
  }

  public Class<Entity> getEntityClass()
  {
    return (Class)((java.lang.reflect.ParameterizedType)getClass()
      .getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public abstract IBaseInterfaceService<Entity> getBaseInterface();
}