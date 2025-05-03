function openPopup() {
document.getElementById('popup').classList.add('show');
}
function closePopup() {
document.getElementById('popup').classList.remove('show');
}
function openPopup1(event) {
    event.stopPropagation();
    document.getElementById('popup1').classList.add('show');
    }
function closePopup1() {
document.getElementById('popup1').classList.remove('show');
}
function openPopup2(event) {
    event.stopPropagation();
    document.getElementById('popup2').classList.add('show');
    }
function closePopup2() {
document.getElementById('popup2').classList.remove('show');
}

function openPopup3(event) {
    event.stopPropagation();
    document.getElementById('popup3').classList.add('show');
    }
function closePopup3() {
document.getElementById('popup3').classList.remove('show');
}
function confirmDelete(button, event) {
    event.stopPropagation();
    let url = button.getAttribute("data-url");
    if (confirm("Bạn có chắc chắn muốn xóa?")) {
        window.location.href = url;
    }
}
function search(event) {
    event.preventDefault();
    let currentPage = window.location.pathname;
    let searchInput = document.getElementById("input").value;
    if (currentPage.includes("/account_user"))
        window.location.href='/account_user/search/'+searchInput;
    else if (currentPage.includes("/card_set"))
        window.location.href='/card_set/search/'+searchInput;
    else if (currentPage.includes("/card"))
        window.location.href='/card/search/'+searchInput;
}