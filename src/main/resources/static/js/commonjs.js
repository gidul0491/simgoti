$(document).ready(function () {
    $("#tabs").load("/tabs.html");
    $("#header").load("/header.html");


});
// 숨겨진 경고문
function showHiddenAlert(text){
    const alert = $("#hidden-alert");
    alert.removeAttr("hidden");
    alert.text(text);
}
function hideHiddenAlert() {
    const alert = $("#hidden-alert");
    alert.attr("hidden","hidden");
}
// 경고팝업 띄워주는 함수
function showPopup(msg){
    $("#warn-popup").removeAttr("hidden");
    $("#warn-popup-content").text(msg);
}


// 숫자를 받아서 2자리수 문자열로 변환하는 함수
function numPadding2(number) {
    return number < 10 ? "0" + number : number.toString();
}



// 페이지 이동 함수
function moveToCalculate() {
    location.href = "/simg/simgOti/calculate";
}

// 폼 등
// html을 클릭했을 때, 뭐가 나타나고 뭐가 사라지고 그런거
$("html").on("click", function (e) {

    // .show-popup를 클릭하면 바로뒤의 div.popup 을 보이게 함
    if ($(e.target).hasClass("show-popup")) {
        $(e.target).next("div.popup").removeAttr("hidden");
    }

    // .popup을 클릭하면 .popup을 숨김
    if ($(e.target).hasClass("popup")) {
        $(".popup").attr("hidden", "hidden");
    }

    // .close-popup을 클릭하면 .popup을 숨김
    if ($(e.target).hasClass("close-popup")) {
        $(".popup").attr("hidden", "hidden");
    }
});

// 이메일 관련
const emailDomain = ["직접입력","naver.com","gmail.com","daum.net","hanmail.net","nate.com"];


// 스크롤 이동하는 함수
function moveScrollTop(item) {
    const top = $(item).offset().top - 10;
    $("html").animate({scrollTop: top}, 10);
}


// 날짜객체를 yyyy-MM-dd hh:mm으로 변환
function dateTimeToStr(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
}

// 날짜객체를 yyyy-MM-dd로 변환
function dateToStr(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');

    return year + '-' + month + '-' + day;
}


// alert 및 이동
function alertAndMove(msg, url) {
    alert(msg);
    location.href = url;
}

// 상태코드 해석 함수
function stateCodeToStr(code){
    let str;
    switch(code) {
        case "401": str="신청";break;
        case "402": str="가입";break;
        case "403": str="가입취소";break;
        case "404": str="가입취소";break;
        case "405": str="만료";break;
        case "406": str="중도해지";break;
        case "407": str="중도해지";break;
        default: str="신청";
    }
    return str;
}

// 1000단위로 끊는 함수
function putComma(text) {
    const regex = /\B(?=(\d{3})+(?!\d))/g;
    const matches = text.toString().replace(regex, ",");
    return matches;
}

// 만, 억 단위로 끊는 함수
function numToKrUnit(num) {
    const unit = ["", "만", "억", "조", "경"];
    let splitedNum = [];
    let tempNum = num;
    let result = "";

    while (tempNum / 10000 > 0) {
        splitedNum.push(tempNum % 10000);
        tempNum = Math.floor(tempNum / 10000);
    }

    for (let i = 0; i < splitedNum.length; i++) {
        if (splitedNum[i] != 0) {
            result = splitedNum[i] + unit[i] + result;
        }
    }

    return result;
}

// 보험 나이 구하는 함수
function calculateInsAge(birth) {
    const year = birth.substring(0, 4);
    const mth = birth.substring(4, 6);
    const dt = birth.substring(6, 8);

    // 보험나이는 오늘을 6개월이후의 하루 전날로 계산한 만나이와 동일함
    let today = new Date();
    today.setMonth(today.getMonth()+6);
    today.setDate(today.getDate()-1);
    today.setHours(0, 0, 0, 0);

    let birthday = new Date(`${year}-${mth}-${dt}`);

    let insAge = today.getFullYear() - year;

    // 월 비교
    if(mth > (today.getMonth() + 2)){
        insAge--;
    }
    // 일 비교
    else if(mth == (today.getMonth() + 1) && dt > today.getDate()){
        insAge--;
    }

    return insAge;
}

// 생년월일 8자리 유효성검사
function validate8birth(dateInput) {
    if (dateInput.length !== 8) {
        return "length";
    }

    const year = dateInput.substring(0, 4);
    const month = dateInput.substring(4, 6);
    const day = dateInput.substring(6, 8);

    const date = new Date(year, month - 1, day);

    if (date.getFullYear() == year && date.getMonth() + 1 == month && date.getDate() == day) {
        if(calculateInsAge(dateInput) > 80 || calculateInsAge(dateInput) < 1){
            return "age";
        }
        else {
            return "valid";
        }

    } else {
        return "invalid";
    }
}

// 생년월일 6자리 유효성검사
function validate6birth(dateInput) {
    if (dateInput.length !== 6) {
        return "length";
    }
    else{
        const yr = dateInput.substring(0, 2);
        const month = dateInput.substring(2, 4);
        const day = dateInput.substring(4, 6);

        const validCentury = ["19", "20"];

        const now = new Date();
        const nowCent = now.getFullYear().toString().substring(0,2);
        const nowYr = now.getFullYear().toString().substring(2,4);


        // 입력한 연도와 현재 연도가 동일한 경우
        if(nowYr == yr){
            // 입력한 월이 오늘의 월보다 큰 경우 - 지금은 2023년9월인데 231001을 입력한경우는 1923년을 의미함
            if((now.getMonth()+1) < month){
                return "age";
            }
            // 입력한 월이 오늘의 월과 같은 경우
            else if((now.getMonth()+1)==month){
                // 입력한 일이 오늘보다 큰 경우 - 지금은 2023년 9월 28일인데 230929를 입력한 경우는 1923년을 의미함
                if(now.getDate() < day){
                    return "age";
                }
                // 입력한 일이 오늘보다 작은 경우 - 지금은 2023년 9월 28인데 230927을 입력한 경우는 2023년 9월 27일로 가정함
                else {
                    return "age";
                }
            }
            // 나머지 경우 - 입력한 월이 오늘의 월보다 작은 경우 - 지금은 2023년 9월인데 230801을 입력한 경우는 2023년 8월 1일로 가정함
            else {
                // 보험나이가 1살이 안되는 경우
                if(calculateInsAge(nowCent+yr+month+day) < 1){
                    return "age";
                }
                else{
                    return "valid";
                }
            }
        }
        else if(nowYr < yr){
            const cent = (parseInt(nowCent) -1).toString();
            if(calculateInsAge(cent+yr+month+day) > 80){
                return "age";
            }
        }
        else if(nowYr > yr){
            if(calculateInsAge(nowCent+yr+month+day) < 1){
                return "age";
            }
            else {
                return "valid";
            }
        }
    }
}

function validateKrNm() {

}
