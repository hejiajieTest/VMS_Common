package com.tscloud.common.framework.mapper;

import java.util.List;
import java.util.Map;

public interface BaseInterfaceMapper<Entity>
{
  public void save(Entity paramEntity);

  public void update(Entity paramEntity);

  public void deleteById(String paramString);

  public Entity findById(String paramString);

  public List<Entity> findAll();

  public List<Entity> findByMap(Map<String, Object> paramMap);

  public Integer getCount(Map<String, Object> paramMap);

  public List<Entity> findByPage(Map<String, Object> paramMap);

  public List<Entity> findByName(Map<String, Object> paramMap);
}