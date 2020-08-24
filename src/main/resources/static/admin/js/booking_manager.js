const bookingAPI = Vue.resource('/admin_auth/booking/all');
const bookingAPIEdit = Vue.resource('/admin_auth/booking/update');
Vue.http.headers.common['Authorization'] = localStorage.getItem('CustomHeader');
axios.defaults.headers.common['Authorization'] = localStorage.getItem('CustomHeader');

let basketImage = new Vue({
    el: '#basketImage',
    methods: {
        activeImageAction: function () {
            window.location = 'admin_booking_manager.html';
        }
    }
});

let appSelector = new Vue({
    el: '#appSelector',
    data: {
        totalProductPrice: 0,
        dataLoadStatus: '',
        firstnameUser: '',
        middleNameUser: '',
        lastNameUser: '',
        emailNameUser: '',
        phoneNameUser: '',
        confirmBooking: '',
        totalItemsAmount: '',
        bookingItems: [],
        paymentType: '',
        deliveryType: '',
        regionUser: '',
        cityUser: '',
        addressUser: '',
        maxId: ''
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
            totalAmount: 0,
            itemAmount: 0,
            itemAmountFlag: true,
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
        '<td hidden><h3>{{this.itemAmount=productItem.itemAmount}}</h3></td>' +
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
        '<div v-if="(this.totalAmount>=0)&&(this.itemAmountFlag===false)" id="availableInBasket"><p>{{count}}</p></div>' +
        '<div v-if="(this.totalAmount>=0)&&(this.itemAmountFlag===true)" id="availableInBasket"><p>{{itemAmount}}</p></div>' +
        '</td>' +
        '<td id="cellStyle" style="width:60px; height: auto">' +
        '<button class="counterButton" type="button" v-on:click="counterPlus" ></button>' +
        '<button class="counterButtonMinus" type="button" v-on:click="counterMinus" ></button>' +
        '</td>' +
        '<td id="cellStyle" style="width:100px; height: auto">' +
        '<div class="productValue" id="productBasketValue3"><p v-if="summaryPrice!==0">{{summaryPrice}} грн</p>' +
        '<p v-if="summaryPrice===0">{{productItem.productPrice}} грн</p>' +
        '</div>' +
        '</td>' +
        '<td style="width:60px; height: auto">' +
        '<button class="deleteButton" type="button" v-on:click="deleteItem"></button>' +
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
            if ((this.count >= 1) && (this.totalAmount >= 0)) {

                let productItem = {
                    description: '',
                    productPrice: '',
                    totalAmount: '',
                    productImageName: ''
                };
                let items = new Set(JSON.parse(localStorage.getItem(String(this.key))));
                if ((Number(this.itemAmount) > Number(this.count)) && (this.itemAmountFlag === true)) {
                    this.count = Number(this.itemAmount);
                }
                for (let productFromBasket of items) {
                    if (Number(productFromBasket.totalAmount) > Number('0')) {
                        let fixedValue = Number(this.productPrice) / Number(this.itemAmount);
                        if (this.summaryPrice === 0) {
                            this.summaryPrice = Number(this.productPrice);
                        }
                        let temporaryValue = this.summaryPrice;
                        this.summaryPrice =
                            (Number(fixedValue.toFixed(1)) + Number(temporaryValue.toFixed(1)));
                        productItem = {
                            description: productFromBasket.description,
                            productPrice: this.summaryPrice,
                            totalAmount: Number(productFromBasket.totalAmount) - Number('1'),
                            itemAmount: Number(productFromBasket.itemAmount) + Number('1'),
                            productImageName: productFromBasket.productImageName
                        };
                        this.count++;
                        let basketItems = [];
                        basketItems.push(productItem);

                        localStorage.setItem(String(this.key), JSON.stringify(basketItems));
                        appSelector.totalProductPrice =
                            (Number((appSelector.totalProductPrice).toFixed(1)) + Number(fixedValue));
                        this.itemAmountFlag = false;
                    }
                }
            }
        },
        counterMinus: function () {
            if (Number(this.itemAmount) > Number(this.count) && (this.itemAmountFlag === true)) {
                this.count = Number(this.itemAmount);
            }
            if ((this.count > 1)) {
                this.itemAmountFlag = false;
                let productItem = {
                    description: '',
                    productPrice: '',
                    totalAmount: '',
                    productImageName: ''
                };
                let items = new Set(JSON.parse(localStorage.getItem(String(this.key))));
                for (let productFromBasket of items) {
                    if (Number(productFromBasket.totalAmount) >= Number('0')) {
                        let fixedValue = Number(this.productPrice) / Number(this.itemAmount);
                        if (this.summaryPrice === 0) {
                            this.summaryPrice = Number(this.productPrice);
                        }
                        let temporaryValue = this.summaryPrice;
                        this.summaryPrice =
                            (Number(temporaryValue.toFixed(1)) - Number(fixedValue.toFixed(1)));

                        productItem = {
                            description: productFromBasket.description,
                            productPrice: this.summaryPrice,
                            totalAmount: Number(productFromBasket.totalAmount) + Number('1'),
                            itemAmount: Number(productFromBasket.itemAmount) - Number('1'),
                            productImageName: productFromBasket.productImageName
                        };
                        let basketItems = [];
                        basketItems.push(productItem);
                        localStorage.setItem(String(this.key), JSON.stringify(basketItems));
                        appSelector.totalProductPrice =
                            (Number((appSelector.totalProductPrice).toFixed(1)) - Number(fixedValue));
                        this.totalAmount = Number(productItem.totalAmount);
                        this.count--;
                    }
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
            let maxValue = Number(this.maxId);
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
                            itemAmount: productFromBasket.itemAmount,
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

Vue.component('newBooking-row', {
    props: ['newBooking'],
    data: function () {
        return {
            show: false,
            showDetails: false,
            showDetailsButton: true,
            flagForBasketMaker: false,
            id: '',
            firstName: '',
            middleName: '',
            lastName: '',
            email: '',
            contact: '',
            deliveryType: '',
            paymentType: '',
            region: '',
            city: '',
            address: '',
            editStatus: '',
            bookingItemsForEdit: [],
            bookingItemsFromDb: []
        }
    },
    template:
        '<div>' +
        '<table id="fromDb" style="width:1000px; height: 300px">' +
        '<tr>' +
        '    <td id="cellStyle" hidden><p>{{this.id=newBooking.id}}</p></td>' +
        '    <td id="cellStyle" hidden><p>{{this.bookingItemsFromDb=newBooking.bookingItems}}</p></td>' +
        '</tr>' +
        '' +
        '<tr>' +
        '    <td style="height: 30px"><p>Дата/время заказа:</p></td>' +
        '    <td style="height: 30px"><p>Имя:</p></td>' +
        '    <td style="height: 30px"><p>Отчество:</p></td>' +
        '    <td style="height: 30px"><p>Фамилия:</p></td>' +

        '    <td rowspan="7" style="width:70px;height:auto">' +
        '        <input class="adminButtonTemplate" type="button" value="Изменить" v-on:click="hiddenFlag">' +
        '        <input class="adminButtonTemplate" type="button" value="Подтвердить">' +
        '        <input class="adminButtonTemplate" type="button" value="Отменить">' +
        '    </td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.dateOfBooking}}</p></td>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.firstName}}</p></td>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.middleName}}</p></td>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.lastName}}</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>E-mail:</p></td>' +
        '    <td style="height: 30px"><p>Номер телефона:</p></td>' +
        '    <td style="height: 30px"><p>Общая сума:</p></td>' +
        '    <td style="height: 30px"><p>Корзина пользователя:</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.email}}</p></td>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.contact}}</p></td>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.totalPrice}} грн</p></td>' +
        '    <td style="height: auto"><div id="basketManager" v-on:click="basketMaker"><div id="countManager">{{newBooking.totalAmount}}</div>' +
        '</div></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>Способ доставки:</p></td>' +
        '    <td style="height: 30px"><p>Соглашение пользователя:</p></td>' +
        '    <td style="height: 30px"><p>Область:</p></td>' +
        '    <td style="height: 30px"><p>Адрес:</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.deliveryType}}</p></td>' +
        '    <td rowspan="3" style="height: auto"><p class="managerTextStyle">{{newBooking.userConfirmation}}</p></td>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.region}}</p></td>' +
        '    <td rowspan="3" style="height: auto; width: 220px"><p class="managerTextStyle">{{newBooking.address}}</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>Способ оплаты:</p></td>' +
        '    <td style="height: 30px"><p>Город:</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.paymentType}}</p></td>' +
        '    <td style="height: auto"><p class="managerTextStyle">{{newBooking.city}}</p></td>' +
        '</tr>' +
        '<tr>' +
        '    <td colspan="5" id="cellStyle" style="width:1000px; height: 25px">' +
        '        <div class="detailsBtnColor" v-if="showDetailsButton" v-on:click="hiddenFlagDetails">' +
        '<button class="btn" type="button"></button>' +
        '        </div>' +
        '    </td>' +
        '</tr>' +
        '</table>' +

        '<transition name="fade">' +
        '<table v-if="showDetails" id="detailsTable" style="width:1000px; height: 270px">' +
        '<tr>' +
        '<td id="cellStyle" style="height: 30px; width: 900px"><p>Коментарий к заказу:</p>' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>' +
        '<div id="productValue2"><br/><p>{{newBooking.bookingComments}}</p></div>' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td style="height: 30px">' +
        '<div class="detailsBtnColor" v-on:click="hiddenFlagDetails"><button class="btnUp" type="button"></button></div>' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '</transition>' +

        ' <transition name="fade">' +
        ' <table v-if="show" style="width:1000px; height: 300px; background: rgb(221,221,221); border: 2px solid #4f4f01">' +
        ' <tr>' +
        '        <td style="width:335px;height: auto"><textarea placeholder="Введите Имя" v-model="firstName"></textarea></td>' +
        '        <td style="width:335px;height: auto"><textarea placeholder="Введите Фамилию" v-model="lastName"></textarea></td>' +
        '        <td style="width:335px;height: auto"><textarea placeholder="Введите Отчество" v-model="middleName"></textarea></td>' +
        ' </tr>' +
        ' <tr>' +
        '         <td style="width:335px;height: auto"><textarea placeholder="Введите E-mail" v-model="email"></textarea></td>' +
        '         <td style="width:335px;height: auto"><textarea placeholder="Номер телефона +38" v-model="contact"></textarea></td>' +
        '         <td style="width:335px;height: auto"><textarea placeholder="Введите город" v-model="city"></textarea></td>' +
        ' </tr>' +
        ' <tr>' +
        '         <td style="width:335px;height: auto"><textarea placeholder="Способ доставки" v-model="deliveryType"></textarea></td>' +
        '         <td rowspan="2" style="width:335px;height: auto"><textarea placeholder="Введите адрес" v-model="address"></textarea></td>' +
        '         <td style="width:335px;height: auto"><textarea placeholder="Введите область" v-model="region"></textarea></td>' +
        ' </tr>' +
        ' <tr>' +
        '         <td style="width:335px;height: auto"><textarea placeholder="Введите способ оплаты" v-model="paymentType"></textarea></td>' +
        '         <td style="width:auto; height: auto"><input id="superAdminBooking" type="button" value="Сохранить" v-on:click="editBooking"></td>' +
        ' </tr>' +
        ' <tr>' +
        '         <td colspan="3"  style="width:auto; height: 30px"><p>{{editStatus}}</p></td>' +
        ' </tr>' +
        ' </table>' +
        ' </transition>' +
        '</div>',
    methods: {
        basketMaker: function () {
            if (this.flagForBasketMaker === false) {
                this.flagForBasketMaker = true;
                let idKey = 1;
                let totalAmount = 0;
                for (let productFromBasket of this.bookingItemsFromDb) {
                    let productItem = {
                        id: productFromBasket.id,
                        key: productFromBasket.key,
                        description: productFromBasket.description,
                        productPrice: productFromBasket.productPrice,
                        totalAmount: productFromBasket.totalAmount,
                        itemAmount: productFromBasket.itemAmount,
                        productImageName: productFromBasket.productImageName
                    }
                    let itemsReadyForParse = [];
                    itemsReadyForParse.push(productItem);
                    localStorage.setItem(String(productItem.key), JSON.stringify(itemsReadyForParse));
                    if (productItem.key > idKey) {
                        idKey = productItem.key;
                    }
                    appBasket.productItems.push(productItem);
                    appSelector.totalProductPrice = (Number(appSelector.totalProductPrice))
                        + (Number(productItem.productPrice));
                    totalAmount++;
                }
                if (localStorage.getItem('ID') === null) {
                    localStorage.setItem('ID', String(idKey));
                }

                localStorage.setItem("TotalAmount", String(totalAmount));
            } else {
                let maxId = Number(localStorage.getItem('ID'));
                for (let i = 0; i <= maxId; i++) {
                    let item = localStorage.getItem(String(i));
                    if (item !== null) {
                        localStorage.removeItem(String(i));
                    }
                }
                appBasket.productItems = appBasket.productItems.splice(0, 0);
                appSelector.totalProductPrice = 0;
                localStorage.removeItem('ID');
                localStorage.removeItem('TotalAmount');
                this.flagForBasketMaker = false;
            }

        },

        hiddenFlag: function () {
            if (this.show === false) {
                this.show = true;
            } else {
                this.show = false;
            }
        },
        hiddenFlagDetails: function () {
            if ((this.showDetails === false) && (this.showDetailsButton === true)) {
                this.showDetails = true;
                this.showDetailsButton = false;
            } else {
                this.showDetails = false;
                this.showDetailsButton = true;
            }
        },

        editBooking: function () {
            if ((this.firstName !== '') || (this.middleName !== '') || (this.lastName !== '')
                || (this.email !== '') || (this.contact !== '') || (this.paymentType !== '')
                || (this.deliveryType !== '') || (this.region !== '') || (this.city !== '')
                || (this.address !== '') || (this.bookingItemsForEdit !== '')) {
                let maxValue = Number(localStorage.getItem('ID'));
                let totalItemsAmount = Number(localStorage.getItem('TotalAmount'));
                if (maxValue !== null) {
                    let itemId = 0;
                    for (let i = 0; i <= maxValue; i++) {
                        let item = localStorage.getItem(String(i));
                        if (item !== null) {
                            let items = new Set(JSON.parse(localStorage.getItem(String(i))));
                            localStorage.removeItem(String(i));
                            localStorage.removeItem('TotalAmount');
                            for (let productFromBasket of items) {
                                let bookingItem = {
                                    id: itemId,
                                    key: i,
                                    description: productFromBasket.description,
                                    productPrice: productFromBasket.productPrice,
                                    totalAmount: productFromBasket.totalAmount,
                                    itemAmount: productFromBasket.itemAmount,
                                    productImageName: productFromBasket.productImageName
                                };
                                this.bookingItemsForEdit.push(bookingItem);
                                itemId++;
                            }
                        }
                    }
                }
                if ((appSelector.totalProductPrice === 0) && (totalItemsAmount === 0)) {
                    appSelector.totalProductPrice = '';
                    totalItemsAmount = '';
                }
                let bookingBody = {
                    id: this.id,
                    firstName: this.firstName,
                    middleName: this.middleName,
                    lastName: this.lastName,
                    email: this.email,
                    contact: this.contact,
                    totalPrice: appSelector.totalProductPrice,
                    totalAmount: totalItemsAmount,
                    paymentType: this.paymentType,
                    deliveryType: this.deliveryType,
                    region: this.region,
                    city: this.city,
                    address: this.address,
                    bookingItems: this.bookingItemsForEdit
                }

                bookingAPIEdit.save({}, bookingBody).then(response => (this.editStatus = response.data.status));
                this.firstnameUser = ''
                this.middleNameUser = ''
                this.lastNameUser = ''
                this.emailNameUser = ''
                this.phoneNameUser = ''
                this.totalProductPrice = ''
                this.totalItemsAmount = ''
                this.paymentType = ''
                this.deliveryType = ''
                this.regionUser = ''
                this.cityUser = ''
                this.addressUser = ''
                this.bookingItems = []

                for (let i = 0; i <= maxValue; i++) {
                    let item = localStorage.getItem(String(i));
                    if (item !== null) {
                        localStorage.removeItem(String(i));
                    }
                }
                appBasket.productItems = appBasket.productItems.splice(0, 0);
                appSelector.totalProductPrice = 0;
                localStorage.removeItem('ID');
                localStorage.removeItem('TotalAmount');
                appBookingManager.newBookings = appBookingManager.newBookings.splice(0, 0);

                setTimeout(function () {
                    bookingAPI.get().then(result =>
                        result.json().then(data =>
                            data.forEach(newBooking => appBookingManager.newBookings.push(newBooking))
                        )
                    );
                }, 700);
            } else {
                alert('Заказ невозможно изменить, нет информации об изменениях');
            }
        }

    }
});

Vue.component('newBookings-list', {
    props: ['newBookings'],
    template: '<div>' +
        '<newBooking-row v-for="newBooking in newBookings" :key="newBooking.id" :newBooking="newBooking"/>' +
        '</div>',
    created: function () {
        let auth = localStorage.getItem('CustomHeader');
        if (auth !== null) {
            bookingAPI.get().then(result =>
                result.json().then(data =>
                    data.forEach(newBooking => this.newBookings.push(newBooking))
                )
            );
        } else {
            window.location = 'authorize.html';
        }
    }
});

let appBookingManager = new Vue({
    el: '#appBookingManager',
    template: '<newBookings-list :newBookings="newBookings" />',
    data: {
        newBookings: []
    }
});


let appHeaderButtons = new Vue({
    el: '#appHeaderButtons',
    props: ['newBookings'],
    methods: {
        getSortedProductsByTypeOfPurpose: function (message) {
            app.newBookings = app.newBookings.splice(0, 0);
            let sortingTag = {
                key: 'sorting tag',
                communicationKey: message,
            }
            productAPISortedByTypeOfPurpose.save({}, sortingTag).then(result =>
                result.json().then(data =>
                    data.forEach(productBrand => app.newBookings.push(productBrand))
                )
            );
        }
    }
})

let appLogoutButtons = new Vue({
    el: '#appLogoutButtons',
    methods: {
        logoutUserAction: function () {
            localStorage.removeItem('CustomHeader');
            let auth = localStorage.getItem('CustomHeader')
            if (auth == null) {
                setTimeout(function () {
                    alert('Завершение работы администратора');
                    window.location = 'authorize.html';
                }, 500);
            } else {
                alert('Некоректное завершение работы, попробуйте еще раз или обратитесь к супер админу');
            }
        },
        registrationUserAction: function () {
            window.location = 'registration.html';
        }
    }
})

