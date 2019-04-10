app.controller("indexController", function ($scope, loginService) {

    $scope.showName = function () {
        loginService.showName().success(function (response) {
            if (response.success) {
                $scope.username = response.data.username;
            } else {
                layer.msg(response.message)
            }
        });
    }

});