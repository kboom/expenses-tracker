angular
    .module('auth', [
        'ngRoute'
    ])
    .config(function ($routeProvider, $locationProvider, $httpProvider) {

        $locationProvider.html5Mode(false);

        $routeProvider
            .when('/', {
                templateUrl: 'pages/login.html'
            })
            .when('/login', {
                templateUrl: 'pages/login.html'
            })
            .when('/register', {
                templateUrl: 'pages/register.html'
            })
            .otherwise({
                redirectTo: '/'
            });

        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    })
    .run(function ($rootScope, $location) {

        // put here everything that you need to run on page load

    });