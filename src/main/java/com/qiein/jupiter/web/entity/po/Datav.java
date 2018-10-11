package com.qiein.jupiter.web.entity.po;
/**
 * author HanJF
 * 管理员日志
 */
public class Datav {
	 /**
     * Id
     */
    private Integer Id;
    /**
     * 图片地址
     */
    private String img;
    /**
     * 地址跳转
     */
    private String  url;
    /**
     * 大屏名字
     */
    private String name ;
    
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
 
}
