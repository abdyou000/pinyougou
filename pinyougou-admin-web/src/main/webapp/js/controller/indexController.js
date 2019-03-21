app.controller("indexController",function($scope,loginService){
	
	$scope.showName = function(){
		loginService.showName().success(function(response){
			if (response.success) {
                $scope.username = response.username;
			} else {
				layer.msg(response.message)
			}
		});
	}
	
});