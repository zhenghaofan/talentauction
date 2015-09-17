require([
 '$',
 'angular',
 'app',
 'cookie',
 'url',
 'info',
 'uiservice',
 // 'localstorage',
], 
function($, angular, app, cookie, url) {
	app.controller('poolCtrl', ['$scope', '$rootScope', '$http', '$q', 'info','uiservice',
   		function($scope, $rootScope, $http, $q, info, uiservice) {
		
		$rootScope.tabIndex = 8;
		$scope.pageIndex = 1;
		$scope.pageSize = 20;
		$scope._searchType = 1;
		$scope._education = '0';
		$scope._city = '0';
		$scope._experience = '0';
		$scope._position = '0';
		$scope.sortVal = 2;
		$scope.pkgPice = 499;
		$scope.placeholder = '请输入职位、技能、城市、学历等';
		$scope.searchType = _formatSelectData(info.getSearchType(), 'id', $scope._searchType);
		$scope.jobYear = _formatSelectData(info.getJobYear(), 'id', $scope._experience);
		$scope.hotCity = _formatSelectData(info.getHotCitys(), 'id', $scope._city);
		$scope.education = _formatSelectData(info.getEducation(), 'id', $scope._education);
		$scope.tmpArray = [{exp: '0'}, {city: '0'}, {edu: '0'}, {sort: ''}, {special: '', specConut: ''}, {searches: '', searchType: '1'}];
		$scope.getGold = function() {
			$q.when($http.post('/fees/getGold'))
			.then(function(result) {
				$scope.bidNum = Math.floor(result.data.gold/50);
			});
		}

		$scope.switchTop = "基本信息";
		
		$scope.getGold();
			
		/* 关闭弹出*/
		$scope._close = function(ele) {
			$(ele).animate({"opacity":"hide","top":"-10px"});
		};
		
		/* 充值弹窗*/
		$scope.showPay = function() {
			$('.guide-modal').animate({"opacity":"show","top":"10px"});
		};
		
		$scope.showPay1 = function() {
			$('#err_modal').css('display','none');
			$('.guide-modal').fadeIn(150);
		};
		
		$scope.tabitemclick = function(){
			$('.tab-box').fadeIn(150);
		}
		$scope.mouseleave = function(){
			$('.tab-box').fadeOut(150);
		}
		
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
			
			$('.tab-box').fadeOut(150);
			if(_event) $(_event.target).addClass('slt').siblings().removeClass('slt');
			
			if(status == 'type') {
				$scope.tmpArray[5].searchType = item.id;
				$scope.switchTop = item.value;
				switch(item.id){
				case '1':
					$scope.placeholder = '请输入职位、技能、城市、学历等';
					break;
				case '2':
					$scope.placeholder = '例如：O2O、金融、银行等项目经验';
					break;
				case '3':
					$scope.placeholder = '例如：百度、阿里、腾讯等工作经历';
					break;
				default:
					break;
			}
				
			}
			
			$scope.queryBidPools = [];
			$scope.pageIndex = 1;

			if(status == 'exp') {
				$scope.tmpArray[0].exp  = item.id;
				$scope.expText = _getText($scope.jobYear, 'id', item.id, 'value');
			}else if(status == 'city') {
				$scope.tmpArray[1].city  = item.value == '不限'? '' : item.value;
				$scope.cityText = _getText($scope.hotCity, 'id', item.id, 'value');
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
					type: $scope.tmpArray[5].searchType
			};
			
            $q.when($http.post('/fees/getResumePools', $.param(params)))
            .then(function(result) {
            	var data = result.data;
				
				$scope.totalPage = data.totalPage;
				$scope.pageIndex = data.page;
				$scope.total = data.total;
				$scope.queryBidPools = _formatQueryBidPools(data);
				_initPager();
				$('.viewport').show();

				$('html,body').animate({
					'scrollTop': 0
				}, 500);
            }, function(err) {
            	console.error('getBidPools', err);
            });
		};
		
		/* 套餐选择*/
		$scope.pkgType = 1;
		$scope.sltPkg = function(_event, type, pice) {
			$(_event.currentTarget).addClass('slt').siblings().removeClass('slt');
			$scope.pkgPice = pice;
			$scope.pkgType = type;
		};
		
		/* 跳转支付*/
		$scope.jumpPay = function() {
			
			
			var xhr = new XMLHttpRequest();
		    xhr.open("GET", "/fees/getRechargeLogTotal", false);  // synchronous request
		    xhr.send(null);
		    try {
		    	var result = angular.fromJson(xhr.responseText);
				if(result.code==200) {
			    	window.open('/vip/vippay?type=' + $scope.pkgType);
			    	$scope._close('.guide-modal');
			    }else {
			    	uiservice.notify('请正确操作。', result.code != 200);
			    }
			} catch (e) {
				uiservice.notify('请正确操作。', true);
			}
		};
		
		/*余额不足弹出*/
		$scope.bidList = function(bidlog){
			userId = bidlog.userId;
			var xhr = new XMLHttpRequest();
		    xhr.open("GET", "/fees/getGold", false);  // synchronous request
		    xhr.send(null);
		    try {
		    	var result = angular.fromJson(xhr.responseText);
				if(Math.floor(result.gold/50)<1) {
					$('#err_modal').css('display','block');
			    }else {
			    	// window.location.reload();
			    	window.open('/vip/vipbid?sd='+userId);//window.location.host
			    	bidlog.readStatus = 0;
			    }
			} catch (e) {
				uiservice.notify('请正确操作。', true);
			}
		}


		/* 数据转换 */
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
		$scope.closer = function(){
			$(".").addClass('slt').siblings().removeClass('slt');
		}
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
 	}])
	.directive(

		);
	
	app.bootstrap();
});
