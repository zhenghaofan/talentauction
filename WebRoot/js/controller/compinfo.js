require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'info'
], 
function($, angular, app, cookie, url) {
	app.controller('infoCtrl', ['$scope', '$rootScope', '$http', '$q', 'info',
   		function($scope, $rootScope, $http, $q, info) {
		$rootScope.tabIndex = 4;
		$scope.compInfo = {area: '1000', size: '0', progress: '0'};
		$scope.webContext = {};
		$scope.webContext.compSizeList = info.getCompSize();
		$scope.webContext.compPhaseList = info.getCompPhase();
		
		/* 清除提示*/
		$scope._clearTip = function() {
			$('.err-wrap label').text('').hide();
		};
		
		/* 获取公司信息*/
		$scope.getCompInfo = function() {
            $q.when($http.post('/company/getCompanyInfo', $.param({})))
            .then(function(result) {
            	var data = result.data;
            	$scope.compInfo.name = data.companyInfo.name;
            	$scope.getAreasList();
            	$scope.getProvinceList();
            	$('.viewport').show();
            }, function(err) {
            	console.error('getCompanyInfo', err);
            });				
		};
		
		/* 获取省份*/
		$scope.getProvinceList = function() {
			var config = {
					superId: 0
			};		
			
            $q.when($http.post('/common/getCityList', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.webContext.provinceList = data.cityList;
            	$scope.compInfo.province = data.cityList[0].id;
            	$scope.getCityList(data.cityList[0].id);
            }, function(err) {
            	console.error('getAreasList', err);
            });				
		};
		
		/* 获取城市*/
		$scope.getCityList = function(id) {
			var config = {
					superId: id
			};
			
            $q.when($http.post('/common/getCityList',  $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.webContext.cityList = data.cityList;
            	$scope.compInfo.city = data.cityList[0].id;
            }, function(err) {
            	console.error('getAreasList', err);
            });				
		};
		
		/* 获取行业领域*/
		$scope.getAreasList = function() {
            $q.when($http.post('/common/getAreasList'))
            .then(function(result) {
            	var data = result.data;
            	$scope.webContext.areasList = data.areasList;
            }, function(err) {
            	console.error('getAreasList', err);
            });				
		};
		
		/* 选择公司阶段*/
		$scope.sltPhase = function(id) {
			$scope.compInfo.progress = id;
		};
		
		/* 保存公司基本信息*/
		$scope.saveCompInfo = function() {
			var config = {
					name: $scope.compInfo.name,
					nickName: $scope.compInfo.nickName,
					province: $scope.compInfo.province,
					city: $scope.compInfo.city,
					addr: $scope.compInfo.addr,
					area: $scope.compInfo.area,
					size: $scope.compInfo.size,
					companyIntro: $scope.compInfo.companyIntro,
					progress: $scope.compInfo.progress
			};
			
			if(!_checkCompInfo(config)) return;

            $q.when($http.post('/company/saveCompanyInfo', $.param(config)))
            .then(function(result) {
            	var data = result.data;
				if(data.code == 200) {
					window.location.replace('/companyform/compdefault');
				}else {
					$('#comp_info_tip').show().text(data.message);
				}   	
            }, function(err) {
            	console.error('saveCompanyInfo', err);
            });				
		};
		
		/* 验证公司信息*/
		function _checkCompInfo(data) {
			$scope._clearTip();
			if(!data.name.length || data.name.length>100) {
				$('#comp_name').focus();
				$('#comp_name_tip').show().text('公司名称长度应该在1~100之间！');
				return false;
			}else if(!data.nickName || data.nickName.length>100) {
				$('#comp_nickname').focus();
				$('#comp_nickname_tip').show().text('公司简称长度应该在1~100之间！');
				return false;				
			}else if(!data.addr || data.addr.length>150) {
				$('#comp_addr').focus();
				$('#comp_addr_tip').show().text('详细地址长度应该在1~150之间！');
				return false;				
			}else if(!data.companyIntro || data.companyIntro.length>800) {
				$('#comp_intro').focus();
				$('#comp_intro_tip').show().text('公司介绍长度需在1~800之间！');
				return false;				
			}
			
			return true;			
		}
 	}]);
	
	app.bootstrap();
});
