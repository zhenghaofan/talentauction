require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('signinCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});

		/* 登录*/
		$scope.signIn = function() {
			var config = {
				email: $.trim($('#email').val()),
				password: $.trim($('#pwd').val())
			};
			
			if(!_checkedSignIn(config)) return;
			
            $q.when($http.post('/user/userLogin', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	if(data.code == 200) {
            		window.location.replace('/common/talentRoute');
            	}else {
            		$('#pwd_tip').show().text(data.message);
            	}
            }, function(err) {
            	console.error('userLogin', err);
            });			
		};
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			$('.err-wrap label').text('').hide();
		};
		
		/* 验证登录*/
		function _checkedSignIn(data, status) {
			var pwd_reg = /[0-9A-Za-z_]{6,16}/;
			
			if(!data.email) {
				$('#email').focus();
				$('#email_tip').show().text('邮箱输入有误！');
				return false;
			}else if(!pwd_reg.test(data.password)) {
				$('#pwd').focus();
				$('#pwd_tip').show().text('密码输入有误！');
				return false;
			}
			
			return true;
		}
		
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
