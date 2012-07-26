var APPLICATION_PATH = '/msxt';
var Calendar={
	source: APPLICATION_PATH+"/resources/calendar/gw_calendar.html",
	create: function( elem ) {
		if ( elem.tagName.toLowerCase()!="input" 
			 || elem.type.toLowerCase()!="text") 
			return;
		
		function CalendarAssistant(elem) {
			var wrapper,frame,timerId,repeater,_this=this,listeners=[],format=elem.getAttribute("_format");
			var defaultDate=elem.value;
			if (!format || format.length==0) 
				format="YYYY-MM-DD";
			
			if (defaultDate!=0) 
				this.defaultDate = defaultDate;
			
			this.addChangeListener=function(listener) {
				listeners.push(listener);
				DOM.addEventHandler(elem,"change",listener);
			};
			
			this.removeChangeListener=function(listener) {
				listeners.remove(listener);
				DOM.removeEventHandler(elem,"change",listener);
			};
			
			this.cancelTimer=function() {
				if (timerId) {
					window.clearTimeout(timerId);
					timerId=null;
				}
				if (repeater) {
					window.clearInterval(repeater);
					repeater=null;
				}
			};
			
			this.changeRemind=function() {
				window.alert(Constants.invalidCalendar);
			};
			
			this._destroy=function() {
				document.body.removeChild(wrapper);
			};
			
			this.format=format;
			
			this.hide=function() {
				wrapper.style["display"]="none";
				window.$currentCalendar=null;
				this.cancelTimer();
			};
			
			this.inUsing = false;
			this.loaded = false;
			this.setDate = function(d) {
				elem.value=d;
			};
			//-- initialize
			wrapper = document.createElement("div");
			wrapper.className="calendarWrapper";
			document.body.appendChild( wrapper );
			
			DOM.addEventHandler( elem, "focus", initCalendar );
			
			function initCalendar() {
				if ( window.$currentCalendar && window.$currentCalendar !=_this ) 
					window.$currentCalendar.hide();
				wrapper.innerHTML="<iframe frameborder='0'></iframe>";
				frame = wrapper.firstChild;
				frame.className = "calendarFrame";
				frame.src = Calendar.source;
				_this.container=frame;
				window.$currentCalendar=_this;
				showWrapper(elem,wrapper);
				repeater=setInterval(function() {showWrapper(elem,wrapper);},500);
				
				DOM.removeEventHandler( elem, "focus", initCalendar );
				DOM.addEventHandler( elem, "blur", function() {
					timerId=null;
					timerId = setTimeout(checkCanlendar,200);
				});
				DOM.addEventHandler( elem, "focus",function() {
					if (window.$currentCalendar && window.$currentCalendar!=_this) 
						window.$currentCalendar.hide();
					window.$currentCalendar=_this;
					_this.cancelTimer();
					showWrapper( elem, wrapper );
					if ( !repeater ) 
						repeater = setInterval( function() {
													showWrapper(elem,wrapper);
												}, 500);
				});
			}
		}
		
		function showWrapper(input, wrapper) {
			var pos = getPosition(input);
			wrapper.style["position"] = "absolute";
			wrapper.style["top"] = (pos.y+input.clientHeight)+"px";
			wrapper.style["left"] = pos.x+"px";
			wrapper.style["display"] = "block";
		}
		
		function checkCanlendar() {
			if (!$currentCalendar) return;
			if (!$currentCalendar.loaded) {
				setTimeout(checkCanlendar,200);
				return;
			}
			if (!$currentCalendar.inUsing) {
				$currentCalendar.hide();
			}
		}
		return new CalendarAssistant(elem);
	}
};

function getPosition(sender) {
	var e=sender,E=e;
	var x=e.offsetLeft;
	var y=e.offsetTop;
	while (e=e.offsetParent) {
		var P=e.parentNode;
		while (P!=(E=E.parentNode)) {
			x-=E.scrollLeft;
			y-=E.scrollTop;
		}
		x+=e.offsetLeft;
		y+=e.offsetTop;
		E=e;
	}
	return {"x":x,"y":y};
}

function $(element) {
  if (arguments.length > 1) {
    for (var i = 0, elements = [], length = arguments.length; i < length; i++)
      elements.push($(arguments[i]));
    return elements;
  }
  element = document.getElementById(""+element);
  return element;
}

