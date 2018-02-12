/**
 *** Restful API - Ebay Controller
 **********************************/

(function () {
    'use strict';

    angular.module('restful.controllers')
        .controller('ebayCtrl', ebayCtrl);

    ebayCtrl.$inject = ['$scope'];

    function ebayCtrl($scope) {
        $scope.actionURL = '/';
        $scope.uri = '';
        $scope.search = '';
        $scope.limit = '';

        $scope.processSearch = function () {
            $scope.actionURL = encodeURI($scope.uri + '.ebay.' + $scope.search + '.' + $scope.limit + '.json');
        };
    }
})();
