package com.iyanda.entity;

import java.util.Date;

public class TableIp {

	private int id ;
	
	private String ip ;
	
	private String port ;
	
	private String country ;
	
	private String province ;
	
	private String city ;
	
	private String isp ;
	
	private Date findTime ;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public Date getFindTime() {
		return findTime;
	}

	public void setFindTime(Date findTime) {
		this.findTime = findTime;
	}
	
	
}
