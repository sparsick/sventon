/* This notice must be untouched at all times.

wz_tooltip.js	 v. 4.01

The latest version is available at
http://www.walterzorn.com
or http://www.devira.com
or http://www.walterzorn.de

Copyright (c) 2002-2007 Walter Zorn. All rights reserved.
Created 1.12.2002 by Walter Zorn (Web: http://www.walterzorn.com )
Last modified: 11.6.2007

Easy-to-use cross-browser tooltips.
Just include the script at the beginning of the <body> section, and invoke
Tip('Tooltip text') from within the desired HTML onmouseover eventhandlers.
No container DIV, no onmouseouts required.
By default, width of tooltips is automatically adapted to content.
Is even capable of dynamically converting arbitrary HTML elements to tooltips
by calling TagToTip('ID_of_HTML_element_to_be_converted') instead of Tip(),
which means you can put important, search-engine-relevant stuff into tooltips.
Appearance of tooltips can be individually configured
via commands passed to Tip() or TagToTip().

Tab Width: 4
LICENSE: LGPL

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License (LGPL) as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

For more details on the GNU Lesser General Public License,
see http://www.gnu.org/copyleft/lesser.html
*/

var config = new Object();


//===================  GLOBAL TOOPTIP CONFIGURATION  =========================//
var  Debug			 = false			// false or true - recommended: false once you release your page to the public
var  TagsToTip	 = false			// false or true - if true, the script is capable of converting HTML elements to tooltips

// For each of the following config variables there exists a command, which is
// just the variablename in uppercase, to be passed to Tip() or TagToTip() to
// configure tooltips individually. Individual commands override global
// configuration. Order of commands is arbitrary.
// Example: onmouseover="Tip('Tooltip text', LEFT, true, BGCOLOR, '#FF9900', FADEOUT, 400)"

config. Above			= false 	// false or true - tooltip above mousepointer?
config. BgColor 		= '#E0E4FF' // Background color
config. BgImg			= ''		// Path to background image, none if empty string ''
config. BorderColor 	= '#002299'
config. BorderStyle 	= 'solid'	// Any permitted CSS value, but I recommend 'solid', 'dotted' or 'dashed'
config. BorderWidth 	= 1
config. CenterMouse 	= false 	// false or true - center the tip horizontally below (or above) the mousepointer
config. ClickClose		= false 	// false or true - close tooltip if the user clicks somewhere
config. CloseBtn		= false 	// false or true - closebutton in titlebar
config. CloseBtnColors	= ['#990000', '#FFFFFF', '#DD3333', '#FFFFFF']	  // [Background, text, hovered background, hovered text] - use empty strings '' to inherit title colors
config. CloseBtnText	= 'Close'	// Close button text (may also be an image tag)
config. Delay			= 400		// Time span in ms until tooltip shows up
config. Duration		= 0 		// Time span in ms after which the tooltip disappears; 0 for infinite duration
config. FadeIn			= 0 		// Fade-in duration in ms, e.g. 400; 0 for no animation
config. FadeOut 		= 0
config. FadeInterval	= 30		// Duration of each fade step in ms (recommended: 30) - shorter is smoother but causes more CPU-load
config. Fix 			= null		// Fixated position - x- an y-oordinates in brackets, e.g. [210, 480], or null for no fixation
config. FollowMouse		= true		// false or true - tooltip follows the mouse
config. FontColor		= '#000044'
config. FontFace		= 'Verdana,Geneva,sans-serif'
config. FontSize		= '8pt' 	// E.g. '9pt' or '12px' - unit is mandatory
config. FontWeight		= 'normal'	// 'normal' or 'bold';
config. Left			= false 	// false or true - tooltip on the left of the mouse
config. OffsetX 		= 14		// Horizontal offset of left-top corner from mousepointer
config. OffsetY 		= 8 		// Vertical offset
config. Opacity 		= 100		// Integer between 0 and 100 - opacity of tooltip in percent
config. Padding 		= 3 		// Spacing between border and content
config. Shadow			= false 	// false or true
config. ShadowColor 	= '#C0C0C0'
config. ShadowWidth 	= 5
config. Sticky			= false 	// Do NOT hide tooltip on mouseout? false or true
config. TextAlign		= 'left'	// 'left', 'right' or 'justify'
config. Title			= ''		// Default title text applied to all tips (no default title: empty string '')
config. TitleAlign		= 'left'	// 'left' or 'right' - text alignment inside the title bar
config. TitleBgColor	= ''		// If empty string '', BorderColor will be used
config. TitleFontColor	= '#ffffff'	// Color of title text - if '', BgColor (of tooltip body) will be used
config. TitleFontFace	= ''		// If '' use FontFace (boldified)
config. TitleFontSize	= ''		// If '' use FontSize
config. Width			= 0 		// Tooltip width; 0 for automatic adaption to tooltip content
//=======  END OF TOOLTIP CONFIG, DO NOT CHANGE ANYTHING BELOW  ==============//




//======================  PUBLIC  ============================================//
function Tip()
{
	tt_Tip(arguments, null);
}
function TagToTip()
{
	if(TagsToTip)
	{
		var a = arguments, el = tt_GetElt(a[0]);
		if(el)
		{
			a[0] = el.innerHTML;
			tt_Tip(a, el);
		}
	}
}

