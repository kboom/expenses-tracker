import angular from 'angular';

angular
    .module("auth")
    .controller("LoginController", ["$http", "$location", "$scope",
        function ($http, $location, $scope) {

    $scope.credentials = {};

    const self = this;

    $http.get("/user").success(function (data) {
        if (data.name) {
            self.user = data.name;
            self.authenticated = true;
        } else {
            self.user = "N/A";
            self.authenticated = false;
        }
    }).error(function () {
        self.user = "N/A";
        self.authenticated = false;
    });

    self.logout = function () {
        $http.post('logout', {}).success(function () {
            self.authenticated = false;
            $location.path("/");
        }).error(function (data) {
            console.log("Logout failed");
            self.authenticated = false;
        });
    };

}]);