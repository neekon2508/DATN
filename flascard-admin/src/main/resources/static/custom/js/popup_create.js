function openPopup() {
document.getElementById('popup').classList.add('show');
}
function closePopup() {
document.getElementById('popup').classList.remove('show');
}
function openPopup1(button, event) {
    event.stopPropagation();
    var popupId = button.getAttribute("data-popup-id"); // Lấy ID của popup
   document.getElementById(popupId).classList.add('show');
    }
function closePopup1(button) {
var popupId = button.getAttribute("data-popup-id"); // Lấy ID của popup
document.getElementById(popupId).classList.remove('show');
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