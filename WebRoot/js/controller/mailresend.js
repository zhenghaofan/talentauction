require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'uiservice'
], 
function($, angular, app, cookie, url) {
	app.controller('resendCtrl', ['$scope', '$rootScope', '$http', '$q', 'uiservice',
   		function($scope, $rootScope, $http, $q, uiservice) {
		
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});
		
		/* 清除提示*/
		$scope._clearTip = function() {
			$('.err-wrap label').text('').hide();
		};
		
		/* 重新发送激活邮件*/
		$scope.mailReSend = function() {
			var config = {
					email: $scope.email	
			};
			
			if(!_checkEmail(config)) return;
			
            $q.when($http.post('/talents/sendEmail', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	if(data.code == 200) {
            		uiservice.alert('发送成功！')
            		
            	}else {
            		uiservice.alert(data.message);
            	}
            	$scope.email = '';
            }, function(err) {
            	console.error('sendEmail', err);
            });				
		};
		
		/* 验证项目信息*/
		function _checkEmail(data) {
			$scope._clearTip();
			if(!data.email) {
				$('#item_email').focus();
				$('#item_email_tip').show().text('邮箱输入有误！');
				return false;
			}
			
			return true;
		}
		
 	}]);
	
	app.bootstrap();
});
