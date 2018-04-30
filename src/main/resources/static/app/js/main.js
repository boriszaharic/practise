var test = angular.module("testApp", ['ngRoute']);

test.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl : 'login'
		})
		.when('/error', {
			templateUrl : '/'
		})
		.when('/home', {
			templateUrl : '/app/partial/home.html'
		})
		.when('/companies', {
			templateUrl : '/app/partial/companies.html'
		})
		.when('/companies/edit/:id', {
			templateUrl : '/app/partial/update_company.html'
		})
		.when('/companies/new', {
			templateUrl : '/app/partial/new_company.html'
		})
		.when('/items', {
			templateUrl : '/app/partial/items.html'
		})
		.when('/items/edit/:id', {
			templateUrl : '/app/partial/update_item.html'
		})
		.when('/items/new', {
			templateUrl : '/app/partial/new_item.html'
		})
		.when('/users', {
			templateUrl : '/app/partial/users.html'
		})
		.when('/users/edit/:id', {
			templateUrl : '/app/partial/update_user.html'
		})
		.when('/users/new', {
			templateUrl : '/app/partial/new_user.html'
		})
		.otherwise({
			redirectTo: '/app/partial/home.html'
		});
}]);

test.controller("itemsCtrl", function($scope, $http, $location){
	
	var baseUrl = "/items";
	
	$scope.item = [];
	
	$scope.pageNum = 0;
	$scope.totalPages= 1;
	
var getItems = function(){
		
		var conf = {params: {}};
		
		conf.params.pageNum = $scope.pageNum;
		
		var promise = $http.get(baseUrl, conf);
		promise.then(
			function success(answ){
				$scope.item = answ.data;
				$scope.totalPages=answ.headers("totalPages");
			},
			function error(answ){
				alert("Something went wrong getting all items data!");
			}
		);
	}


getItems();

$scope.changePage = function(direction){
	$scope.pageNum = $scope.pageNum + direction;
	getItems();
}

$scope.deleteItem = function (id){
	
	$http.delete(baseUrl + "/" + id).then(
		function success(answ){
			getItems();
		},
		function error(answ){
			alert("Couldn't delete item!");
		}	
	);
}

// za edit na posebnoj stranici
$scope.goToEdit = function(id){
	$location.path('/items/edit/' + id);
}

// za dodavanje na posebnoj stranici
$scope.goToAdd = function(){
	$location.path('/items/new');
}

});

// kontroler za edit u posebnoj stranici
test.controller("editItemCtrl", function($scope, $routeParams, $http, $location){
	
	var baseUrl = "/items";
	var id = $routeParams.id;
	
	$scope.updatedItem = {};
	
	$scope.getItembyId = function(){
		
		$http.get(baseUrl + "/" + id).then(
			function success(answ){
				$scope.updatedItem = answ.data;
			},
			function error(answ){
				alert('Something went wrong getting item!');
			}
		);
	}

	$scope.getItembyId();

	$scope.edit = function(){
	
		$http.put(baseUrl + "/" + id, $scope.updatedItem).then(
			function success(answ){
				$location.path("/items");
				},
			function error(answ){
				alert('Something went wrong editing!');
				}
		);
	}

});

test.controller("newItemCtrl", function($scope, $http, $location){
	
	var baseUrl = "/items";
	
	$scope.item = [];
	
	$scope.newItem = {};
	$scope.newItem.name = "";
	$scope.newItem.orderNumber = "";
	$scope.newItem.category = "";
	$scope.newItem.value = "";
	$scope.newItem.description = "";
	$scope.newItem.barcode = "";
	
		
	$scope.addItem = function(){

		var promise = $http.post(baseUrl, $scope.newItem);
			promise.then(
				function success(answ){
					$location.path("/items");
				},
				function error(answ){
					alert("Something went wrong adding company!");
				}
			);
		}
});

test.controller("companiesCtrl", function($scope, $http, $location){
	
	var baseUrl = "/companies";
	
	$scope.company = [];
	
	$scope.pageNum = 0;
	$scope.totalPages= 1;
	
var getCompanies = function(){
		
		var conf = {params: {}};
		
		conf.params.pageNum = $scope.pageNum;
		
		var promise = $http.get(baseUrl, conf);
		promise.then(
			function success(answ){
				$scope.company = answ.data;
				$scope.totalPages=answ.headers("totalPages");
			},
			function error(answ){
				alert("Something went wrong getting all companies data!");
			}
		);
	}


getCompanies();

$scope.changePage = function(direction){
	$scope.pageNum = $scope.pageNum + direction;
	getCompanies();
}

$scope.deleteCompany = function (id){
	
	$http.delete(baseUrl + "/" + id).then(
		function success(answ){
			getCompanies();
		},
		function error(answ){
			alert("Couldn't delete company!");
		}	
	);
}

// za edit na posebnoj stranici
$scope.goToEdit = function(id){
	$location.path('/companies/edit/' + id);
}

// za dodavanje na posebnoj stranici
$scope.goToAdd = function(){
	$location.path('/companies/new');
}

});