//==================  PUBLIC EXTENSION API	==================================//
// Extension eventhandlers currently supported:
// OnLoadConfig, OnCreateContentString, OnSubDivsCreated, OnShow, OnMouseMove, OnHideInit, OnHide, OnKill

var tt_aElt = new Array(10), // Container DIV, outer title and body DIVs, inner TDs of title & body, closebutton SPAN in title, shadow DIVs, and IFRAME to cover windowed elements in IE
tt_aV = new Array(),	// Caches and enumerates config data for currently active tooltip
tt_sContent,			// Inner tooltip text or HTML
tt_elToTip,				// If TagToTip() has been called, this is the DOM node to be converted
tt_x, tt_y, tt_w, tt_h; // Position, width and height of currently displayed tooltip

function tt_Extension()
{
	tt_ExtCmdEnum();
	tt_aExt[tt_aExt.length] = this;
	return this;
}
function tt_SetTipPos(x, y)
{
	var css = tt_aElt[0].style;

	tt_x = x;
	tt_y = y;
	css.left = x + "px";
	css.top = y + "px";
	if(tt_ie56)
	{
		var ifrm = tt_aElt[tt_aElt.length - 1];
		if(ifrm)
		{
			ifrm.style.left = css.left;
			ifrm.style.top = css.top;
		}
	}
}
function tt_HideTip()
{
	if(tt_db)
	{
		if(tt_iState & 2)
		{
			tt_aElt[0].style.visibility = "hidden";
			tt_ExtCallFncs(0, "Hide");
		}
		tt_tShow.EndTimer();
		tt_tHide.EndTimer();
		tt_tDurt.EndTimer();
		tt_tFade.EndTimer();
		if(!tt_op && !tt_ie)
		{
			tt_tWaitMov.EndTimer();
			tt_bWait = false;
		}
		if(tt_aV[CLICKCLOSE])
			tt_RemEvtFnc(document, "mouseup", tt_HideInit);
		tt_AddRemEvtFncEx(1, false);
		tt_AddRemEvtFncEx(0, false);
		tt_ExtCallFncs(0, "Kill");
		tt_iState = 0;
		tt_Move.over = null;
		tt_ResetMainDiv();
		if(tt_aElt[tt_aElt.length - 1])
			tt_aElt[tt_aElt.length - 1].style.display = "none";
	}
}
function tt_GetElt(id)
{
	return(document.getElementById ? document.getElementById(id)
			: document.all ? document.all[id]
			: null);
}
function tt_GetDivW(el)
{
	if(el)
		return(el.offsetWidth || el.style.pixelWidth || 0);
	return 0;
}
function tt_GetDivH(el)
{
	if(el)
		return(el.offsetHeight || el.style.pixelHeight || 0);
	return 0;
}
function tt_GetScrollX()
{
	return((typeof(window.pageXOffset) != tt_u) ? window.pageXOffset
			: tt_db ? (tt_db.scrollLeft || 0)
			: 0);
}
function tt_GetScrollY()
{
	return((typeof(window.pageYOffset) != tt_u) ? window.pageYOffset
			: tt_db ? (tt_db.scrollTop || 0)
			: 0);
}
function tt_GetClientW()
{
	return(document.body && (typeof(document.body.clientWidth) != tt_u) ? document.body.clientWidth
			: (typeof(window.innerWidth) != tt_u) ? window.innerWidth
			: tt_db ? (tt_db.clientWidth || 0)
			: 0);
}
function tt_GetClientH()
{
	// Exactly this order seems to yield correct values in all major browsers
	return(document.body && (typeof(document.body.clientHeight) != tt_u) ? document.body.clientHeight
			: (typeof(window.innerHeight) != tt_u) ? window.innerHeight
			: tt_db ? (tt_db.clientHeight || 0)
			: 0);
}
function tt_AddEvtFnc(el, sEvt, PFnc)
{
	if(el)
	{
		if(el.addEventListener)
			el.addEventListener(sEvt, PFnc, false);
		else
			el.attachEvent("on" + sEvt, PFnc);
	}
}
function tt_RemEvtFnc(el, sEvt, PFnc)
{
	if(el)
	{
		if(el.removeEventListener)
			el.removeEventListener(sEvt, PFnc, false);
		else
			el.detachEvent("on" + sEvt, PFnc);
	}
}

//======================  PRIVATE  ===========================================//
var tt_aExt = new Array(),	// Array of extension objects

tt_db, tt_op, tt_op78, tt_ie, tt_ie56, tt_bBoxOld,	// Browser flags
tt_body,
tt_flagOpa, 			// Opacity support: 1=IE, 2=Khtml, 3=KHTML, 4=Moz, 5=W3C
tt_scrlX, tt_scrlY,
tt_maxPosX, tt_maxPosY,
tt_iState = 0,			// Tooltip active = 1, shown = 2
tt_opa, 				// Currently applied opacity
tt_bJmpVert,			// tip above mouse (or ABOVE tip below mouse)

tt_elDeHref,			// The tag from which Opera has removed the href attribute
// Timer
tt_tShow = new Number(0), tt_tHide = new Number(0), tt_tDurt = new Number(0),
tt_tFade = new Number(0), tt_tWaitMov = new Number(0),
tt_bMovFnc, tt_bOutFnc,
tt_bWait = false,
tt_u = "undefined";


