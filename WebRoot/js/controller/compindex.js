require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('indexCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		$('.viewport').show();
		$rootScope.tabIndex = 2;
//		鼠标hover按钮出现
		$(document).on("mouseover",'.cooper-item',function(_event){
			$(_event.currentTarget).children('.img-text').show();
		}).on("mouseout", '.cooper-item', function(_event){
			$(_event.currentTarget).children('.img-text').hide();
		});
 	}]);
	
	app.bootstrap();
});
