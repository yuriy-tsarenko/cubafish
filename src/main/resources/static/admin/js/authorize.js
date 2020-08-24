const loginApi = Vue.resource('/guest/login');
let appLogin = new Vue({
    el: '#appLogin',
    data: {
        username: '',
        password: '',
        auth: ''
    },
    methods: {
        authorisation: function () {
            let created = {
                username: this.username,
                password: this.password,
            };

            loginApi.save({}, created).then(response => this.auth = response.headers.get('authorization').toString())
                .then(this.saveData);
        },

        saveData: function () {
            if (this.auth !== '' || status.ok) {
                localStorage.setItem('CustomHeader', this.auth);
                setTimeout(function () {
                    alert('вы авторизованы, перенаправление на Ваш аккаунт');
                    window.location = 'admin.html';
                }, 1000);
            } else {
                setTimeout(function () {
                    alert('вы не авторизованы, проверте правильность введенных данных');
                }, 500);
            }
        }
    }
});