function tt_Init()
{
	tt_MkCmdEnum();
	// Send old browsers instantly to hell
	if(!tt_Browser() || !tt_MkMainDiv())
		return;
	tt_IsW3cBox();
	tt_OpaSupport();
	// In Debug mode we search for TagToTip() calls in order to notify
	// the user if they've forgotten to set the TagsToTip config flag
	if(TagsToTip || Debug)
		tt_SetOnloadFnc();
	tt_AddEvtFnc(window, "scroll", tt_HideOnScrl);
	// Ensure the tip be hidden when the page unloads
	tt_AddEvtFnc(window, "unload", tt_HideTip);
	tt_HideTip();
}
// Creates command names by translating config variable names to upper case
function tt_MkCmdEnum()
{
	var n = 0;
	for(var i in config)
		eval("window." + i.toString().toUpperCase() + " = " + n++);
	tt_aV.length = n;
}
function tt_Browser()
{
	var n, nv, n6, w3c;

	n = navigator.userAgent.toLowerCase(),
	nv = navigator.appVersion;
	tt_op = (document.defaultView && typeof(eval("w" + "indow" + "." + "o" + "p" + "er" + "a")) != tt_u);
	tt_op78 = (tt_op && !window.getSelection);
	tt_ie = n.indexOf("msie") != -1 && document.all && !tt_op;
	if(tt_ie)
	{
		var ieOld = (!document.compatMode || document.compatMode == "BackCompat");
		tt_db = !ieOld ? document.documentElement : (document.body || null);
		if(tt_db)
			tt_ie56 = parseFloat(nv.substring(nv.indexOf("MSIE") + 5)) >= 5.5
					&& typeof document.body.style.maxHeight == tt_u;
	}
	else
	{
		tt_db = document.documentElement || document.body ||
				(document.getElementsByTagName ? document.getElementsByTagName("body")[0]
				: null);
		if(!tt_op)
		{
			n6 = document.defaultView && typeof document.defaultView.getComputedStyle != tt_u;
			w3c = !n6 && document.getElementById;
		}
	}
	tt_body = (document.getElementsByTagName ? document.getElementsByTagName("body")[0]
				: (document.body || null));
	if(tt_ie || n6 || tt_op || w3c)
	{
		if(tt_body && tt_db)
		{
			if(document.attachEvent || document.addEventListener)
				return true;
		}
		else
			tt_Err("wz_tooltip.js must be included INSIDE the body section,"
					+ " immediately after the opening <body> tag.");
	}
	tt_db = null;
	return false;
}
function tt_MkMainDiv()
{
	// Create the tooltip DIV
	if(tt_body.insertAdjacentHTML)
		tt_body.insertAdjacentHTML("afterBegin", tt_MkMainDivHtm());
	else if(typeof tt_body.innerHTML != tt_u && document.createElement && tt_body.appendChild)
		tt_body.appendChild(tt_MkMainDivDom());
	// FireFox Alzheimer bug
	if(window.tt_GetMainDivRefs && tt_GetMainDivRefs())
		return true;
	tt_db = null;
	return false;
}
function tt_MkMainDivHtm()
{
	return('<div id="WzTtDiV"></div>' +
			(tt_ie56 ? ('<iframe id="WzTtIfRm" src="javascript:false" scrolling="no" frameborder="0" style="filter:Alpha(opacity=0);position:absolute;top:0px;left:0px;display:none;"></iframe>')
			: ''));
}
function tt_MkMainDivDom()
{
	var el = document.createElement("div");
	if(el)
		el.id = "WzTtDiV";
	return el;
}
function tt_GetMainDivRefs()
{
	tt_aElt[0] = tt_GetElt("WzTtDiV");
	if(tt_ie56 && tt_aElt[0])
	{
		tt_aElt[tt_aElt.length - 1] = tt_GetElt("WzTtIfRm");
		if(!tt_aElt[tt_aElt.length - 1])
			tt_aElt[0] = null;
	}
	if(tt_aElt[0])
	{
		var css = tt_aElt[0].style;

		css.visibility = "hidden";
		css.position = "absolute";
		css.overflow = "hidden";
		return true;
	}
	return false;
}
function tt_ResetMainDiv()
{
	var w = (window.screen && screen.width) ? screen.width : 10000;

	tt_SetTipPos(-w, 0);
	tt_aElt[0].innerHTML = "";
	tt_aElt[0].style.width = (w - 1) + "px";
}
function tt_IsW3cBox()
{
	var css = tt_aElt[0].style;

	css.padding = "10px";
	css.width = "40px";
	tt_bBoxOld = (tt_GetDivW(tt_aElt[0]) == 40);
	css.padding = "0px";
	tt_ResetMainDiv();
}
function tt_OpaSupport()
{
	var css = tt_body.style;

	tt_flagOpa = (typeof(css.filter) != tt_u) ? 1
				: (typeof(css.KhtmlOpacity) != tt_u) ? 2
				: (typeof(css.KHTMLOpacity) != tt_u) ? 3
				: (typeof(css.MozOpacity) != tt_u) ? 4
				: (typeof(css.opacity) != tt_u) ? 5
				: 0;
}
// Ported from http://dean.edwards.name/weblog/2006/06/again/
// (Dean Edwards et al.)
function tt_SetOnloadFnc()
{
	tt_AddEvtFnc(document, "DOMContentLoaded", tt_HideSrcTags);
	tt_AddEvtFnc(window, "load", tt_HideSrcTags);
	// Conditional comment below is IE code - DON'T REMOVE!!!
	/*@cc_on
	if(document.attachEvent)
		document.attachEvent("onreadystatechange",
			function(){ if(document.readyState == "complete") tt_HideSrcTags(); });
	document.write('<scr' + 'ipt id="TT_ie_onload" defer src="'
					+ ((location.protocol == "https:") ? '//0' : 'javascript:void(0)')
					+ '"><\/scr' + 'ipt>');
	var scrb = document.getElementById("TT_ie_onload");
	scrb.onreadystatechange = function() {
		if(this.readyState == "complete")
			tt_HideSrcTags();
	};
	@*/
	if(/WebKit|KHTML/i.test(navigator.userAgent))
	{
		var t = setInterval(function() {
					if(/loaded|complete/.test(document.readyState))
					{
						clearInterval(t);
						tt_HideSrcTags();
					}
				}, 10);
	}
}
function tt_HideSrcTags()
{
	if(!window.tt_HideSrcTags || window.tt_HideSrcTags.done)
		return;
	window.tt_HideSrcTags.done = true;
	if(!tt_HideSrcTagsRecurs(tt_body))
		tt_Err("To enable the capability to convert HTML elements to tooltips,"
				+ " you must set TagsToTip in the global tooltip configuration"
				+ " to true.");
}
function tt_HideSrcTagsRecurs(dad)
{
	var a, ovr, asT2t;

	// Walk the DOM tree for tags that have an onmouseover attribute
	// containing a TagToTip('...') call.
	// (.childNodes first since .children is bugous in Safari)
	a = dad.childNodes || dad.children || null;
	for(var i = a ? a.length : 0; i;)
	{--i;
		if(!tt_HideSrcTagsRecurs(a[i]))
			return false;
		ovr = a[i].getAttribute ? a[i].getAttribute("onmouseover")
				: (typeof a[i].onmouseover == "function") ? a[i].onmouseover
				: null;
		if(ovr)
		{
			asT2t = ovr.toString().match(/TagToTip\s*\(\s*'[^'.]+'\s*[\),]/);
			if(asT2t && asT2t.length)
			{
				if(!tt_HideSrcTag(asT2t[0]))
					return false;
			}
		}
	}
	return true;
}
function tt_HideSrcTag(sT2t)
{
	var id, el;

	// The ID passed to the found TagToTip() call identifies an HTML element
	// to be converted to a tooltip, so hide that element
	id = sT2t.replace(/.+'([^'.]+)'.+/, "$1");
	el = tt_GetElt(id);
	if(el)
	{
		if(Debug && !TagsToTip)
			return false;
		else
			el.style.display = "none";
	}
	else
		tt_Err("Invalid ID\n'" + id + "'\npassed to TagToTip()."
				+ " There exists no HTML element with that ID.");
	return true;
}
function tt_Tip(arg, el)
{
	if(!tt_db)
		return;
	if(tt_iState)
		tt_HideTip();
	// Expose content globally so extensions can modify it
	tt_elToTip = el;
	tt_sContent = arg[0];
	if(!tt_ReadCmds(arg))
		return;
	tt_iState = 1;
	tt_AdaptConfig1();
	tt_MkTipSubDivs();
	tt_FormatTip();
	tt_AddRemEvtFncEx(0, true);
	tt_bJmpVert = false;
	tt_scrlX = tt_GetScrollX();
	tt_scrlY = tt_GetScrollY();
	tt_maxPosX = tt_GetClientW() + tt_scrlX - tt_w - 1;
	tt_maxPosY = tt_GetClientH() + tt_scrlY - tt_h - 1;
	tt_AdaptConfig2();
	// IE and Op won't fire a mousemove accompanying the mouseover, so we
	// must ourselves fake that first mousemove in order to ensure the tip
	// be immediately shown and positioned
	if(window.event)
		tt_Move(window.event);
}
function tt_ReadCmds(a)
{
	var i;

	// First load the global config values, to initialize also values
	// for which no command has been passed
	i = 0;
	for(var j in config)
		tt_aV[i++] = config[j];
	// Then replace each cached config value for which a command has been
	// passed; ensure the # of command args plus value args be even
	if(a.length & 1)
	{
		for(i = a.length - 1; i > 0; i -= 2)
			tt_aV[a[i - 1]] = a[i];
		return true;
	}
	tt_Err("Incorrect call of Tip() or TagToTip().\n"
			+ "Each command must be followed by a value.");
	return false;
}
function tt_AdaptConfig1()
{
	tt_ExtCallFncs(0, "LoadConfig");
	// Inherit unspecified title formattings from body
	if(!tt_aV[TITLEBGCOLOR].length)
		tt_aV[TITLEBGCOLOR] = tt_aV[BORDERCOLOR];
	if(!tt_aV[TITLEFONTCOLOR].length)
		tt_aV[TITLEFONTCOLOR] = tt_aV[BGCOLOR];
	if(!tt_aV[TITLEFONTFACE].length)
		tt_aV[TITLEFONTFACE] = tt_aV[FONTFACE];
	if(!tt_aV[TITLEFONTSIZE].length)
		tt_aV[TITLEFONTSIZE] = tt_aV[FONTSIZE];
	if(tt_aV[CLOSEBTN])
	{
		// Use title colors for non-specified closebutton colors
		if(!tt_aV[CLOSEBTNCOLORS])
			tt_aV[CLOSEBTNCOLORS] = new Array("", "", "", "");
		for(var i = 4; i;)
		{--i;
			if(!tt_aV[CLOSEBTNCOLORS][i].length)
				tt_aV[CLOSEBTNCOLORS][i] = (i & 1) ? tt_aV[TITLEFONTCOLOR] : tt_aV[TITLEBGCOLOR];
		}
		// Enforce titlebar be shown
		if(!tt_aV[TITLE].length)
			tt_aV[TITLE] = " ";
	}
	// Circumvents broken display of images and fade-in flicker in Geckos < 1.8
	if(tt_aV[OPACITY] == 100 && typeof tt_aElt[0].style.MozOpacity != tt_u && !Array.every)
		tt_aV[OPACITY] = 99;
	// Smartly shorten the delay for fade-in tooltips
	if(tt_aV[FADEIN] && tt_flagOpa && tt_aV[DELAY] > 100)
		tt_aV[DELAY] = Math.max(tt_aV[DELAY] - tt_aV[FADEIN], 100)
}
function tt_AdaptConfig2()
{
	if(tt_aV[CENTERMOUSE])
		tt_aV[OFFSETX] = -((tt_w - (tt_aV[SHADOW] ? tt_aV[SHADOWWIDTH] : 0)) >> 1);
}
function tt_MkTipSubDivs()
{
	var sTbTr = ' cellpadding="0" cellspacing="0" border="0" style="position:relative;margin:0px;border-width:0px;"><tr>',
	sTdCss = 'position:relative;margin:0px;padding:0px;border-width:0px;';

	tt_ExtCallFncs(0, "CreateContentString");
	tt_aElt[0].innerHTML =
		(''
		+ (tt_aV[TITLE].length ?
			('<div id="WzTiTl" style="position:relative;z-index:1;">'
			+ '<table id="WzTiTlTb"' + sTbTr
			+ '<td id="WzTiTlI" style="' + sTdCss + '">'
			+ tt_aV[TITLE]
			+ '</td>'
			+ (tt_aV[CLOSEBTN] ?
				('<td align="right" style="' + sTdCss
				+ ';text-align:right;">'
				+ '<span id="WzClOsE" style="padding-left:2px;padding-right:2px;'
				+ 'cursor:' + (tt_ie ? 'hand' : 'pointer')
				+ ';" onmouseover="tt_OnCloseBtnOver(1)" onmouseout="tt_OnCloseBtnOver(0)" onclick="tt_HideInit()">'
				+ tt_aV[CLOSEBTNTEXT]
				+ '</span></td>')
				: '')
			+ '</tr></table></div>')
			: '')
		+ '<div id="WzBoDy" style="position:relative;z-index:0;">'
		+ '<table' + sTbTr + '<td id="WzBoDyI" style="' + sTdCss + '">'
		+ tt_sContent
		+ '</td></tr></table>'
		+ '</div>'
		+ (tt_aV[SHADOW]
			? ('<div id="WzTtShDwR" style="position:absolute;overflow:hidden;"></div>'
				+ '<div id="WzTtShDwB" style="position:relative;overflow:hidden;"></div>')
			: '')
		);
	tt_GetSubDivRefs();
	tt_ExtCallFncs(0, "SubDivsCreated");
}
function tt_GetSubDivRefs()
{
	var aId = new Array("WzTiTl", "WzTiTlTb", "WzTiTlI", "WzClOsE", "WzBoDy", "WzBoDyI", "WzTtShDwB", "WzTtShDwR");

	for(var i = aId.length; i; --i)
		tt_aElt[i] = tt_GetElt(aId[i - 1]);
}
function tt_FormatTip()
{
	var css, dy, w;

	//--------- Title DIV ----------
	if(tt_aV[TITLE].length)
	{
		css = tt_aElt[1].style;
		css.background = tt_aV[TITLEBGCOLOR];
		css.paddingTop = (tt_aV[CLOSEBTN] ? 2 : 0) + "px";
		css.paddingBottom = "1px";
		css.paddingLeft = css.paddingRight = tt_aV[PADDING] + "px";
		css = tt_aElt[3].style;
		css.color = tt_aV[TITLEFONTCOLOR];
		css.fontFamily = tt_aV[TITLEFONTFACE];
		css.fontSize = tt_aV[TITLEFONTSIZE];
		css.fontWeight = "bold";
		css.textAlign = tt_aV[TITLEALIGN];
		// Close button DIV
		if(tt_aElt[4])
		{
			css.paddingRight = (tt_aV[PADDING] << 1) + "px";
			css = tt_aElt[4].style;
			css.background = tt_aV[CLOSEBTNCOLORS][0];
			css.color = tt_aV[CLOSEBTNCOLORS][1];
			css.fontFamily = tt_aV[TITLEFONTFACE];
			css.fontSize = tt_aV[TITLEFONTSIZE];
			css.fontWeight = "bold";
		}
		if(tt_aV[WIDTH] > 0)
			tt_w = tt_aV[WIDTH] + ((tt_aV[PADDING] + tt_aV[BORDERWIDTH]) << 1);
		else
		{
			tt_w = tt_GetDivW(tt_aElt[3]) + tt_GetDivW(tt_aElt[4]);
			// Some spacing between title DIV and closebutton
			if(tt_aElt[4])
				tt_w += tt_aV[PADDING];
		}
		// Ensure the top border of the body DIV be covered by the title DIV
		dy = -tt_aV[BORDERWIDTH];
	}
	else
	{
		tt_w = 0;
		dy = 0;
	}

	//-------- Body DIV ------------
	css = tt_aElt[5].style;
	css.top = dy + "px";
	if(tt_aV[BORDERWIDTH])
	{
		css.borderColor = tt_aV[BORDERCOLOR];
		css.borderStyle = tt_aV[BORDERSTYLE];
		css.borderWidth = tt_aV[BORDERWIDTH] + "px";
	}
	if(tt_aV[BGCOLOR].length)
		css.background = tt_aV[BGCOLOR];
	if(tt_aV[BGIMG].length)
		css.backgroundImage = "url(" + tt_aV[BGIMG] + ")";
	css.padding = tt_aV[PADDING] + "px";
	css.textAlign = tt_aV[TEXTALIGN];
	// TD inside body DIV
	css = tt_aElt[6].style;
	css.color = tt_aV[FONTCOLOR];
	css.fontFamily = tt_aV[FONTFACE];
	css.fontSize = tt_aV[FONTSIZE];
	css.fontWeight = tt_aV[FONTWEIGHT];
	css.background = "";
	css.textAlign = tt_aV[TEXTALIGN];
	if(tt_aV[WIDTH] > 0)
		w = tt_aV[WIDTH] + ((tt_aV[PADDING] + tt_aV[BORDERWIDTH]) << 1);
	else
		// We measure the width of the body's inner TD, because some browsers
		// expand the width of the container and outer body DIV to 100%
		w = tt_GetDivW(tt_aElt[6]) + ((tt_aV[PADDING] + tt_aV[BORDERWIDTH]) << 1);
	if(w > tt_w)
		tt_w = w;

	//--------- Shadow DIVs ------------
	if(tt_aV[SHADOW])
	{
		var off;

		tt_w += tt_aV[SHADOWWIDTH];
		off = tt_CalcShadowOffset();
		// Bottom shadow
		css = tt_aElt[7].style;
		css.top = dy + "px";
		css.left = off + "px";
		css.width = (tt_w - off - tt_aV[SHADOWWIDTH]) + "px";
		css.height = tt_aV[SHADOWWIDTH] + "px";
		css.background = tt_aV[SHADOWCOLOR];
		// Right shadow
		css = tt_aElt[8].style;
		css.top = off + "px";
		css.left = (tt_w - tt_aV[SHADOWWIDTH]) + "px";
		css.width = tt_aV[SHADOWWIDTH] + "px";
		css.background = tt_aV[SHADOWCOLOR];
	}

	//-------- Container DIV -------
	tt_SetTipOpa(tt_aV[FADEIN] ? 0 : tt_aV[OPACITY]);
	tt_FixSize(dy);
}
// Fixate the size so it can't dynamically change while the tooltip is moving.
function tt_FixSize(offY)
{
	var wIn, wOut, i;

	tt_aElt[0].style.width = tt_w + "px";
	tt_aElt[0].style.pixelWidth = tt_w;
	wOut = tt_w - ((tt_aV[SHADOW]) ? tt_aV[SHADOWWIDTH] : 0);
	// Body
	wIn = wOut;
	if(!tt_bBoxOld)
		wIn -= ((tt_aV[PADDING] + tt_aV[BORDERWIDTH]) << 1);
	tt_aElt[5].style.width = wIn + "px";
	// Title
	if(tt_aElt[1])
	{
		wIn = wOut - (tt_aV[PADDING] << 1);
		if(!tt_bBoxOld)
			wOut = wIn;
		tt_aElt[1].style.width = wOut + "px";
		tt_aElt[2].style.width = wIn + "px";
	}
	tt_h = tt_GetDivH(tt_aElt[0]) + offY;
	// Right shadow
	if(tt_aElt[8])
		tt_aElt[8].style.height = (tt_h - tt_CalcShadowOffset()) + "px";
	i = tt_aElt.length - 1;
	if(tt_aElt[i])
	{
		tt_aElt[i].style.width = tt_w + "px";
		tt_aElt[i].style.height = tt_h + "px";
	}
}
function tt_CalcShadowOffset()
{
	return(Math.floor((tt_aV[SHADOWWIDTH] * 4) / 3));
}
function tt_StartMov()
{
	tt_DeAlt(tt_Move.over);
	tt_OpDeHref(tt_Move.over);
	tt_tShow.Timer("tt_ShowTip()", tt_aV[DELAY], true);
	tt_AddRemEvtFncEx(1, true);
	if(tt_aV[CLICKCLOSE])
		tt_AddEvtFnc(document, "mouseup", tt_HideInit);
}
function tt_DeAlt(el)
{
	var aKid;

	if(el.alt)
		el.alt = "";
	if(el.title)
		el.title = "";
	aKid = el.childNodes || el.children || null;
	if(aKid)
	{
		for(var i = aKid.length; i;)
			tt_DeAlt(aKid[--i]);
	}
}
function tt_OpDeHref(el)
{
	// I hope those annoying native tooltips over links never revive in Opera
	if(!tt_op78)
		return;
	if(tt_elDeHref)
		tt_OpReHref();
	while(el)
	{
		if(el.hasAttribute("href"))
		{
			el.t_href = el.getAttribute("href");
			el.t_stats = window.status;
			el.removeAttribute("href");
			el.style.cursor = "hand";
			tt_AddEvtFnc(el, "mousedown", tt_OpReHref);
			window.status = el.t_href;
			tt_elDeHref = el;
			break;
		}
		el = el.parentElement;
	}
}
function tt_ShowTip()
{
	var css = tt_aElt[0].style;

	// Override the z-index of the topmost wz_dragdrop.js D&D item
	css.zIndex = Math.max((window.dd && dd.z) ? (dd.z + 2) : 0, 1010);
	if(tt_aV[STICKY] || !tt_aV[FOLLOWMOUSE])
		tt_AddRemEvtFncEx(0, false);
	if(tt_aV[DURATION] > 0)
		tt_tDurt.Timer("tt_HideInit()", tt_aV[DURATION], true);
	tt_ExtCallFncs(0, "Show")
	css.visibility = "visible";
	tt_iState = 2;
	if(tt_aV[FADEIN])
		tt_Fade(0, 0, tt_aV[OPACITY], Math.round(tt_aV[FADEIN] / tt_aV[FADEINTERVAL]));
	tt_ShowIfrm();
}
function tt_ShowIfrm()
{
	if(tt_ie56)
	{
		var ifrm = tt_aElt[tt_aElt.length - 1];
		if(ifrm)
		{
			var css = ifrm.style;
			css.zIndex = tt_aElt[0].style.zIndex - 1;
			css.width = tt_w + "px";
			css.height = tt_h + "px";
			css.display = "block";
		}
	}
}
function tt_Move(e)
{
	// Protect some browsers against jam of mousemove events
	if(!tt_op && !tt_ie)
	{
		if(tt_bWait)
			return;
		tt_bWait = true;
		tt_tWaitMov.Timer("tt_bWait = false;", 1, true);
	}
	e = e || window.event || null;
	if(tt_aV[FIX])
	{
		tt_AddRemEvtFncEx(0, false);
		tt_SetTipPos(tt_aV[FIX][0], tt_aV[FIX][1]);
	}
	else if(!tt_ExtCallFncs(e, "MouseMove"))
		tt_SetTipPos(tt_PosX(e), tt_PosY(e));
	// The first onmousemove when the HTML element has just been hovered
	if(!tt_Move.over)
	{
		tt_Move.over = e.target || e.srcElement;
		if(tt_Move.over)
			tt_StartMov();
	}
}
function tt_PosX(e)
{
	var x;

	x = (typeof(e.pageX) != tt_u) ? e.pageX : (e.clientX + tt_scrlX);
	if(tt_aV[LEFT])
		x -= tt_w + tt_aV[OFFSETX] - (tt_aV[SHADOW] ? tt_aV[SHADOWWIDTH] : 0);
	else
		x += tt_aV[OFFSETX];
	// Prevent tip from extending past right/left clientarea boundary
	if(x > tt_maxPosX)
		x = tt_maxPosX;
	return((x < tt_scrlX) ? tt_scrlX : x);
}
function tt_PosY(e)
{
	var yMus, y;

	yMus = (typeof(e.pageY) != tt_u) ? e.pageY : (e.clientY + tt_scrlY);
	// The following logic applys some hysteresis when the tip has snapped
	// to the other side of the mouse. In doubt (insufficient space above
	// and below the mouse) the tip is positioned below.
	if(tt_aV[ABOVE] && (!tt_bJmpVert || tt_CalcPosYAbove(yMus) >= tt_scrlY + 16))
		y = tt_DoPosYAbove(yMus);
	else if(!tt_aV[ABOVE] && tt_bJmpVert && tt_CalcPosYBelow(yMus) > tt_maxPosY - 16)
		y = tt_DoPosYAbove(yMus);
	else
		y = tt_DoPosYBelow(yMus);
	// Snap to other side of mouse if tip would extend past window boundary
	if(y > tt_maxPosY)
		y = tt_DoPosYAbove(yMus);
	if(y < tt_scrlY)
		y = tt_DoPosYBelow(yMus);
	return y;
}
function tt_DoPosYBelow(yMus)
{
	tt_bJmpVert = tt_aV[ABOVE];
	return tt_CalcPosYBelow(yMus);
}
function tt_DoPosYAbove(yMus)
{
	tt_bJmpVert = !tt_aV[ABOVE];
	return tt_CalcPosYAbove(yMus);
}
function tt_CalcPosYBelow(yMus)
{
	return(yMus + tt_aV[OFFSETY]);
}
function tt_CalcPosYAbove(yMus)
{
	var dy = tt_aV[OFFSETY] - (tt_aV[SHADOW] ? tt_aV[SHADOWWIDTH] : 0);
	if(tt_aV[OFFSETY] > 0 && dy <= 0)
		dy = 1;
	return(yMus - tt_h - dy);
}
function tt_OnOut()
{
	tt_AddRemEvtFncEx(1, false);
	if(!(tt_aV[STICKY] && (tt_iState & 2)))
		tt_HideInit();
}
// Most browsers don't fire a mouseout if the mouse leaves an element just
// because the window being scrolled.
function tt_HideOnScrl()
{
	if(tt_iState && !(tt_aV[STICKY] && (tt_iState & 2)))
		tt_HideInit();
}
function tt_HideInit()
{
	tt_ExtCallFncs(0, "HideInit");
	tt_AddRemEvtFncEx(0, false);
	if(tt_flagOpa && tt_aV[FADEOUT])
	{
		tt_tFade.EndTimer();
		if(tt_opa)
		{
			var n = Math.round(tt_aV[FADEOUT] / (tt_aV[FADEINTERVAL] * (tt_aV[OPACITY] / tt_opa)));
			tt_Fade(tt_opa, tt_opa, 0, n);
			return;
		}
	}
	tt_tHide.Timer("tt_HideTip();", 1, false);
}
function tt_OpReHref()
{
	if(tt_elDeHref)
	{
		tt_elDeHref.setAttribute("href", tt_elDeHref.t_href);
		tt_RemEvtFnc(tt_elDeHref, "mousedown", tt_OpReHref);
		window.status = tt_elDeHref.t_stats;
		tt_elDeHref = null;
	}
}
function tt_Fade(a, now, z, n)
{
	if(n)
	{
		now += Math.round((z - now) / n);
		if((z > a) ? (now >= z) : (now <= z))
			now = z;
		else
			tt_tFade.Timer("tt_Fade("
							+ a + "," + now + "," + z + "," + (n - 1)
							+ ")",
							tt_aV[FADEINTERVAL],
							true);
	}
	now ? tt_SetTipOpa(now) : tt_HideTip();
}
// To circumvent the opacity nesting flaws of IE, we must set the opacity
// for each sub-DIV by its own, rather than for the container DIV.
function tt_SetTipOpa(opa)
{
	tt_SetOpa(tt_aElt[5].style, opa);
	if(tt_aElt[1])
		tt_SetOpa(tt_aElt[1].style, opa);
	if(tt_aV[SHADOW])
	{
		opa = Math.round(opa * 0.8);
		tt_SetOpa(tt_aElt[7].style, opa);
		tt_SetOpa(tt_aElt[8].style, opa);
	}
}
function tt_OnCloseBtnOver(iOver)
{
	var css = tt_aElt[4].style;

	iOver <<= 1;
	css.background = tt_aV[CLOSEBTNCOLORS][iOver];
	css.color = tt_aV[CLOSEBTNCOLORS][iOver + 1];
}
function tt_Int(x)
{
	var y;

	return(isNaN(y = parseInt(x)) ? 0 : y);
}
// Adds or removes the document.mousemove or HoveredElem.mouseout handler
// conveniently. Keeps track of those handlers to prevent them from being
// set or removed redundantly.
function tt_AddRemEvtFncEx(iTyp, bAdd)
{
	var PSet = bAdd ? tt_AddEvtFnc : tt_RemEvtFnc;

	if(iTyp)
	{
		if(bAdd != tt_bOutFnc)
		{
			PSet(tt_Move.over, "mouseout", tt_OnOut);
			tt_bOutFnc = bAdd;
			if(!bAdd)
				tt_OpReHref();
		}
	}
	else
	{
		if(bAdd != tt_bMovFnc)
		{
			PSet(document, "mousemove", tt_Move);
			tt_bMovFnc = bAdd;
		}
	}
}
Number.prototype.Timer = function(s, iT, bUrge)
{
	if(!this.value || bUrge)
		this.value = window.setTimeout(s, iT);
}
Number.prototype.EndTimer = function()
{
	if(this.value)
	{
		window.clearTimeout(this.value);
		this.value = 0;
	}
}
function tt_SetOpa(css, opa)
{
	tt_opa = opa;
	if(tt_flagOpa == 1)
	{
		// Hack for bugs of IE:
		// A DIV cannot be made visible in a single step if an opacity < 100
		// has been applied while the DIV was hidden.
		// Moreover, in IE6, applying an opacity < 100 has no effect if the
		// concerned element has no layout (position, size, zoom, ...).
		if(opa < 100)
		{
			var bVis = css.visibility != "hidden";
			css.zoom = "100%";
			if(!bVis)
				css.visibility = "visible";
			css.filter = "alpha(opacity=" + opa + ")";
			if(!bVis)
				css.visibility = "hidden";
		}
		else
			css.filter = "";
	}
	else
	{
		opa /= 100.0;
		switch(tt_flagOpa)
		{
		case 2:
			css.KhtmlOpacity = opa; break;
		case 3:
			css.KHTMLOpacity = opa; break;
		case 4:
			css.MozOpacity = opa; break;
		case 5:
			css.opacity = opa; break;
		}
	}
}
function tt_Err(sErr)
{
	if(Debug)
		alert("Tooltip Script Error Message:\n\n" + sErr);
}

//===========  DEALING WITH EXTENSIONS	==============//
function tt_ExtCmdEnum()
{
	var s;

	// Add new command(s) to the commands enum
	for(var i in config)
	{
		s = "window." + i.toString().toUpperCase();
		if(eval("typeof(" + s + ") == tt_u"))
		{
			eval(s + " = " + tt_aV.length);
			tt_aV[tt_aV.length] = null;
		}
	}
}
function tt_ExtCallFncs(arg, sFnc)
{
	var b = false;
	for(var i = tt_aExt.length; i;)
	{--i;
		var fnc = tt_aExt[i]["On" + sFnc];
		// If the extension has defined a method for this event, call it
		if(fnc && fnc(arg))
			b = true;
	}
	return b;
}

tt_Init();
