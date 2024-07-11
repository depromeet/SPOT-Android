window.androidObj = function AndroidClass() {};

var svgBody = document.getElementById('div').innerHTML;

var query = '*[id^=section]';
var tablePathList = document.querySelectorAll(query);

document.addEventListener("click", doSomething);

function doSomething(e) {
    var clickedItem = e.target.id;
    var item;

    for (item = 0; item < tablePathList.length; item++) {
        if (clickedItem === tablePathList[item].id) {
            window.androidObj.textToAndroid(clickedItem);
        }
    }

    e.stopPropagation();
}

function resetZoom() {
    console.log("hi");
    // 모르겠다
//    document.body.style.zoom = "100% !important"
//    width: 100%;
//      height: 100%;
//    document.getElementsByTagName("body").style.transform = "scale(1)";
//    document.body.style.transform = "scale(1)";
//    document.body.style.transformOrigin = "0 0";
}