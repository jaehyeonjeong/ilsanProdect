document.addEventListener("DOMContentLoaded", function () {
    const labels = document.querySelectorAll("label");
    const cardData = document.getElementById("cardData");
    const form = document.getElementById("cardForm");

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
