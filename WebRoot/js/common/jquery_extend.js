if (typeof define !== 'function') {
	var define = require('amdefine')(module);
}

define(['$'], function($) {

	$.extend({
		
		/**
		 * 自定义弹出框
		 * 使用 $.dialog({
		 * 		id: 弹出框的id，必填
		 * 		width: 默认为400px
		 * 		title: 默认为'提醒'
		 * 		domId: 要把弹出框html插入到的DOM节点里面，默认为 document.body，但由于单页模式，所以一定要填当前页面的Id
		 * 		content: 内容，例如 <p>1234</p>
		 * 		btn: [{
		 * 			text: '按钮1',
		 * 			callback: funciton() {
		 * 				$scope.test();
		 * 			}
		 * 		}, {
		 * 			text: '按钮2',
		 * 			callback: funciton() {
		 * 				alert(1);
		 * 			}
		 * 		}]	 默认为关闭按钮
		 * });
		 * */
		dialog : function( params ) {
			var config = {
				width : 400,
				title : '提醒',
				dom : document.body,
				top : 0,
				autoShow : false,
				beforeShow : null,
				beforeClose : null
			};
			
			var _paramsInit = function ( params ){
				config.id = params.id,
				config.closeId = params.id + '_close', 
				config.width = !!params.width ? params.width : config.width;
				config.title = !!params.title ? params.title : config.title;
				config.dom = !!params.domId ? $('#'+params.domId) : $(config.dom);
				
				if( typeof params.content == 'string') {
					config.content = params.content;
				} else if ( typeof params.content == 'function' ) {
					config.content = params.content();
				}
				config.control = params.btn;
				config.autoShow = !!params.autoShow ? params.autoShow : false;
				config.beforeShow = !!params.beforeShow ? params.beforeShow : null;
				config.beforeClose = !!params.beforeClose ? params.beforeClose : null;
			};
			
			var _getPosition = function () {
				var screenWidth = $(window).width();
				var screenHeight = $(window).height();
				var scrollTop = $(window).scrollTop();
				var boxWidth = config.width;
				var _left = (screenWidth - boxWidth) / 2;
				var _top = (screenHeight - 500 ) / 2 + scrollTop;
				return {
					top : _top,
					left : _left
				};
			};
			
			var _setPosition = function() {
				var position = _getPosition();
				$("#" + config.id + " .dialog-box").css({
					top : position.top/3 ,
					left : position.left
				});
				config.top = position.top;
			};
			
			var _setShowPosition = function() {
				var position = _getPosition();
				$("#" + config.id + " .dialog-box").css({
					top : position.top,
					left : position.left
				});
				config.top = position.top;
			};
			
			var _createHtml = function () {
				var html = [];
				html.push('<div id="'+ config.id +'" class="dialog">');
				html.push('<div class="dialog-bg"></div>');
				html.push('<div class="dialog-box" >');	
				html.push('<div class="dialog-box-con" style="width: '+ config.width +'px">');	
				html.push('<div class="dialog-box-con-title">');
				html.push('<div >'+ config.title +'</div>');
				html.push('<i id="'+ config.closeId  +'"></i>');
				html.push('</div>');
				html.push('<div class="dialog-box-con-detial">');
				
				html.push('<div class="con-detial-con">');
				html.push(config.content);
				html.push('</div>');
				html.push('<div class="con-detial-operation">');
				
				var conCtrlHtml = _createBtnHtml();
				html.push(conCtrlHtml);
				
				html.push('</div>');
				html.push('</div>');
				html.push('</div>');
				html.push('</div>');
				html.push('</div>');
				
				return html.join('');;
			};
			
			var _createBtnHtml = function() {
				var btnHtml = [];
				var colorClass = '';
				if( !config.control ) {
					btnHtml.push('<div class="detial-operation-btn detial-oper-close" >关闭</div>');
					return btnHtml.join('');
				}
				var len = config.control.length;
				var btnClass= 'detial-control-btn';
				for( var i = 0; i < len; i++ ) {
					btnClass += i; 
					btnHtml.push('<div class="detial-operation-btn '+ btnClass +'" >'+ config.control[i].text +'</div>');
					btnClass = 'detial-control-btn';
				}
				return btnHtml.join('');
			};
			
			var _on = function() {
				$(document).on('click', '#'+ config.closeId, function() {
					close();
				});
				
				$(document).on('click', '#' + config.id + ' .detial-oper-close' , function() {
					close();
				});
				
				$(window).on('resize', function() {
					_setShowPosition();
				})
				.on('scroll', function() {
					_setShowPosition();
				});
				
				if( !config.control ) return;
				var len = config.control.length;
				var btnClass= 'detial-control-btn';
				for( var i = 0; i < len; i++ ) {
					btnClass += i; 
					$(document).on('click', '#'+ config.id + ' .'+btnClass, config.control[i].callback);
					btnClass = 'detial-control-btn';
				}
			};
			
			var close = function() {
				if( !!config.beforeClose ) config.beforeClose();
				$("#" + config.id + " .dialog-box").animate({
					top: config.top / 3 
				}, 300, function() {
					$("#" + config.id).hide();
				});
			};
			
			var show = function() {
				_setPosition();
				if( !!config.beforeShow ) config.beforeShow();
				$("#" + config.id).show();
				$("#" + config.id + " .dialog-box").animate({
					top: config.top
				}, 500);
			};
			
			var init = function() {
				_paramsInit(params);
				var html = _createHtml();
				config.dom.append(html);
				_setPosition();
				_on();
				if( config.autoShow ) show();
			};
			
			init();
			
			return {
				show : show,
				close: close
			}
			
		}
			
	});

	return {};
});
