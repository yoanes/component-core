var MobileRequest = new Class({
	Extends: Request,
	
	initialize: function(options) {
		this.options.headers['user-agent'] = navigator.userAgent;
		this.parent(options);
	}
});