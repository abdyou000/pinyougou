app.controller('searchController', function ($scope, $location, searchService) {

    //定义搜索对象的结构  category:商品分类
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'spec': {},
        'price': '',
        'pageNum': 1,
        'pageSize': 40,
        'sort': '',
        'sortField': ''
    };

    //搜索
    $scope.search = function () {
        $scope.searchMap.pageNum = parseInt($scope.searchMap.pageNum);//转换为数字
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response.data;

                buildPageLabel();//构建分页栏
                //$scope.searchMap.pageNum=1;//查询后显示第一页
            }
        );
    }

    //构建分页栏
    buildPageLabel = function () {
        //构建分页栏
        $scope.pageLabel = [];
        var firstPage = 1;//开始页码
        var lastPage = $scope.resultMap.totalPages;//截止页码
        $scope.firstDot = true;//前面有点
        $scope.lastDot = true;//后边有点

        if ($scope.resultMap.totalPages > 7) {  //如果页码数量大于7

            if ($scope.searchMap.pageNum <= 4) {//如果当前页码小于等于4 ，显示前7页
                lastPage = 7;
                $scope.firstDot = false;//前面没点
            } else if ($scope.searchMap.pageNum >= $scope.resultMap.totalPages - 2) {//显示后7页
                firstPage = $scope.resultMap.totalPages - 6;
                $scope.lastDot = false;//后边没点
            } else {  //显示以当前页为中心的7页
                firstPage = $scope.searchMap.pageNum - 2;
                lastPage = $scope.searchMap.pageNum + 2;
            }
        } else {
            $scope.firstDot = false;//前面无点
            $scope.lastDot = false;//后边无点
        }

        //构建页码
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    }


    //添加搜索项  改变searchMap的值
    $scope.addSearchItem = function (key, value) {

        if (key == 'category' || key == 'brand' || key == 'price') {//如果用户点击的是分类或品牌
            $scope.searchMap[key] = value;

        } else {//用户点击的是规格
            $scope.searchMap.spec[key] = value;
        }
        $scope.search();//查询
    }

    //撤销搜索项
    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand' || key == 'price') {//如果用户点击的是分类或品牌
            $scope.searchMap[key] = "";
        } else {//用户点击的是规格
            delete $scope.searchMap.spec[key];
        }
        $scope.search();//查询
    }

    //分页查询
    $scope.queryByPage = function (pageNum) {
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageNum > $scope.resultMap.totalPages) {
            pageNum = $scope.resultMap.totalPages;
        }
        $scope.searchMap.pageNum = pageNum;
        $scope.search();//查询
    }

    //判断当前页是否为第一页
    $scope.isTopPage = function () {
        return $scope.searchMap.pageNum == 1;
    }

    //判断当前页是否为最后一页
    $scope.isEndPage = function () {
        return $scope.searchMap.pageNum == $scope.resultMap.totalPages;
    }

    //排序查询
    $scope.sortSearch = function (sortField, sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;

        $scope.search();//查询
    }

    //判断关键字是否是品牌
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {
                return true;
            }
        }
        return false;
    }

    //加载关键字
    $scope.loadkeywords = function () {
        $scope.searchMap.keywords = $location.search()['keywords'];
        $scope.search();//查询
    }

});