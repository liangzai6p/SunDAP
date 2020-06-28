package com.sunyard.ars.system.pojo.mc;

public class DataSource {
    /**
     *数据源编号
     */
    private Integer id;

    /**
     *	数据源名
     */
    private String name;

    /**
     * 连接类型0：ODBC；1：JDBC
     */
    private String type;

    /**
     * 驱动名称
     */
    private String driver;

    /**
     * 连接串
     */
    private String connectStr;

    /**
     * 用户名
     */
    private String uname;

    /**
     * 密码
     */
    private String pwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver == null ? null : driver.trim();
    }

    public String getConnectStr() {
        return connectStr;
    }

    public void setConnectStr(String connectStr) {
        this.connectStr = connectStr == null ? null : connectStr.trim();
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getPwd() {
        return pwd;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }
}