package com.tscloud.common.framework.rest.view;

import java.io.Serializable;
import java.util.List;

public class Page
  implements Serializable
{
  private int pageSize = 10;
  private int pageNumber = 1;
  private int total = 0;
  private int totalPageNum = 0;
  private int startRowNum = 0;
  private int endRowNum = 0;
  private Object objCondition;
  private List rows;

  public Page()
  {
  }

  public Page(int pageSize, int total)
  {
    this.pageSize = pageSize;
    this.total = total;
    int mod = total % pageSize;
    this.totalPageNum = (mod == 0 ? total / pageSize : total / pageSize + 1);
    if (this.startRowNum <= 0) {
      this.startRowNum = 0;
      this.endRowNum = pageSize;
    }
  }

  public int getPageSize() {
    return this.pageSize;
  }
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  public int getPageNumber() {
    return this.pageNumber;
  }
  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
    this.startRowNum = (this.pageSize * (pageNumber - 1));
    this.endRowNum = (this.pageSize * pageNumber);
  }

  public int getTotal() {
    return this.total;
  }
  public void setTotal(int total) {
    this.total = total;
    int mod = total % this.pageSize;
    this.totalPageNum = (mod == 0 ? total / this.pageSize : total / this.pageSize + 1);
    if (this.startRowNum <= 0) {
      this.startRowNum = 0;
      this.endRowNum = this.pageSize;
    }
  }

  public int getTotalPageNum() {
    return this.totalPageNum;
  }
  public void setTotalPageNum(int totalPageNum) {
    this.totalPageNum = totalPageNum;
  }
  public int getStartRowNum() {
    return this.startRowNum;
  }

  public void setStartRowNum(int startRowNum) {
    this.startRowNum = (this.pageSize * (this.pageNumber - 1));
  }

  public int getEndRowNum() {
    this.endRowNum = (this.pageSize * this.pageNumber);
    return this.endRowNum;
  }
  public void setEndRowNum(int endRowNum) {
    this.endRowNum = endRowNum;
  }

  public void setObjCondition(Object objCondition) {
    this.objCondition = objCondition;
  }

  public Object getObjCondition() {
    return this.objCondition;
  }

  public void setRows(List rows) {
    this.rows = rows;
  }

  public List getRows() {
    return this.rows;
  }
}