//购物车控制层
app.controller('cartController', function ($scope, cartService) {
    //查询购物车列表
    $scope.findCart = function () {
        cartService.findCart().success(
            function (response) {
                $scope.cart = response.data;
                $scope.totalValue = cartService.sum($scope.cart);
            }
        );
    }


    //查询提交订单的商品列表
    $scope.findProductList = function () {
        cartService.findSelectedCart($scope.cartSettleInfo).success(
            function (response) {
                $scope.confirmCart = response.data;
                $scope.totalValue = cartService.sum($scope.cart);
            }
        );
    }

    //{cartItemList:[{sellerId:'',productList:[itemId:'']}]}
    $scope.criteiaCart = {cartItemList: [{productList: []}]};
    //查询提交订单的商品列表
    $scope.findProductList2 = function () {
        cartService.findSelectedCart2($scope.cartSettleInfo).success(
            function (response) {
                $scope.confirmCart = response.data;
                $scope.totalValue = cartService.sum($scope.cart);
            }
        );
    }

    $scope.selectCart = function ($event) {
        if ($event.target.checked) {
            $scope.criteiaCart = $scope.cart;
        } else {
            $scope.criteiaCart = {cartItemList: [{productList: []}]};
        }
    }
    $scope.selectCartItem = function ($event, cartItem) {
        let cartItemList = $scope.criteiaCart.cartItemList;
        if ($event.target.checked) {
            cartItemList.push(JSON.parse(JSON.stringify(cartItem)));
        } else {
            let temp = cartItemList.find(x => x.sellerId == cartItem.sellerId);
            let idx = cartItemList.indexOf(temp);
            cartItemList.splice(idx, 1);
        }
    }
    $scope.selectCartProduct = function ($event, cartItem, cartProduct) {
        let cartItemList = $scope.criteiaCart.cartItemList;
        if ($event.target.checked) {
            let temp = JSON.parse(JSON.stringify(cartItem));
            temp.productList.push(JSON.parse(JSON.stringify(cartProduct)));
            cartItemList.push(temp);
        } else {
            let tempCartItem = cartItemList.find(x => x.sellerId == cartItem.sellerId);
            let tempCartProduct = tempCartItem.find(x => x.itemId == cartProduct.itemId);
            let idx = tempCartItem.indexOf(tempCartProduct);
            tempCartItem.splice(idx, 1);
        }
    }
    //数量加减
    $scope.addGoodsToCart = function (itemId, num) {
        cartService.addGoodsToCart(itemId, num).success(
            function (response) {
                if (response.success) {//如果成功
                    $scope.findCart();//刷新列表
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //获取当前用户的地址列表
    $scope.findAddressList = function () {
        cartService.findAddressList().success(
            function (response) {
                $scope.addressList = response;
                for (let i = 0; i < $scope.addressList.length; i++) {
                    if ($scope.addressList[i].isDefault == '1') {
                        $scope.address = $scope.addressList[i];
                        break;
                    }
                }

            }
        );
    }

    //选择地址
    $scope.selectAddress = function (address) {
        $scope.address = address;
    }

    //判断某地址对象是不是当前选择的地址
    $scope.isSeletedAddress = function (address) {
        return address == $scope.address;
    }

    $scope.order = {paymentType: '1'};//订单对象

    //选择支付类型
    $scope.selectPayType = function (type) {
        $scope.order.paymentType = type;
    }

    //保存订单
    $scope.submitOrder = function () {
        $scope.order.receiverAreaName = $scope.address.address;//地址
        $scope.order.receiverMobile = $scope.address.mobile;//手机
        $scope.order.receiver = $scope.address.contact;//联系人

        cartService.submitOrder({order:$scope.order,criteriaCart:$scope.criteiaCart}).success(
            function (response) {
                //alert(response.message);
                if (response.success) {
                    //页面跳转
                    if ($scope.order.paymentType == '1') {//如果是微信支付，跳转到支付页面
                        location.href = "pay.html";
                    } else {//如果货到付款，跳转到提示页面
                        location.href = "paysuccess.html";
                    }

                } else {
                    alert(response.message);	//也可以跳转到提示页面
                }

            }
        );
    }
});