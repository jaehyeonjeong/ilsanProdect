document.addEventListener('DOMContentLoaded', function () {
    const dropdownContainers = document.querySelectorAll('.custom-dropdown-container');

    function updateSelectedCount(container) {
        const selectedCountSpan = container.querySelector('.selected-count');
        if (!selectedCountSpan) return;

        const type = selectedCountSpan.getAttribute('data-filter-type') || '';
        const checkboxes = container.querySelectorAll('.filter-checkbox');
        let selectedCount = 0;

        checkboxes.forEach(function (checkbox) {
            if (checkbox.checked) {
                selectedCount++;
            }
        });

        selectedCountSpan.textContent = `${type} ${selectedCount}개 선택됨`;
    }

    // 각 드롭다운 초기화
    dropdownContainers.forEach(function (container) {
        const header = container.querySelector('.dropdown-header');
        const list = container.querySelector('.dropdown-list');
        const checkboxes = container.querySelectorAll('.filter-checkbox');

        if (!header || !list) return;

        // 첫 진입 시 선택 개수 세팅
        updateSelectedCount(container);

        // 헤더 클릭 시 해당 드롭다운만 토글
        header.addEventListener('click', function (e) {
            e.stopPropagation();

            document.querySelectorAll('.dropdown-list').forEach(otherList => {
                if (otherList !== list) {
                    otherList.style.display = 'none';
                }
            });

            list.style.display = (list.style.display === 'block') ? 'none' : 'block';
        });

        // 체크박스 변경 시 개수 갱신
        checkboxes.forEach(function (checkbox) {
            checkbox.addEventListener('change', function () {
                updateSelectedCount(container);
            });
        });
    });

    // 드롭다운 외부 클릭 시 닫기
    document.addEventListener('click', function (event) {
        document.querySelectorAll('.dropdown-list').forEach(list => {
            if (!event.target.closest('.custom-dropdown-container')) {
                list.style.display = 'none';
            }
        });
    });
});
