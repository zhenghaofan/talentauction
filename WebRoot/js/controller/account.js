require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'uiservice'
], 
function($, angular, app, cookie, url) {
	app.controller('accountCtrl', ['$scope', '$rootScope', '$http', '$q', 'uiservice','$timeout',
   		function($scope, $rootScope, $http, $q, uiservice,$timeout) {
		$scope.pageSize = 5;
		$scope.pageIndex = 1;
		$scope.totalPage = 1;
		$scope.pkgPice = 499;
		$scope.pkgType = 1;
		$scope.listStatus = 0;
		
		$scope.initHead = function() {
			$timeout(function() {
				$('.det-tab .ng-scope').removeAttr('href');
				$('.det-tab .ng-scope').attr("onclick",'window.location.reload()');   
			},100);
		}
		
		$scope.shoePayPanel = function() {
			$('.pay-box').fadeToggle(150);
			$('.info-box-directory').css('display','none');
		};
		
		$scope.closeuser = function(){
			$('.pay-box').css('display','none');
			$('.info-box-directory').fadeToggle(150);
			$('.record-box').css('display','none');
			$scope.getAccountInfo();
		}
		
		/* 获取账户信息*/
		$scope.getAccountInfo = function() {
			$q.when($http.post('/fees/getAccountInfo'))
			.then(function(result) {
				var data = result.data;
				$scope.accountInfo = data.accountInfo;
				$('.viewport').show();
				$scope.initHead();
			}, function(err) {
            	  console.error('getAccountInfo', err);
			});
		};
		
		/* 记录选择*/
		$scope.sltPayList = function(_event, status) {
			$scope.pageIndex = 1;
			$scope.totalPage = 1;
			$scope.listStatus = status;
			$('#sltPayLook'+status).addClass('slt').siblings().removeClass('slt');
			//$(_event.currentTarget).addClass('slt').siblings().removeClass('slt');
			$('.record-box').css('display','block');
			$('.info-box-directory').css('display','none');
			if(!status) {
				$scope.getPayList();
			}else {
				$scope.getDeductList();
			}
		};
		
		
		/* 套餐选择*/
		$scope.sltPkg = function(_event, type, pice) {
			$(_event.currentTarget).addClass('slt').siblings().removeClass('slt');
			$scope.pkgType = type;
			$scope.pkgPice = pice;
		};
		
		
		/* 获取充值记录*/
		$scope.getPayList = function() {
			var config = {
					page: $scope.pageIndex,
					pageSize: $scope.pageSize
			};
			
			$q.when($http.post('/fees/getRechargeLogs', $.param(config)))
			.then(function(result) {
				var data = result.data;
				$scope.pageIndex = data.page;
				$scope.totalPage = data.totalPage;
				$scope.payList = data.recharge;
			}, function(err) {
            	  console.error('getRechargeLogs', err);
			});			
		};
		
		/* 获取扣除记录*/
		$scope.getDeductList = function() {
			var config = {
					page: $scope.pageIndex,
					pageSize: $scope.pageSize
			};
			
			$q.when($http.post('/fees/getDebitLogs', $.param(config)))
			.then(function(result) {
				var data = result.data;
				$scope.pageIndex = data.page;
				$scope.totalPage = data.totalPage;
				$scope.payList = data.debit;
			}, function(err) {
            	  console.error('getDebitLogs', err);
			});			
		};
		
		/* 支付跳转*/
		$scope.jumpPay = function() {
			var xhr = new XMLHttpRequest();
		    xhr.open("GET", "/fees/getRechargeLogTotal", false);  // synchronous request
		    xhr.send(null);
		    try {
		    	var result = angular.fromJson(xhr.responseText);
				if(result.code==200) {
					
			    	window.open('/vip/vippay?type=' + $scope.pkgType);
			    }else {
			    	uiservice.notify('请正确操作。', result.code != 200);
			    }
			} catch (e) {
				uiservice.notify('请正确操作。', true);
			}
			
			
		};
		
		/* 翻页*/
		$scope.turnPage = function(_event, status) {
			if(status == '+') {
				$scope.pageIndex ++;
			}else if(status == '-') {
				$scope.pageIndex --;
			}else {
				$scope.pageIndex = status;
			}
			
			if($scope.listStatus) {
				$scope.getDeductList();
			}else {
				$scope.getPayList();
			}
		};
 	}]);
	
	app.bootstrap();
});
