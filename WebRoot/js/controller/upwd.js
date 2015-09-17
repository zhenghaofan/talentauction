require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'uiservice'
], 
function($, angular, app, cookie, url) {
	app.controller('upwdCtrl', ['$scope', '$rootScope', '$http', '$q', 'uiservice',
   		function($scope, $rootScope, $http, $q, uiservice) {
		
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});
		
		var code = url.get('code');
		$scope.userInfo = {};
		
		if(!!code) 
			$scope.step = 3;
		else 
			$scope.step = 1;
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			$(ele).text('').hide();
		};
		
		/* 重新发送邮件*/
		$scope.resendEmail = function() {
			var config = {
					status: 1,
					email: $scope.userInfo.email
			};
			
			if(!_checkUserInfo(config) && !uiservice.isNotify()) return;
			
            $q.when($http.post('/talents/sendEmail', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200) $scope.step = 2;
    			uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('sendEmail', err);
            });	
		};
		
		/* 修改密码*/
		$scope.upPwd = function() {
			var config = {
					code: code,
					newpwd: $('#newpwd').val(),
					compwd: $('#compwd').val()
			};
			
			if(!_checkUserInfo(config) && !uiservice.isNotify()) return;
			
            $q.when($http.post('/user/resetPwd', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
    			uiservice.notify(data.message, data.code != 200, function() {
    				if(data.code == 200) {
    					window.location.replace('/base/signin');
    				}
        		});
            }, function(err) {
            	console.error('resetPwd', err);
            });	
		};		
		
		/* 验证用户信息*/
		function _checkUserInfo(data) {
			$scope._clearTip();
			var email_reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			var pwd_reg = /[0-9A-Za-z_]{6,16}/;
			
			if($scope.step == 1 && !email_reg.test(data.email)) {
				$('#email').focus();
				$('#email_tip').show().text('邮箱输入有误！');
				return false;
			}else if($scope.step == 3 && !pwd_reg.test(data.newpwd)) {
				$('#newpwd').focus();
				$('#newpwd_tip').show().text('密码输入有误！');
				return false;
			}else if($scope.step == 3 && !pwd_reg.test(data.compwd)) {
				$('#compwd').focus();
				$('#compwd_tip').show().text('确认密码输入有误！');
				return false;
			}else if(!angular.equals(data.newpwd, data.compwd)) {
				$('#compwd').focus();
				$('#compwd_tip').show().text('两次密码输入不一致！');
				return false;
			}
			
			return true;	
		}
 	}]);
	
	app.bootstrap();
});
