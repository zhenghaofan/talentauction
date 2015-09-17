$(function($) {
	var status = _getParam('s')? 1 : 0;
	$('.sign-wrap').show();
	var isLock = false;
	var step = 0;
	var user = LocalStorage.get('user')?  LocalStorage.get('user') : false;
	var isJump = LocalStorage.get('isJump')?  LocalStorage.get('isJump') : false;
	var skill = LocalStorage.get('skill')?  LocalStorage.get('skill') : [];
	var city = LocalStorage.get('city')?  LocalStorage.get('city') : [];

	if(status) {
		$('.sign-wrap .job').hide();
		$('.sign-wrap .comp').show();
	}
	
	if(user.email) {
		$("#email").val(user.email);
	}
	
	if(user.pwd) {
		$("#pwd").val(user.pwd);
	}
	
	if(user.phone) {
		$("#phone").val(user.phone);
	}
	
	if(skill.length) {
		$("#skill").val(skill.toString());
	}
	
	if(city.length) {
		$("#city").val(city.toString());
	}
	
	if(isJump) {
		isJump = false;
		LocalStorage.set('isJump', isJump);
	}
	
	/* 检查刷新*/
	window.addEventListener("pagehide", function(){
		if(!isJump) {
    		LocalStorage.set('user', {});
    		LocalStorage.set('skill', []);
    		LocalStorage.set('city', []);
    		$("#email").val('');
    		$("#pwd").val('');
    		$("#phone").val('');
    	}
	}, false);
	
	/* 获取基本信息*/
	function getInfo() {
		var info = {
			email: $("#email").val(),
			password: $("#pwd").val(),
			telephone: $("#phone").val(),
			status: status,
			inviteCode: $('#code').val()
		}
		
		return info;
	}
	
	/* 注册*/
	$("#submit").on('click', function(){
		var config = getInfo();
		
		if(status) {
			config.name = $("#name").val();
			config.jobTitle = $("#title").val();
		}else {
			config.skills = $("#skill").val();
			config.destination = $("#city").val();
		}
		
		if(!_check(config)) return;
		
		if(!config.inviteCode ) {
			$('#code').focus();
			T.alert('请输入验证码');
			return false;
		}
		
		$.post('/user/userRegister', config, function(res){
			  res = $.parseJSON(res);
			  if( res.code == 200 ) {
				  showTip();
				  LocalStorage.set('skill', []);
				  LocalStorage.set('city', []);
			  } else {
				  T.alert(res.message);
			  }
		});
	});
	
	/* 校验邮箱*/
	$('#email').on('change', function() {
		var config = {
				email: $(this).val()
		};
		
		if( !_checkComEmail(config.email) && status) {
			$('#email').focus();
		 	T.alert('请输入常见企业邮箱');
		 	return false;
		} 

		$.post('/user/emailVerify', config, function(res){
			  res = $.parseJSON(res);
			  
			  if( res.code != 200 ) {
				  $(this).focus();
				  T.alert(res.message);
			  }
		});	
	});
	
	/* 获取验证码*/
	$('.send-btn').on('click', function() {
		var config = getInfo();
		
		if(status) {
			config.name = $("#name").val();
			config.jobTitle = $("#title").val();
		}else {
			config.skills = $("#skill").val();
			config.destination = $("#city").val();
		}
		
		if(!_check(config)) return;
		if(isLock) return;
		var param = {telephone : config.telephone};
		
		$.post('/sendMessage/sendSMS', param, function(res){
			  res = $.parseJSON(res);
			  
			  if( res.code == 200 ) {
				  timeMsg();
			  } else {
				  T.alert(res.message);
			  }
		});
	});
	
	/* 短信倒计时*/
	var timeMsg = function() {
		var time = 60;
		var ele = $('.send-btn');
		isLock = true;
		
		ele.text('60秒后重发').css({
			'background-color' : '#ccc'
		});
		$('#phone').attr({'readonly': 'true'});
		
		var handle = setInterval(function() {
			time --;
			ele.text(time + '秒后重发');
			if( time <= 1 ) {
				clearInterval(handle);
				ele.attr('data-work', '0');
				ele.text('获取验证码').css({
					'background-color' : '#3a89ce'
				});
				isLock = false;
				$('#phone').removeAttr('readonly');
			}
		}, 1000);		
	};
	
	/* 校验验证码*/
	$('#code').on('change', function() {
		var config = {
				code: $(this).val()	
		};
		
		if(code.length < 6) return;
		
		$.post('/sendMessage/smsVerify', config, function(res){
			  res = $.parseJSON(res);
			  if( res.code != 200 ) {
				  T.alert(res.message);
			  }
		});
	});
		
	/* 跳转*/
	$('.sign-form .job').click(function() {
		var id = $(this).find('input').attr('id');
		var info = getInfo();
		
		isJump = true;
		LocalStorage.set('isJump', true);
		LocalStorage.set('user', {
			email: info.email,
			pwd: info.password,
			phone: info.telephone
		});
		
		if(id == 'skill') {
			window.location.href = '/mobile/sltskill';
		}else {
			window.location.href = '/mobile/sltcity';
		}
	});
	
	/* 关闭微信浏览器*/
	$("#close_btn").on('click', function() {
		 WeixinJSBridge.call('closeWindow');
	});
	
	/* 后退*/
	$('.bar-back').click(function() {
		window.location.href="/mobile/sltsign";
	});
	
	/* 注册提示信息*/
	function showTip() {
		var html = [];
		var time = 3;
		var handle = setInterval(function() {
			if( time > 1 ) {
				time --;
				$("#tip_sec").text(time);
			} else {
				clearInterval(handle);
				WeixinJSBridge.call('closeWindow');
			}
		 }, 1000);		
		
		if(status) {
			$('#tip_text').append('<span>恭喜你已经注册成功，请用PC登录实力拍验证邮箱填写资料，参加竞拍，<span id="tip_sec">3</span>秒后关闭，谢谢。</span>');
		}else {
			$('#tip_text').append('恭喜你已经注册成功，请用PC登录实力拍验证邮箱提交简历，参加拍卖，<span id="tip_sec">3</span>秒后关闭，谢谢。');
		}
		
		$('body').css({'background-color': '#fff'});
		$(".sign-wrap").hide();
		$('.tip-wrap').show();
	};
	
	/* 验证注册*/
	function _check(data) {
		var pwdReg = /^[0-9A-Za-z_]{6,16}$/;
		
		if( !data.email ) {
			$('#email').focus();
			T.alert('请输入邮箱');
			return false;
		} else if( data.email.indexOf('@') == -1 ) {
			$('#email').focus();
			T.alert('邮箱格式不对');
			return false;
		} if( !_checkComEmail(data.email) && status) {
			$('#email').focus();
		 	T.alert('请输入常见企业邮箱');
		 	return false;
		} else if( data.password.length < 6 ) {
			$('#pwd').focus();
			T.alert('请输入6-16位密码！');
			return false;
		} else if( !pwdReg.test(data.password)) {
			$('#pwd').focus();
			T.alert('密码只能是字母、数字、下划线！');
			return false;
		} else if( !data.skills && !status) {
			$('#skill').focus();
			T.alert('请选择技能');
			return false;
		} else if( !data.destination && !status) {
			$('#city').focus();
			T.alert('请选择城市');
			return false;
		} else if( !data.name && status) {
			$('#name').focus();
			T.alert('请选择姓名');
			return false;
		} else if( !data.jobTitle && status) {
			$('#title').focus();
			T.alert('请选择职位');
			return false;
		} else if( !data.telephone ) {
			$('#phone').focus();
			T.alert('请输入手机号');
			return false;
		} else if( data.telephone.length < 11 ) {
			$('#phone').focus();
			T.alert('请输入11位手机号！');
			return false;
		}
		
		return true;
	}
	
	
	/* 企业邮箱验证*/
	function _checkComEmail(email) {
		var _eArr = email.split('@');
		var flag = true;
		
		if( _eArr.length < 2 ) return false;
		$t = _eArr[1];
		$t = $t.toLowerCase();
		
		if ($t == '163.com') {
			flag = false;
		} else if ($t == 'vip.163.com') {
			flag = false;
		} else if ($t == '126.com') {
			flag = false;
		} else if ($t == 'qq.com' || $t == 'vip.qq.com' || $t == 'foxmail.com') {
			flag = false;
		} else if ($t == 'gmail.com') {
			flag = false;
		} else if ($t == 'sohu.com') {
			flag = false;
		} else if ($t == 'tom.com') {
			flag = false;
		} else if ($t == 'vip.sina.com') {
			flag = false;
		} else if ($t == 'sina.com.cn' || $t == 'sina.com') {
			flag = false;
		} else if ($t == 'tom.com') {
			flag = false;
		} else if ($t == 'yahoo.com.cn' || $t == 'yahoo.cn') {
			flag = false;
		} else if ($t == 'tom.com') {
			flag = false;
		} else if ($t == 'yeah.net') {
			flag = false;
		} else if ($t == '21cn.com') {
			flag = false;
		} else if ($t == 'hotmail.com') {
			flag = false;
		} else if ($t == 'sogou.com') {
			flag = false;
		} else if ($t == '188.com') {
			flag = false;
		} else if ($t == '139.com') {
			flag = false;
		} else if ($t == '189.cn') {
			flag = false;
		} else if ($t == 'wo.com.cn') {
			flag = false;
		} else if ($t == '139.com') {
			flag = false;
		} else if ($t == 'outlook.com') {
			flag = false;
		} 
		return flag;
	};
	
	/* 获取参数*/
	function _getParam( name ) {
		var query = window.location.search;
		if (name) {
			var r = new RegExp("[?&]" + name + "=([^&]*)");
			var m = r.exec(query);
			if (m) {
				return m[1];
			} else {
				return null;
			}
		} 
	}
});