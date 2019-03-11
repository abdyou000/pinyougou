//控制层
app.controller('typeTemplateController', function ($scope, $controller,
                                                   typeTemplateService,
                                                   brandService,
                                                   specificationService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        typeTemplateService.findAll().success(
            function (response) {
                if (response.success) {
                    $scope.list = response.data;
                } else {
                    alert(response.message)
                }
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        typeTemplateService.findPage(page, rows).success(
            function (response) {
                if (response.success) {
                    $scope.list = response.data.rows;
                    $scope.paginationConf.totalItems = response.data.total;//更新总记录数
                } else {
                    alert(response.message)
                }
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {
                if (response.success) {
                    $scope.entity = string2Json(response.data);
                } else {
                    alert(response.message)
                }
            }
        );
    }

    string2Json = function (entity) {
        let temp = entity;
        temp.brandIds = JSON.parse(entity.brandIds);
        temp.specIds = JSON.parse(entity.specIds);
        temp.customAttributeItems = JSON.parse(entity.customAttributeItems);
        return temp;
    }
    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = typeTemplateService.update($scope.entity); //修改
        } else {
            serviceObject = typeTemplateService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        typeTemplateService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                } else {
                    alert(response.message)
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象
    //搜索
    $scope.search = function (page, rows) {
        typeTemplateService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                if (response.success) {
                    let temp = response.data.rows;
                    $scope.list = temp.map(x => {
                        return string2Json(x);
                    });
                    $scope.paginationConf.totalItems = response.data.total;//更新总记录数
                } else {
                    alert(response.message)
                }
            }
        );
    }
    $scope.jsonToString = function (jsonArr, attrName) {
        return jsonArr.map(x => {
            return x[attrName];
        }).reduce((result, attrVal) => {
            return result + "," + attrVal;
        });
    }
    $scope.brandList = {data: []};
    $scope.specList = {data: []};
    $scope.findBrandList = function () {
        brandService.findAll().success((response) => {
            if (response.success) {
                let temp = response.data.map(x => {
                    return {
                        id: x.id,
                        text: x.name,
                    };
                })
                $scope.brandList = {
                    data: temp
                }
            } else {
                alert(response.message);
            }
        })
    }
    $scope.findSpecList = function () {
        specificationService.findAll().success((response) => {
            if (response.success) {
                let temp = response.data.map(x => {
                    return {
                        id: x.id,
                        text: x.specName,
                    };
                })
                $scope.specList = {
                    data: temp
                }
            } else {
                alert(response.message);
            }
        })
    }

    $scope.entity = {customAttributeItems: []};
    $scope.addTableRow = function () {
        $scope.entity.customAttributeItems.push({});
    }

    $scope.deleteTableRow = function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    }
});	
