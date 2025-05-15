document.addEventListener('DOMContentLoaded', function () {
    const quill = new Quill('#editor', {
        theme: 'snow'
    });

    quill.on('text-change', function () {
        document.getElementById('description-textarea').value = quill.root.innerHTML;
    });
});
