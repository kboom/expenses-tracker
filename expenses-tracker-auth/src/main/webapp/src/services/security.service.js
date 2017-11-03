angular.module('auth').service('authInterceptor',
    function ($q, $location) {

        return {

            request: function(config) {
                config.headers = config.headers || {};
                return config;
            },

            responseError: function(response) {
                if (response.status === 404) {
                    $location.path('/');
                    return $q.reject(response);
                } else {
                    return $q.reject(response);
                }
            }

        };

    });
