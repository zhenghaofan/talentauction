require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'uiservice'
], 
function($, angular, app, cookie, url) {
	app.controller('mailboxCtrl', ['$scope', '$rootScope', '$http', '$q', 'uiservice',
   		function($scope, $rootScope, $http, $q, uiservice) {
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});
		
		$scope.email = url.get('email') ? url.get('email') : '';
		
		/* 重新发送激活邮件*/
		$scope.mailReSend = function() {
			var config = {
					email: $rootScope.user.email || $scope.email
			};
			
            $q.when($http.post('/talents/sendEmail', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	if(data.code == 200) {
            		uiservice.alert(data.message);
            	}else {
            		uiservice.alert(data.message);
            	}
            }, function(err) {
            	console.error('sendEmail', err);
            });				
		};
		
 	}]);
	
	app.bootstrap();
});
