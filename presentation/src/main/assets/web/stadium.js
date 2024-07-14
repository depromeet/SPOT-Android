window.androidObj = function AndroidClass() {};

var svgBody = document.getElementById('div').innerHTML;

var query = '*[id^=section]';
var sectionPaths = document.querySelectorAll(query);

document.addEventListener("click", onClickSection);

function onClickSection(e) {
    var sectionId = e.target.id;
    var index;

    for (index = 0; index < sectionPaths.length; index++) {
        if (sectionId === sectionPaths[index].id) {
            window.androidObj.getStadiumBlockNumber(sectionId);
        }
    }

    e.stopPropagation();
}

function getStadiumBlockNumber(sectionId) {
    window.androidObj.getStadiumBlockNumber(sectionId)
}
