package com.tscloud.common.framework.service.impl;

import com.tscloud.common.framework.Exception.ServiceException;
import com.tscloud.common.framework.domain.TrackableEntity;
import com.tscloud.common.framework.mapper.BaseInterfaceMapper;
import com.tscloud.common.framework.rest.view.Page;
import com.tscloud.common.framework.service.IBaseInterfaceService;
import com.tscloud.common.utils.IDGenerator;
import com.tscloud.common.utils.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseInterfaceServiceImpl<Entity extends TrackableEntity>
  implements IBaseInterfaceService<Entity>
{
  protected Logger log = LoggerFactory.getLogger(getClass());

  @Context
  protected HttpServletResponse response;

  @Context
  protected HttpServletRequest request;

  public Page findByPage(Page page, Map<String, Object> map) throws ServiceException { try { page.setTotal(getBaseInterfaceMapper().getCount(map).intValue());
      map.put("startRowNum", Integer.valueOf(page.getStartRowNum()));
      map.put("pageSize", Integer.valueOf(page.getPageSize()));
      map.put("endRowNum", Integer.valueOf(page.getEndRowNum()));
      page.setRows(getBaseInterfaceMapper().findByPage(map));
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return page; }

  public String save(Entity entity) throws ServiceException
  {
    String id = IDGenerator.getID();
    try {
      if ((StringUtils.isBlank(entity.getId())) || (entity.getId().equalsIgnoreCase("0"))) {
        entity.setId(id);
      }
      if (entity.getId().equalsIgnoreCase("-1")) {
        entity.setId("0");
      }
      entity.setCreateDate(new Date());
      getBaseInterfaceMapper().save(entity);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return id;
  }

  public boolean update(Entity entity) throws ServiceException {
    try {
      entity.setUpdateDate(new Date());
      getBaseInterfaceMapper().update(entity);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return true;
  }

  public boolean deleteById(String id) throws ServiceException {
    try {
      getBaseInterfaceMapper().deleteById(id);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return true;
  }

  public Entity findById(String id) throws ServiceException {
    TrackableEntity entity = null;
    try {
      entity = (TrackableEntity)getBaseInterfaceMapper().findById(id);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return (Entity) entity;
  }

  public List<Entity> findAll() throws ServiceException {
    List list = null;
    try {
      list = getBaseInterfaceMapper().findAll();
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return list;
  }

  public List<Entity> findByMap(Map<String, Object> map) throws ServiceException {
    List list = null;
    try {
      list = getBaseInterfaceMapper().findByMap(map);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e);
    }
    return list;
  }

  public abstract BaseInterfaceMapper<Entity> getBaseInterfaceMapper();
}