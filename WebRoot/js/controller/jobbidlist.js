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
		$scope.compSize = info.getCompSize();
		$scope.compPhase = info.getCompPhase();
		$rootScope.tabIndex = 5;
		$scope.replayState = 0;
		$scope.pageIndex = 1;
		$scope.pageSize = 30;
		var _index = 0;
		var rjt_id = 0;
		var rjt_companyId = 0;
		
		bind();
		
		/* 清除提示*/
		$scope._clearTip = function(ele) {
			if(ele) {
				$(ele).text('').hide();
			}else {
				$('.err-wrap label').text('').hide();
			}
		};
		
		/* 关闭窗口*/
		$scope._close = function(ele, fn) {
			$scope._clearTip();
			$(ele).fadeOut(150);
			
			if(typeof _errback === 'function') fn();
			
			if(ele == '.reject-modal') {
				$(".rjt-con  input:checked").removeAttr('checked');
				$(".rjt-con  textarea").val('');
			}
		};
		
		/* 开启窗口*/
		$scope._show = function(ele, fn) {
			$scope._clearTip();
			$(ele).fadeIn(150);
			if(typeof _errback === 'function') fn();
		};
		
		/* 选择排序方法*/
		$scope.sltSort = function(status) {
			$scope.replayState = status;
			$scope.pageIndex = 1;
			$scope.getBidLogs('sort');
			var _index = 0;
		};
		
		/* 获取拍卖记录*/
		$scope.getBidLogs = function(str) {
			var config = {
					pageSize: $scope.pageSize,
					page: $scope.pageIndex,
					reply: $scope.replayState
			};			
			
            $q.when($http.post('/resume/getBidLogs', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	
            	if(data.code == 200 && data.bidLogs.length) {
            		$scope.listData = data.bidLogs;
            		$scope.getBidInfo($scope.listData[_index], str);
            	}else {
            		$scope.listData = [];
            		$scope._initPage();
            	}
            }, function(err) {
            	console.error('getBidLogs', err);
            });
		};
		
		/*获取拍卖详情*/
		$scope.getBidInfo = function(item, _event, _index) {
			var config = {
					companyId: item.companyId,
					reply: $scope.replayState
			};
			
			_index = _index;
			item.isUserRead = 1;
			
			if(_event && typeof _event != 'string') {
				$(_event.currentTarget).addClass('slt').siblings().removeClass('slt');
			}
			
			$q.when($http.post('/resume/getBidLogsInfo', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.bidInfo = _fomartResume(data.companyInfo);
            	$scope.bidList = data.bidLogs;
            	
            	if(_event == 'sort') $('.list-box .list-item:first').addClass('slt');
            	$scope._initPage();
            }, function(err) {
            	console.error('getBidLogsInfo', err);
            });
		};
		
		/* 拒绝面板*/
		$scope.rejectBox = function(_id, _companyId) {
			rjt_id = _id;
			rjt_companyId = _companyId;
			$scope._show('.reject-modal');
		};
		
		/* 拒绝操作*/
		$scope.rejectOpt = function() {
			var checkbox = $(".rjt-con  input:checked");
			var textarea = $(".rjt-con  textarea");
			var config = {
					id: rjt_id,
					companyId: rjt_companyId,
					isReply: 2
			};
			
			if(checkbox.length) {
				config.rejectReason = checkbox.next().text();
			}else if(textarea.val()) {
				config.rejectReason = textarea.val();
			}else {
				$('#rjt_tip').show().text('请选择拒绝理由！');
				return;
			}
			
			$scope._close('.reject-modal');
			$scope.subReply(config);
		};
		
		/* 接受面试*/
		$scope.agreeOpt = function(_id, _companyId) {
			var config = {
					id: _id,
					companyId: _companyId,
					isReply: 1
			};
			
			uiservice.confirm('确定联系面试吗？', function() {
				$scope.subReply(config);
			});
		};
		
		/* 提交回复*/
		$scope.subReply = function(config) {
            $q.when($http.post('/resume/userReply', $.param(config)))
            .then(function(result) {
            	var data = result.data;
         
            	if(data.code == 200) $scope.getBidLogs();
            	uiservice.notify(data.message, data.code != 200);
            }, function(err) {
            	console.error('userReply', err);
            });
		};
		
		/* 页面初始化*/
		$scope._initPage = function() {
			if($('.viewport').css('display') == 'none') {
        		setTimeout(function() {
        			$('.viewport').show(0, function() {
        				$('body').css({'background-color': '#2C2F37'});
        			});
        		});
    		}
		};
		
		/* 事件绑定*/
		function bind() {
			$('.rjt-con input:checkbox').click(function() {
				$scope._clearTip();
				$(this).siblings(':checkbox').attr('checked', !this.checked);
			});
			
			$('.rjt-box textarea').focus(function() {
				$scope._clearTip();
				$('.rjt-con input:checkbox').attr('checked', false);
			});
		}
		
		
		/* 格式化简历信息*/
		function _fomartResume(data) {
        	for(var i in $scope.compSize) {
        		if($scope.compSize[i].id == data.size)
        			data.sizeStr = $scope.compSize[i].value;
        	}
        	
        	for(var i in $scope.compPhase) {
        		if($scope.compPhase[i].id == data.progress)
        			data.pgsStr = $scope.compPhase[i].value;
        	}
        	
			return data;
		}
		
		/*验证回复*/
		function _checkReply(data) {
			$scope._clearTip();
			
			if(!data.rejectReason) {
				$('#reject_inp_tip').show().text('拒绝原因输入有误！');
				return false;
			}
			
			return true;
		}
 	}]);
	
	app.bootstrap();
});