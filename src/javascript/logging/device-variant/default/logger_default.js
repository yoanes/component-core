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
	lineBreak: "\n",
	LOG: null,
		
	/***
		Start a timer with a name/id.
		Works pretty much like console.time() in firebug.
	***/
	time: function(name) {
		this.Times.push(new Timer(name));
	},
	
	/***
		Stop a timer of the given name/id.
		Works pretty much like console.timeEnd() in firebug.
	***/
	timeEnd: function(name) {
		var et = null;
		var timesLength = this.Times.length;
		for(var i=0; i<timesLength; i++) {
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
		this.time('pageLoad');
		this.LOG = document.createElement("textarea");
		this.LOG.id = "logger";
		this.LOG.style.width = '99%';
		this.LOG.style.height = '100px';
		this.LOG.style.fontSize = '90%';
		this.LOG.style.overflowY = "auto";
		
		var _initLog = function() {
			this.log("Logger started ...");
			this.timeEnd('pageLoad');
			this._cmd();
		}.bind(this);
		
		window.addEvent("load", function() {
			document.getElementsByTagName("body")[0].appendChild(this.LOG);
			_initLog();
			return false;
		}.bind(this));
	},
	
	/***
		Private method to log messages. It takes care of all 
		the underlying innerHTML for various devices.
	***/
	_log: function(msg) {
		var dL = this.LOG;
		if(dL) {
			var t = dL.innerText = dL.textContent = dL.innerHTML = dL.value;
			if(t != "") t += this.lineBreak;
			t += msg;
			dL.innerText = dL.textContent = dL.innerHTML = dL.value = t;
			dL.scrollTop = dL.scrollHeight;
		}
	},
	
	/***
		The neat interface for log(). Use this one for logging any message.
		All it does is calling the underlying _log();
	***/
	log: function(msg) { this._log(msg); },
	warn: function(msg) { this._log(msg); },
	
	/***
		Method to clear out the "logger"
	***/
	clear: function() {
		var log = this.LOG;
		log.innerText = log.textContent = log.innerHTML = log.value = "";
	}, 
	
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
		var prev = null;
		
		while(current) {
			var s = this._getFuncName(current) + this._getFuncArgs(current);
			stack.push(s);
			if(prev != current.caller) { // try to detect cross referencing function like the mootools does at the backend
				prev = current;
				current = current.caller;
			}
			else break;
		}
		this.log("-------- START trace --------");
		this.log(stack.join('\n'));
		this.log("-------- FINISH trace --------");
	},
	
	/***
		This method creates a cmd input field and a button that will try to evaluate the input field's 
		value as if it is a javascript code. Private method called when the logger object is instantiated.
	***/
	_cmd: function() {
	
		var cmdField = document.createElement("input");
		cmdField.type = "text";
		cmdField.id = "cmd";
		cmdField.style.width = '100%';
		cmdField.style.fontSize = '80%';
		
		var cmdRun = document.createElement("input");
		cmdRun.type = "button";
		cmdRun.value = "Run";
	 	cmdRun.id = "runButton";
	 
	 	var cmdSource = document.createElement("input");
	 	cmdSource.type = "button";
	 	cmdSource.value = "View Source";
	 	cmdSource.id = "sourceButton";
	 	
 		var cmdClear = document.createElement("input");
	 	cmdClear.type = "button";
	 	cmdClear.value = " Clear Log";
	 	cmdClear.id = "clearButton";
	 		
	 	cmdRun.addEvent('click', function() { _exec($('cmd').value); return false; });
	 	cmdSource.addEvent('click', function() { _viewSource(); return false; });
	 	cmdClear.addEvent('click', function() { _clear(); return false; });
    	
		cmdClear.style.border = cmdSource.style.border = cmdRun.style.border = '1px solid black';
		
		var b = document.getElementsByTagName("body")[0];
		b.appendChild(cmdField); 
		b.appendChild(cmdRun);	
		b.appendChild(cmdSource);
		b.appendChild(cmdClear);
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
				if(!fresh)
					Cookie.write(uri+'-etag', ajax.getHeader("Etag")); 
			}.bind(this),
			onComplete: function() {
				this.timeEnd("Data " + name);
			}.bind(this)
		});

		this.time("Processing " + name); this.time("Data " + name); 
		if(!fresh) ajax.setHeader('If-None-Match', Cookie.read(uri+'-etag')); 
		ajax.send(toSend);
	},
	
	/** recurse through the DOM tree and print it to the log **/
	traverseNode: function(elementObj, nodeStack) {
		/** ignore these nodes:
		 	- those generated by the logger class
		 	- all comments
		 	- firebug injected nodes
		 **/
		if(elementObj.nodeName == "#comment" || elementObj.id == "cmd" || 
		   elementObj.id == "logger" || elementObj.id == "runButton" || elementObj.id == "clearButton" || 
		   elementObj.id == "sourceButton" || elementObj.id == "_firebugConsole" ||
		   elementObj.id == "_firebugCommandLineInjector") 
			return;
		
		var node = elementObj; 
		var nodeString = new String();
		var spaceJump = new String();
		var nsc = 0; var n = 0; var a = 0;
		
		for(nsc=0; nsc<nodeStack; nsc++) spaceJump = spaceJump.concat("  ");	
		
		// Handles the TextNode separately
		if(node.nodeName == "#text") {
			if(node.textContent && node.textContent != "")
				this.log(spaceJump.concat(node.textContent));		
			else if(node.nodeValue != "") 
				this.log(spaceJump.concat(node.nodeValue));		
			else if(node.innerHTML != "")
				this.log(spaceJump.concat(node.innerHTML));
			return;
		}
		
		// Print the opening tag with all attributes if they exist
		nodeString = nodeString.concat("<",node.nodeName); 
		var nodeAttributesLength = node.attributes.length;
		if(nodeAttributesLength > 0) {
			for(n=0; n<nodeAttributesLength; n++)
				nodeString = nodeString.concat(" " + node.attributes[n].nodeName + "=\"" + node.attributes[n].nodeValue + "\"");
		}
		nodeString = nodeString.concat(">");
		this.log(spaceJump.concat(nodeString)); 
		
		// Traverse down the childNodes if there's one or more
		var childNodesLength = node.childNodes.length;	
		if(childNodesLength > 0) {
			for(a=0; a<childNodesLength; a++) 
				this.traverseNode(node.childNodes[a], nodeStack + 1);
			
			// Print the closing tag		
			nodeString = "</" + node.nodeName + ">";
			this.log(spaceJump.concat(nodeString));
		}
		return;
	},
	
	extract: function(obj) {
		var c = 0;
		sensis.log("---- Start extracting ----");
		for(var attr in obj) {
			sensis.log(attr + ': ' + obj[attr]); c++;
		}
		sensis.log("Total attributes found: " + c);
		sensis.log("---- End of extraction ----");
	}
});

var sensis = new Logger();

/** This function is padded to onclick of the "Run" button instantiated by the logger._cmd(); **/
function _exec(line) {
	if(line == "") return;
	sensis.log("Executing: " + line);
	try{ 
		var result = eval(line); 
		if($defined(result)) sensis.log(result);
	}
	catch(err) { 
		if($defined(err.lineNumber))
			sensis.warn(err + ' @ line ' + err.lineNumber);
		else sensis.warn(err);
		if($defined(err.fileName)) sensis.warn(err.fileName);
	}
}

/** This function is padded to onclick of the "View Source" button instantiated by the logger._cmd(); **/
function _viewSource() { sensis.traverseNode(document.getElementsByTagName("html")[0], 0); }

/** This function is padded to onclick of the "Clear Log" button instantiated by the logger._cmd(); **/
function _clear() { sensis.clear(); }