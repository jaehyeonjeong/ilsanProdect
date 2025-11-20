document.addEventListener("DOMContentLoaded", () => {

    /* ------------------------------
       1) CSV → 차트 데이터 매핑
    ------------------------------ */

    fetch("/csv/card_table_scrap.csv")
        .then(res => res.text())
        .then(csv => {

            const rows = csv.trim().split("\n").map(v => v.split(","));

            // 헤더 제거
            const body = rows.slice(1);

            // 카드 객체 배열로 변환
            const cards = body.map(col => ({
                id: col[0],
                name: col[1],
                cate: col[2],      // CRD, CHK
                corp: col[3],
                img: col[4],
                pre: Number(col[6]) // 실적
            }));

            // 카테고리별 분류
            const creditCards = cards
                .filter(c => c.cate === "CRD")
                .sort((a, b) => b.pre - a.pre)
                .slice(0, 5);

            const checkCards = cards
                .filter(c => c.cate === "CHK")
                .sort((a, b) => b.pre - a.pre)
                .slice(0, 5);

            // 차트 박스에 데이터 넣기
            fillChart("credit", creditCards);
            fillChart("check", checkCards);
        });


    /* ------------------------------
       2) 차트 DOM에 데이터 삽입 함수
    ------------------------------ */
    function fillChart(type, data) {
        const box = document.querySelector(`.chart-box[data-chart="${type}"]`);
        if (!box) return;

        const list = box.querySelector(".chart-list");
        list.innerHTML = "";

        data.forEach((card, index) => {
            const li = document.createElement("li");
            li.innerHTML = `
                <span class="rank">${index + 1}위</span>
                <span class="card-name">${card.name}</span>
                <span class="card-pre">실적 ${card.pre}만원</span>
            `;
            list.appendChild(li);
        });
    }


    /* ------------------------------
       3) 차트 박스 hover 확장 기능
       (chartHover.js 내용과 충돌 제거 버전)
    ------------------------------ */
    document.querySelectorAll(".chart-box").forEach(box => {

        box.addEventListener("mouseenter", () => {
            box.classList.add("expand");
        });

        box.addEventListener("mouseleave", () => {
            box.classList.remove("expand");
        });

    });

});
