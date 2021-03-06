require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('vipCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		var type = !!url.get('type') ? url.get('type') : 1;
		
		/* 支付跳转*/
		$scope.jumpPay = function() {
			$q.when($http.post('/fees/alipayapi?type=' + type))
			.then(function(result) {
				$('.pay-wrap').append(result.data.sHtmlText);
			}, function(err) {
            	  console.error('alipayapi', err);
			});			
		};
 	}]);
	
	app.bootstrap();
});
