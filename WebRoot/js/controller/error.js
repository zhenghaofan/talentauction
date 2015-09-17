require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('errorCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});
 	}]);
	
	app.bootstrap();
});
