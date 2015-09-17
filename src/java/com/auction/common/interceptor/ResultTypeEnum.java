package com.auction.common.interceptor;

/**
 * After authentication fails returned data format
 * @author ISEC
 *
 */
public enum ResultTypeEnum {
	//forbidden and redirect to html page
	HTML,
	//forbidden and return json data
	JSON,
	//file or other
	BINAARY
	}