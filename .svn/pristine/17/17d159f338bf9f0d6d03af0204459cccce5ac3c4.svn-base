if (typeof define !== 'function') {
	var define = require('amdefine')(module);
}

define([], function() {
	return {
		/*
		 * util.toQueryString({a:'a01',b:2})   ->  '?a=01&b=2'
		 */
		toQueryString: function(obj, q) {
			q = q || '?';
			var array = [],
				k, v;
			for (k in obj) {
				v = obj[k];
				if (typeof v === 'function') {
					continue;
				}
				array.push(k + '=' + encodeURIComponent(v));
			}
			return (array.length > 0) ? (q + array.join('&')) : '';
		},

		/*
		 * util.formatDate()                            ->  2014/05/09 09:40:24
		 * util.formatDate(new Date(1900,10,10,9,0,0))  ->  1900/10/10 09:00:00
		 */
		formatDate: function(date, format) {
			format = format || "yyyy/MM/dd HH:mm:ss";
			var d = date ? date : new Date();
			var o = {
				"M+": d.getMonth() + 1, //month
				"d+": d.getDate(), //day
				"H+": d.getHours(), //hour
				"m+": d.getMinutes(), //minute
				"s+": d.getSeconds() //second
			};

			if (/(y+)/.test(format)) {
				format = format.replace(RegExp.$1, (d.getFullYear() + "").substr(4 - RegExp.$1.length));
			}
			for (var k in o) {
				if (new RegExp("(" + k + ")").test(format)) {
					format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
				}
			}

			return format;
		},

		/*
		 * util.toDate(1399599725)   ->  Fri May 09 2014 09:42:05 GMT+0800  (Date Object)
		 */
		toDate: function(secondTicket) {
			return new Date(secondTicket * 1000);
		}
	};
});