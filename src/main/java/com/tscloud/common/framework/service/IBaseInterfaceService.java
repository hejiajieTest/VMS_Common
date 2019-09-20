package com.tscloud.common.framework.service;

import com.tscloud.common.framework.Exception.ServiceException;
import com.tscloud.common.framework.domain.TrackableEntity;
import com.tscloud.common.framework.rest.view.Page;
import java.util.List;
import java.util.Map;

public abstract interface IBaseInterfaceService<Entity extends TrackableEntity>
{
  public abstract Page findByPage(Page paramPage, Map<String, Object> paramMap)
    throws ServiceException;

  public abstract String save(Entity paramEntity)
    throws ServiceException;

  public abstract boolean update(Entity paramEntity)
    throws ServiceException;

  public abstract boolean deleteById(String paramString)
    throws ServiceException;

  public abstract Entity findById(String paramString)
    throws ServiceException;

  public abstract List<Entity> findAll()
    throws ServiceException;

  public abstract List<Entity> findByMap(Map<String, Object> paramMap)
    throws ServiceException;

}