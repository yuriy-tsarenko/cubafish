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

let appShowBasket = new Vue({
        el: '#appShowBasket',
        data: {
            show: false,
            maxId: ''
        },
        methods: {
            hiddenFlagForBasket: function () {
                if (this.show === false) {
                    this.show = true
                    if (this.show === true) {
                        let amount;
                        if (localStorage.getItem('TotalAmount') == null) {
                            amount = 0;
                        } else {
                            amount = Number(localStorage.getItem('TotalAmount'));
                        }
                        amount++;
                        localStorage.setItem('TotalAmount', String(amount));
                        basketImage.basketItemsAmount = localStorage.getItem('TotalAmount');
                        basketImage.basketItemsCount = true;
                    }
                } else {
                    this.show = false;
                }
                setTimeout(function () {

                    let basketItem = {
                        id: '',
                        key: '',
                        status: '',
                        communicationData: ''
                    };
                    let sortingTag = {
                        key: 'required argument',
                        communicationKey: 'give me the argument for basket engine',
                    }
                    productAPIId.save({}, sortingTag)
                        .then(response => this.maxId = response.data.communicationData);

                    localStorage.setItem('ID', String(this.maxId));
                }, 700);
            }
        }
    })
;

let basketImage = new Vue({
    el: '#basketImage',
    data: {
        basketItemsAmount: '',
        basketItemsCount: false
    },
    methods: {
        activeImageAction: function () {
            window.location = 'card.html';
        }
    }
});

Vue.component('newProduct-row', {
    props: ['newProduct'],
    data: function () {
        return {
            show: false,
            showDetails: false,
            showDetailsButton: true,
            showImage: false,
            showRightSide: false,
            showLeftSide: false,
            showBackSide: false,
            id: '',
            productCategory: '',
            productSubCategory: '',
            productBrand: '',
            totalAmount: '',
            descriptionForRequest: '',
            specification: '',
            typeOfPurpose: '',
            productPrice: '',
            productImageRightName: '',
            productImageLeftName: '',
            productImageBackName: ''
        }
    },
    template:
        '<div>' +
        '<table id="fromDb" style="width:1000px; height: 250px" >' +
        '<tr>' +
        '<td hidden><p>{{this.id=newProduct.id}}</p></td>' +
        '<td hidden><p>{{this.totalAmount=newProduct.totalAmount}}</p></td>' +
        '<td hidden><p>{{this.descriptionForRequest=newProduct.description}}</p></td>' +
        '<td hidden><p>{{this.productImageRightName=newProduct.productImageRightName}}</p></td>' +
        '<td hidden><p>{{this.productImageLeftName=newProduct.productImageLeftName}}</p></td>' +
        '<td hidden><p>{{this.productImageBackName=newProduct.productImageBackName}}</p></td>' +
        '</tr>' +
        '<tr>' +
        '<td rowspan="3" style="alignment:center;vertical-align:center;width:250px">' +
        '<img v-on:click="hiddenFlag" style="width: 250px; height: 250px; border-radius:15%" :src="newProduct.productImageName" alt="photo"/>' +
        '</td>' +
        '<td rowspan="3" id="cellStyleDescription" style="width:450px; height:auto">' +
        '<div class="productValue" id="productValue1"><br/><p>{{newProduct.description}}</p></div>' +
        '</td>' +
        '<td id="cellStyle" style="width:300px; height:auto">' +
        '<div v-if="this.totalAmount>0" id="available"><p>Есть в наличии</p></div>' +
        '<div v-if="this.totalAmount==0" id="notAvailable"><p>Нет в наличии</p></div>' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td id="cellStyle" style="width:300px; height: auto">' +
        '<div class="productValue" id="productValue3"><p>{{newProduct.productPrice}} грн</p></br></div></td>' +
        '</tr>' +
        '<tr>' +
        '<td id="cellStyle" style="width:300px; height: auto" >' +
        '<input class="bueButton" type="button" value="Купить" v-on:click="hiddenFlagForBasket">' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td colspan="4" id="cellStyle" style="width:1000px; height: 25px" >' +
        '<div class="detailsBtnColor" v-if="showDetailsButton" v-on:click="hiddenFlagDetails"><input class="btn" type="button"></div>' +
        '</td>' +
        '</tr>' +
        '</table>' +

        '<transition name="fade">' +
        '<div v-if="show" id="window" v-on:click="hiddenFlag">' +
        '<div id="insideWindow">' +
        '<table id="bigPhoto">' +
        '<tr>' +
        '<td style="width: 900px; height: 30px">' +
        '<div id="bigPhotoButtonMove"><input class="bigPhotoButton" type="button" v-on:click="this.show=!this.show"></div>' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>' +
        '<img v-if="showImage" style="width: 900px;height: 900px; border-radius:0" :src="newProduct.productImageName" alt="photo"/>' +
        '<img v-if="showRightSide" style="width: 900px;height: 900px; border-radius:0" :src="newProduct.productImageRightName" alt="photo"/>' +
        '<img v-if="showLeftSide" style="width: 900px;height: 900px; border-radius:0" :src="newProduct.productImageLeftName" alt="photo"/>' +
        '<img v-if="showBackSide" style="width: 900px;height: 900px; border-radius:0" :src="newProduct.productImageBackName" alt="photo"/>' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '</div>' +
        '</div>' +
        '</transition>' +

        '<transition name="fade">' +
        '<table v-if="showDetails" id="detailsTable" style="width:1000px; height: 270px">' +
        '<tr>' +
        '<td style="width:auto; height: auto">' +
        '<img v-if="productImageRightName!=null" v-on:click="hiddenFlagRightSide" style="width: 180px;height: 180px; border-radius:5%" :src="newProduct.productImageRightName" alt="photo"/>' +
        '<img v-if="productImageLeftName!=null" v-on:click="hiddenFlagLeftSide" style="width: 180px;height: 180px; border-radius:5%" :src="newProduct.productImageLeftName" alt="photo"/>' +
        '<img v-if="productImageBackName!=null" v-on:click="hiddenFlagBackSide" style="width: 180px;height: 180px; border-radius:5%" :src="newProduct.productImageBackName" alt="photo"/>' +
        '</td>'+
        '<td>'+
        '<div id="productValue2"><br/><p>{{newProduct.specification}}</p></div>' +
        '</td>'+
        '</tr>' +
        '<tr>' +
        '<td colspan="2" style="height: 30px">' +
        '<div class="detailsBtnColor" v-on:click="hiddenFlagDetails"><input class="btnUp" type="button"></div>' +
        '</td>'+
        '</tr>'+
        '</table>' +
        '</transition>' +
        '</div>',
    methods: {
        hiddenFlag: function () {
            if (!(this.show === true) && !(this.showImage === true)) {
                this.show = true;
                this.showImage = true;
            } else {
                this.show = false;
                this.showImage = false;
                this.showRightSide = false;
                this.showLeftSide = false;
                this.showBackSide = false;
            }
        },
        hiddenFlagRightSide: function () {
            if (!(this.show === true) && !(this.showRightSide === true)) {
                this.show = true;
                this.showRightSide = true;
            } else {
                this.show = false;
                this.showRightSide = false;
            }
        },
        hiddenFlagLeftSide: function () {
            if (!(this.show === true) && !(this.showLeftSide === true)) {
                this.show = true;
                this.showLeftSide = true;
            } else {
                this.show = false;
                this.showLeftSide = false;
            }
        },
        hiddenFlagBackSide: function () {
            if (!(this.show === true) && !(this.showBackSide === true)) {
                this.show = true;
                this.showBackSide = true;
            } else {
                this.show = false;
                this.showRightSide = false;
            }
        },
        hiddenFlagDetails: function () {
            if (!(this.showDetails === true) && (this.showDetailsButton === true)) {
                this.showDetails = true;
                this.showDetailsButton = false;
            } else {
                this.showDetails = false;
                this.showDetailsButton = true;
            }
        },
        hiddenFlagForBasket: function () {
            let key = this.id;
            localStorage.setItem(key, this.descriptionForRequest);
            appShowBasket.hiddenFlagForBasket();
        }
    }
});

