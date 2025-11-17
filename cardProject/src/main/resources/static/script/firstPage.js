document.addEventListener("DOMContentLoaded", function () {
    const labels = document.querySelectorAll("label");
    const cardData = document.getElementById("cardData");
    const form = document.getElementById("cardForm");

    // ✅ 초기 체크된 라디오/체크박스에 selected 클래스 적용
    labels.forEach(label => {
        const input = label.querySelector("input");
        if (input && input.checked) {
            label.classList.add("selected");
        }
    });

    labels.forEach(label => {
        label.addEventListener("click", (event) => {
            const input = label.querySelector("input");
            if (!input) return;

            event.preventDefault();

            if (input.type === "checkbox") input.checked = !input.checked;
            if (input.type === "radio") {
                input.checked = true;
                document.querySelectorAll(`input[name="${input.name}"]`).forEach(i => {
                    i.closest("label")?.classList.remove("selected");
                });
            }

            label.classList.toggle("selected", input.checked);
            updateCardCount();
        });
    });

    updateCardCount();

    // ✅ 핵심 부분: 서버에 실제 countList만 요청
    function updateCardCount() {
        const formData = new FormData(form);

        fetch(form.action, {
            method: form.method,
            body: formData
        })
            .then(res => res.text())
            .then(html => {
                // ✅ 서버 응답 HTML에서 정확히 countList 값만 추출
                const match = html.match(/id="cardData"[^>]*value="([^"]*)"/);
                const count = match ? match[1] : "0";

                cardData.value = count || "0";
                cardData.classList.add("updated");
                setTimeout(() => cardData.classList.remove("updated"), 300);
            })
            .catch(err => {
                console.error("카드 개수 갱신 실패:", err);
                cardData.value = "0";
            });
    }
});


// 라디오 버튼(카테고리 항목) 스크립트 동작
const radios = document.querySelectorAll('input[name="category"]');

// 페이지 로드 시 저장된 값 복원
window.addEventListener("DOMContentLoaded", () => {
    const savedCategory = localStorage.getItem("selectedCategory");

    if (savedCategory) {
        const target = document.querySelector(`input[name="category"][value="${savedCategory}"]`);
        if (target) target.checked = true;
    }
});

// 라디오 버튼 선택 시 값 저장
radios.forEach(radio => {
    radio.addEventListener("change", () => {
        if (radio.checked) {
            localStorage.setItem("selectedCategory", radio.value);
        }
    });
});


// 체크 박스 (혜택 항목) 스크립트 동작
const benefitCheckboxes = document.querySelectorAll('input[name="benefit"]');

// 체크박스 상태 복원
window.addEventListener("DOMContentLoaded", () => {
    benefitCheckboxes.forEach(checkbox => {
        const saved = localStorage.getItem(checkbox.value);
        checkbox.checked = saved === "true";
    });
});

// 체크박스 클릭 시 자동 제출
document.querySelectorAll('input[name="benefit"]').forEach(checkbox => {
    checkbox.addEventListener("change", () => {
        localStorage.setItem(checkbox.value, checkbox.checked);
        const checkedBenefits = document.querySelectorAll('input[name="benefit"]:checked');
        const count = checkedBenefits.length;

        // 선택된 체크박스 개수를 cardData에 반영
        // document.getElementById("cardData").value = count;
        // 필요 시 서버로 자동 제출
        document.getElementById("cardForm").submit();
    });
});

// 카드 조회 버튼 클릭 시 리셋 동작 적용
document.getElementById("resetBtn").addEventListener("click", function () {
    // 1. localStorage 초기화
    localStorage.removeItem("selectedCategory");
    const benefitCheckboxes = document.querySelectorAll('input[name="benefit"]');
    benefitCheckboxes.forEach(cb => localStorage.removeItem(cb.value));

    // 2. 폼 필드 초기화
    document.querySelectorAll('input[name="category"]').forEach(radio => radio.checked = false);
    benefitCheckboxes.forEach(cb => cb.checked = false);
});