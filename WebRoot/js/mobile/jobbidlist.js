$(function($) {
	var param = {
			page: 1,
			pageSize: 5,
			totle: 0,
			reply: 0
	};
	
	var reject = {
			id: 0,
			companyId: 0,
			rejectReason: 1
	};
	
	_getData();
	_bind();
	
	function _getData() {
		T.alert('正在加载...');
		
		var config = {page: param.page,  pageSize: param.pageSize, reply: param.reply};
		
		$.post('/resume/wechatBidLogs', config, function( res ) {
			res = $.parseJSON(res);
			if( res.code == 200 ) {
				var list = res.bidLogs;
				if( list.length > 0 ) {
					var html = _createHtml(list);
					param.page = Number(res.page);
					param.totle = Number(res.totalPage);
				} else {
					var html = '<li class="con-li no-data box-shadow">暂无拍卖数据</li>';
				}
				$("#list_content").append(html);
				setTimeout(function() {
					T.alertClose();
				}, 300);
			} else {
				T.alertClose();
				T.alert(res.message);
			}
		});
	}
	
	function _createHtml(list) {
		var html = [];
		for( var i=0; i<list.length; i++ ) {
			html.push('<li class="con-li box-shadow">');
				html.push('<div class="li-title">');
					html.push('<a href="/mobile/compdefault?companyId='+ list[i].companyId +'"><span class="li-title-name">'+list[i].nickName+'</span></a>');
					html.push('<span class="li-title-city">'+list[i].cityName+'</span>');
				html.push('</div>');
				
				html.push('<div class="li-con" id="'+list[i].id+'">');
					html.push('<div class="li-con-text-b">拍卖详情</div>');
					html.push('<div class="li-con-time">'+list[i].bidTime+'</div>');
					
					if( list[i].isOption == 1) {
						html.push('<div class="li-con-text">'+ list[i].minBidPrice+'k ～ '+list[i].maxBidPrice+'k 提供股票期权</div>');
					} else {
						html.push('<div class="li-con-text">'+ list[i].minBidPrice+'k ～ '+list[i].maxBidPrice+'k</div>');
					}
					
					html.push('<div class="li-con-detial">企业：'+ list[i].workTitle +'</div>');
					
					if( list[i].isReply == 0 ) {
						html.push('<div class="li-con-btn-box" id="btn_'+list[i].id+'">');
							html.push('<span class="li-con-btn m-r10 send-contact" data-id="'+list[i].id+'" data-comid="'+list[i].companyId+'">发送联系方式</span>');
							html.push('<span class="li-con-btn reject-btn" data-id="'+list[i].id+'" data-comid="'+list[i].companyId+'">残忍拒绝</span>');
						html.push('</div>');
					} else if( list[i].isReply == 1 || list[i].isReply == 3 ) {
						html.push('<div class="li-con-btn-result">');
							html.push('<div>已答应</div>');
						html.push('</div>');
					} else if( list[i].isReply == 2 ) {
						list[i].rejectReason = !!list[i].rejectReason ? list[i].rejectReason : '无';
						html.push('<div class="li-con-btn-result">');
							html.push('<div>已拒绝</div>');
							html.push('<div class="li-con-detial">理由：'+ list[i].rejectReason+'</div>');
						html.push('</div>');
					}
				
				html.push('</div>');
			html.push('</li>');
		}
		
		return html.join('');
	}
	
	function _bind() {
		
		$(".query-item").on('tap', function() {
			var t = $(this);
			if( t.hasClass('query-item-s') ) return;
			$(".query-item-s").removeClass('query-item-s');
			t.addClass('query-item-s');
			var _val = $(this).attr('data-value');
			param.reply = _val;
			$("#list_content").html('');
			param.index = 1;
			_getData();
		});
		
		$(".reject-li").on('tap', function() {
			var t = $(this);
			if( t.hasClass('reject-li-s') ) return;
			$(".reject-li-s").removeClass('reject-li-s');
			t.addClass('reject-li-s');
			if( t.text() == '其他' ) {
				$("#reject_area").focus();
			}
			var _val = $(this).attr('data-value');
			reject.rejectReason = _val;
		});
		
		$(document).on('tap', '.reject-btn', function() {
			$("#main_content").hide();
			$("#reject_box").show();
			$('#reject_area').val('');
			reject.id = $(this).attr('data-id');
			reject.companyId = $(this).attr('data-comid');
			$(".reject-li-s").removeClass('reject-li-s');
			$(".reject-li").eq(0).addClass('reject-li-s');
			reject.rejectReason = 1;
		});
		
		$(document).on('tap', '.reject-cancle', function() {
			$("#main_content").show();
			$("#reject_box").hide();
			reject.id = 0;
			reject.companyId = 0;
		});
		
		$(document).on('tap', '.reject-submit', function() {
			var id = reject.id;
			var comId = Number(reject.companyId);
			var reason = Number(reject.rejectReason);
			var rejectText = '';
			if( reason == 0) {
				rejectText = $('#reject_area').val();
				if( !rejectText )  {
					 T.alert('请输入其他拒绝理由');
					 return;
				}
			} else {
				switch(reason) {
					case 1:
						rejectText = '薪资条件不合适';
						break;
					case 2: 
						rejectText = '地点不合适';
						break;
					case 3: 
						rejectText = '已有更好的机会';
						break;
				}
			}
			
			var config = {id: id, isReply: 2, rejectReason: rejectText};
			$.post('/resume/userReply', config, function(res) {
				res = $.parseJSON(res);
				if( res.code == 200 ) {
					$("#main_content").show();
					$("#reject_box").hide();
					$("#reject_area").blur();
					$("#list_content").html('');
					_getData();
				} else {
					T.alert(res.message);
				}
				
			});
			
		});
		
		$(document).on('tap', '.send-contact', function() {
			var id = $(this).attr('data-id');
			var comId = $(this).attr('data-comid');
			var sendEle = $(this);
			
			var config = {id: id, isReply: 1, companyId: comId};
			$.post('/resume/userReply', config, function(res) {
				res = $.parseJSON(res);
				if( res.code == 200 ) {
					var html = [];
					html.push('<div class="li-con-btn-result">');
					html.push('<div>已答应</div>');
					html.push('</div>');
					$('#btn_' + id).remove();
					$('#' + id).append(html.join(''));
				} else {
					T.alert(res.message);
				}
				
			});
		});
		
		$(document).on('scroll', function() {
			if($('#main_content').css('display') == 'none') return;
			if($(window).scrollTop() + $(window).height() + 300 >= $(document).height()) {
				if( param.page < param.totle ) {
					param.page ++;
					_getData();
				}
			}
		});
	}
});