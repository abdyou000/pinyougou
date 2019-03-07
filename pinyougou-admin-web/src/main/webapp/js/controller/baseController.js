//品牌控制层
app.controller('baseController', function ($scope) {

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.selectAll = false;
        $scope.selectIds = [];
    }

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    $scope.selectIds = [];//选中的ID集合
    $scope.list = [];
    $scope.selectAll = false;//全选按钮

    //更新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
            if ($scope.selectIds.length == $scope.list.length) {
                $scope.selectAll = true;
            }
        } else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除
            if ($scope.selectIds.length != $scope.list.length) {
                $scope.selectAll = false;
            }
        }
    }
    //更新全选
    $scope.updateSelectAll = function ($event) {
        if ($event.target.checked) {//如果是被选中,则当前页面全部选中
            $scope.selectIds = [];
            $scope.selectIds = $scope.list.map(function (x) {
                return x.id;
            });
            $scope.selectAll = true;
            // console.log($scope.selectIds, $scope.selectAll)
        } else {
            $scope.selectIds = [];
            $scope.selectAll = false;
        }
    }

    $scope.isSelected = function (id) {
        return $scope.selectIds.includes(id);
    }
});	