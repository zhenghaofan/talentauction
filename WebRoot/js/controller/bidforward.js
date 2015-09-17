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
	app.controller('bidCtrl', ['$scope', '$rootScope', '$http', '$q', 'info', 'uiservice',
   		function($scope, $rootScope, $http, $q, info, uiservice) {
		$rootScope.tabIndex = 3;
		$scope.edus = info.getEducation();
		$scope.phases = info.getCompPhase();
		$scope.bidInfo = {};
		$scope.isDisable = false;
		var code = url.get('code') || '441ea07f-f94c-4e42-ab3c-295bab37b202_1436949894847_402efa4c0e8af9326be359f5a24d7c5e';
		
		/* 清除提示*/
		$scope._clearTip = function() {
			$('.err-wrap label').text('').hide();
		};	
		
		/* 关闭窗口*/
		$scope._close = function(ele, fn) {
			$scope._clearTip();
			$(ele).fadeOut(150);
			
			if(typeof _errback === 'function') fn();
			
			if(ele == '.forward-modal') {
				$scope.bidInfo = {};
			}
		};
		
		/* 开启窗口*/
		$scope._show = function(ele, fn) {
			$scope._clearTip();
			$(ele).fadeIn(150);
			if(typeof _errback === 'function') fn();
		};
		
		/* 获取简历信息*/
		$scope.getResume = function() {
			var config = {
					code: code
			};
			
            $q.when($http.post('/forward/gerResume', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	if(data.code == 200) {
            		$scope.userInfo = _fomartResume(data);
            	}else {
            		$scope.isDisable = true;
            	}
            	$('.viewport').show();
            }, function(err) {
            	console.error('gerResume', err);
            });
		};
		
		/* 拍卖*/
		$scope.compAuction = function () {
			var config = {
					code: code,
					userId: $scope.userInfo.resume.userId,
					bidCount: $scope.userInfo.resume.bidCount,
					isOption: $scope.bidInfo.isOption? 1 : 0,
					minBidPrice: $scope.bidInfo.minBidPrice,
					maxBidPrice: $scope.bidInfo.maxBidPrice,
					workTitle: $scope.bidInfo.workTitle,
					special: $scope.userInfo.resume.special,
					specialCount: $scope.userInfo.resume.specialCount
			};
			
			if(!_checkAuctionInfo(config)) return;
			
            $q.when($http.post('/forward/forwardAuction', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope._close('.forward-modal');
            	if(data.code == 200) {
            		$scope.isDisable = true;
            		//cookie.set('isDisable', true, true);
            		_closeBrowser();
            	}
            	uiservice.alert(data.message);
            }, function(err) {
            	console.error('forwardAuction', err);
            });					
		};
		
		
		/* 拒绝拍卖*/
		$scope.rjtBid = function() {
			var config = {
				code: code
			};
			
			uiservice.alert('您真的要拒绝该人才面试邀请吗？', function() {
	            $q.when($http.post('/forward/forwardDeclined', $.param(config)))
	            .then(function(result) {
	            	var data = result.data;
	            	if(data.code == 200) {
	            		$scope.isDisable = true;
	            		_closeBrowser();
	            	}
	            	
	            }, function(err) {
	            	console.error('forwardDeclined', err);
	            });					
			});
		};
		
		/* 关闭浏览器*/
		function _closeBrowser() {
			var browserName=navigator.appName;
			
		    if (browserName=="Netscape") {
		    	window.open('','_self','');
		    	window.close();     
		    } else {
		    	window.close();
		    }  
		}
		
		/* 验证工作信息*/
		function _checkAuctionInfo(data) {
			$scope._clearTip();
			var isNumber = /^[1-9]\d{0,9}$/;
			
			if(!isNumber.test(data.minBidPrice)) {
				$('#bid_minPrice').focus();
				$('#bid_info_tip').show().text('最低薪资输入有误！');
				return false;
			}else if(data.minBidPrice > 50) {
				$('#bid_minPrice').focus();
				$('#bid_info_tip').show().text('最低薪资不太合理！');
				return false;
			}else if(!isNumber.test(data.maxBidPrice)) {
				$('#bid_maxPrice').focus();
				$('#bid_info_tip').show().text('最高薪资输入有误！');
				return false;
			}else if(data.maxBidPrice > 50) {
				$('#bid_maxPrice').focus();
				$('#bid_info_tip').show().text('最高薪资不太合理！');
				return false;
			}else if(Number(data.maxBidPrice) < Number(data.minBidPrice)) {
				$('#bid_maxPrice').focus();
				$('#bid_info_tip').show().text('薪资范围输入有误！');
				return false;
			}else if(!data.workTitle) {
				$('#bid_workTitle').focus();
				$('#bid_info_tip').show().text('邀请短笺输入有误！');
				return false;
			}
			
			return true;
		}
		
		/* 格式化拍卖信息*/
		function _fomartBidInfo(data) {
			
			for ( var i in data) {
	        	for(var j in $scope.phases) {
	        		if($scope.phases[j].id == data[i].progress)
	        			data[i].progressStr = $scope.phases[j].value;
	        	}
	        	
				if(data[i].bidTime == '0') {
					data[i].bidTimeStr = '今天';
				}else if(data[i].bidTime == '1') {
					data[i].bidTimeStr = '昨天';
				}else {
					data[i].bidTimeStr = data[i].bidTime + '天前';
				}
			}
			
			return data;
		};
		
		/* 格式化简历信息*/
		function _fomartResume(data) {
        	if(data.resume.productUrl) {
        		data.productUrl = data.resume.productUrl.split(',');
        	}
        	
        	if(data.resume.productImg) {
        		data.productImg = data.resume.productImg.split(',');
        	}        	
        	
        	for(var i in $scope.edus) {
        		if($scope.edus[i].id == data.resume.education)
        			data.resume.educationStr = $scope.edus[i].value;
        	}
        	
        	if(data.resume.summary) {
        		data.resume.summaryArray = data.resume.summary.split(',');
			}
        	
			return data;
		}
		
		/* 保留小数*/
        function _fomatFloat(src, pos) { 
        	if(src < 1) return src;
        	
            return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);     
        }
 	}]);
	
	app.bootstrap();
});
