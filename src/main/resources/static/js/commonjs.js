// 숫자를 받아서 2자리수 문자열로 변환하는 함수
function numPadding2(number) {
    return number < 10 ? "0" + number : number.toString();
}

