/** 
	Timer object. Used to track a timer/stopwatch when the console calls a time(name/id).
**/
var Timer = new Class({
	id: new String(),
	start: null,
	end: null,
	
	initialize: function(n) {
		this.id = n;
		this.start = $time();
	},
	
	stop: function() {
		this.end = $time();
		return this.end - this.start;
	}
});

/** 
	The Logger class/object. Treated as a singleton
**/
var Logger = new Class({
	Times: new Array(),
	LOG: null,
	
	/***
		Start a timer with a name/id.
		Works pretty much like console.time() in firebug.
	***/
	time: function(name) {
		var l = this.Times.length;
		var nT = new Timer(name);
		this.Times[l] = nT;
	},

	/***
		Stop a timer of the given name/id.
		Works pretty much like console.timeEnd() in firebug.
	***/
	timeEnd: function(name) {
		var et = null;
		var i;
		for(i=0; i<this.Times.length; i++) {
			if(this.Times[i].id == name) {
				et = this.Times[i].stop(); break;
			}
		}
		if(et != null) {
			this.log(name + ": " + et + " ms"); 
			this.Times.splice(i,1);
		}
	},

	/*** 
		Initialization of the debugger/logger.
		It will append the logger area and command line field at the end of the document body.
	***/
	initialize: function() {
		// Start the pageload timer
		this.time('pageLoad');
		
		// Create our log area
		this.LOG = document.createElement("div");
		this.LOG.id = "logger";
		this.LOG.set('style', 'width: 100%; height: 100px; font-size: 90%; border: 1px solid #000; overflow-y: auto;');
		
		
		// Inject the log area into our document
		window.onload = function() { 
			document.getElementsByTagName("body")[0].appendChild(this.LOG);
			this.log("Logger started ...");
			this.timeEnd('pageLoad');
			this._cmd();
		}.bind(this);
	},
	
	/***
		This method creates a cmd input field and a button that will try to evaluate the input field's 
		value as if it is a javascript code. Private method called when the logger object is instantiated.
	***/
	_cmd: function() {
		var cmdField = document.createElement("input");
		cmdField.id = "cmd";
		cmdField.style.width = '95%';
		cmdField.style.fontSize = '80%';

		var cmdRun = document.createElement("input");
		cmdRun.type = "button";
		cmdRun.value = "Run";
		cmdRun.style.border = '1px solid black';

		cmdRun.setAttribute("onclick", "_exec(document.getElementById('cmd').value);return false;");

		/* Add the command elements to the document */
		var b = document.getElementsByTagName("body")[0];
		b.appendChild(cmdField);
		b.appendChild(cmdRun);
	},

	/***
		Private method to log messages. It takes care of all 
		the underlying innerHTML for various devices.
	***/
	_log: function(msg, type) {
		if(this.LOG) {
			var newLogLine = new Element('span');

			// Show errors in red
			if (type == "warn") {
				newLogLine.set('style', 'color: red;display: block;clear: left; padding: 3px 3px; border-top: 1px solid #ccc;');
			} else {
				newLogLine.set('style', 'display: block;clear: left; padding: 3px 3px; border-top: 1px solid #eee;');
			}
			
			newLogLine.set('text', msg);
			this.LOG.appendChild(newLogLine);
			this.LOG.scrollTop = this.LOG.scrollHeight;
		}
	},

	/***
		The neat interface for log(). Use this one for logging any message.
		All it does is calling the underlying _log();
	***/
	log: function(msg) { this._log(msg, ""); },
	
	warn: function(msg) { this._log(msg, "warn"); },

	/***
		Method to clear out the "logger"
	***/
	clear: function() { this.LOG.empty(); }, 

	/***
		Spit out the browser profile. Including the user agent
	***/
	browser: function() {
		this.log("Browser name: " + Browser.Engine.name);
		this.log("Browser version: " + Browser.Engine.version);
		this.log("Browser platform: " + Browser.Platform.name);
		this.log("User Agent: " + navigator.userAgent);
	},

	/***
		Private method to get a function name given a function object
	***/
	_getFuncName: function(FC) {
		if(FC.name) return FC.name;
		var fcStr = FC.toString().split("\n")[0];
		var expr = new RegExp("^function ([^\s(]+).+");
		if(expr.test(fcStr)) 
			return fcStr.split("\n")[0].replace(expr, "$1") || "anonymous";
		return "anonymous";
	},

	/***
		Private method to get a function arguments given a function object
	***/
	_getFuncArgs: function(FC) {
		var args = FC.arguments;
		var _args = new Array();
		var j;
		if(args.length > 0) {
			for(j=0; j<args.length; j++) 
				_args.push(args[j]);
		}

		var impArgs = _args.length > 0 ? _args.join(', ') : "";
		return "(" + impArgs + ")";
	},

	/***
		Method to print out the stack trace from the point it is called. 
		Acts like putting a break point in a class/function. 
		It will print the deepest function/object in the trace first.
		Will call the _getFuncArgs and _getFuncName. 
		Works somewhat similar to console.trace();
		NOTE: it doesn't work on recursive function/method
	***/
	trace: function() {
		var current = this.trace.caller;
		var stack = new Array();
		while(current) {
			var s = this._getFuncName(current) + this._getFuncArgs(current);
			stack.push(s);
			current = current.caller;
		}
		this.log("-------- START trace --------");
		this.log(stack.join('\n'));
		this.log("-------- FINISH trace --------");
	},

	/***
		A method to trace the response header and load time for a given uri. It will try to launch an asynchronous
		request to the server and spit out the header + the load time. Set the 'fresh' (2nd argument) to true
		to force the browser to omit the cache (will try to make a request as if it is a new one).
	***/
	getResponseHeader: function(uri, fresh) {
		var name = "Load time for " + uri;
		var toSend = null;

		if(fresh) {
			var r = Math.random() * 1000000000000;
			name = "Load time for " + uri + '?r=' + r;
			toSend = "r="+r;
		}

		var ajax = new Request({
			method: 'get', 
			url: uri,
			onSuccess: function(){ 
				this.timeEnd("Processing " + name);
				this.log("------------- Header Response -------------");
				this.log("Age: " + ajax.getHeader("Age"));
				this.log("Date: " + ajax.getHeader("Date"));
				this.log("Content-Length: " + ajax.getHeader("Content-Length"));
				this.log("Content-Type: " + ajax.getHeader("Content-Type"));
				this.log("Server: " + ajax.getHeader("Server"));
				this.log("Etag: " + ajax.getHeader("Etag"));
				this.log("Last Modified: " + ajax.getHeader("Last-Modified"));
				this.log("-------------------------------------------");
				if(!fresh) {
					Cookie.write(uri+'-etag', ajax.getHeader("Etag")); 
				}
			}.bind(this),
			onComplete: function() {
				this.timeEnd("Data " + name);
			}.bind(this)
		});

		this.time("Processing " + name); this.time("Data " + name); 
		if(!fresh) ajax.setHeader('If-None-Match', Cookie.read(uri+'-etag')); 
		ajax.send(toSend);
	}
});

var sensis = new Logger();

/** This function is padded to onclick of the "Run" button instantiated by the logger._cmd(); **/
function _exec(line) {
	if(line == "") return;
	sensis.log("Executing: " + line);
	try{ eval(line); }
	catch(err) { sensis.warn(err); }
}