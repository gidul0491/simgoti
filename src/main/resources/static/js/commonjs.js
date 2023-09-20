// 숫자를 받아서 2자리수 문자열로 변환하는 함수
function numPadding2(number) {
    return number < 10 ? "0" + number : number.toString();
}

// // html문서 변수선언
// const calculate = "calculate.html"

// // #content 의 내용을 바꾸는 함수
// function changeContent(html) {
//     $("#content").load(html);
// }
//
// // 탭을 누르면 #content를 변경
// $("#calculateTab").on("click", function(){
//    changeContent(calculate);
// });

// 탭을 누르면 페이지 이동
$("#calculateTab").on("click", function(){
    location.href = "/simg/simgOti/calculate";
});

// 폼 등
// html을 클릭했을 때, 뭐가 나타나고 뭐가 사라지고 그런거
$("html").on("click", function(e){

    // .text-option 이외의 요소를 클릭하면 .text-option을 숨김
    if(!$(e.target).hasClass("text-option")){
        $(".text-option").attr("hidden","hidden");
    }

    // .text-select를 클릭하면 다음 select요소를 보이게 함
    if($(e.target).hasClass("text-select")){
        $(e.target).next("select").removeAttr("hidden");
    }

    // .show-popup를 클릭하면 바로뒤의 div.popup 을 보이게 함
    if($(e.target).hasClass("show-popup")){
        $(e.target).next("div.popup").removeAttr("hidden");
    }

    // .popup을 클릭하면 .popup을 숨김
    if($(e.target).hasClass("popup")){
        $(".popup").attr("hidden","hidden");
    }

    // .close-popup을 클릭하면 .popup을 숨김
    if($(e.target).hasClass("close-popup")){
        $(".popup").attr("hidden","hidden");
    }
});

// input text의 클래스를 text-select, 바로 뒤 요소인 select의 클래스를 text-option이라고 하면
// select값 변경시 이전 요소인 input text에 value값을 집어넣고 select요소가 사라짐
$('.text-option').on("change", function(){
    $(this).prev("input").val($(this).prev("input").next("select").children("option:selected").text());
    $(this).attr("hidden","hidden");
});

// 스크롤 이동하는 함수
function moveScrollTop(item){
    const offset = $(item).offset();
    const top = Math.floor(offset.top);
    $("body").animate({scrollTop:top}, 400);
}

// utf8을 base64로, base64를 utf8로
function utob( str ) {
    return btoa(unescape(encodeURIComponent( str )));
}

function btou( str ) {
    return decodeURIComponent(escape(atob( str )));
}

// // popup
// const popup = $(".popupBackground");
// const hidePopup = () => {
//     popup.removeClass("show");
//     popup.addClass("hide");
// }
// const showPopup = () => {
//     popup.removeClass("hide");
//     popup.addClass("show");
// }
//
// $(".popupBackground").on("click", function(e){
//     if(!$(e.target).hasClass("popup")){
//         hidePopup();
//     }
// });
//
// $(".popupNext").on("click",function(){
//     hidePopup();
// });
// // popup end

