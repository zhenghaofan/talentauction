require([
 '$',
 'angular',
 'app',
 'cookie',
 'url'
], 
function($, angular, app, cookie, url) {
	app.controller('intentCtrl', ['$scope', '$rootScope', '$http', '$q',
   		function($scope, $rootScope, $http, $q) {
		
		setTimeout(function() {
			$('.viewport').show(0, function() {
				$('body').css({'background-color': '#2C2F37'});
			});
		});
		
		$rootScope.tabIndex = 4;
		$scope.intent = {shield: ['']};
		
		/* 添加求职意向*/
		$scope.addIntent = function() {
			var config = {
					jobTitle : $scope.intent.jobTitle,
					expectedSalary: $('#salary').val(),
					content: $scope.intent.shield.toString()					
			};
			
			if(!_checkIntent($scope.intent.shield, config)) return;
			config.expectedSalary = _fomatFloat(config.expectedSalary/1000, 0);
			
	        $q.when($http.post('/cvmake/addIntent', $.param(config)))
	        .then(function(result) {
	        	var data = result.data;
	        	if(data.code == 200) {
	        		window.location.replace('/talentform/jobinfo');
	        	}else {
	        		$('#url_tip').show().text(data.message);
	        	}
	        }, function(err) {
	        	console.error('addIntent', err);
	        });				
		};
		
		/* 增加、移除屏蔽*/
		$scope.opt_shield = function(str, item, index) {
			if(str == '+') {
				if(item.shield.length <= 3) item.shield.push('');
			}else {
				if(item.shield.length > 0) {
					item.shield.splice(index, 1);
				}
			}
		};
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			$('.err-wrap label').text('').hide();
		};
		
		/* 输入验证*/
		$scope.keyUp = function(item) {
			var isNumber = /^[1-9]\d{0,9}$/;
			if(!isNumber.test($scope.intent[item])) $scope.intent[item] = '';
		}
		
		/* 验证求职意向*/
		function _checkIntent(item, data) {
			var isNumber = /^[1-9]\d{0,9}$/;
			
			if(!data.jobTitle) {
				$('#title').focus();
				$('#title_tip').show().text('期望职位输入有误！');
				return false;
			}else if(!isNumber.test(data.expectedSalary)) {
				$('#salary').focus();
				$('#salary_tip').show().text('期望薪资输入有误！');
				return false;
			}
			
			for ( var i in item) {
				for ( var j in item) {
					if(i == j) {
						continue;
					}else if(item[i] == item[j]) {
						$('#url_tip').show().text('请不要重复输入！');
						return false;
					}
				}
			}
			
			return true;
		}
		
		/* 保留小数*/
        function _fomatFloat(src, pos) { 
        	if(src < 1) return src;
        	
            return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);     
        }
        
		function $safeApply($scope, fn) {
			var phase = $scope.$$phase;
			if (phase == '$apply' || phase == '$digest') {
				$scope.$eval(fn);
			} else {
				$scope.$apply(fn);
			}
		}
 	}]);
	
	app.bootstrap();
});
