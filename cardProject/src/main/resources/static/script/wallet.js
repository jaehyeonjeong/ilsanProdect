const swiper = new Swiper('.buy-list-container', {
    autoplay: {
        delay: 1000,
    },

    // Optional parameters
    loop: true,
    slidesPerView: 3,
    spaceBetween: 20,
    speed: 1000,


    // If we need pagination
    pagination: {
        el: '.swiper-pagination',
    },

    // Navigation arrows
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },

    // And if we need scrollbar
    scrollbar: {
        el: '.swiper-scrollbar',
    },
});

const select = document.querySelector(".card-count-select");
const result = document.querySelector(".newcard-review-section .newcard-list");

// 처음 서버의 사이즈 값을 주입 GetMapping("/")의 model 사이즈 호출
const serverSize = parseInt(document.getElementById("serverSize").value);
loadFromServer(serverSize);

// ajax async 방식으로 카드 데이터 사이즈 변경 시 리스트 사이즈도 변경
select.addEventListener("change", (e) => {
    const count = parseInt(select.value);
    e.preventDefault();
    loadFromServer(count);
    // select 할 때 마다 리스트 사이즈 변경
});

// ajax 방식으로 데이터를 전송하면 url 상관없이 size 정보 수정
async function loadFromServer(count) {
    try {
        const res = await fetch("/card-list?size="+count);
        const items = await res.json();

        // 기존 내용 비우기(update => delete, insert)
        result.innerHTML = "";
        console.log(items)

        // 선택된 개수만큼 잘라내기
        const selectedItems = items.slice(0, count);

        selectedItems.forEach(item => {
            const div = document.createElement("div");
            div.classList.add("newcard-item");

            div.innerHTML = `
                <div class="newcard-rank">NEW!</div>
                <div class="newcard-text">
                    <span class="newcard-category">${item.corp ?? "카드사"}</span>
                    <p class="newcard-title-text">${item.name ?? "카드 이름"}</p>
                </div>
                <a href="/card/${item.id}/normal_info">
                    <img src="${item.cardImagePath}" onerror="this.src='/images/default.png';" class="newcard-img">
                </a>
            `;

            result.appendChild(div);
        });

    } catch (err) {
        console.error("로드 오류:", err);
    }
}