Vue.component('newProducts-list', {
    props: ['newProducts'],
    template: '<div>' +
        '<newProduct-row v-for="newProduct in newProducts" :key="newProduct.id" :newProduct="newProduct"/>' +
        '</div>',
    created: function () {
        productAPI.get().then(result =>
            result.json().then(data =>
                data.forEach(newProduct => this.newProducts.push(newProduct))
            )
        );
    }
});

let app = new Vue({
    el: '#app',
    template: '<newProducts-list :newProducts="newProducts" />',
    data: {
        newProducts: []
    }
});

Vue.component('newProductCategory-row', {
    props: ['newProductCategory'],
    data: function () {
        return {
            categoryNameForRequest: ''
        }
    },
    template:
        '<div>' +
        '<table style="width:320px; height: 80px" >' +
        '<tr>' +
        '<td hidden><h3>{{this.categoryNameForRequest=newProductCategory.communicationData}}</h3>' +
        '</td>' +
        '</tr>' +
        '<td style="width:270px; height: auto" colspan="2">' +
        '<button type="submit" v-on:click="getSortedProducts" class="menuButton">' +
        '{{newProductCategory.communicationData}}</button></td>' +
        '</tr>' +
        '</table>' +
        '</div>',
    methods: {
        getSortedProducts: function () {
            let sortingTag = {
                key: 'sorting tag',
                communicationKey: this.categoryNameForRequest,
            }

            app.newProducts = app.newProducts.splice(0, 0);
            productAPISorted.save({}, sortingTag).then(result =>
                result.json().then(data =>
                    data.forEach(newProductCategory => app.newProducts.push(newProductCategory))
                )
            );
            setTimeout(function () {
                appProductBrand.productBrands = appProductBrand.productBrands.splice(0, 0);
                appSubCategory.newProductSubCategories = appSubCategory.newProductSubCategories.splice(0, 0);
                productCategoryApiGetSubCategories.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(newProductCategory =>
                            appSubCategory.newProductSubCategories.push(newProductCategory))
                    ));
            }, 700);
        },
    }
});

