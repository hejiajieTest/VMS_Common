package com.tscloud.common.framework.rest.view;

public class JsonViewObject
{
  private String status;
  private String message;
  private String content;

  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getMessage() {
    return this.message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getContent() {
    return this.content;
  }
  public void setContent(String content) {
    this.content = content;
  }

  public JsonViewObject success()
  {
    setContent("true");
    setStatus("success");
    return this;
  }

  public JsonViewObject successPack(String result) {
    setMessage("");
    setContent(result);
    setStatus("success");
    return this;
  }

  public JsonViewObject successPack(String result, String msg) {
    setContent(result);
    setMessage(msg);
    setStatus("success");
    return this;
  }

  public JsonViewObject failPack(Exception e) {
    String message = e.getMessage();
    int index = message.indexOf(":");
    setMessage(index == -1 ? message : message.substring(index + 1));
    setContent("");
    setStatus("fail");
    return this;
  }

  public JsonViewObject failPack(String errMsg) {
    setMessage(errMsg);
    setContent("");
    setStatus("fail");
    return this;
  }

  public JsonViewObject failPack(String result, String errMsg) {
    setMessage(errMsg);
    setContent(result);
    setStatus("fail");
    return this;
  }

  public JsonViewObject failPackMessage(String errMsg, String content) {
    setMessage(errMsg);
    setContent(content);
    setStatus("fail");
    return this;
  }
}