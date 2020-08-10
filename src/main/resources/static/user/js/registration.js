const registrationApi = Vue.resource('/guest/registration');
let appReg = new Vue({
    el: '#appReg',
    data: {
        username: '',
        password: '',
        email: '',
        userContact: '',
        responseApi: ''
    },
    methods: {
        saveUser: function () {
            let created = {
                username: this.username,
                password: this.password,
                email: this.email,
                userContact: this.userContact
            };
            registrationApi.save({}, created).then(response => (this.responseApi = response.data.status));
        }
    }
});
