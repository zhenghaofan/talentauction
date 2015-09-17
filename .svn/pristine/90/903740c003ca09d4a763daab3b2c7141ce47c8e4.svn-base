$(function($) {
	
	var query = {
			page: 1,
			pageSize: 5,
			totalPage: 0,
			exp: '',
			city: '',
			edu: '',
			spe: '',
			speCount: '',
			searches: '',
			previous: 0
	};
	
	var time = {
			day: 0,
			hour: 0,
			min: 0,
			sec: 0,
			handle: null
	};
	
	var _spe = _getParam('spe');
	var _speCount = _getParam('speCount');
	var _speName = _getParam('speName');
	
	if( !!_spe ) {
		query.spe = _spe;
		query.speCount = _speCount;
		
		$('#spec_title').text(decodeURI(_speName));
		init_Skill();
		_getDetialData();
	} else {
		T.alert('请选择专场');
	}

	//技能筛选初始化
	function init_Skill() {
		var list = getHotWords()[_spe].value;
		var html = [];
		
		for ( var i in list) {
			html.push('<li class="side-r-item-li side-skill" data-id="' + i + '">' + list[i]);
				html.push('<img class="side-r-item-s-ico" src="../css/img/app_auction_r.png"/>');
			html.push('</li>');
		}

		$('#side_skill').append(html.join(''));
	};
	
	//获取对应专场数据
	function _getDetialData() {
		T.alert('正在加载...');
		
		var params = {
				page : query.page,
				pageSize : query.pageSize,
				education: query.edu == '0' ? '' : query.edu,
				city: query.city == '0' ? '' : query.city,
				jobYear: query.exp == '0' ? '' : query.exp,
				special:  query.spe,
				specialCount: query.speCount,
				searches: query.searches,
				previous: query.previous
		};
		
		$.post('/bidme/getBidPools', params, function(res) {
			res = $.parseJSON(res);
			if( res.code == 200 ) {
				var list = res.bidPools;
				
				for ( var i in list) {
					list[i].eduStr = getEducation()[list[i].education].value;
				}
				
				if( list.length > 0 ) {
					var html = _createDetialHtml(list);
				} else {
					var html = '<li class="spec-det-li no-data">暂无数据</li>';
				}
				
				query.page = Number(res.page);
				query.totalPage = Number(res.totalPage);
				$("#spec_det_ul").append(html);
				_getCountTime();
				
				setTimeout(function() {
					T.alertClose();
				}, 300);
			} else {
				T.alertClose();
				T.alert(res.message);
			}
		});
	}
	
	// 生成专场简历
	function _createDetialHtml(list) {
		var html = [];
		for( var i = 0; i < list.length; i++ ) {
			var sums = !!list[i].summary ? list[i].summary.split(',') : ['暂无总结'];
			
			html.push('<li class="spec-det-li" data-id="'+ list[i].userId +'">');
				html.push('<a class="spec-det-box" href="/mobile/placedetail?userId=' + list[i].userId + '">');
					html.push('<div class="spec-item top m-t6">');
						html.push('<span class="spec-title fc-1 m-r10">'+ list[i].jobTitle +'</span>');
						html.push('<span class="spec-no fc-2">['+ list[i].code +'号]</span>');
					html.push('</div>');
					
					html.push('<div class="spec-item m-t6">');
						html.push('<div class="spec-info">');
							html.push('<div class="m-t6">');
								html.push('<span class="m-r10">'+ list[i].destination.split(',').join(' ') +'</span>');
								html.push('<span class="m-r10">'+ list[i].jobYear +'年经验</span>');
								html.push('<span class="m-r10">'+ list[i].eduStr +'</span>');					
							html.push('</div>');
							
							html.push('<div class="m-t6">');
								html.push('<span >期望薪资 '+ (list[i].expectedSalary < 1? "面议" : list[i].expectedSalary + "k") +'</span>');
							html.push('</div>');
						html.push('</div>');
						
						html.push('<div class="spec-introduct">');
							if( sums.length > 1 && !!sums[1] ) {
								html.push('<div class="spec-intro-item m-b5">'+ sums[0] +'</div>');
								html.push('<div class="spec-intro-item ">'+ sums[1] +'</div>');
							} else {
								html.push('<div class="spec-intro-item m-b5 m-t10">'+ sums[0] +'</div>');
							}
						html.push('</div>');						
					html.push('</div>');
					
					html.push('<div class="clear"></div>');
				html.push('</a>');
			html.push('</li>');
		}
		
		return html.join('');
	}
	
	function _getCountTime() {
		var config = {
				special: query.spec || '', 
				specialCount: query.speCount || '',
				previous: query.previous || ''
		};
		
		$.post('/bidme/getCountdown', config, function( res ) {
			res = $.parseJSON(res);
			if( res.code == 200 ) {
				time.day = res.day;
				time.hour = res.hours;
				time.min = res.minute;
				time.sec = res.second;
				_countTime();
			} else {
				 T.alert(res.message);
			}
		});
	}
	
	function _countTime() {
		if( time.day < 10 ) {
			$("#time_day").text('0' + time.day);
		} else {
			$("#time_day").text(time.day);
		}
		
		if( time.hour < 10 ) {
			$("#time_hour").text('0' + time.hour);
		} else {
			$("#time_hour").text(time.hour);
		}
		
		if( time.min < 10 ) {
			$("#time_min").text('0' + time.min);
		} else {
			$("#time_min").text(time.min);
		}
		
		if( time.sec < 10 ) {
			$("#time_sec").text('0' + time.sec);
		} else {
			$("#time_sec").text(time.sec);
		}
		clearInterval(time.handle);
		
		time.handle = setInterval(function() {
			if( time.sec >= 1 ) {
				time.sec --;
				if( time.sec < 10 ) {
					$("#time_sec").text('0' + time.sec);
				} else {
					$("#time_sec").text(time.sec);
				}
			}else if( time.min >= 1 ) {
				time.min --;
				time.sec = 59;
				if( time.min < 10 ) {
					$("#time_min").text('0' + time.min);
				} else {
					$("#time_min").text(time.min);
				}
				
				$("#time_sec").text(time.sec);
			}else if( time.hour >= 1 ) {
				time.hour --;
				time.min = 59;
				time.sec = 59;
				
				if( time.hour < 10 ) {
					$("#time_hour").text('0' + time.hour);
				} else {
					$("#time_hour").text(time.hour);
				}
				
				$("#time_min").text(time.min);
				$("#time_sec").text(time.sec);
			} else if( time.day >= 1 ) {
				time.day --;
				time.hour = 23;
				time.min = 59;
				time.sec = 59;
				
				if( time.day < 10 ) {
					$("#time_day").text('0' + time.day);
				} else {
					$("#time_day").text(time.day);
				}
				
				$("#time_hour").text(time.hour);
				$("#time_min").text(time.min);
				$("#time_sec").text(time.sec);
			} else {
				clearInterval(time.handle);
			}
			
		}, 1000);
		
	}
	
	function bodyClick() {
		var body2Dom = $("#body2");
		var menuDom = $("#menu_btn");
		$("#main_content").append('<div class="main-bg"></div>');
		
		$(".main-bg").on('tap', function(e) {
			if( screenState == 1 ) {
				screenState = 2;
				menuDom.attr('state', '0');
				body2Dom.css({left: '0'});
				$('#spec_det_ul').css({'height': 'auto'});
				$('#sidebar_r').hide();
			} else if( screenState == 3 ) {
				screenState = 2;
				body2Dom.css({left: '-220px'});
				query.page = 1;
				$("#spec_det_ul").html('');
				_getDetialData();
			}
			$(".main-bg").off('tap');
			$(".main-bg").remove();
		});
	}
	
	function clearBodyClick() {
		$("#main_content").off('tap');
	}
	

	_bind();
	
	function _bind() {
		$('#sidebar_r').css({'height': $(window).height() - 40 + 'px'});
		
		$(".cycle .text").on('click', function() {
			var str = $(this).text();
			query.page = 1;
			if(str == '上期拍卖') {
				query.previous = 1;
			}else {
				query.previous = 0;
			}
			 $(this).addClass('slt').siblings().removeClass('slt');
			$("#spec_det_ul").empty();
			_getDetialData();
		});
		
		$("#menu_btn").on('tap', function(e) {
			var body2Dom = $("#body2");
			var menuDom = $("#menu_btn");
			var flag = menuDom.attr('state');
			
			if( flag == 1 ) {
				screenState = 2;
				menuDom.attr('state', '0');
				body2Dom.css({left: 0});
				setTimeout(clearBodyClick, 0);
				$(".main-bg").off('tap');
				$(".main-bg").remove();
				$('#spec_det_ul').css({'height': 'auto'});	
				$('#sidebar_r').hide();
			} else {
				$('#sidebar_r').show().css({right: 0});
				screenState = 1;
				menuDom.attr('state', '1');
				body2Dom.css({left: '-220px'});
				e.stopPropagation();
				setTimeout(bodyClick, 0);
				$('#spec_det_ul').css({height: $(window).height() - 132 + 'px', overflow: 'hidden'});	
			}
		});
		
		/* 后退*/
		$("#query_btn").on('tap', function(e) {
			window.location.href = '/wap/placelist';
		});
			
		$(".side-r-item-title").on('tap', function() {
			var t_dom = $(this);
			var img_dom = t_dom.find('img');
			var list_dom = t_dom.next();
			var flag = t_dom.attr('state');
			if( flag == 1 ) {
				list_dom.hide();
				img_dom.attr('src', '../css/img/app_auction_q_d.png');
				t_dom.attr('state', '0');
			} else {
				list_dom.show();
				img_dom.attr('src', '../css/img/app_auction_q_p.jpg');
				t_dom.attr('state', '1');
			}
		});
		
		$("body").on('swipeLeft', function(e) {
			var body2Dom = $("#body2");
			var menuDom = $("#menu_btn");
			var flag = menuDom.attr('state');
			
			$('#sidebar_r').show().css({right: 0});
			screenState = 1;
			menuDom.attr('state', '1');
			body2Dom.css({left: '-220px'});
			e.stopPropagation();
			setTimeout(bodyClick, 0);
			$('#spec_det_ul').css({height: $(window).height() - 132 + 'px', overflow: 'hidden'});				
		});
		
		$("body").on('swipeRight', function(e) {
			var body2Dom = $("#body2");
			var menuDom = $("#menu_btn");
			var flag = menuDom.attr('state');
			
			screenState = 2;
			menuDom.attr('state', '0');
			body2Dom.css({left: 0});
			setTimeout(clearBodyClick, 0);
			$(".main-bg").off('tap');
			$(".main-bg").remove();
			$('#spec_det_ul').css({'height': 'auto'});
			$('#sidebar_r').hide();
		});
		
		$(".side-exp").on('tap', function() {
			if( $(this).hasClass('side-r-item-s') ) return;
			var id = $(this).attr('data-id');
			var text = $(this).text();
			query.exp = id;
			query.page = 1;
			$(".side-exp").removeClass('side-r-item-s');
			$(this).addClass('side-r-item-s');
			$("#exp_t").text(text);
			$("#spec_det_ul").empty();
			_getDetialData();
		});
		
		$(".side-city").on('tap', function() {
			if( $(this).hasClass('side-r-item-s') ) return;
			var text = $(this).text();
			query.city = text == '不限'? 0 : text;
			query.page = 1;
			$(".side-city").removeClass('side-r-item-s');
			$(this).addClass('side-r-item-s');
			$("#city_t").text(text);
			$("#spec_det_ul li").detach();
			$("#spec_det_ul").empty();
			_getDetialData();
		});
		
		$(".side-edu").on('tap', function() {
			if( $(this).hasClass('side-r-item-s') ) return;
			var id = $(this).attr('data-id');
			var text = $(this).text();
			query.edu = id;
			query.page = 1;
			$(".side-edu").removeClass('side-r-item-s');
			$(this).addClass('side-r-item-s');
			$("#edu_t").text(text);
			$("#spec_det_ul").empty();
			_getDetialData();
		});
		
		$(".side-skill").on('tap', function() {
			if( $(this).hasClass('side-r-item-s') ) return;
			var id = $(this).attr('data-id');
			var text = $(this).text();
			query.searches = text == '不限'? '' : text;
			query.page = 1;
			$(".side-skill").removeClass('side-r-item-s');
			$(this).addClass('side-r-item-s');
			$("#skill_t").text(text);
			$("#spec_det_ul").empty();
			_getDetialData();
		});
		
//		$(document).on('touchmove', '#spec_det_l', function() {
//			if($('#main_content').css('left') == '220px') return;
//			if($(window).scrollTop() + $(window).height() >= $(document).height()) {
//				if( query.page < query.totalPage ) {
//					query.page ++;
//					_getDetialData();
//				}
//			}
//		});
		
		$(document).on('scroll', function() {
			if($('#body2').css('left') == '-220px') return;
			if($(window).scrollTop() + $(window).height() >= $(document).height()) {
				if( query.page < query.totalPage ) {
					query.page ++;
					_getDetialData();
				}
			}
		});
	}

	/* 学历*/
	function getEducation() {
		return [{
			id: '0',
			value: '不限'
		},{
			id: '1',
			value: '大专'
		},{
			id: '2',
			value: '本科'
		},{
			id: '3',
			value: '硕士'
		},{
			id: '4',
			value: '博士'
		}]
	}	
	
	/* 热门搜索条件*/
	function getHotWords() {
		return [{
			id: '0',
			value: 'Android,iOS,HTML5,Java,PHP,UI设计,产品经理,运营总监'.split(',')
		},{
			id: '1',
			value: 'HTML5,iOS,Android,Javascript,U3D'.split(',')
		},{
			id: '2',
			value: 'Java,PHP,Python,.Net,C'.split(',')
		},{
			id: '3',
			value: '产品经理,游戏策划,产品设计师,产品总监'.split(',')
		},{
			id: '4',
			value: '网页设计,App设计,UI设计,原画师,设计总监'.split(',')
		},{
			id: '5',
			value: '内容运营,用户运营,新媒体运营,文案策划,运营总监'.split(',')
		},{
			id: '6',
			value: '网络安全,测试开发,硬件测试,游戏测试,运维开发'.split(',')
		}]
	}
	
	function _getParam( name ) {
		var query = window.location.search;
		if (name) {
			var r = new RegExp("[?&]" + name + "=([^&]*)");
			var m = r.exec(query);
			if (m) {
				return m[1];
			} else {
				return null;
			}
		} 
	}
});