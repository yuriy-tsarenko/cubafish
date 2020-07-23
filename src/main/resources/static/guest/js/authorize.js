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
            window.location = 'http://localhost:8080/super_admin/super_admin.html'
        },
        saveData: function () {
            if (this.auth !== '') {
                localStorage.setItem('CustomHeader', this.auth);
            }

        }
    }
});

