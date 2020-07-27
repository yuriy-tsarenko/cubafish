const loginApi = Vue.resource('/login');
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
            setTimeout(function () {
                alert('вы авторизованы, перенаправление на Ваш аккаунт');
                window.location = 'http://localhost:8080/super_admin/super_admin.html';
            }, 1000);
        },
        saveData: function () {
            if (this.auth !== '') {
                localStorage.setItem('CustomHeader', this.auth);
            }

        }
    }
});

