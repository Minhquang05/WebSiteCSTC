// cart.js

// Thêm sản phẩm vào giỏ hàng
$("#addToCartForm").submit(function(e) {
    e.preventDefault();  // Ngừng gửi form để xử lý bằng AJAX

    var form = $(this);
    var quantity = form.find('input[name="quantity"]').val();
    var productId = form.find('input[name="productId"]').val();

    $.ajax({
        url: '/cart/add/' + productId,
        type: 'POST',
        data: { quantity: quantity },
        success: function(response) {
            var result = JSON.parse(response);  // Chuyển đổi dữ liệu JSON từ server

            // Cập nhật lại số lượng sản phẩm và tổng giá trị trong giỏ hàng
            $("#cartCount").text(result.totalQuantity);  // Số lượng sản phẩm trong giỏ
            $("#cartTotalPrice").text(result.totalPrice);  // Tổng giá trị giỏ hàng
        },
        error: function() {
            alert("Có lỗi khi thêm sản phẩm vào giỏ hàng.");
        }
    });
});

// Xóa sản phẩm khỏi giỏ hàng
$(".remove-from-cart").click(function() {
    var productId = $(this).data('product-id');

    $.ajax({
        url: '/cart/remove/' + productId,
        type: 'GET',
        success: function(response) {
            var result = JSON.parse(response);  // Chuyển đổi dữ liệu JSON từ server

            // Cập nhật lại số lượng sản phẩm và tổng giá trị trong giỏ hàng
            $("#cartCount").text(result.totalQuantity);
            $("#cartTotalPrice").text(result.totalPrice);

            // Cập nhật lại giỏ hàng trên trang
            $(this).closest('.cart-item').remove();  // Xóa sản phẩm khỏi danh sách giỏ hàng trên trang
        },
        error: function() {
            alert("Có lỗi khi xóa sản phẩm khỏi giỏ hàng.");
        }
    });
});

// Dọn giỏ hàng
$("#clearCartButton").click(function() {
    $.ajax({
        url: '/cart/clear',
        type: 'GET',
        success: function(response) {
            var result = JSON.parse(response);  // Chuyển đổi dữ liệu JSON từ server

            // Cập nhật lại giỏ hàng trên trang (số lượng và giá trị là 0)
            $("#cartCount").text(result.totalQuantity);
            $("#cartTotalPrice").text(result.totalPrice);

            // Xóa tất cả sản phẩm trong giỏ hàng trên trang
            $(".cart-item").remove();
        },
        error: function() {
            alert("Có lỗi khi dọn giỏ hàng.");
        }
    });
});
