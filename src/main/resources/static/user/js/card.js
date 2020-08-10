const productAPI = Vue.resource('/guest/products/all');
const productAPIId = Vue.resource('/guest/products/get_id');
const productCategoryApi = Vue.resource('/guest/products/get_menu_categories');
const productCategoryApiGetSubCategories = Vue.resource('/guest/products/get_sub_categories');
const productCategoryApiGetBrands = Vue.resource('/guest/products/get_product_brands');
const productAPISorted = Vue.resource('/guest/products/sorted_by_category');
const productAPISortedBySubCategory = Vue.resource('/guest/products/sorted_by_sub_category');
const productAPISortedByBrand = Vue.resource('/guest/products/sorted_by_brand');
const productAPISortedByTypeOfPurpose = Vue.resource('/guest/products/sorted_by_type_of_purpose');
const productAPISortedByDescription = Vue.resource('/guest/products/sorted_by_description');
Vue.http.headers.common['Authorization'] = localStorage.getItem('CustomHeader');
axios.defaults.headers.common['Authorization'] = localStorage.getItem('CustomHeader');

Vue.component('productItem-row', {
    props: ['productItem'],
    data: function () {
        return {
            id: '',
            productCategory: '',
            productSubCategory: '',
            productBrand: '',
            productSubCategoryForRequest: '',
            productBrandForRequest: '',
            totalAmount: '',
            description: '',
            specification: '',
            typeOfPurpose: '',
            productPrice: '',
            productImageName: '',
            counter: 1
        }
    },
    template:
        '<div>' +
        '<table id="basketTable" style="width:800px; height: 100px">' +
        '<tr>' +
        '<td hidden><h3>{{this.id=productItem.id}}</h3></td>' +
        '<td hidden><h3>{{this.totalAmount=productItem.totalAmount}}</h3></td>' +
        '<td hidden><h3>{{this.productPrice=productItem.productPrice}}</h3></td>' +
        '<td hidden><h3>{{this.productBrandForRequest=productItem.productBrand}}</h3></td>' +
        '</tr>' +
        '<tr>' +
        '<td  style="alignment:center;vertical-align:center;width:90px">' +
        '<img style="width: 100px; height: 100px; border-radius:20%" :src="productItem.productImageName" alt="photo"/>' +
        '</td>' +
        '<td  id="cellStyleDescription" style="width:170px; height:auto">' +
        '<div class="productValue" id="productBasketValue1"><p><br/>{{productItem.description}}</p></div>' +
        '</td>' +
        '<td  id="cellStyleSpecification" style="width:230px; height:auto">' +
        '<div class="productValue" id="productBasketValue2"><p><br/>{{productItem.specification}}</p></div>' +
        '</td>' +
        '<td id="cellStyle" style="width:70px; height:auto">' +
        '<div v-if="this.totalAmount>0" id="availableInBasket"><p>{{counter}}</p></div>' +
        '<div v-if="this.totalAmount==0" id="notAvailableInBasket"><p>Нет в наличии</p></div>' +
        '<td id="cellStyle" style="width:60px; height: auto">' +
        '<input class="counterButton" type="button" value="+" v-on:click="counter+=1" >' +
        '</td>' +
        '<td id="cellStyle" style="width:120px; height: auto">' +
        '<div class="productValue" id="productBasketValue3"><p>{{productPrice}} грн</p>' +
        '</div>' +
        '</td>' +
        '<td id="cellStyle" style="width:50px; height: auto">' +
        '<input class="deleteFromBasketButton" type="button" value="X" v-on:click="counter+=1" >' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '</div>'
});

Vue.component('productItems-list', {
    props: ['productItems'],
    data: function () {
        return {
            maxId: ''
        }
    },
    template: '<div>' +
        '<productItem-row v-for="productItem in productItems" :key="productItem.id" :productItem="productItem"/>' +
        '</div>',
    created: function () {

        let basketSet = new Set();
        appBasket.productItems = appBasket.productItems.splice(0, 0);
        if (this.maxId !== null) {
            let maxValue = Number(this.maxId)

            for (let i = 0; i <= maxValue; i++) {
                let key = localStorage.getItem(String(i));
                if (key !== null) {
                    basketSet.add(key);
                }
            }
            for (let item of basketSet) {
                let sortingTag = {
                    key: 'sorting tag',
                    communicationKey: item,
                }
                setTimeout(function () {
                productAPISortedByTypeOfPurpose.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(productItem => appBasket.productItems.push(productItem))
                    )
                );
            }, 100);
            }
        }



    }
});

let appBasket = new Vue({
    el: '#appBasket',
    template: '<productItems-list :productItems="productItems"/>',
    data: {
        productItems: []
    }
});

let appHeaderButtons = new Vue({
    el: '#appHeaderButtons',
    props: ['newProducts'],
    methods: {
        getSortedProductsByTypeOfPurpose: function (message) {
            app.newProducts = app.newProducts.splice(0, 0);
            let sortingTag = {
                key: 'sorting tag',
                communicationKey: message,
            }
            productAPISortedByDescription.save({}, sortingTag).then(result =>
                result.json().then(data =>
                    data.forEach(productBrand => app.newProducts.push(productBrand))
                )
            );
        }
    }
});

let appLogoutButtons = new Vue({
    el: '#appLogoutButtons',
    methods: {
        loginUserAction: function () {
            window.location = 'http://91.235.128.12:8081/guest/authorize.html';
        },
        registrationUserAction: function () {
            window.location = 'http://91.235.128.12:8081/guest/registration.html';
        }
    }
});





