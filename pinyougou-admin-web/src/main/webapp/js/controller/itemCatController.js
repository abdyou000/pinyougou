//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.findAll = function () {
        itemCatService.findAll().success(
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
        itemCatService.findPage(page, rows).success(
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
        itemCatService.findOne(id).success(
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
        if ($scope.grade == 1) {
            $scope.entity.parentId = 0;
        } else if ($scope.grade == 2) {
            $scope.entity.parentId = $scope.entity_1.id;
        } else {
            $scope.entity.parentId = $scope.entity_2.id;
        }

        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    // $scope.reloadList();//重新加载
                    $scope.findByParentId($scope.entity.parentId);
                } else {
                    layer.msg(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        itemCatService.dele($scope.selectIds).success(
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
        itemCatService.search(page, rows, $scope.searchEntity).success(
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

    $scope.findByParentId = function (parentId) {
        itemCatService.findByParentId(parentId).success(
            function (response) {
                if (response.success) {
                    $scope.list = response.data;
                } else {
                    layer.msg(response.message)
                }
            }
        );
    }
    $scope.grade = 1;
    $scope.entity_1 = null;
    $scope.entity_2 = null;

    $scope.setGrade = function (grade) {
        $scope.grade = grade;
    }

    $scope.selectList = function (entity) {
        /*if (entity.id == 0) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        } else if (entity == $scope.entity_1) {
            $scope.entity_2 = null;
        } else if (entity == $scope.entity_2) {

        } else {
            if ($scope.entity_1 == null && $scope.entity_2 == null) {
                $scope.entity_1 = entity;
            } else {
                $scope.entity_2 = entity;
            }
        }*/
        //代码优化
        if ($scope.grade == 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        } else if ($scope.grade == 2) {
            $scope.entity_1 = entity;
            $scope.entity_2 = null;
        } else {
            $scope.entity_2 = entity;
        }
        $scope.findByParentId(entity.id);
    }
    $scope.typeTemplateList = {data: []};
    $scope.findTypeTemplateList = function () {
        typeTemplateService.findAll().success((response) => {
            if (response.success) {
                let temp = response.data.map(x => {
                    return {
                        id: x.id,
                        text: x.name,
                    };
                });
                $scope.typeTemplateList = {
                    data: temp
                }
            } else {
                layer.msg(response.message);
            }
        })
    }
})
;
