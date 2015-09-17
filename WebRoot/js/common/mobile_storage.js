
	var localStorage = window.localStorage;

	var LocalStorage = function() {
		this.events = new EventsSupport(this);
	};

	LocalStorage = {
		on: function() {
			this.events.addListener.apply(this.events, arguments);
		},
		off: function() {
			this.events.removeListener.apply(this.events, arguments);
		},
		fireEvent: function(name, event) {
			if (this.events) {
				this.events.fireEvent(name, event);
			}
		},
		get: function get(name) {
			var s = localStorage.getItem(name),
				obj;
			try {
				obj = s ? JSON.parse(s) : null;
			} catch (e) {
				console.log(name + " : " + s);
			}
			return obj;
		},
		set: function(name, obj) {
			var args = {};
			args[name] = {
				oldValue: this.get(name),
				value: null
			};

			if (obj === undefined) {
				localStorage.removeItem(name);
			} else {
				localStorage.setItem(name, JSON.stringify(obj));
			}
			args[name].value = obj;
			this.fireEvent("PropertyChanged", args);
		},
		destory: function(name) {
			localStorage.removeItem(name);
		}
	};