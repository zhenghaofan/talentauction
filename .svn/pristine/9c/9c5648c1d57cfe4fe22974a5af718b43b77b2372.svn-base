require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('questionCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		$('.viewport').show();
		$scope.type = !!url.get('type') ? Number(url.get('type')) : 1;
		
		$scope.changeType = function(no) {
			if( $scope.type != no ) {
				$scope.type = no;
			}
		};
 	}]);
	
	app.bootstrap();
});
