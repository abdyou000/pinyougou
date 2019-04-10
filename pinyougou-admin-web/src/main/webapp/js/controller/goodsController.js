//控制层
app.controller('goodsController', function ($scope,$location, $controller, itemCatService, goodsService,brandService) {

    $controller('baseController', {$scope: $scope});//继承

    //查询实体
    $scope.findOne = function () {
        var id = $location.search()['id'];
        goodsService.findOne(id).success(
            function (response) {
                if (response.success) {
                    $scope.entity = response.data;

                    // 调用处理富文本编辑器：
                    editor.html($scope.entity.goodsDesc.introduction);

                    // 处理图片列表，因为图片信息保存的是JSON的字符串，让前台识别为JSON格式对象
                    $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);

                    // 处理扩展属性:
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);

                    // 处理规格
                    $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

                    // 遍历SKU的集合:
                    for (var i = 0; i < $scope.entity.itemList.length; i++) {
                        $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                    }
                } else {
                    layer.msg(response.message);
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                if (response.success) {
                    $scope.list = response.data.rows;
                    $scope.paginationConf.totalItems = response.data.total;//更新总记录数
                } else {
                    layer.msg(response.message);
                }
            }
        );
    }

    // 显示状态
    $scope.status = ["未审核", "审核通过", "审核未通过", "关闭"];

    $scope.itemCatList = [];
    $scope.brandList = [];
    // 显示分类:
    $scope.findItemCatListAndBrandList = function () {

        itemCatService.findAll().success(function (response) {
            let data = response.data;
            if (response.success) {
                for (var i = 0; i < data.length; i++) {
                    $scope.itemCatList[data[i].id] = data[i].name;
                }
            } else {
                layer.msg(response.message);
            }
        });
        brandService.findAll().success(function (response) {
            let data = response.data;
            if (response.success) {
                for (var i = 0; i < data.length; i++) {
                    $scope.brandList[data[i].id] = data[i].name;
                }
            } else {
                layer.msg(response.message);
            }
        });
    }

    // 审核的方法:
    $scope.updateStatus = function (status) {
        goodsService.updateStatus($scope.selectIds, status).success(function (response) {
            if (response.success) {
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            } else {
                layer(response.message);
            }
        });
    }
});	
