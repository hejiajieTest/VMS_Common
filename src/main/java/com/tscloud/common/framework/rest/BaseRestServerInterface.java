package com.tscloud.common.framework.rest;

import com.tscloud.common.framework.Exception.DubboProviderException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public interface BaseRestServerInterface
{
  @POST
  @Path("getPage")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String getPage(String paramString);

  @GET
  @Path("getAll")
  @Produces({"application/json"})
  public String getAll();

  @POST
  @Path("getByWhere")
  @Consumes({"application/json"})
  public String getByWhere(String paramString);

  @GET
  @Path("getById")
  public String getById(@QueryParam("id") String paramString);

  @GET
  @Path("getByName")
  public String getByName(@QueryParam("name") String paramString);

  @GET
  @Path("deleteById")
  public String deleteById(@QueryParam("id") String paramString);

  @GET
  @Path("deleteByIds")
  public String deleteByIds(@QueryParam("ids") String paramString)
    throws DubboProviderException;

  @POST
  @Path("save")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String save(String paramString);

  @POST
  @Path("update")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public String update(String paramString);

  @GET
  @Path("getTreeData")
  public String getTreeData(@QueryParam("orgId") String paramString);
}