var _$f;
var DOM = new (function() {
	var sinks = {};
	this.addEventHandler = function(elem,type,handler) {
		eventHandlerPlugIn[_Bs].add(elem,type,handler);
	};

	this.removeEventHandler = function(elem,type,handler) {
		eventHandlerPlugIn[_Bs].remove(elem, type,handler);
	};

	this.setFocus = function(elem) {
		elem.focus();
	};

	this.sinkEvent = function(id,type,listener) {
		var h;
		if (h = sinks[id]) {
			if (!h[type]) h[type] = [];
			h[type].push(listener);
		} else {
			var o = {};
			o[type] = [listener];
			sinks[id] = o;
			delete o;
		}
	};

	this.detectBrowser= function() {
		var _d = document;
		if (_d.addEventListener) _Bs="Moz";
		else if (_d.attachEvent) _Bs="IE";
	};
	
	this.deleteEvent= function(id) {
		if (sinks[id]) {
			for (var i in sinks[id]) {
				delete sinks[id][i];
			}
			delete sinks[id];
		}
	};

	function dispatchEvent(oEvent) {
		var t;
		if (document.all) {
			oEvent = window.event;
			t = oEvent.srcElement;
		} else {
			t = oEvent.target;
		}
		var attr, __evt;
		if (t.disabled) return;
		if (t && t.attributes && (attr = t.attributes.getNamedItem("__evt")) && (__evt = parseInt(attr.nodeValue))>=0) {
			if (!sinks[__evt]) return;
			listeners = sinks[__evt][oEvent.type];
			if (listeners) {
				listeners.each( function(item){
					eventListenerWrapper[oEvent.type](item,t)();
					});
			}
		}
	};

	this.fireEvent = function(elem,evtType) {
		if (document.all) {
			var evt = document.createEventObject();
			elem.fireEvent('on'+evtType,evt);
		} else {
			var evt = document.createEvent( eventPlugIn[evtType] );
			evt.initEvent(evtType, true, true);
			elem.dispatchEvent(evt);
		}
	};

	var eventPlugIn = {
		"click": "MouseEvents",
		"dblclick": "MouseEvents",
		"mousedown": "MouseEvents",
		"mouseup": "MouseEvents",
		"mousemove": "MouseEvents",
		"change": "HTMLEvents"
	};

	var eventListenerWrapper = {
		"blur": function(listener, sender) { return function() {
														listener.onLostFocus(sender);
													}; },
		"click":function(listener, sender) { return function() {
														listener.onClick(sender);
													}; },
		"change":function(listener,sender) { return function() {
											 			listener.onChange(sender);
											 		}; },
		"dblclick":function(listener,sender) { return function() {
														listener.onDoubleClick(sender);
													}; },
		"focus":function(listener, sender) { return function() {
														listener.onFocus(sender);
													}; },
		"mousedown":function(listener, sender) { return function() { 
														listener.onMouseDown(sender);
													}; },
		"mouseup":function(listener, sender) { return function() {
														listener.onMouseUp(sender);
													}; },
		"mousemove":function(listener, sender) { return function() {
														listener.onMouseMove(sender);
													}; },
		"keydown":function(listener, sender) { return function() {
														listener.onKeyDown(sender);
													}; },
		"keyup":function(listener,sender) { return function() {
														listener.onKeyUp(sender);
													}; },
		"keypress":function(listener,sender) { return function() {
														listener.onKeyPress(sender);
													}; }
	};

	var eventHandlerPlugIn = {
		"Moz":{
			add: function(e,t,h) {e.addEventListener(t,h,false);},
			remove: function(e,t,h) {e.removeEventListener(t,h,false);}
		},
		"IE":{
			add: function(e,t,h) {e.attachEvent("on"+t,h);},
			remove: function(e,t,h) {e.detachEvent("on"+t,h);}
		},
		"Unknown":{
			add: function(e,t,h) {e["on"+t] = h;},
			remove: function(e,t,h) {e["on"+t] = null;}
		}
	};
	
	this.detectBrowser();
	this.addEventHandler(window,"load",function() {
		var _b=document.body,fn=DOM.addEventHandler;
		fn(_b,"click",dispatchEvent);
		fn(_b,"dblclick",dispatchEvent);
		fn(_b,"mousedown",dispatchEvent);
		fn(_b,"mouseup",dispatchEvent);
		fn(_b,"mousemove",dispatchEvent);
		fn(_b,"keydown",dispatchEvent);
		fn(_b,"keyup",dispatchEvent);
		fn(_b,"keypress",dispatchEvent);
		_$f = dispatchEvent;
	});
});

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};
String.prototype.trim = function() { 
	return this.replace(/(^[\s/n]*)|([\s/n]*$)/g, ""); 
};
String.prototype.ltrim = function() { 
	return this.replace(/(^[\s/n]*)/g, ""); 
}; 
String.prototype.rtrim = function() { 
	return this.replace(/([\s/n]*$)/g, ""); 
}; 
