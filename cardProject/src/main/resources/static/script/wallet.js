const swiper = new Swiper('.buy-list-container', {
    autoplay: {
        delay: 1000,
    },

    // Optional parameters
    loop: true,
    slidesPerView: 3,
    spaceBetween:20,
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

window.onload = function() {
    if(window.location.hash === '#bottom-section') {
        document.getElementById('bottom-section').scrollIntoView({behavior: 'smooth'});
    }
};