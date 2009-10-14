var McsBrowser = new Class({
	name: new String(),
	platform: new String(),
	version: new String(), 
	telSupport: new String(),
	wtaiSupport: new Array(),
	
	initialize: function(bName, bPlatform, bversion, btel, bwtai) {
		this.name = bName;
		this.platform = bPlatform;
		this.version = bversion;
		this.telSupport = btel;
		this.wtaiSupport = (bwtai != "" ) ? bwtai.split(" ") : this.wtaiSupport;
	}
});

var McsCustom = new Class({
	businessPhone: false,
	imageCategory: new String("S"),
	
	initialize: function(bPhone, imgCat) {
		this.businessPhone = bPhone;
		this.imageCategory = imgCat;
	}
});

var McsOutput = new Class({
	pixelsx: 120,
	pixelsy: 120,
	
	initialize: function(px, py) {
		this.pixelsx = px;
		this.pixelsy = py;
	}
});

var McsDevice = new Class({
	Name: new String(),
	Browser: null,
	Custom: null,
	Output: null,
	
	initialize: function(dname, dbrowser, dcustom, doutput) {
		this.Name = dname;
		this.Browser = dbrowser;
		this.Custom = dcustom;
		this.Output = doutput;
	}
});