require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('sltCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		$rootScope.tabIndex = 4;
		$scope.compInfo = {};
		
		/* 清除提示*/
		$scope._clearTip = function() {
			$('.err-wrap label').text('').hide();
		};
		
		/*退出并注册*/
        $scope.backRegister = function() {
            $q.when($http.get('/common/userCancel'))
            .then(function(result) {
            	$rootScope.user = {};
            	window.location.replace('/base/signup?c=1');
            }, function(err) {
            	console.error(err, 'userCancel');
            });
        };
		
		/* 获取匹配公司*/
		$scope.getCompList = function() {
            $q.when($http.post('/company/getSameCompanyList', $.param({})))
            .then(function(result) {
            	var data = result.data;
            	$scope.queryList = data.queryList;
            	setTimeout(function() {
        			$('.viewport').show(0, function() {
        				$('body').css({'background-color': '#2C2F37'});
        			});
        		});
            }, function(err) {
            	console.error('getSameCompanyList', err);
            });
		};
		
		/* 编辑公司*/
		$scope.editComp = function(status) {
			$scope.editStatus = status;
		};
		
		/* 使用公司*/
		$scope.useExistCompany = function(id) {
			var config = {
					companyId: id
			};
			
            $q.when($http.post('/company/useExistCompany', $.param(config)))
            .then(function(result) {
            	var data = result.data;
				if(data.code == 200) {
					window.location.replace('/companyform/compinfo');
				}    	
            }, function(err) {
            	console.error('useExistCompany', err);
            });			
		};
		
		/* 添加公司*/
		$scope.addCompany = function() {
			var config = {
					companyName: $scope.compInfo.companyName
			};
			
			if(!_checkCompInfo(config)) return;
			
            $q.when($http.post('/company/addCompany', $.param(config)))
            .then(function(result) {
            	var data = result.data;
				if(data.code == 200) {
					window.location.replace('/companyform/compinfo');
				}else {
					$('#comp_info_tip').show().text(data.message);
				}         	
            }, function(err) {
            	console.error('addCompany', err);
            });				
		};
		
		/* 验证公司信息*/
		function _checkCompInfo(data) {
			$scope._clearTip();
			if(!data.companyName) {
				$('#comp_info').focus();
				$('#comp_info_tip').show().text('公司名称输入有误！');
				return false;
			}
			
			return true;			
		}
 	}]);
	
	app.bootstrap();
});