// kontroler za edit u posebnoj stranici
test.controller("editCompanyCtrl", function($scope, $routeParams, $http, $location){
	
	var baseUrl = "/companies";
	var id = $routeParams.id;
	
	$scope.updatedCompany = {};
	
	$scope.getCompanybyId = function(){
		
		$http.get(baseUrl + "/" + id).then(
			function success(answ){
				$scope.updatedCompany = answ.data;
			},
			function error(answ){
				alert('Something went wrong getting company!');
			}
		);
	}

	$scope.getCompanybyId();

	$scope.edit = function(){
	
		$http.put(baseUrl + "/" + id, $scope.updatedCompany).then(
			function success(answ){
				$location.path("/companies");
				},
			function error(answ){
				alert('Something went wrong editing!');
				}
		);
	}

});

test.controller("newCompanyCtrl", function($scope, $http, $location){
	
	var baseUrl = "/companies";
	
	$scope.company = [];
	
	$scope.newCompany = {};
	$scope.newCompany.name = "";
	$scope.newCompany.address = "";
	$scope.newCompany.validLicenceTill = "";
	$scope.newCompany.contactPerson = "";
	
		
	$scope.addCompany = function(){

		var promise = $http.post(baseUrl, $scope.newCompany);
			promise.then(
				function success(answ){
					$location.path("/companies");
				},
				function error(answ){
					alert("Something went wrong adding company!");
				}
			);
		}
});

test.controller("usersCtrl", function($scope, $http, $location){
	
	var baseUrl = "/users";
	
	$scope.user = [];
	
	$scope.pageNum = 0;
	$scope.totalPages= 1;
	
var getUsers = function(){
		
		var conf = {params: {}};
		
		conf.params.pageNum = $scope.pageNum;
		
		var promise = $http.get(baseUrl, conf);
		promise.then(
			function success(answ){
				$scope.user = answ.data;
				$scope.totalPages=answ.headers("totalPages");
			},
			function error(answ){
				alert("Something went wrong getting all users data!");
			}
		);
	}


getUsers();

$scope.changePage = function(direction){
	$scope.pageNum = $scope.pageNum + direction;
	getUsers();
}

$scope.deleteUser = function (id){
	
	$http.delete(baseUrl + "/" + id).then(
		function success(answ){
			getUsers();
		},
		function error(answ){
			alert("Couldn't delete user!");
		}	
	);
}

// za edit na posebnoj stranici
$scope.goToEdit = function(id){
	$location.path('/users/edit/' + id);
}

// za dodavanje na posebnoj stranici
$scope.goToAdd = function(){
	$location.path('/users/new');
}

});

// kontroler za edit u posebnoj stranici
test.controller("editUserCtrl", function($scope, $routeParams, $http, $location){
	
	var baseUrl = "/users";
	var id = $routeParams.id;
	
	$scope.updatedUser = {};
	
	$scope.getUserbyId = function(){
		
		$http.get(baseUrl + "/" + id).then(
			function success(answ){
				$scope.updatedUser = answ.data;
			},
			function error(answ){
				alert('Something went wrong getting user!');
			}
		);
	}

	$scope.getUserbyId();

	$scope.edit = function(){
	
		$http.put(baseUrl + "/" + id, $scope.updatedUser).then(
			function success(answ){
				$location.path("/users");
				},
			function error(answ){
				alert('Something went wrong editing!');
				}
		);
	}

});

test.controller("newUserCtrl", function($scope, $http, $location){
	
	var baseUrl = "/users";
	
	$scope.user = [];
	
	$scope.newCompany = {};
	$scope.newCompany.name = "";
	$scope.newCompany.lastName = "";
	$scope.newCompany.email = "";
	$scope.newCompany.password = "";
	$scope.newCompany.company = "";
	$scope.newCompany.roles = "";
	
		
	$scope.addUser = function(){

		var promise = $http.post(baseUrl, $scope.newUser);
			promise.then(
				function success(answ){
					$location.path("/users");
				},
				function error(answ){
					alert("Something went wrong adding user!");
				}
			);
		}
});


