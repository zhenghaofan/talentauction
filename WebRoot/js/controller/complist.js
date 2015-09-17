require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'info'
], 
function($, angular, app, cookie, url) {
	app.controller('listCtrl', ['$scope', '$rootScope', '$http', '$q', 'info',
   		function($scope, $rootScope, $http, $q, info) {
		$scope.pageIndex = 1;
		$scope.pageSize = 21;
		
		$scope.getCompList = function() {
			var config = {
					pageSize: $scope.pageSize,
					page: $scope.pageIndex
			};
			
            $q.when($http.post('/common/getCompanyList', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.dataList = _formateData(data.companyList);
            	$scope.totalPage = data.totalPage;
				$scope.pageIndex = data.page;
            	_initPager();
            	$('.viewport').show();
            }, function(err) {
            	console.error('getCompanyList', err);
            });			
		};
		
		
		/* 翻页*/
		$scope.turnPage = function(_event, status) {
			if(status == '+') {
				$scope.pageIndex ++;
			}else if(status == '-') {
				$scope.pageIndex --;
			}else {
				$scope.pageIndex = status;
				$(_event.target).addClass('slt').siblings().removeClass('slt');
			}
			$scope.getCompList();
		};		
		
		/*初始化翻页*/
		function _initPager() {
			$scope._pageCount = [];
			var index = Math.ceil($scope.pageIndex / 6);
			var total = Math.ceil($scope.totalPage / 6);
			var size = index < total? index * 6 : $scope.totalPage;
			var i = (index - 1) * 6;
			
			for (i; i < size; i++) {
				$scope._pageCount.push(i + 1);
			}
		}
	
		function _formateData(data) {
			var sizeList = info.getCompSize();
			var progressList = info.getCompPhase();
			var sizeText, progressText;
		
			for( var i = 0, len = data.length; i < len; i++ ) {
				sizeText = sizeList[Number(data[i].size)].value;
				progressText = progressList[Number(data[i].progress)].value;
				data[i].sizeText = sizeText;
				data[i].progressText = progressText;
			}
			
			return data;
		}
 	}]);
	
	app.bootstrap();
});
