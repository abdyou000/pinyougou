//控制层
app.controller('brandController', function ($scope, $controller, brandService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                if (response.success) {
                    $scope.list = response.data;
                } else {
                    layer.msg(response.message)
                }
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                if (response.success) {
                    $scope.list = response.data.rows;
                    $scope.paginationConf.totalItems = response.data.total;//更新总记录数
                } else {
                    layer.msg(response.message)
                }
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                if (response.success) {
                    $scope.entity = response.data;
                } else {
                    layer.msg(response.message)
                }
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = brandService.update($scope.entity); //修改
        } else {
            serviceObject = brandService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    layer.msg(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        brandService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                } else {
                    layer.msg(response.message)
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        brandService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                if (response.success) {
                    $scope.list = response.data.rows;
                    $scope.paginationConf.totalItems = response.data.total;//更新总记录数
                } else {
                    layer.msg(response.message)
                }
            }
        );
    }

});	
