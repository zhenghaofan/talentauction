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
	app.controller('poolCtrl', ['$scope', '$rootScope', '$http', '$q', 'info', 'uiservice',
   		function($scope, $rootScope, $http, $q, info, uiservice) {
		$rootScope.tabIndex = 3;
		$scope._spc = url.get('spc') ? url.get('spc') : '' ;
		$scope.pageIndex = 1;
		$scope.pageSize = 20;
		$scope._education = '0';
		$scope._city = '0';
		$scope._experience = '0';
		$scope._position = '0';
		$scope.sortVal = 2;
		
		$scope.jobYear = _formatSelectData(info.getJobYear(), 'id', $scope._experience);
		$scope.getHotCitys = _formatSelectData(info.getHotCitys(), 'id', $scope._city);
		$scope.education = _formatSelectData(info.getEducation(), 'id', $scope._education);
		$scope.tmpArray = [{exp: '0'}, {city: '0'}, {edu: '0'}, {sort: ''}, {special: '', specConut: ''}, {searches: '', searchType: ''}, {previous: 0}];
		$scope.jdInfo = {};
		var handle = undefined;
		
		
		/* 关闭弹出*/
		$scope._close = function(ele) {
			$(ele).fadeOut(150);
		};
		
		$scope._clearTip = function(ele) {
			if(ele) 
				$(ele).text('').hide();
			else
				$('.err-wrap label').text('').hide();
		};
		
		
		 /* 获取期数*/
		$scope.getPrevious = function(special) {
			var config = {};
			
			if(special) config.special = special;
			
            $q.when($http.post('/bidme/getPrevious', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.previous = data.previous;
            }, function(err) {
            	console.error('getPrevious', err);
            });				
		};
		
		/* 获取jd*/
		$scope.getTalentDemand = function() {
            $q.when($http.post('/company/getTalentDemand'))
            .then(function(result) {
            	var data = result.data;
				$scope.talentList = data.talentList;
				
				if($scope.talentList.length) {
					setTimeout(function() {
						$('.jd-list li').mouseenter(function(e) {
							$(this).find('.opt-ico, .add-ico').show();
						}).mouseleave(function() {
							$(this).find('.opt-ico, .add-ico').hide();
						});	
					});
				}else {
					$('.guide-modal').show();
				}
            }, function(err) {
            	console.error('getTalentDemand', err);
            });				
		};
		
		 /* 增加人才需求*/
		$scope.addJd = function() {
			var config = $scope.jdInfo;
			
			if(!_checkJdInfo(config)) return;
			if(config.website && config.website.indexOf('http') == -1) {
				config.website = 'http://' + config.website;
			}
			
            $q.when($http.post('/company/updateTalentDemand', $.param(config)))
            .then(function(result) {
            	var data = result.data;
        		$scope.jdInfo = {};
        		$scope.getTalentDemand();
            }, function(err) {
            	console.error('updateTalentDemand', err);
            });				
		};
		
		 /* 删除人才需求*/
		$scope.delJd = function(id) {
			var config = {
					id: id
			};
			
			if(uiservice.isNotify()) return;
			
            $q.when($http.post('/company/delTalentDemand', $.param(config)))
            .then(function(result) {
            	var data = result.data;
            	$scope.getTalentDemand();
            }, function(err) {
            	console.error('delTalentDemand', err);
            });				
		};
		
		/* 编辑人才需求*/
		$scope.editJd = function() {
			$('.guide-modal').css({'margin-top': '160px'}).fadeIn(150).find('.anime-wrap').hide();
		};
		
		/*获取专场列表*/
		$scope.getSpecialList = function() {
			$q.when($http.post('/bidme/getSpecialList'))
			.then(function(result) {
				var tmp = result.data.specialList;
				tmp.unshift({id:0, name: '全部', specialCount: ''});
				
				if($scope._spc && $scope._spc != 0) {
					for (var i in tmp) {
						if(tmp[i].id == $scope._spc) {
							$scope.tmpArray[4].special = tmp[i].id;
							$scope.tmpArray[4].specialCount  = tmp[i].specialCount;
							$scope.spcText = tmp[i].name;
							tmp[i].seleted = true;
						}
					}					
				}else {
					$scope.spcText = tmp[0].name;
					tmp[0].seleted = true;
				}
				
				$scope.skillsList = tmp;
				$scope.wordsList = info.getHotWords()[0].value;
				$scope.getBidPool();
			}, function(err) {
            	  console.error('getSpecialList', err);
			});			
		};
		
		/* 搜索*/
		$scope.search = function(status) {
			var text = $('#search_inp').val();
			
			if(status && text != '') return;
			
			$scope.pageIndex = 1;
			$scope.tmpArray[5].searches = text;
			$scope.getBidPool();
		};
		
		/* 热门搜索*/
		$scope.hotSearch = function(text) {
			$('#search_inp').val(text);
			$('.search-warp .drop-btn').text('职位');
			$scope.tmpArray[5].searchType = 1;
			$scope.tmpArray[5].searches = text;
			$scope.getBidPool();
		};
		
		/* 选择条件 */
		$scope.sltExe = function(item, _event, status) {
			$scope.queryBidPools = [];
			$scope.pageIndex = 1;
			if(_event) $(_event.target).addClass('item-seleted').siblings().removeClass('item-seleted');
			
			if(status == 'exp') {
				$scope.tmpArray[0].exp  = item.id;
				$scope.expText = _getText($scope.jobYear, 'id', item.id, 'value');
			}else if(status == 'city') {
				$scope.tmpArray[1].city  = item.value == '不限'? '' : item.value;
				$scope.cityText = _getText($scope.getHotCitys, 'id', item.id, 'value');
			}else if(status == 'edu') {
				$scope.tmpArray[2].edu  = item.id;
				$scope.eduText = _getText($scope.education, 'id', item.id, 'value');
			}else  if(status == 'sort') {
				if(item == 2)
					$scope.tmpArray[3].sort = '';
				else
					$scope.tmpArray[3].sort = item;
			}else if(status == 'special') {
				$scope.tmpArray[4].special = item.id == 0? '' : item.id;
				$scope.tmpArray[4].specialCount  = item.specialCount;
				$scope.spcText = item.name;
				$scope.wordsList = info.getHotWords()[item.id].value;
			}else  if(status == 'previous') {
					$scope.tmpArray[6].previous = item;
			}
			
			$scope.getBidPool();
		};
		
		/* 清除条件 */
		$scope.clearExe = function(ele) {
			$scope.queryBidPools = [];
			$scope.pageIndex = 1;
			
			if(ele == '#exp') {
				$scope.tmpArray[0].exp  = 0;
			}else if(ele == '#city') {
				$scope.tmpArray[1].city  = 0;
			}else if(ele == '#edu') {
				$scope.tmpArray[2].edu  = 0;
			}
			
			$(ele + ' ul li').each(function() {
				var _that = $(this);
				if(_that.text() == '不限') {
					_that.addClass('item-seleted').siblings().removeClass('item-seleted');
				}
			});
			
			$scope.getBidPool();
		};		
		
		
		/* 加载数据 */
		$scope.getBidPool = function() {
			var exparr = ['', '0,3', '3,5', '5'];

			var params = {
					page : $scope.pageIndex,
					pageSize : $scope.pageSize,
					jobYear: exparr[$scope.tmpArray[0].exp],
					city: $scope.tmpArray[1].city == '0' ? '' : $scope.tmpArray[1].city,		
					education: $scope.tmpArray[2].edu == '0' ? '' : $scope.tmpArray[2].edu,
					sort: $scope.tmpArray[3].sort,
					special:  $scope.tmpArray[4].special,
					specialCount: $scope.tmpArray[4].specialCount,
					searches: $scope.tmpArray[5].searches,
					searchType: $scope.tmpArray[5].searchType,
					previous: $scope.tmpArray[6].previous
			};
			
            $q.when($http.post('/bidme/getBidPools', $.param(params)))
            .then(function(result) {
            	var data = result.data;
				$(".main-containter").addClass('spec-type-'+ 2);
				
				$scope.totalPage = data.totalPage;
				$scope.pageIndex = data.page;
				$scope.queryBidPools = _formatQueryBidPools(data);
				_initPager();
				$('.viewport').show();
				$scope.getTalentDemand();
				$scope.getPrevious(params.special);
				$scope.getBidTime(params);
				
				$('html,body').animate({
					'scrollTop': 0
				}, 500);
            }, function(err) {
            	console.error('getBidPools', err);
            });
		};		
		
		
		$scope.day = 0;
		$scope.hours = 0;
		$scope.min = 0;
		$scope.sec = 0;
		
		/* 拍卖时间*/
		$scope.getBidTime = function(item) {
			var params = {
					special: item.special || '', 
					specialCount: item.specialCount || '',
					previous: item.previous || ''
			};
			
			$q.when($http.post('/bidme/getCountdown', $.param(params)))
			.then(function(result) {
				var data = result.data;
				$scope.day = data.day;
				$scope.hours = data.hours;
				$scope.min = data.minute;
				$scope.sec = data.second;
				
				clearInterval(handle);
				
				handle = setInterval(function() {
					if( $scope.sec > 1 ) {
						$scope.sec --;
						$safeApply($scope, function() {
							$scope.sec;
						});
					} else if( $scope.min > 1 ) {
						$scope.min --;
						$scope.sec = 59;
						$safeApply($scope, function() {
							$scope.min;
							$scope.sec;
						});
					} else if( $scope.hours > 1 ) {
						$scope.hours --;
						$scope.min = 59;
						$scope.sec = 59;
						$safeApply($scope, function() {
							$scope.hours;
							$scope.min;
							$scope.sec;
						});
					} else if( $scope.day > 1 ) {
						$scope.day --;
						$scope.hours = 23;
						$scope.min = 59;
						$scope.sec = 59;
						$safeApply($scope, function() {
							$scope.day;
							$scope.hours;
							$scope.min;
							$scope.sec;
						});
					} else {
						clearInterval(handle);
					}
					
				}, 1000);
			}, function(err) {
				console.error('getCountdown', err.message);
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
			$scope.getBidPool();
		};		
		
		/* 初始化翻页*/
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
		
		function _formatSelectData(data, key, value) {
			for( var i = 0, len = data.length; i < len; i++) {
				if(data[i][key] == value) {
					data[i].seleted = true;
				} else {
					data[i].seleted = false;
				}
			}
			return data;
		}
		
		function _getText(data, key, value, name) {
			var text;
			for( var i = 0, len = data.length; i < len; i++) {
				if(data[i][key] == value) {
					name = !!name ? name : 'name';
					text = data[i][name];
				} 
			}
			return text;
		}
		
		function _formatQueryBidPools(args) {
			if (angular.isUndefined(args.bidPools)) return args;
			var bidPools = args.bidPools ? args.bidPools : [];
			for (var i = 0; i < bidPools.length; i++) {
				if (!bidPools[i].education) {
					args.bidPools[i].education = '不祥';
				} else if (bidPools[i].education == 1) {
					args.bidPools[i].education = '大专';
				} else if (bidPools[i].education == 2) {
					args.bidPools[i].education = '本科';
				} else if (bidPools[i].education == 3) {
					args.bidPools[i].education = '硕士';
				} else if (bidPools[i].education == 4) {
					args.bidPools[i].education = '博士';
				} 

				if (bidPools[i].status == '0') {
					bidPools[i].statusText = '已看';
				}else if (bidPools[i].status == '1') {
					bidPools[i].statusText = '已拍';
				}
				
				if( !bidPools[i].maxPrice) bidPools[i].maxPrice = 0;
				if( !bidPools[i].num ) bidPools[i].num = 0;
				if(bidPools[i].summary != null && !angular.isUndefined(bidPools[i].summary)) {
					bidPools[i].summary = bidPools[i].summary.split(',');
					if( bidPools[i].summary.length == 1 || !bidPools[i].summary[1] ) {
						bidPools[i].sunmarylength = 1;
					}
				}
				
				if( !bidPools[i].avgPrice ) bidPools[i].avgPrice = 0;
				
				bidPools[i].destination = bidPools[i].destination.split(',').join(' ');
				
			}
			
			for(var i in args.bidPools) {
				if(args.bidPools[i].weight) args.bidPools[i].weight = parseInt(Number(args.bidPools[i].weight) * 100);
			}
			
			return args;
		}
		

//		/* 绑定下拉框*/
//		$(document).on("click", "#drop_down", function() {
//			var obj = $(this);
//			obj.find('#drop_list').toggle();
//			
//			obj.find('#drop_list li').click(function() {
//				$('.drop-btn').text($(this).text());
//			});
//		});
		
		/* 验证人才需求*/
		function _checkJdInfo(data) {
			var isNumber = /^[1-9]\d{0,9}$/;
			$scope._clearTip();
			
			if(!data.skills) {
				$('#jd_skills').focus();
				$('#jd_skills_tip').show().text('职位输入有误！');
				return false;
			}else if(!isNumber.test(data.minJobYear)) {
				$('#jd_exp_min').focus();
				$('#jd_exp_tip').show().text('最低经验输入有误！');
				return false;
			}else if(data.minJobYear > 18) {
				$('#jd_exp_min').focus();
				$('#jd_exp_tip').show().text('最低经验不太合理！');
				return false;
			}else if(!isNumber.test(data.maxJobYear)) {
				$('#jd_exp_max').focus();
				$('#jd_exp_tip').show().text('最高经验输入有误！');
				return false;
			}else if(data.maxJobYear > 18) {
				$('#jd_exp_max').focus();
				$('#jd_exp_tip').show().text('最高经验不太合理！');
				return false;
			}else if(Number(data.maxJobYear) < Number(data.minJobYear)) {
				$('#jd_exp_max').focus();
				$('#jd_exp_tip').show().text('经验范围输入有误！');
				return false;
			}else if(!isNumber.test(data.minSalary)) {
				$('#jd_salary_min').focus();
				$('#jd_salary_tip').show().text('最低薪资输入有误！');
				return false;
			}else if(data.minSalary > 50) {
				$('#jd_salary_min').focus();
				$('#jd_salary_tip').show().text('最低薪资不太合理！');
				return false;
			}else if(!isNumber.test(data.maxSalary)) {
				$('#jd_salary_max').focus();
				$('#jd_salary_tip').show().text('最高薪资输入有误！');
				return false;
			}else if(data.maxSalary > 50) {
				$('#jd_salary_max').focus();
				$('#jd_salary_tip').show().text('最高薪资不太合理！');
				return false;
			}else if(Number(data.maxSalary) < Number(data.minSalary)) {
				$('#jd_salary_max').focus();
				$('#jd_salary_tip').show().text('薪资范围输入有误！');
				return false;
			}else if(uiservice.isNotify()) {
				return false;
			}
			
			return true;			
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
