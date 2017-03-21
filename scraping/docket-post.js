//This script uses PhantomJS to navigate to the CT judicial 'search by docket-number' page. Submits a request with docket-number and saves the docket to html file for future extraction of elements.
"use strict";
var fs = require('fs');
var page = require('webpage').create();
var system = require('system');
var args = system.args;
var clicks = 0;

if(args.length === 1){
	console.log("u forgot args");
	phantom.exit();
}

var dockNum=args[1];
console.log("arg:"+dockNum);
page.onLoadFinished = function(){
	console.log('load finished');
    if(clicks++ == 1){
	console.log(page.content.title);
//    page.render('ok'+clicks+'.png');
      fs.write('scrapes/'+dockNum.split(",")[0]+'.html',page.content,'w');
      phantom.exit();
    }else{
       
//    page.render('ok'+clicks+'.png');
    }

}
page.onUrlChanged = function(targetUrl) {
    console.log('New URL: ' + targetUrl);
};

page.open('http://www.jud2.ct.gov/crdockets/DocketNoEntry.aspx?source=Disp', function(status) {

var system = require('system');

page.onConsoleMessage = function(msg) {
    system.stderr.writeLine('console: ' + msg);
};


  page.evaluate(function(dockNum) {
function click(el){
    var ev = document.createEvent("MouseEvent");
    ev.initMouseEvent(
        "click",
        true /* bubble */, true /* cancelable */,
        window, null,
        0, 0, 0, 0, /* coordinates */
        false, false, false, false, /* modifier keys */
        0 /*left*/, null
    );
    el.dispatchEvent(ev);
}
    console.log("SUBMITTING:"+dockNum);
var split = dockNum.split(',');
var docString=split[0];
var ga_jd=split[1];
var type=split[2];
var year=split[3];
var docnum=split[4];
var suffix=split[5];

    document.getElementById('_ctl0_cphBody_ddlCourts').value=ga_jd;
    document.getElementById('_ctl0_cphBody_ddlCaseType').value=type;
    document.getElementById('_ctl0_cphBody_txtYear').value=year;
    document.getElementById('_ctl0_cphBody_txtDktNo').value=docnum;
    
    document.getElementById('_ctl0_cphBody_DrpSuffix').value=suffix;
    
    click(document.getElementById('_ctl0_cphBody_btnSearch'));

  },dockNum);
console.log('bye');
 //phantom.exit();

});




/*
setTimeout(function() {
page.open(server, 'post', data, function (status) {
console.log("opening");
    if (status !== 'success') {
        console.log('Unable to post!');
    } else {
        console.log(page.content);
    }
//    phantom.exit();
});
}, 2000);*/
