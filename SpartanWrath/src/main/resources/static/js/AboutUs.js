let arbol = document.getElementById('tree');
let cloud = document.getElementById('cloud');
let mountains_behind = document.getElementById('mountains_02');
let texto = document.getElementById('texto');
let mountain3 = document.getElementById('mountains_03');
let header = document.querySelector('header');




window.addEventListener('scroll',function (){
    let value = window.scrollY;
    arbol.style.top =  value * 1.5 + 'px';
    cloud.style.top = value * 1.05 + 'px';
    mountains_behind.style.top = value * 0.5 + 'px';
    texto.style.marginRight = value * 4 + 'px';
    texto.style.marginTop = value * 1.5 + 'px';
    mountain3.style.top = value * 1.05 + 'px';
    header.style.top = value * 0.5 + 'px';

})

    let imgBx = document.querySelectorAll('.imgBx');
    let contentBx = document.querySelectorAll('.contentBx');

    for (var x = 0; x < imgBx.length; x++) {
    imgBx[x].addEventListener('mouseover', function () {
        for (var j = 0; j < contentBx.length; j++) {
            contentBx[j].className = 'contentBx';
        }
        document.getElementById(this.dataset.id).className = 'contentBx active';

        for (var k = 0; k < imgBx.length; k++) {
            imgBx[k].className = 'imgBx';
        }
        this.className = 'imgBx active';
    });
}
