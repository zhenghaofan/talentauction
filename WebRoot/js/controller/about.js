require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('aboutCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		$(".team-item").hover(function() {
			$(this).find(".team-tab").slideDown();
			
		}, function() {
			$(this).find(".team-tab").slideUp();
		});
 	}]);
	
	app.bootstrap();
});
