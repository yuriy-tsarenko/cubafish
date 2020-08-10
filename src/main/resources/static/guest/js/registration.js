const registrationApi = Vue.resource('/guest/registration');
let appReg = new Vue({
    el: '#appReg',
    data: {
        username: '',
        password: '',
        passwordAgain: '',
        email: '',
        userContact: '',
        responseApi: '',
        showMarker: false
    },
    methods: {
        saveUser: function () {
            let created = {
                username: this.username,
                password: this.password,
                email: this.email,
                userContact: this.userContact
            };
            if (this.password === this.passwordAgain) {
                registrationApi.save({}, created).then(response => (this.responseApi = response.data.status));
            } else {
                alert('поля "введите пароль" и "повторите пароль" не совпадают');
            }
            if (this.responseApi !== ''){
                alert(this.responseApi);
            }

        }
    }
});
