$(function($) {
	var param = {
			page: 1,
			pageSize: 5,
			totle: 0,
			reply: ''
	};
	
	_getData();
	_bind();
	
	function _getData() {
		T.alert('正在加载...');
		
		var config = {page: param.page, pageSize: param.pageSize, reply: param.reply};
		
		$.post('/company/wechatBidLogs', config, function( res ) {
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
					html.push('<div class="li-job">');
						html.push('<span class="li-title-name"><a href="/mobile/placedetail?userId='+ list[i].userId +'">'+ list[i].jobTitle+'</a> &nbsp;</span>');
						if( list[i].isReply == 1 || list[i].isReply == 3) {
							html.push('<span class="li-title-state">已答应</span>');
						} else if(list[i].isReply == 2) {
							html.push('<span class="li-title-state">已拒绝</span>');
						} else {
							html.push('<span class="li-title-state">未回复</span>');
						}
						
					html.push('</div>');
					
					html.push('<div class="li-state">');
						html.push('<a href="/mobile/placedetail?userId='+ list[i].userId +'"><span class="li-state-no">'+ list[i].code +'号</span></a>');
						if( !!list[i].isReply ) {
							html.push('<span class="li-state-tiem">'+ list[i].replyTime +'</span>');
						}
					html.push('</div>');
					
				html.push('</div>');
			
				html.push('<div class="li-con" id="'+ list[i].id +'">');
					html.push('<div class="li-con-text-b">拍卖详情</div>');
					html.push('<div class="f-c-b">'+ list[i].bidTime +'</div>');
					if( list[i].isOption == 1) {
						html.push('<div class="li-con-text">'+ '月薪：' + list[i].minBidPrice+'k ～ '+list[i].maxBidPrice+'k 提供股票期权</div>');
					} else {
						html.push('<div class="li-con-text">'+ '月薪：' + list[i].minBidPrice+'K ～ '+list[i].maxBidPrice+'k</div>');
					}
					html.push('<div class="f-c-b">邀请短笺：'+ list[i].workTitle +'</div>');
					if( list[i].isReply == 1 ) {
						html.push('<div class="li-con-btn-box">');
							html.push('<span class="li-con-btn get-concact" data-id="'+ list[i].id +'">获取联系方式</span>');
						html.push('</div>');
					} else if( list[i].isReply == 2 ) {
						list[i].rejectReason = !!list[i].rejectReason ? list[i].rejectReason : '无';
						html.push('<div class="li-con-text">拒绝理由</div>');
						html.push('<div class="f-c-b">'+ list[i].rejectReason +'</div>');
					} else if( list[i].isReply == 3 ) {
						html.push('<div class="li-con-text">联系信息</div>');
						html.push('<div class="f-c-b">姓名：'+ list[i].name +'</div>');
						html.push('<div class="f-c-b">手机：'+ list[i].telephone +'</div>');
						html.push('<div class="f-c-b">邮箱：'+ list[i].userEmail +'</div>');
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
			var _val = t.attr('data-value');
			param.reply = _val;
			param.page = 1;
			$("#list_content").html('');
			_getData();
		});
		
		$(document).on('tap', '.get-concact', function() {
			var t = $(this);
			var id = t.attr('data-id');
			
			$.post('/company/getContacts', {id: id}, function( res ) {
				res = $.parseJSON(res);
				if( res.code == 200 ) {
					var html = [];
					html.push('<div class="li-con-text">联系信息</div>');
					html.push('<div class="f-c-b">姓名：'+ res.name +'</div>');
					html.push('<div class="f-c-b">手机：'+ res.telephone +'</div>');
					html.push('<div class="f-c-b">邮箱：'+ res.email +'</div>');
					$('#' + id).append(html.join(''));
					t.parent('.li-con-btn-box').remove();
				} else {
					 T.alert(res.message);
				}
			});
		});
		
		$(document).on('scroll', function() {
			if($(window).scrollTop() + $(window).height() + 300 >= $(document).height()) {
				if( param.page < param.totle ) {
					param.page ++;
					_getData();
				}
			}
		});
	}
});