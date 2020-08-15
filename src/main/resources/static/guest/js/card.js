Vue.http.headers.common['Authorization'] = localStorage.getItem('CustomHeader');
axios.defaults.headers.common['Authorization'] = localStorage.getItem('CustomHeader');

let appSelector = new Vue({
    el: '#appSelector',
    data: {
        totalProductPrice: 0,
        dataLoadStatus:'',
        firstnameUser: '',
        middleNameUser: '',
        lastNameUser: '',
        emailNameUser: '',
        phoneNameUser: '',
        confirmBooking: '',
        totalItemsAmount:'',
        bookingItems:[],
        paymentType: '',
        deliveryType: '',
        regionUser:'',
        cityUser:'',
        addressUser:'',
        maxId: ''
    },
    methods: {
        bookingMaker: function () {
            this.maxId = localStorage.getItem('ID');
            this.totalItemsAmount = localStorage.getItem('TotalAmount');
            if (this.maxId !== null) {
                let maxValue = Number(this.maxId)
                let itemId = 0;
                for (let i = 0; i <= maxValue; i++) {
                    let item = localStorage.getItem(String(i));
                    if (item !== null) {
                        let items = new Set(JSON.parse(localStorage.getItem(String(i))));
                        for (let productFromBasket of items) {
                            let bookingItem = {
                                id: itemId,
                                key: i,
                                description: productFromBasket.description,
                                productPrice: productFromBasket.productPrice,
                                totalAmount: productFromBasket.totalAmount,
                                productImageName: productFromBasket.productImageName
                            };
                            this.bookingItems.push(bookingItem);
                            itemId++;
                        }
                    }
                }
            }

            let formData = new FormData();
            formData.append('firstnameUser', this.firstnameUser);
            formData.append('middleNameUser', this.middleNameUser);
            formData.append('lastNameUser', this.lastNameUser);
            formData.append('emailNameUser', this.emailNameUser);
            formData.append('phoneNameUser', this.phoneNameUser);
            formData.append('confirmBooking', this.confirmBooking);
            formData.append('totalProductPrice', this.totalProductPrice);
            formData.append('totalItemsAmount', this.totalItemsAmount);
            formData.append('paymentType', this.paymentType);
            formData.append('deliveryType', this.deliveryType);
            formData.append('regionUser', this.regionUser);
            formData.append('cityUser', this.cityUser);
            formData.append('addressUser', this.addressUser);
            formData.append('bookingItems', this.bookingItems);

            axios.post('/guest/booking/create',
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }
            ).then(response => (this.dataLoadStatus = response.data.status));
        }
    }
});

