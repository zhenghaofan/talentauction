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
		$rootScope.tabIndex = 8;
		$scope.edus = info.getEducation();
		$scope.phases = info.getCompPhase();
		$scope.bidInfo = {};
		var userId = url.get('sd');
		
		
		$(document).on('mouseenter', '.email-suffix' ,function() {
			$('.tipso-bubble').show();
		}).on('mouseleave', '.email-suffix', function() {
			$('.tipso-bubble').hide();
		});
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			if(ele)
				$(ele).text('').hide();
			else
				$('.err-wrap label').text('').hide();
		};
		
		/* 获取简历信息*/
		$scope.getResume = function() {
            $q.when($http.post('/fees/getUserResume', $.param({userId: userId})))
            .then(function(result) {
            	var data = result.data;
            	var expectedSalary = Number(data.resume.expectedSalary);
            	$scope.userInfo = _fomartResume(data);
            	
            	$('.viewport').show();
            }, function(err) {
            	console.error('getUserResume', err);
            });
		};
		
		/* 拍卖*/
		$scope.compAuction = function () {
			var config = {
					userId: $scope.userInfo.resume.userId,
					bidCount: $scope.userInfo.resume.bidCount,
					isOption: $scope.bidInfo.isOption? 1 : 0,
					minBidPrice: $scope.bidInfo.minPrice,
					maxBidPrice: $scope.bidInfo.maxPrice,
					workTitle: $scope.bidInfo.workTitle,
					special: $scope.userInfo.resume.special,
					specialCount: $scope.userInfo.resume.specialCount
			};
			
			if(!_checkAuctionInfo(config)) return;
			
			uiservice.confirm('您确定发送面试邀请吗？我们将先扣除你<span style="color:#f25d0f">50金币</span>哟,如果候选人拒绝邀约，我们将全额返还。谢谢', function() {
	            $q.when($http.post('/fees/interviewInvite', $.param(config)))
	            .then(function(result) {
	            	var data = result.data;
	            	
	            	if(data.code == 200) {
	            		$scope.bidInfo = {};
						$scope.getResume();
	            	}
					uiservice.notify(data.message, data.code != 200);
	            }, function(err) {
	            	console.error('companyAuction', err);
	            });					
			});
		};
		
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
			}else if(Number(data.maxBidPrice) <= Number(data.minBidPrice)) {
				$('#bid_maxPrice').focus();
				$('#bid_info_tip').show().text('薪资范围输入有误！');
				return false;
			}else if(!data.workTitle || data.workTitle.length>150) {
				$('#bid_workTitle').focus();
				$('#bid_info_tip').show().text('邀请短笺长度应在1~150之间！');
				return false;
			}else if(uiservice.isNotify()) {
				return false;
			}
			
			return true;
		}
		
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
