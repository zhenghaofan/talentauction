require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('agreemtCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		$('.viewport').show();
		
 	}]);
	
	app.bootstrap();
});
