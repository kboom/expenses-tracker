import angular from 'angular';

require('angular-route');

angular
    .module('auth', [
        'ngRoute'
    ]).config(['$routeProvider', '$locationProvider', '$httpProvider',
    function ($routeProvider, $locationProvider, $httpProvider) {

        $locationProvider.html5Mode(true);

        $routeProvider
            .when('/login', {
                templateUrl: 'pages/login/login.html',
            })
            // .otherwise({
            //     redirectTo: '/login'
            // });

        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    }])
    .run(function ($rootScope, $location) {

        // put here everything that you need to run on page load

    });

require('./services/security.service');
require('./pages/login/login.js');
require('./pages/register/register.js');