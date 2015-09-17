package com.auction.resume.model;

public class Projects {
	private int id;
	private String resumeId;
	private String name; //项目名称
	private String startTime; //
	private String endTime;
	private String softwares; //软件环境
	private String hardwares; //硬件环境
	private String projectDetails; //项目描述
	private String responsibilities; //责任描述
	private String applicationTechnology; //应用技术
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getResumeId() {
		return resumeId;
	}
	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSoftwares() {
		return softwares;
	}
	public void setSoftwares(String softwares) {
		this.softwares = softwares;
	}
	public String getHardwares() {
		return hardwares;
	}
	public void setHardwares(String hardwares) {
		this.hardwares = hardwares;
	}
	public String getProjectDetails() {
		return projectDetails;
	}
	public void setProjectDetails(String projectDetails) {
		this.projectDetails = projectDetails;
	}
	public String getResponsibilities() {
		return responsibilities;
	}
	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}
	public String getApplicationTechnology() {
		return applicationTechnology;
	}
	public void setApplicationTechnology(String applicationTechnology) {
		this.applicationTechnology = applicationTechnology;
	}
	
}
