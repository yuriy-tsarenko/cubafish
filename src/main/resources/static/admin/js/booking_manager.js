const bookingAPI = Vue.resource('/guest/booking/all');
Vue.http.headers.common['Authorization'] = localStorage.getItem('CustomHeader');
axios.defaults.headers.common['Authorization'] = localStorage.getItem('CustomHeader');

let basketImage = new Vue({
    el: '#basketImage',
    methods: {
        activeImageAction: function () {
            window.location = 'http://91.235.128.12:8081/admin/admin_booking_manager.html';
        }
    }
});

Vue.component('newBooking-row', {
    props: ['newBooking'],
    data: function () {
        return {
            show: false,
            showDetails: false,
            showDetailsButton: true,
            id: '',
            firstName: '',
            middleName: '',
            lastName: '',
            email: '',
            contact: '',
            deliveryType: '',
            paymentType:'',
            region: '',
            city: '',
            address: '',
            editStatus: ''
        }
    },
    template:
        '<div>' +
        '<table id="fromDb" style="width:1000px; height: 300px">' +
        '<tr>' +
        '    <td id="cellStyle" hidden><p>{{this.id=newBooking.id}}</p></td>' +
        '</tr>' +
        '' +
        '<tr>' +
        '    <td style="height: 30px"><p>Дата/время заказа</p></td>' +
        '    <td style="height: 30px"><p>Имя</p></td>' +
        '    <td style="height: 30px"><p>Отчество</p></td>' +
        '    <td style="height: 30px"><p>Фамилия</p></td>' +

        '    <td rowspan="8" style="width:70px;height:auto">' +
        '        <input id="superAdminButtonProductTemplateManager" type="button" value="Изменить" v-on:click="hiddenFlag">' +
        '        <input id="superAdminButtonProductTemplateManager2" type="button" value="Заказ оформлен">' +
        '    </td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>{{newBooking.dateOfBooking}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.firstName}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.middleName}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.lastName}}</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>E-mail</p></td>' +
        '    <td style="height: 30px"><p>Номер телефона</p></td>' +
        '    <td style="height: 30px"><p>Общая сума</p></td>' +
        '    <td style="height: 30px"><p>Общее количество</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>{{newBooking.email}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.contact}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.totalPrice}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.totalAmount}}</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>Способ доставки</p></td>' +
        '    <td style="height: 30px"><p>Соглашение пользователя</p></td>' +
        '    <td style="height: 30px"><p>Область</p></td>' +
        '    <td style="height: 30px"><p>Адрес</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>{{newBooking.deliveryType}}</p></td>' +
        '    <td rowspan="3" style="height: 30px"><p>{{newBooking.userConfirmation}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.region}}</p></td>' +
        '    <td rowspan="3" style="height: 30px"><p>{{newBooking.address}}</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>Способ оплаты</p></td>' +
        '    <td style="height: 30px"><p>Город</p></td>' +
        '</tr>' +

        '<tr>' +
        '    <td style="height: 30px"><p>{{newBooking.paymentType}}</p></td>' +
        '    <td style="height: 30px"><p>{{newBooking.city}}</p></td>' +
        '</tr>' +
        '<tr>' +
        '    <td colspan="5" id="cellStyle" style="width:1000px; height: 25px">' +
        '        <div class="detailsBtnColor" v-if="showDetailsButton" v-on:click="hiddenFlagDetails">' +
        '<input class="btn" type="button">' +
        '        </div>' +
        '    </td>' +
        '</tr>' +
        '</table>' +

        ' <transition name="fade">' +
        ' <table v-if="show" style="width:1000px; height: 300px; background: rgb(221,221,221); border: 2px solid #4f4f01">' +
        ' <tr>' +
        '        <td style="width:335px;height: auto"><textarea placeholder="Введите Имя" v-model="firstName"></textarea></td>' +
        '        <td style="width:335px;height: auto"><textarea placeholder="Введите Фамилию" v-model="middleName"></textarea></td>' +
        '        <td style="width:335px;height: auto"><textarea placeholder="Введите Отчество" v-model="lastName"></textarea></td>' +
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
        '         <td style="width:auto; height: auto"><input id="superAdminBooking" type="button" value="Сохранить" @click="editProduct"></td>' +
        ' </tr>' +
        ' <tr>' +
        '         <td colspan="3"  style="width:auto; height: 30px"><p>{{editStatus}}</p></td>' +
        ' </tr>' +
        ' </table>' +
        ' </transition>' +

        '<transition name="fade">' +
        '<table v-if="showDetails" id="detailsTable" style="width:1000px; height: 270px">' +
        '<tr>' +
        '<td id="cellStyle" style="height: 30px; width: 900px"><p>Наименование товаров</p>' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>' +
        '<div id="productValue2"><br/><p>{{newBooking.bookingItems}}</p></div>' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td style="height: 30px">' +
        '<div class="detailsBtnColor" v-on:click="hiddenFlagDetails"><input class="btnUp" type="button"></div>' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '</transition>' +

        '</div>',
    methods: {
        hiddenFlag: function () {
            if (this.show === false) {
                this.show = true
            } else {
                this.show = false
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

        editBooking: function () {
            let formData = new FormData();
            formData.append('id', this.id);
            formData.append('productCategory', this.productCategory);
            formData.append('productSubCategory', this.productSubCategory);
            formData.append('productBrand', this.productBrand);
            formData.append('typeOfPurpose', this.typeOfPurpose);
            formData.append('description', this.description);
            formData.append('specification', this.specification);
            formData.append('totalAmount', this.totalAmount);
            formData.append('productPrice', this.productPrice);
            formData.append('file', this.file);
            formData.append('fileRightSide', this.fileRightSide);
            formData.append('fileLeftSide', this.fileLeftSide);
            formData.append('fileBackSide', this.fileBackSide);


            axios.post('/guest/booking/edit',
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }
            ).then(response => (this.editStatus = response.data.status));

            this.productCategory = ''
            this.productSubCategory = ''
            this.productBrand = ''
            this.description = ''
            this.specification = ''
            this.productPrice = ''
            this.file = null
            this.fileRightSide = null
            this.fileLeftSide = null
            this.fileBackSide = null

            let sortingTag = {
                key: 'sorting tag',
                communicationKey: this.productSubCategoryForRequest
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
        bookingAPI.get().then(result =>
            result.json().then(data =>
                data.forEach(newBooking => this.newBookings.push(newBooking))
            )
        );
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
                    window.location = 'http://91.235.128.12:8081/admin/authorize.html';
                }, 500);
            } else {
                alert('Некоректное завершение работы, попробуйте еще раз или обратитесь к супер админу');
            }
        },
        registrationUserAction: function () {
            window.location = 'http://91.235.128.12:8081/admin/registration.html';
        }
    }
})