Vue.component('productItem-row', {
    props: ['productItem'],
    data: function () {
        return {
            id: '',
            key: '',
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
            summaryPrice: 0,
            productImageName: '',
            count: 1
        }
    },
    template:
        '<div>' +
        '<table id="basketTable" style="width:590px; height: 100px; font-size: 15px;' +
        'font-family: Arial, sans-serif;font-style: normal;color: black;">' +
        '<tr>' +
        '<td hidden><h3>{{this.totalAmount=productItem.totalAmount}}</h3></td>' +
        '<td hidden><h3>{{this.productPrice=productItem.productPrice}}</h3></td>' +
        '<td hidden><h3>{{this.id=productItem.id}}</h3></td>' +
        '<td hidden><h3>{{this.key=productItem.key}}</h3></td>' +
        '</tr>' +
        '<tr>' +
        '<td  style="alignment:center;vertical-align:center;width:60px">' +
        '<img style="width: 50px; height: 50px; border-radius:20%" :src="productItem.productImageName" alt="photo"/>' +
        '</td>' +
        '<td  id="cellStyleDescription" style="width:210px; height:auto">' +
        '<div class="productValue" id="productBasketValue1"><p>{{productItem.description}}</p></div>' +
        '</td>' +
        '<td id="cellStyle" style="width:100px; height:auto">' +
        '<div v-if="this.totalAmount>0" id="availableInBasket"><p>{{count}}</p></div>' +
        '<div v-if="this.totalAmount==0" id="notAvailableInBasket"><p>Нет в наличии</p></div>' +
        '<td id="cellStyle" style="width:60px; height: auto">' +
        '<input class="counterButton" type="button" v-on:click="counterPlus" >' +
        '<input class="counterButtonMinus" type="button" v-on:click="counterMinus" >' +
        '</td>' +
        '<td id="cellStyle" style="width:100px; height: auto">' +
        '<div class="productValue" id="productBasketValue3"><p v-if="summaryPrice!==0">{{summaryPrice}} грн</p>' +
        '<p v-if="summaryPrice===0">{{productItem.productPrice}} грн</p>' +
        '</div>' +
        '</td>' +
        '<td style="width:60px; height: auto">' +
        '<input class="deleteButton" type="button" v-on:click="deleteItem" >' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '</div>',
    methods: {
        deleteItem: function () {
            let idForDelete = this.id;
            let newSet = new Set();
            let fixedValue = this.productPrice;
            if (this.summaryPrice === 0) {
                this.summaryPrice = Number(fixedValue);
            }
            let temporaryValue = this.summaryPrice;

            appBasket.productItems.forEach(function (item) {
                if (item.id !== idForDelete) {
                    newSet.add(item);
                }
            })
            appBasket.productItems = appBasket.productItems.splice(0, 0);
            for (let productFromNewSet of newSet) {
                appBasket.productItems.push(productFromNewSet);
            }
            localStorage.removeItem(String(this.key));
            let basketTotalAmount = (Number(localStorage.getItem('TotalAmount'))) - 1;
            localStorage.setItem('TotalAmount', String(basketTotalAmount));
            appSelector.totalProductPrice = appSelector.totalProductPrice - temporaryValue;
        },
        counterPlus: function () {
            if ((this.count >= 1) && (this.totalAmount > 0)) {
                let productItem = {
                    description: '',
                    productPrice: '',
                    totalAmount: '',
                    productImageName: ''
                };
                let items = new Set(JSON.parse(localStorage.getItem(String(this.key))));
                for (let productFromBasket of items) {
                    if (Number(productFromBasket.totalAmount) > Number('0')) {
                        let fixedValue = this.productPrice;
                        if (this.summaryPrice === 0) {
                            this.summaryPrice = Number(fixedValue);
                        }
                        let temporaryValue = this.summaryPrice;
                        this.summaryPrice = (Number(fixedValue) + temporaryValue);

                        productItem = {
                            description: productFromBasket.description,
                            productPrice: this.summaryPrice,
                            totalAmount: Number(productFromBasket.totalAmount) - Number('1'),
                            productImageName: productFromBasket.productImageName
                        };
                        this.count++;
                        let basketItems = [];
                        basketItems.push(productItem);

                        localStorage.setItem(String(this.key), JSON.stringify(basketItems));
                        appSelector.totalProductPrice = appSelector.totalProductPrice + Number(fixedValue);
                    }
                }
            }
        },
        counterMinus: function () {
            if ((this.count > 1)) {
                this.count--;

                let productItem = {
                    description: '',
                    productPrice: '',
                    totalAmount: '',
                    productImageName: ''
                };
                let items = new Set(JSON.parse(localStorage.getItem(String(this.key))));
                for (let productFromBasket of items) {
                    let fixedValue = this.productPrice;

                    let temporaryValue = this.summaryPrice;
                    this.summaryPrice = (Number(temporaryValue) - fixedValue);
                    productItem = {
                        description: productFromBasket.description,
                        productPrice: this.summaryPrice,
                        totalAmount: Number(productFromBasket.totalAmount) + Number('1'),
                        productImageName: productFromBasket.productImageName
                    };
                    let basketItems = [];
                    basketItems.push(productItem);
                    localStorage.setItem(String(this.key), JSON.stringify(basketItems));
                    appSelector.totalProductPrice = appSelector.totalProductPrice - Number(fixedValue);
                }

            }
        }
    }
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
            let itemId = 0;
            for (let i = 0; i <= maxValue; i++) {
                let item = localStorage.getItem(String(i));
                if (item !== null) {
                    let items = new Set(JSON.parse(localStorage.getItem(String(i))));
                    for (let productFromBasket of items) {
                        let productItem = {
                            id: itemId,
                            key: i,
                            description: productFromBasket.description,
                            productPrice: productFromBasket.productPrice,
                            totalAmount: productFromBasket.totalAmount,
                            productImageName: productFromBasket.productImageName
                        };
                        this.productItems.push(productItem);
                        itemId++;
                        appSelector.totalProductPrice = (Number(appSelector.totalProductPrice))
                            + (Number(productItem.productPrice));
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






