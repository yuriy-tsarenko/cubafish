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

new Vue({
    el: '#appSelector',
    data: {
        paymentType: '',
        deliveryType: ''

    }
});

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
        '<table id="basketTable" style="width:600px; height: 100px">' +
        '<tr>' +
        '<td hidden><h3>{{this.totalAmount=productItem.totalAmount}}</h3></td>' +
        '<td hidden><h3>{{this.productPrice=productItem.productPrice}}</h3></td>' +
        '</tr>' +
        '<tr>' +
        '<td  style="alignment:center;vertical-align:center;width:90px">' +
        '<img style="width: 80px; height: 80px; border-radius:20%" :src="productItem.productImageName" alt="photo"/>' +
        '</td>' +
        '<td  id="cellStyleDescription" style="width:250px; height:auto">' +
        '<div class="productValue" id="productBasketValue1"><p>{{productItem.description}}</p></div>' +
        '</td>' +
        '<td id="cellStyle" style="width:100px; height:auto">' +
        '<div v-if="this.totalAmount>0" id="availableInBasket"><p>{{counter}}</p></div>' +
        '<div v-if="this.totalAmount==0" id="notAvailableInBasket"><p>Нет в наличии</p></div>' +
        '<td id="cellStyle" style="width:60px; height: auto">' +
        '<input class="counterButton" type="button" value="+" v-on:click="counter+=1" >' +
        '</td>' +
        '<td id="cellStyle" style="width:100px; height: auto">' +
        '<div class="productValue" id="productBasketValue3"><p>{{productItem.productPrice}} грн</p>' +
        '</div>' +
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

        this.maxId = localStorage.getItem('ID');
        if (this.maxId !== null) {
            let maxValue = Number(this.maxId)

            for (let i = 0; i <= maxValue; i++) {
                let item = localStorage.getItem(String(i));
                if (item !== null) {
                    let items = new Set(JSON.parse(localStorage.getItem(String(i))));
                    for (let productItem of items) {
                        this.productItems.push(productItem);
                    }
                }
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






