(function() {
	var years,yearSelector,yearEditor,months,monthSelector,monthEditor,gridBody;
	var _months=[Constants.Jan,Constants.Feb,Constants.Mar,Constants.Apr,
	             Constants.May,Constants.Jun,Constants.Jul,Constants.Aug,
	             Constants.Sep,Constants.Oct,Constants.Nov,Constants.Dec];
	var _weeks=[Constants.Sunday,Constants.Monday,Constants.Tuesday,
	            Constants.Wednesday,Constants.Thursday,Constants.Friday,Constants.Saturday];
	var now = new Date();
	var curYear=now.getFullYear(),curMonth=now.getMonth()+1,curDay=now.getDate();
	var _year=curYear, _month=curMonth, _day=curDay;

	function printErrorMsg() {
		window.onload = function() {
//			document.getElementById("ErrorMsg").style["display"]="none";
			document.getElementById("MainContent").style["display"]="none";
		};
	}		
	
	try {
		var $p=window.parent.$currentCalendar;
		if (!$p) { printErrorMsg(); alert("This is the Calendar Modular. Please access it in right way!"); return; }
	} catch (e) {
		printErrorMsg();
		return;
	}
	var Focus={
			hit:function (oEvent) {
				$p.inUsing=true;
				$p.cancelTimer();
			},out:function(oEvent) {
				$p.inUsing=false;
				$p.hide();
			},mozImpl:{
				out:function(oEvent) {
					var target=oEvent.target;
					if (!target.ownerDocument) Focus.out(oEvent);
				}
			}
	};

	if (document.all) {
		$p.container.onfocus=Focus.hit;
		$p.container.onblur=Focus.out;
	} else {
		window.onfocus=Focus.hit;
		window.onblur=Focus.mozImpl.out;
	}
	window.onload = function() {
		$p.loaded=true;
		var CUtil=$("CUtil");
		var utilLinks=CUtil.getElementsByTagName("a");
		gridBody=$("DateGrid").getElementsByTagName("tbody")[0];
		for (var i=0;i<utilLinks.length;i++) {
			utilLinks[i].href="javascript:void(0)";
		}
		if ($p.defaultDate) initDate($p.defaultDate);
		initQucikBtn();
		initMonthNavBtn("lastMonth",-1);
		initMonthNavBtn("nextMonth",1);
		initYearTuneBtn("lastYear",-1);
		initYearTuneBtn("nextYear",1);
		
		years=$("Years");
		yearSelector=$("yearSelector");
		yearEditor=$("yearEditor");
		
		months=$("Months");
		monthSelector=$("monthSelector");
		monthEditor=$("monthEditor");
		
		years.onclick=yearSelector.onmouseover=function() {
			hide(years);
			show(yearSelector);
			yearEditor.focus();
		};
		yearSelector.onmouseout=function() {
			show(years);
			hide(yearSelector);
			var t=yearEditor.value;
			if (t.length==4 && !isNaN(t=parseInt(t)) && t>1000) _year=t;
			changeDate(true);
		};
		
		for (var i=0;i<_months.length;i++) {
			var _opt=document.createElement("option");
			_opt.appendChild(document.createTextNode(_months[i]));
			_opt.setAttribute("value",i+1);
			monthEditor.appendChild(_opt);
			_opt=null;
		}
		months.onclick=function() {
			hide(months);
			show(monthSelector);
			monthEditor.focus();
		};
		monthEditor.onchange=monthEditor.onblur=function() {
			_month=parseInt(monthEditor.value);
			hide(monthSelector);
			show(months);
			changeDate(true);
		};
		initWeekName();
		if ($p.defaultDate) {
			setupCheckChange($p.defaultDate);
			changeDate(true);
		} else {
			changeDate();
		}
		$p.addChangeListener(changeHandler);
	};
	
	function changeHandler(oEvent) {
		if (document.all) {
			oEvent = window.parent.event;
			t = oEvent.srcElement.value;
		} else {
			t = oEvent.target.value;
		}
		if (t.length==0) return;
		initDate(t);
		setupCheckChange(t);
		changeDate(true);
	}
	function initDate(str) {
		var f,t=str,_y1,_m1,_d1;
		f=$p.format;
		if (t.length==0) return;
		var p;
		var year,month,day;
		if ((p=f.search(/yyyy/ig))>=0) {
			year=parseInt(t.substring(p,p+4));
			f=f.replace(/yyyy/ig,"OOOO");
			if (!isNaN(year) && year>1000) _y1=year;
		}
		if ((p=f.indexOf('YY'))>=0) {
			year=parseInt((""+curYear).substring(0,2)+t.substring(p,p+2));
			if (!isNaN(year) && year>1000) _y1=year;
		}
		if ((p=f.indexOf('mm'))>=0) {
			month=parseInt(t.substring(p,p+2));
			if (!isNaN(month)){
				if (month>12) {
					month=parseInt(t.substring(p,p+1));
					if (!isNaN(month)) _m1=month;
				} else if (month>0) {
					_m1=month;
				}
			}
		}
		if ((p=f.indexOf('MM'))>=0) {
			month=parseInt(t.substring(p,p+2),10);
			if (!isNaN(month) && month>0 && month<13) _m1=month;
		}
		if ((p=f.indexOf('dd'))>=0) {
			day=parseInt(t.substring(p,p+2));
			if (!isNaN(day)){
				if (day>31) {
					day=parseInt(t.substring(p,p+1));
					if (!isNaN(day)) _d1=day;
				} else if (day>0) {
					_d1=day;
				}
			}
		}
		if ((p=f.indexOf('DD'))>=0) {
			day=parseInt(t.substring(p,p+2),10);
			if (!isNaN(day) && day>0 && day<32) _d1=day;
		}
		var d=new Date(_y1+"/"+_m1+"/"+_d1);
		try {
			if (d.getFullYear()!=_y1) throw "N";
			if ((d.getMonth()+1)!=_m1) throw "N";
			if (d.getDate()!=_d1) throw "N";
			_year=_y1;_month=_m1;_day=_d1;
		} catch (e) {}
	}
	function initQucikBtn() {
		var qToday=$("qToday");
		qToday.innerHTML=Constants.today;
		qToday.onclick=function() {
			_year=curYear;
			_month=curMonth;
			_day=curDay;
			changeDate(true);
			hiddenDateGrid();
		};
		var qYesterday=$("qYesterday");
		qYesterday.innerHTML=Constants.yesterday;
		qYesterday.onclick=function() {
			var n=new Date(curYear+"/"+curMonth+"/"+curDay);
			n.setMinutes(-1);
			_year=n.getFullYear();
			_month=n.getMonth()+1;
			_day=n.getDate();
			changeDate(true);
			hiddenDateGrid();
		};
		var qTomorrow=$("qTomorrow");
		qTomorrow.innerHTML=Constants.tomorrow;
		qTomorrow.onclick=function() {
			var n=new Date();
			n.setDate(n.getDate()+1);
			_year=n.getFullYear();
			_month=n.getMonth()+1;
			_day=n.getDate();
			changeDate(true);
			hiddenDateGrid();
		};
	}
	function initWeekName() {
		var DateGrid=$("DateGrid");
		var ths=DateGrid.getElementsByTagName("th");
		for (var i=0;i<_weeks.length;i++) {
			ths[i].innerHTML=_weeks[i];
		}
	}
	function initDateGrid() {
		var trs=gridBody.getElementsByTagName("tr");
		while (trs.length>1) {gridBody.removeChild(trs[1]);}
		var _s=new Date(_year+"/"+_month+"/1");
		var _e=new Date(_year+"/"+(_month+1)+"/1");
		_e.setMinutes(-1);
		var maxD=_e.getDate();
		if (maxD<_day) _day=maxD;
		var i=1,pos=_s.getDay(),_tr;
		while(i<=maxD) {
			if (!_tr) _tr=getTR();
			var _tds=_tr.childNodes;
			_tds[pos].innerHTML=(i==_day?"<span class=\"curDate\">"+i+"</span>":i);
			_tds[pos].onclick=createEvent(i);
			if (pos==6) {
				_tr.style["display"]="";
				gridBody.appendChild(_tr);
				_tr=getTR();
				pos=0;
			} else pos++;
			i++;
		}
		if (pos!=0) {
			_tr.style["display"]="";
			gridBody.appendChild(_tr);
		}
		_tr=null;
		function getTR() {
			return trs[0].cloneNode(true);
		}
		function createEvent(num) {
			return function() {
				_day=num;
				changeDate(true);
				hiddenDateGrid();
			};
		}
	}
	function initMonthNavBtn(name,offset) {
		var elem=$(name);
		elem.className=name;
		elem.onmouseover=function() {elem.className=name+"_over";};
		elem.onmouseout=function() {elem.className=name;};
		elem.onclick=function() {
			var monthEditor=$("monthEditor");
			var __m=_month+offset-1;
			if (__m<0) {_month=12;_year--;}
			else if (__m>11) {_month=1;_year++;}
			else {_month=__m+1;}
			changeDate(true);
		}
	}
	function initYearTuneBtn(name,offset) {
		var elem=$(name);
		elem.className=name;
		elem.onmouseover=function() {elem.className=name+"_over";};
		elem.onmouseout=elem.onmouseup=function() {elem.className=name;};
		elem.onmousedown=function() {elem.className=name+"_down";}
		elem.onclick=function() {
			var t=yearEditor.value;
			if (t.length==4 && !isNaN(t=parseInt(t)) && t>1000) {
				_year=t+offset;changeDate(true);
			}
		}
	}
	function changeDate(_apply) {
		years.innerHTML=yearEditor.value=_year;
		monthEditor.childNodes[_month-1].selected=true;
		months.innerHTML=_months[_month-1];
		initDateGrid();
		var nv=formatDate($p.format);
		checkChange(nv);
		if (_apply)
			$p.setDate(nv);

	}
	function hiddenDateGrid(){
		$p.inUsing=false;
		$p.hide();
	}
	function checkChange() {}
	function setupCheckChange(txt) {
		var oFn=checkChange;
		checkChange=function(nv) {
			if (nv!=txt) $p.changeRemind();
			checkChange=oFn;
			oFn=null;
			txt=null;
		};
	}
	function $(elem) {
		return document.getElementById(""+elem);
	}
	function formatDate(format) {
		var t = format;
		if (t.search(/yyyy/ig)>=0) t=t.replace(/yyyy/ig,_year);
		if (t.indexOf('YY')>=0) t=t.replace('YY',(""+_year).substring(2));
		if (t.indexOf('mm')>=0) t=t.replace('mm',_month);
		if (t.indexOf('MM')>=0) t=t.replace('MM',(_month>9?'':'0')+_month);
		if (t.indexOf('dd')>=0) t=t.replace('dd',_day);
		if (t.indexOf('DD')>=0) t=t.replace('DD',(_day>9?'':'0')+_day);
		return t;
	}
	function hide(elem) {elem.style["display"]="none";}
	function show(elem) {elem.style["display"]="block";}
})();