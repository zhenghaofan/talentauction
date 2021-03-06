package com.auction.wechat.event;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auction.util.SysConfig;
import com.auction.wechat.Util.CommonUtil;
import com.auction.wechat.Util.MenuUtil;
import com.auction.wechat.model.req.Token;
import com.auction.wechat.widget.Button;
import com.auction.wechat.widget.ClickButton;
import com.auction.wechat.widget.ComplexButton;
import com.auction.wechat.widget.Menu;
import com.auction.wechat.widget.ViewButton;



/**
 * 菜单管理器类
* @author tobber
 * @date 2015-01-10
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	/**
	 * 定义菜单结构
	 * @return
	 */
	private static Menu getMenu() {
		
		ViewButton btn12 = new ViewButton();
		btn12.setName("我的Offer");
		btn12.setType("view");
		btn12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+SysConfig.getValue("WECHAT_APPID").toString()+"&redirect_uri="+SysConfig.getValue("SERVICE_URL").toString()+"/wechat/wechatRoute&response_type=code&scope=snsapi_base&state=jobbidlist#wechat_redirect");
		
		ViewButton btn13 = new ViewButton();
		btn13.setName("简历状态");
		btn13.setType("view");
		btn13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+SysConfig.getValue("WECHAT_APPID").toString()+"&redirect_uri="+SysConfig.getValue("SERVICE_URL").toString()+"/wechat/wechatRoute&response_type=code&scope=snsapi_base&state=jobdefault#wechat_redirect");
		
		ClickButton btn14 = new ClickButton();
		btn14.setName("简历下架");
		btn14.setType("click");
		btn14.setKey("offShelves");
		
		ViewButton btn21 = new ViewButton();
		btn21.setName("最新人才");
		btn21.setType("view");
		btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+SysConfig.getValue("WECHAT_APPID").toString()+"&redirect_uri="+SysConfig.getValue("SERVICE_URL").toString()+"/wechat/wechatRoute&response_type=code&scope=snsapi_base&state=placelist#wechat_redirect");
		
		ViewButton btn22 = new ViewButton();
		btn22.setName("邀请记录");
		btn22.setType("view");
		btn22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+SysConfig.getValue("WECHAT_APPID").toString()+"&redirect_uri="+SysConfig.getValue("SERVICE_URL").toString()+"/wechat/wechatRoute&response_type=code&scope=snsapi_base&state=compbidlist#wechat_redirect");
	
		ViewButton btn23 = new ViewButton();
		btn23.setName("公司主页");
		btn23.setType("view");
		btn23.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+SysConfig.getValue("WECHAT_APPID").toString()+"&redirect_uri="+SysConfig.getValue("SERVICE_URL").toString()+"/wechat/wechatRoute&response_type=code&scope=snsapi_base&state=compdefault#wechat_redirect");

		ViewButton btn24 = new ViewButton();
		btn24.setName("实力拍Plus");
		btn24.setType("view");
		btn24.setUrl("http://www.shilipai.net/base/plusspread");
		
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("我是人才");
		mainBtn1.setSub_button(new Button[] {btn12, btn13, btn14});
		
		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("我是企业");
		mainBtn2.setSub_button(new Button[] {btn24, btn21, btn22, btn23});
		
		/**
		ClickButton btn3 = new ClickButton();
		btn3.setName("登陆");
		btn3.setType("click");
		btn3.setKey("userlogin");
		*/
		
		ViewButton btn31 = new ViewButton();
		btn31.setName("登录");
		btn31.setType("view");
		btn31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+SysConfig.getValue("WECHAT_APPID").toString()+"&redirect_uri="+SysConfig.getValue("SERVICE_URL").toString()+"/wechat/wechatRoute&response_type=code&scope=snsapi_base&state=login#wechat_redirect");
	
		ViewButton btn32 = new ViewButton();
		btn32.setName("注册");
		btn32.setType("view");
		btn32.setUrl("http://www.shilipai.net/mobile/sltsign");

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("账号");
		mainBtn3.setSub_button(new Button[] { btn31, btn32});
		
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

		return menu;
	}

	public static void main(String[] args) {
		
		// 第三方用户唯一凭证
		String appId = SysConfig.getValue("WECHAT_APPID").toString();
		// 第三方用户唯一凭证密钥
		String appSecret = SysConfig.getValue("WECHAT_APPSECRET").toString();
		// 调用接口获取凭证
		Token token = CommonUtil.getToken(appId, appSecret);
		if (null != token) {
			// 创建菜单
			boolean result = MenuUtil.createMenu(getMenu(), token.getAccessToken());
			// 判断菜单创建结果
			if (result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败！");
		}
		
		/*
		// 第三方用户唯一凭证
		String appId = SysConfig.getValue("WECHAT_APPID").toString();
		// 第三方用户唯一凭证密钥
		String appSecret = SysConfig.getValue("WECHAT_APPSECRET").toString();
		Token token = CommonUtil.getToken(appId, appSecret);
		JSONObject jsonObject = CommonUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token.getAccessToken(), "POST", "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}");
		
		System.out.print(jsonObject);
		*/
		
	}
}
