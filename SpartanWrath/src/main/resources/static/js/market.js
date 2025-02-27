let cartItems = [];

function addToCart(productName, price, productId) {
    const url = '/products/purchase';
    console.log("Nombre del producto:", productName);
    console.log("Precio del producto:", price);
    const existingItem = cartItems.find(item => item.name === productName);

    if (existingItem) {
        existingItem.quantity++;
    } else {
        cartItems.push({ name: productName, price: price, quantity: 1, productId: productId });
    }

    updateCartCount();
    showCart();
}

function updateCartCount() {
    const cartCount = document.getElementById('cart-count');
    if (cartCount) {
        cartCount.innerText = cartItems.reduce((total, item) => total + item.quantity, 0);
    }
}

function showCart() {
    const cart = document.getElementById('cart');
    const cartItemsList = document.getElementById('cart-items');
    cartItemsList.innerHTML = '';// Clear the list before adding elements

    if (cartItems.length === 0) {
        const emptyCartMsg = document.createElement('p');
        emptyCartMsg.textContent = 'Cart is Empty';
        cartItemsList.appendChild(emptyCartMsg);
    } else {
        cartItems.forEach(item => {
            const li = document.createElement('li');
            const quantitySpan = document.createElement('span');
            const nameSpan = document.createElement('span');
            const priceSpan = document.createElement('span');
            const addButton = document.createElement('button');
            const removeButton = document.createElement('button');

            quantitySpan.textContent = `${item.quantity} x `;
            nameSpan.textContent = `${item.name}`;
            priceSpan.textContent = `$${(item.price * item.quantity).toFixed(2)} `;
            addButton.textContent = '+';
            removeButton.textContent = '-';

            // Circle icon filled with orange color
            const circleIcon = document.createElement('i');
            circleIcon.classList.add('bi', 'bi-circle-fill');
            circleIcon.style.color = '#FFA500';

            // Add space between price and buttons
            const space = document.createElement('span');
            space.textContent = ' ';

            addButton.addEventListener('click', () => {
                item.quantity++;
                quantitySpan.textContent = `${item.quantity} x `;
                updateCartCount();
                showCart();
            });

            removeButton.addEventListener('click', () => {
                if (item.quantity > 1) {
                    item.quantity--;
                    quantitySpan.textContent = `${item.quantity} x `;
                } else {
                    //If there is only one unit and "-" is clicked, remove the product from the cart
                    const index = cartItems.indexOf(item);
                    if (index !== -1) {
                        cartItems.splice(index, 1);
                    }
                    showCart();
                }
                updateCartCount();
            });

            li.appendChild(quantitySpan);
            li.appendChild(circleIcon);
            li.appendChild(nameSpan);
            li.appendChild(priceSpan);
            li.appendChild(space);
            li.appendChild(addButton);
            li.appendChild(removeButton);

            cartItemsList.appendChild(li);
        });

        const purchaseButton = document.createElement('button');
        purchaseButton.textContent = 'Comprar';
        purchaseButton.addEventListener('click', () => {
            purchaseItems();
        });
        cartItemsList.appendChild(purchaseButton);
    }

    cart.classList.toggle('hide');
}


function purchaseItems() {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/products/purchase';

    cartItems.forEach(item => {
        const productIdInput = document.createElement('input');
        productIdInput.type = 'hidden';
        productIdInput.name = 'productIds';
        productIdInput.value = item.productId;
        form.appendChild(productIdInput);

        const quantityInput = document.createElement('input');
        quantityInput.type = 'hidden';
        quantityInput.name = 'quantities';
        quantityInput.value = item.quantity;
        form.appendChild(quantityInput);
    });

    document.body.appendChild(form);
    form.submit();
}



