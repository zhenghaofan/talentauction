require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'info',
 'uiservice'
], 
function($, angular, app, cookie, url) {
	app.controller('bidlistCtrl', ['$scope', '$rootScope', '$http', '$q', 'info', 'uiservice',
   		function($scope, $rootScope, $http, $q, info, uiservice) {
			$scope.edus = info.getEducation();
			$rootScope.tabIndex = 5;
			$scope.replayState = 2;
			$scope.pageIndex = 1;
			$scope.pageSize = 30;
			$scope._index = 0;
			$scope.bidswitch = 0;
			$scope.firstStep = true;
			$scope.reply = 4; //默认获取全部拍卖记录
			$scope.status = 0;
			var urls = {
						log: '/company/companyBidLogs',
						detail: '/company/readDetail'
					};
			$scope.isCommon = true;
			$scope.$watch('isCommon',function(){
				if($scope.isCommon){
					$scope.bidType = "普通拍卖记录";
					urls = {
						log: '/company/companyBidLogs',
						detail: '/company/readDetail'
					};
				}else{
					$scope.bidType = "会员拍卖记录";
					urls = {
						log: '/fees/getInviteLogs',
						detail: '/fees/getInviteDetail'
					}
				}
				// $('.box-shadow li:first-child').trigger('click');
			});
		
		$scope.switchstl = function(){
			$('.item-top-list').fadeIn(150);
		}
		$scope.mouseleave = function(){
			$('.item-top-list').fadeOut(150);
		}

		/* 切换拍卖记录，普通与会员*/
		$scope.switchTab = function(_event,status) {
			$scope.isCommon = status;
			angular.element('.box-shadow li:first-child').trigger('click');
			$('.item-top-list').fadeOut(150);
			if(_event) $(_event.target).addClass('slt').siblings().removeClass('slt');
			$scope.initHref();
			$scope._index = 0;
			$scope.getBidLogs();
		};

		/* 获取拍卖记录*/
		$scope.getBidLogs = function(str) {
			var config = {
					pageSize: $scope.pageSize,
					page: $scope.pageIndex,
					reply: $scope.replayState == 2? '' : $scope.replayState
			};
			
            $q.when($http.post(urls.log, $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200 && data.bidLogs.length) {
            		$scope.listData = data.bidLogs;
            		// $scope.getBidInfo($scope.listData[$scope._index], str);
            	}else {
            		$scope.listData = [];
            		$scope._initPage();
            	}
            }, function(err) {
            	console.error('companyBidLogs', err);
            });
		};


		/* 页面初始化*/
		$scope._initPage = function() {
			if($('.viewport').css('display') == 'none') {
        		setTimeout(function() {
        			$('.viewport').show(0);
        		});
    		}
		};

		// 选择拍卖类型
		$scope.sltBidType = function(type){
			$scope.reply = type;
			$scope.status = type;
			$scope.firstStep = true;
			var config = {
					pageIndex:$scope.pageIndex,
					pageSize:$scope.pageSize,
					reply:type === 4? null:type
			};
            $q.when($http.post(urls.log, $.param(config)))
            .then(function(result) {
            	var data = result.data;            	
            	if(data.code == 200 && data.bidLogs.length) {
            		$scope.listData = data.bidLogs;
            		$scope._initPage();
            		// $scope.getBidInfo($scope.listData[$scope._index], str);
            	}else {
            		$scope.listData = [];
            		$scope._initPage();
            	}
            }, function(err) {
            	console.error('sltBidType', err);
            });
		}

		$scope.toggle = function(){
			$scope.firstStep = !$scope.firstStep;
		}
		// 查看详情
		$scope.getDetails = function(userId){
			var config = {
					userId: userId,
					reply: $scope.replayState == 2? '' : $scope.replayState
			};
			// $scope._index = _index;
			// item.isCompanyRead = 1;
			// if(_event && typeof _event != 'string') {
			// 	$(_event.currentTarget).addClass('slt').siblings().removeClass('slt');
			// }
			
			$q.when($http.post(urls.detail, $.param(config)))
            .then(function(result) {
            	var data = result.data;
        		$scope.bidInfo = data.resume;
            	$scope.bidList = data.bidLogs;
            	$scope.firstStep = false;
            	// $('.info-wrap').fadeOut(150);
            	// $('.details').fadeIn(150);
            	// if(_event == 'sort') $('.list-box .list-item:first').addClass('slt');
            	// $scope._initPage();

            }, function(err) {
            	console.error('readDetail', err);
            });
		}
		
		$scope.initHref = function() {
			if(!$scope.bidswitch){
				$scope.jumpHref = '/talentform/placebid?sd=';
			}else {
				$scope.jumpHref = '/vip/vipbid?sd=';
			}
		};

		/* 获取联系*/
		$scope.getReplay = function(item) {
			var config = {
					id: item.id
			};
			
			uiservice.confirm('确定获取联系方式吗？', function() {
	            $q.when($http.post('/company/getContacts', $.param(config)))
	            .then(function(result) {
	            	var data = result.data;
					if(data.code == 200) 
						$scope.getBidInfo($scope.listData[$scope._index],null,$scope._index);   	
	            }, function(err) {
	            	console.error('getContacts', err);
	            });
			});
		};		
		
		// $scope.initHref();	
		
 	}]);

	
	app.filter('sexFilter' , function(){
		return function(sex){
			return sex? '女':'男';
		};
	})
	.filter('educationFilter',['info',function(info){
		return function(id){
		var arr = info.getEducation();
		var res = '';
		var go = true;
		angular.forEach(arr,function(obj){
			if(go && obj.id == id.toString()){
		 			res = obj.value;
		 			go = false;

		}});
		return res;
	}}]);
	
	app.bootstrap();
});
