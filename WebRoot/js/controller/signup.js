require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('signupCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		$scope.status = !!url.get('c') ? url.get('c') : 0;
		$scope.userInfo = {emailLock: false, codeLock: false, isCodeSucceed: false};
		var EMAIL_REGEXP = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		var inviteCode = '';
		var inviteUrl = window.location.href;
		var strStart = inviteUrl.indexOf('base/') + 5;
		var strEnd = inviteUrl.indexOf('base/') + 13;
		var strCount = inviteUrl.length;
		$scope.cityArray = [];
		$scope.skillArray = [];
		
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});
		
		if(strCount >= strEnd) {
			var inviteCode = inviteUrl.substring(strStart, strEnd);
			_getInviteName();
		}
		
		/* 获取推荐人姓名*/
		function _getInviteName() {
			var config = {
					inviteCode: inviteCode
			};
			
			$q.when($http.post('/invite/getInviteName', $.param(config)))
			.then(function(result) {
				var data = result.data;
				if(data.code == 200) {
					$('.signup-title-box .tip').css({'display': 'inline-block'})
					.find('#invite_people').text('由好友' + data.userName + '推荐注册实力拍' );
				}
			}, function(err) {
				console.error('getInviteName', err);
			});				
		}		
		
		/* 注册*/
		$scope.signUp = function() {
			$scope.userInfo.email = $.trim($('#email').val());
			$scope.userInfo.pwd = $.trim($('#pwd').val());
			$scope.userInfo.skill = $.trim($('#skill').val());
			$scope.userInfo.name = $.trim($('#name').val());
			$scope.userInfo.city = $.trim($('#city').val());
			$scope.userInfo.title = $.trim($('#title').val());
			$scope.userInfo.phone = $.trim($('#phone').val());
			
			if(!_checkedSignUp($scope.userInfo, $scope.status)) {
				return;
			}else if(!$scope.userInfo.isCodeSucceed) {
				if($('#code').css('display') == 'none') {
					$('#phone').focus();
					$('#phone_tip').show().text('请输入验证码！');
				}else {
					$('#code').focus();
					$('#code_tip').show().text('请输入验证码！');
				}
				return;
			}else if(!$scope.userInfo.agreementBox){
				$('#agreement_tip').show().text('请接受协议！');
				return;
			}
			
			var config = {
				email: $scope.userInfo.email,
				password: $scope.userInfo.pwd,
				telephone: $scope.userInfo.phone,
				status: $scope.status			
			}
			
			if($scope.status) {
				config.name = $scope.userInfo.name;
				config.jobTitle = $scope.userInfo.title;
			}else {
				config.skills = $scope.userInfo.skill;
				config.destination = $scope.userInfo.city;
			}
			
			if(inviteCode) config.inviteCode = inviteCode;
			
			$q.when($http.post('/user/userRegister', $.param(config)))
			.then(function(retsult) {
				var data = retsult.data;
				if(data.code == 200) {
					window.location.replace('/base/mailbox?email=' + config.email);
				}else {
					$('#agreement_tip').show().text(retsult.data.message);
				};
			}, function(err) {
				console.error('userRegister', err);
			});			
		};
		
		/* 切换注册方式*/
		$scope.changStatus = function(status) {
			$scope.status = status;
			$scope.userInfo = {emailLock: false, codeLock: false};
			$('.register-l .checked label').hide();
		};
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			if(ele) 
				$(ele).text('').hide();
			else
				$('.checked label').text('').hide();
		};		
		
		/* 验证邮箱*/
		$scope.checkEmail = function() {
			$scope.userInfo.email = $('#email').val();
			if(!$scope.userInfo.email) return;
			
			if(EMAIL_REGEXP.test($scope.userInfo.email)) {
				if($scope.status && !_checkComEmail($scope.userInfo.email)) {
					$('.register-l .checked label').hide();
					$('#email').focus();
					$('#email_tip').show().text('请输入企业邮箱！');
					$scope.userInfo.emailLock = true;					
					return;
				}
				
				$q.when($http.post('/user/emailVerify?email=' + $scope.userInfo.email))
				.then(function(result) {
					if(result.data.code != 200) {
						 $('.register-l .checked label').hide();
						 $('#email').focus();
						 $('#email_tip').show().text(result.data.message);
						 $scope.userInfo.emailLock = true;
					 }else {
						 $scope.userInfo.emailLock = false;
					 }
				}, function(err) {
	            	  console.error('checkEmail', err);
				});
				 
			}else {
				$('.register-l .checked label').hide();
				$('#email').focus();
				$('#email_tip').show().text('请输入有效邮箱！');
				$scope.userInfo.emailLock = true;
			}		
		};
		
		$scope.sendCode = function() {
			$scope.userInfo.email = $('#email').val();
			$scope.userInfo.pwd = $('#pwd').val();
			$scope.userInfo.skill = $('#skill').val();
			$scope.userInfo.name = $('#name').val();
			$scope.userInfo.city = $('#city').val();
			$scope.userInfo.title = $('#title').val();
			$scope.userInfo.phone = $('#phone').val();
			
			if(!_checkedSignUp($scope.userInfo, $scope.status) || $scope.userInfo.codeLock) return;
			
			$scope._clearTip('#phone_tip');
			
			var param = {telephone : $scope.userInfo.phone};
			var dom = $("#send_code_btn");
			dom.attr('data-work', '1');
			dom.text('发送中...').css({'background-color' : '#ccc'});
			
			$q.when($http.post('/sendMessage/sendSMS', $.param(param)))
			.then(function(result) {
				_countTime();
			}, function(err) {
				console.error('sendSMS', err.message);
				dom.attr('data-work', '0');
				dom.text('发送验证码').css({
					'background-color' : '#b783c3'
				});
				$('#phone_tip').show().text(err.message);
			});	
		};
		
		/* 校验验证码*/
		$scope.checkPhoneCode = function() {
			var code = $('#code').val();
			var param = {code : code};
			
			if( !code || code.length != 6 ) return;
			
			$q.when($http.post('/sendMessage/smsVerify', $.param(param)))
			.then(function(result) {
				var data = result.data;
				if(data.code == 200) {
					$scope.userInfo.isCodeSucceed = true;
					$('#code_tip').show().removeClass('err-ico').addClass('correct-ico').text('验证成功！');
				}else {
					$scope.userInfo.isCodeSucceed = false;
					$('#code_tip').show().text('验证码错误！');
				}
			}, function(err) {
				console.error('smsVerify', err.message);
				$scope.userInfo.isCodeSucceed = false;
				$('#code_tip').show().text('验证码错误！');
			});
		};		
		
		/* 短信倒计时*/
		function _countTime() {
			var time = 60;
			var dom = $("#send_code_btn");
			dom.attr('data-work', '1');
			dom.text('60秒后可重送').css({
				'background-color' : '#ccc'
			});
			
			$scope.userInfo.codeLock = true;
			$('#phone').attr({'readOnly': 'true'});
			$("#code").slideDown();
			
			var handle = setInterval(function() {
				time --;
				dom.text(time+'秒后可重送');
				if( time <= 1 ) {
					clearInterval(handle);
					dom.attr('data-work', '0');
					dom.text('发送验证码').css({
						'background-color' : '#b783c3'
					});
					$scope.userInfo.codeLock = false;
					$('#phone').removeAttr('readOnly');
				} 
			}, 1000);
		}
		
		/* 验证注册*/
		function _checkedSignUp(data, status) {
			if(data.emailLock && data.email) return;
			$('.register-l .checked label').hide();
			$scope._clearTip();
			var pwdReg = /^[0-9A-Za-z_]{6,16}$/;
			
			if(!data.email) {
				$('#email').focus();
				$('#email_tip').show().text('请输入邮箱！');
				return false;
			}else if(!data.pwd || data.pwd.length < 6) {
				$('#pwd').focus();
				$('#pwd_tip').show().text('请输入6-16位密码！');
				return false;
			}else if(!pwdReg.test(data.pwd)) {
				$('#pwd').focus();
				$('#pwd_tip').show().text('密码只能是字母、数字、下划线！');
				return false;
			}else if(!data.skill && !status) {
				$('#skill').focus();
				$('#skill_tip').show().text('请选择技能！');
				return false;
			}else if(!data.name && status) {
				$('#name').focus();
				$('#name_tip').show().text('请输入姓名！');
				return false;
			}else if(!data.city && !status) {
				$('#city').focus();
				$('#city_tip').show().text('请选择城市！');
				return false;
			}else if(!data.title && status) {
				$('#title').focus();
				$('#title_tip').show().text('请输入职称！');
				return false;
			}else if(!data.phone || data.phone.length < 11) {
				$('#phone').focus();
				$('#phone_tip').show().text('请输入11位手机号！');
				return false;
			}
			return true;
		}		
		
		/*初始化城市*/
		$scope.initCity = function() {
			$('.city-modal').fadeIn(150);
			$('.other-city-in').val('');
			
			if($scope.userInfo.city){
				$scope.cityArray = $scope.userInfo.city.split(',');
			}else{
				$scope.cityArray = [];			
			}
		};
		
		/* 清除城市*/
		$scope.clearCity = function(item) {
			for ( var i in $scope.cityArray) {
				if($scope.cityArray[i] == item) {
					$scope.cityArray.splice(i, $scope.cityArray.length == 1? 1 : $scope.cityArray.length -1);
				}
			}
		};
		
		/* 增加城市*/
		$scope.addCity = function(_event) {
			var keycode = window.event? _event.keyCode : _event.which;
			
            if(keycode == 13) {
            	var inp = $('.other-city-in');
            	if($scope.cityArray.length > 1 || !inp.val()) return;
            	
            	for(var i in $scope.cityArray) {
    				if($scope.cityArray[i]  == inp.val()) return;
    			}
            	
            	$scope.cityArray.push(inp.val());
            	inp.val('');
            }
		};
		
		/*选择城市*/
		$scope.sltCity = function(_event) {
			var ele = $(_event.target);
			
			if($scope.cityArray.length > 1) return;
			
			for(var i in $scope.cityArray) {
				if($scope.cityArray[i]  == ele.text()) return;
			}
			
			$scope.cityArray.push(ele.text());
		};
		
		/*确认选择城市*/
		$scope.confirmCity = function() {
			$scope.userInfo.city = $scope.cityArray.toString();
			$scope.close('.city-modal');
		};
		
		/*关闭*/
		$scope.close = function(ele) {
			$(ele).fadeOut(150);
		};
		
		/*初始化技能*/
		$scope.initSkill = function() {
			if(!$scope.skillsList) {
				$scope.getSkillsList();
			}else {
				_bindSkillEle();
				$('.skill-modal').fadeIn(150);
			}
		};
		
		/*获取技能列表*/
		$scope.getSkillsList = function() {
			$q.when($http.post('/common/getSkillsList'))
			.then(function(result) {
				var tmp = result.data.specialList;
				for ( var i in tmp) {
					if(tmp[i].skillName != null && tmp[i].skillName) {
						tmp[i].skillName = tmp[i].skillName.split('、');
					}
				}
				$scope.skillsList = tmp;
				setTimeout(function() {
					_bindSkillEle();
				});
				$('.skill-modal').fadeIn(150);
			}, function(err) {
            	  console.error('getSkillsList', err);
			});
		};
		
		/*获取技能详情*/
		$scope.getSkillsDetails = function(item, _event) {
			$(_event.target).addClass('slt').siblings().removeClass('slt');
			$scope.skillNameList = item.skillName;
		};
		
		/*选择技能*/
		$scope.sltSkill = function(_event) {
			var ele = $(_event.target);
			
			if($scope.skillArray.length > 1) return;
			
			for(var i in $scope.skillArray) {
				if($scope.skillArray[i]  == ele.text()) return;
			}
			
			$scope.skillArray.push(ele.text());
		};
		
		/* 清除技能*/
		$scope.clearSkill = function(item) {
			for ( var i in $scope.skillArray) {
				if($scope.skillArray[i] == item) {
					$scope.skillArray.splice(i, $scope.skillArray.length == 1? 1 : $scope.skillArray.length -1);
				}
			}
		};
		
		/*确定技能*/
		$scope.confirmSkill = function() {
			$scope.userInfo.skill = $scope.skillArray.toString();
			$scope.close('.skill-modal');
		};
		
		//绑定元素
		function _bindSkillEle() {
			$safeApply($scope, function() {
				$('.skill-warp ul li:first').addClass('slt').siblings().removeClass('slt');
				$scope.skillNameList = $scope.skillsList[0].skillName;
			});
		}
		
		/* 企业邮箱验证*/
		function _checkComEmail(email) {
			var _eArr = email.split('@');
			if( _eArr.length < 2 ) return false;
			$t = _eArr[1];
			$t = $t.toLowerCase();
			
			var flag = true;
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
		
		function $safeApply($scope, fn) {
			var phase = $scope.$$phase;
			if (phase == '$apply' || phase == '$digest') {
				$scope.$eval(fn);
			} else {
				$scope.$apply(fn);
			}
		}
 	}]);
	
	app.bootstrap();
});