Vue.component('newProductCategories-list', {
    props: ['newProductCategories'],
    template: '<div>' +
        '<newProductCategory-row v-for="newProductCategory in newProductCategories" ' +
        ':key="newProductCategory.id" :newProductCategory="newProductCategory"/>' +
        '</div>',
    created: function () {
        setTimeout(function () {
            appCategory.newProductCategories = appCategory.newProductCategories.splice(0, 0);
            productCategoryApi.get().then(result =>
                result.json().then(data =>
                    data.forEach(newProductCategory => appCategory.newProductCategories.push(newProductCategory))
                ));
        }, 500);
    }
});

let appCategory = new Vue({
    el: '#appCategory',
    template: '<newProductCategories-list :newProductCategories="newProductCategories" />',
    data: {
        newProductCategories: []
    }
});

Vue.component('newProductSubCategory-row', {
    props: ['newProductSubCategory'],
    data: function () {
        return {
            subCategoryNameForRequest: ''
        }
    },
    template:
        '<div>' +
        '<table style="width:320px; height: 80px; position: relative; top: 10px" >' +
        '<tr>' +
        '<td hidden><h3>{{this.subCategoryNameForRequest=newProductSubCategory.communicationData}}</h3></td>' +
        '</tr>' +
        '<tr>' +
        '<td style="width:270px; height: auto" colspan="2">' +
        '<button type="submit" v-on:click="getSortedProducts" class="subMenuButton">' +
        '{{newProductSubCategory.communicationData}}</button></td>' +
        '</tr>' +
        '</table>' +
        '</div>',
    methods: {
        getSortedProducts: function () {
            app.newProducts = app.newProducts.splice(0, 0);
            let sortingTag = {
                key: 'sorting tag',
                communicationKey: this.subCategoryNameForRequest,
            }
            productAPISortedBySubCategory.save({}, sortingTag).then(result =>
                result.json().then(data =>
                    data.forEach(newProductSubCategory => app.newProducts.push(newProductSubCategory))
                )
            );
            setTimeout(function () {
                appProductBrand.productBrands = appProductBrand.productBrands.splice(0, 0);
                productCategoryApiGetBrands.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(newProductSubCategory => appProductBrand.productBrands.push(newProductSubCategory))
                    )
                );
            }, 700);
        }
    }
});

Vue.component('newProductSubCategories-list', {
    props: ['newProductSubCategories'],
    template: '<div>' +
        '<newProductSubCategory-row v-for="newProductSubCategory in newProductSubCategories" ' +
        ':key="newProductSubCategory.id" :newProductSubCategory="newProductSubCategory"/>' +
        '</div>',
});

let appSubCategory = new Vue({
    el: '#appSubCategory',
    template: '<newProductSubCategories-list :newProductSubCategories="newProductSubCategories" />',
    data: {
        newProductSubCategories: []
    }
});

Vue.component('productBrand-row', {
    props: ['productBrand'],
    data: function () {
        return {
            productBrandForRequest: '',
        }
    },
    template:
        '<div>' +
        '<table style="width:320px; height: 70px; position: relative; top: 10px" >' +
        '<tr>' +
        '<td hidden><h3>{{this.productBrandForRequest=productBrand.communicationData}}</h3></td>' +
        '</tr>' +
        '<tr>' +
        '<td style="width:270px; height: auto" colspan="2">' +
        '<button type="submit" v-on:click="getSortedProducts" class="brandMenuButton">' +
        '{{productBrand.communicationData}}</button>' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '</div>',
    methods: {
        getSortedProducts: function () {
            app.newProducts = app.newProducts.splice(0, 0);
            let sortingTag = {
                key: 'sorting tag',
                communicationKey: this.productBrandForRequest,
            }
            productAPISortedByBrand.save({}, sortingTag).then(result =>
                result.json().then(data =>
                    data.forEach(productBrand => app.newProducts.push(productBrand))
                )
            );
        }
    }
});

Vue.component('productBrands-list', {
    props: ['productBrands'],
    template: '<div>' +
        '<productBrand-row v-for="productBrand in productBrands" :key="productBrand.id" :productBrand="productBrand"/>' +
        '</div>',
});

let appProductBrand = new Vue({
    el: '#appProductBrand',
    template: '<productBrands-list :productBrands="productBrands" />',
    data: {
        productBrands: []
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
            productAPISortedByTypeOfPurpose.save({}, sortingTag).then(result =>
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




