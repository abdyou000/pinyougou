//购物车服务层
app.service('cartService', function ($http) {
    //购物车列表
    this.findCart = function () {
        return $http.get('cart/findCart.do');
    }

    //选中的购物车列表
    this.findSelectedCart = function () {
        return $http.get('cart/findSelectedCart.do');
    }

    //添加商品到购物车
    this.addGoodsToCart = function (itemId, num) {
        return $http.get('cart/addGoodsToCart.do?itemId=' + itemId + '&num=' + num);
    }

    //求合计数
    this.sum = function (cart) {
        var totalValue = {totalNum: 0, totalMoney: 0};
        var cartItemList = cart.cartItemList;
        for (var i = 0; i < cartItemList.length; i++) {
            var orderItemList = cartItemList[i].orderItemList;
            for (var j = 0; j < orderItemList.length; j++) {
                var orderItem = orderItemList[j];//购物车明细
                totalValue.totalNum += orderItem.num;//累加数量
                totalValue.totalMoney += orderItem.totalFee;//累加金额
            }
        }
        return totalValue;

    }

    //获取当前登录账号的收货地址
    this.findAddressList = function () {
        return $http.get('address/findListByLoginUser.do');
    }

    //提交订单
    this.submitOrder = function (obj) {
        return $http.post('order/add.do', obj);
    }


});