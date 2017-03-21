// Example using HTTP POST operation

//This script is used to gather meta-data (offender + docket#). Saves each page as HTML for further extraction. You must set the data value to POST data of the first character of last name you are querying (A-Z). This script only prints, so must redirect output to a file > 

"use strict";
var fs = require('fs');
var page = require('webpage').create(),
    server = 'http://www.jud2.ct.gov/crdockets/SearchResultsDisp.aspx',
    data = '';

var count = 0;
var nextPage = 0;
page.onLoadFinished = function() {
 console.log("loaded page "+(++count));
 fs.write(count+'.html', page.content, 'w');

nextPage++;
if(count<6){
	//first pass
}else if(count==6){
	nextPage = 2;
}else if(nextPage==7){
	//next page
	nextPage = 2;
}


console.log("nextpage="+nextPage+",count="+count);
  
page.evaluate(function(nextPage, count){
 var ev = document.createEvent("MouseEvents");
  ev.initEvent("click", true, true);
   document.querySelector("a[href=\"javascript:__doPostBack('_ctl0$cphBody$grdDockets$_ctl54$_ctl"+nextPage+"','')\"]").click();
}, nextPage, count);
};
setTimeout(function() {
page.open(server, 'post', data, function (status) {
    if (status !== 'success') {
        console.log('Unable to post!');
    } else {
       // console.log(page.content);
    }
//    phantom.exit();
});
}, 2000);

