window.androidObj = function AndroidClass() {};

var unclick = '.unclick { pointer-events: none; }'
var unselected = '.unselected { opacity: 0.1; }'

var svg = document.getElementsByTagName('svg')[0];

var defs = document.createElement('defs'); // <defs></defs>
var style = document.createElement('style'); // <style></style>
style.setAttribute('type', 'text/css'); // <style type="text/css"></style>

style.innerHTML = unclick + unselected;
/**
<style>
    .unclick { pointer-events: none; }
    .unselected { opacity: 0.8; }
</style>
**/

defs.appendChild(style); // <defs> <style></style> </defs>
svg.appendChild(defs) // <svg> <defs> <style></style> </defs>  </svg>

var query = '*[id^=section]';
var sectionPaths = document.querySelectorAll(query);

var blockQuery = '*[id^=block]';
var blockPaths = document.querySelectorAll(blockQuery);

var groupQuery = '*[id^=group]';
var groupPaths = document.querySelectorAll(groupQuery);
var groupIds = new Set(Array.from(groupPaths).map(el => el.id));

var groundQuery = '*[id^=ground]';
var groundPaths = document.querySelectorAll(groundQuery);
var groundIds = new Set(Array.from(groundPaths).map(el => el.id));

var fontQuery = '*[id^=font]';
var fontPaths = document.querySelectorAll(fontQuery);
var fontIds = new Set(Array.from(fontPaths).map(el => el.id));


/**
모든 태그 id 값 찾아서 클릭하지 못하게 막는 반복문 =======
**/
var allElements = document.querySelectorAll("*");

var allIds = Array.from(allElements)
    .map(el => el.id)
allIds.forEach(id => {
    var element = document.getElementById(id);
    if (element) {
        element.classList.add("unclick");
    }
})
// ==========================================



document.addEventListener("click", onClickSection);


function onClickSection(e) {
    var sectionId = e.target.id;
    console.log(sectionId)
    var index;

    for (index = 0; index < sectionPaths.length; index++) {
        if (sectionId === sectionPaths[index].id) {
            window.androidObj.getStadiumBlockNumber(sectionId);
        }
    }

    e.stopPropagation();
}

function onActiveBlock(block) {
    console.log(block)
    allIds.forEach(id => { // 모든 id 값을 가지는 태그들 style class .unclick 과 .unselected 제거 (초기화 한다는 의미)
        var element = document.getElementById(id);
        if (element) {
            element.classList.remove("unclick");
            element.classList.remove("unselected");
        }
    })

    var activeIds = new Set();

    if (Array.isArray(block)) { // 들어오는 block 매개변수가 Array 일 때 ex) "101,102,103"
        block.forEach(function(id) {
            var activeQuery = "*[id*='" + id + "']"; // 블록 코드를 가진 id를 찾는다. ex) "*[id*='101']"
            var activeElements = document.querySelectorAll(activeQuery); // 모든 id 중 "101"을 포함한 원소를 찾는다. ex) section_red_101

            Array.from(activeElements).forEach(el => { // 휠체어석과 일반석 구분을 위한 로직
                var elIdSplit = el.id.split('_')
                /**
                1) section_red_101 일반석일 경우,
                    ex) elIdSplit = "section,red,101"
                        elIdSplit[elIdSplit.length - 1] = 101
                        id = 101
                        => 동일하니 add
                2) section_red_101w 휠체어석일 경우,
                    ex) elIdSplit = "section,red,101w"
                        elIdSplit[elIdSplit.length - 1] = 101w
                        id = 101
                        => 다르니 pass
                **/
                if (elIdSplit[elIdSplit.length - 1] === id) {
                    activeIds.add(el.id)
                }
            });

        });
    } else { // 들어오는 block 매개변수가 단순히 문자열 일 때 ex) "red", "blue"...
        var activeQuery = "*[id*=" + block + "]";
        var activeElements = document.querySelectorAll(activeQuery);

        Array.from(activeElements).forEach(el => activeIds.add(el.id));
    }


    /**
    전체 id를 가져옴.
    해당 id 에서는 필터가 진행되는데,
    - 매개변수에 따라서 다름
        - 1) 배열
            : 블록 코드를 포함하는 id 모두 삭제. 즉, 배열 = "101,102" section_red_101, section_red_102 삭제
        - 2) 문자열
            : 문자열로 들어온 색상을 가진 id 모두 삭제. 즉, 문자열 = red => section_red_ 로 시작 id 삭제
    - group_ 로 시작 id 삭제
    - font_ 로 시작 id 삭제
    - ground_ 로 시작 id 삭제
    **/
    var otherIds = Array.from(allElements)
        .map(el => el.id)
        .filter(id => id && !activeIds.has(id) && !groupIds.has(id) && !fontIds.has(id) && !groundIds.has(id));


    /**
    위에서 필터를 진행한 뒤 남은 id 원소를 가진 배열이 만들어졌다.
    해당 id를 가진 원소는 클릭이 되지 않고, opacity 50% 스타일을 가진다.
    즉, 비활성화 된 블럭들이 되는 것이다.
    **/
    otherIds.forEach(id => {
        var element = document.getElementById(id);
        if (element) {
            element.classList.add('unclick');
            element.classList.add('unselected');
        }
    });
}

function removeStyle() {
    var allIds = Array.from(allElements)
        .map(el => el.id)
    allIds.forEach(id => {
        var element = document.getElementById(id);
        if (element) {
            element.classList.add("unclick");
            element.classList.remove("unselected");
        }
    })
}


function getStadiumBlockNumber(sectionId) {
    window.androidObj.getStadiumBlockNumber(sectionId)
}
