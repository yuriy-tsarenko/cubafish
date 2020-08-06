const productAPI = Vue.resource('/admin_auth/products/all');
const productAPISave = Vue.resource('/admin_auth/products/create');
const productAPIEditAndDelete = Vue.resource('/admin_auth/products/{id}');
const productCategoryApi = Vue.resource('/admin_auth/products/get_menu_categories');
const productCategoryApiGetSubCategories = Vue.resource('/admin_auth/products/get_sub_categories');
const productCategoryApiGetBrands = Vue.resource('/admin_auth/products/get_product_brands');
const productAPISorted = Vue.resource('/admin_auth/products/sorted_by_category');
const productAPISortedBySubCategory = Vue.resource('/admin_auth/products/sorted_by_sub_category');
const productAPISortedByBrand = Vue.resource('/admin_auth/products/sorted_by_brand');
const productAPISortedByTypeOfPurpose = Vue.resource('/admin_auth/products/sorted_by_type_of_purpose');
Vue.http.headers.common['Authorization'] = localStorage.getItem('CustomHeader');
axios.defaults.headers.common['Authorization'] = localStorage.getItem('CustomHeader');

Vue.component('newProduct-row', {
    props: ['newProduct'],
    data: function () {
        return {
            show: false,
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
            file: null,
            editStatus: ''
        }
    },
    template:
        '<div>' +
        '<table id="fromDb" style="width:1100px; height: 170px" >' +
        '<tr>' +
        '<td id="cellStyle" hidden><h3>{{this.id=newProduct.id}}</h3></td>' +
        '<td id="cellStyle" hidden><h3>{{this.productImageName=newProduct.productImageName}}</h3></td>' +
        '<td id="cellStyle" hidden><h3>{{this.productSubCategoryForRequest=newProduct.productSubCategory}}</h3></td>' +
        '<td colspan="3" id="cellStyle" hidden><h3>{{this.productBrandForRequest=newProduct.productBrand}}</h3></td>' +
        '</tr>' +
        '<tr>' +
        '<td rowspan="2" style="alignment:center;vertical-align:center; width=200px">' +
        '<img style="width: 170px; height: 170px; border-radius:30%" :src="newProduct.productImageName" alt="photo"/>' +
        '</td>' +
        '<td rowspan="2" id="cellStyle" style="width:120px; height: auto">' +
        '<div class="productValue" id="productValue1"><h3>{{newProduct.productCategory}}</h3></br></br>' +
        '<h3>{{newProduct.productSubCategory}}</h3></div></td>' +
        '<td rowspan="2" id="cellStyle" style="width:100px; height: auto">' +
        '<div class="productValue" id="productValue2"><h3>{{newProduct.productBrand}}</h3></br></br>' +
        '<h3>{{newProduct.typeOfPurpose}}</h3></div></td>' +
        '<td rowspan="2" id="cellStyleDescription" style="width:150px; height: auto">' +
        '<div class="productValue" id="productValue3"><h3>{{newProduct.description}}</h3></div></td>' +
        '<td rowspan="2" id="cellStyleSpecification" style="width:320px; height: auto">' +
        '<div class="productValue" id="productValue4"><h3>{{newProduct.specification}}</h3></div></td>' +
        '<td rowspan="2" id="cellStyle" style="width:100px; height: auto">' +
        '<div class="productValue" id="productValue5"></br><h3>{{newProduct.productPrice}} грн</h3></br></br>' +
        '<h3>В наличии: {{newProduct.totalAmount}} ед.</h3></div></td>' +
        '<td id="cellStyle" style="width:50px; height: auto">' +
        '<input id="superAdminButtonProductTemplate" type="button" value="Изменить" v-on:click="hiddenFlag"></td>' +
        '</tr>' +
        '<tr>' +
        '<td id="cellStyle" style="width:50px; height: auto">' +
        '<input id="superAdminButtonProductTemplate" type="button" value="Удалить" v-on:click="deleteProduct"></td>' +
        '</tr>' +
        '</table>' +
        '<transition name="fade">' +
        '<table v-if="show" style="width:1100px; height: 120px; background: rgba(0,74,113,0.6)">' +
        '<tr>' +
        '<td height="30px"><h4>Выберите изображение</h4></td>' +
        '<td height="30px"><h4>Введите категорию</h4></td>' +
        '<td height="30px"><h4>Введите подкатегорию</h4></td>' +
        '<td height="30px"><h4>Введите имя бренда</h4></td>' +
        '<td height="30px"><h4>Количество товара в наличии</h4></td>' +
        '<td></td>' +
        '</tr>' +
        '<tr>' +
        '<td style="width: 150px"><input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/></td>' +
        '<td><textarea style="width:auto; height: auto" placeholder="Введите категорию" v-model="productCategory"></textarea></td>' +
        '<td><textarea style="width:auto; height: auto" placeholder="Введите подкатегорию" v-model="productSubCategory"></textarea></td>' +
        '<td><textarea style="width:auto; height: auto" placeholder="Введите имя бренда" v-model="productBrand"></textarea></td>' +
        '<td><textarea style="width:auto; height: auto" placeholder="Введите количество единиц" v-model="totalAmount"></textarea></td>' +
        '</tr>' +
        '<tr>' +
        '<td height="30px"><h4>Введите полное имя товара</h4></td>' +
        '<td height="30px"><h4>Введите описание товара</h4></td>' +
        '<td height="30px"><h4>Введите тип ловли</h4></td>' +
        '<td height="30px"><h4>Цена</h4></td>' +
        '<td></td>' +
        '</tr>' +
        '<tr>' +
        '<td><textarea style="width:auto; height: auto" placeholder="Введите полное имя товара" v-model="description"></textarea></td>' +
        '<td><textarea style="width:auto; height: auto" placeholder="Введите описание товара" v-model="specification"></textarea></td>' +
        '<td><textarea style="width: auto; height: auto" placeholder="Введите тип ловли" v-model="typeOfPurpose"></textarea></td>' +
        '<td><textarea style="width:auto; height: auto" placeholder="Цена" v-model="productPrice"></textarea></td>' +
        '<td><input id="superAdminButton" type="button"  value="Сохранить" @click="editProduct"></td>' +
        '</tr>' +
        '<tr>' +
        '<td colspan="5" height="30px"><h2>Status: {{editStatus}}</h2></td>' +
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
        handleFileUpload() {
            this.file = this.$refs.file.files[0];
        },
        editProduct: function () {
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
            if (this.file == null) {
                this.file = new Blob([], {type: 'image/png'})
                formData.delete('file');
                formData.append('file', this.file, 'no_image')
            }

            axios.post('/admin_auth/products/update',
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

            let sortingTag = {
                key: 'sorting tag',
                communicationKey: this.productSubCategoryForRequest
            }

            setTimeout(function () {
                app.newProducts = app.newProducts.splice(0, 0);
                productAPISortedBySubCategory.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(newProduct => app.newProducts.push(newProduct))
                    )
                );
            }, 1000);

            setTimeout(function () {
                appCategory.newProductCategories = appCategory.newProductCategories.splice(0, 0);
                productCategoryApi.get().then(result =>
                    result.json().then(data =>
                        data.forEach(newProductCategory => appCategory.newProductCategories.push(newProductCategory))
                    ));
            }, 500);

            setTimeout(function () {
                appSubCategory.newProductSubCategories = appSubCategory.newProductSubCategories.splice(0, 0);
                productCategoryApiGetSubCategories.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(newProductCategory =>
                            appSubCategory.newProductSubCategories.push(newProductCategory))
                    ));
            }, 500);
        },
        deleteProduct: function () {
            productAPIEditAndDelete.delete({id: this.id});
            let formDataPath = new FormData();
            formDataPath.append('path', this.productImageName);
            axios.post('/admin_auth/products/delete_product_image',
                formDataPath,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }
            ).then(response => (this.editStatus = response.data.status));

            let sortingTag = {
                key: 'sorting tag',
                communicationKey: this.productSubCategoryForRequest
            }
            let sortingTagBrand = {
                key: 'sorting tag',
                communicationKey: this.productBrandForRequest
            }

            setTimeout(function () {
                app.newProducts = app.newProducts.splice(0, 0);
                productAPISortedBySubCategory.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(newProduct => app.newProducts.push(newProduct))
                    )
                );
            }, 1000);

            setTimeout(function () {
                appCategory.newProductCategories = appCategory.newProductCategories.splice(0, 0);
                productCategoryApi.get().then(result =>
                    result.json().then(data =>
                        data.forEach(newProductCategory => appCategory.newProductCategories.push(newProductCategory))
                    ));
            }, 500);
            setTimeout(function () {
                appSubCategory.newProductSubCategories = appSubCategory.newProductSubCategories.splice(0, 0);
                productCategoryApiGetSubCategories.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(newProductCategory =>
                            appSubCategory.newProductSubCategories.push(newProductCategory))
                    ));
            }, 500);
            setTimeout(function () {
                appProductBrand.productBrands = appProductBrand.productBrands.splice(0, 0);
                productCategoryApiGetBrands.save({}, sortingTagBrand).then(result =>
                    result.json().then(data =>
                        data.forEach(newProductSubCategory => appProductBrand.productBrands.push(newProductSubCategory))
                    )
                );
            }, 500);
        }
    }
});

Vue.component('newProducts-list', {
    props: ['newProducts'],
    template: '<div>' +
        '<newProduct-row v-for="newProduct in newProducts" :key="newProduct.id" :newProduct="newProduct"/>' +
        '</div>'
});

let app = new Vue({
    el: '#app',
    template: '<newProducts-list :newProducts="newProducts" />',
    data: {
        newProducts: []
    }
});

let appNewProduct = new Vue({
    el: '#appNewProduct',
    data: {
        productCategory: '',
        productSubCategory: '',
        productBrand: '',
        totalAmount: '',
        description: '',
        specification: '',
        typeOfPurpose: '',
        productPrice: '',
        file: null,
        dataLoadStatus: ''
    },
    methods: {
        handleFileUpload() {
            this.file = this.$refs.file.files[0];
        },
        saveProduct: function () {
            let formData = new FormData();
            formData.append('productCategory', this.productCategory);
            formData.append('productSubCategory', this.productSubCategory);
            formData.append('productBrand', this.productBrand);
            formData.append('typeOfPurpose', this.typeOfPurpose);
            formData.append('description', this.description);
            formData.append('specification', this.specification);
            formData.append('totalAmount', this.totalAmount);
            formData.append('productPrice', this.productPrice);
            formData.append('file', this.file);
            if (this.file == null) {
                this.file = new Blob([], {type: 'image/png'})
                formData.delete('file');
                formData.append('file', this.file, 'no_image')
            }

            axios.post('/admin_auth/products/create',
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }
            ).then(response => (this.dataLoadStatus = response.data.status));

            let sortingTag = {
                key: 'sorting tag',
                communicationKey: this.productSubCategory,
            }

            this.productCategory = ''
            this.productSubCategory = ''
            this.productBrand = ''
            this.totalAmount = ''
            this.description = ''
            this.specification = ''
            this.typeOfPurpose = ''
            this.productPrice = ''

            setTimeout(function () {
                app.newProducts = app.newProducts.splice(0, 0);
                productAPISortedBySubCategory.save({}, sortingTag).then(result =>
                    result.json().then(data =>
                        data.forEach(newProductSubCategory => app.newProducts.push(newProductSubCategory))
                    )
                );
            }, 1000);
            setTimeout(function () {
                appCategory.newProductCategories = appCategory.newProductCategories.splice(0, 0);
                productCategoryApi.get().then(result =>
                    result.json().then(data =>
                        data.forEach(newProductCategory => appCategory.newProductCategories.push(newProductCategory))
                    ));
            }, 700);
        },
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
        '<table style="width:270px; height: 60px" >' +
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
        let auth = localStorage.getItem('CustomHeader');
        if (auth !== null) {
            setTimeout(function () {
                appCategory.newProductCategories = appCategory.newProductCategories.splice(0, 0);
                productCategoryApi.get().then(result =>
                    result.json().then(data =>
                        data.forEach(newProductCategory => appCategory.newProductCategories.push(newProductCategory))
                    ));
            }, 500);
        } else {
            setTimeout(function () {
                window.location = 'http://91.235.128.12:8081/admin/authorize.html';
            }, 200);
        }
    }
});

let appCategory = new Vue({
    el: '#appCategory',
    template: '<newProductCategories-list :newProductCategories="newProductCategories" />',
    data: {
        newProductCategories: []
    }
});

let appNewCategory = new Vue({
    props: ['newProducts'],
    el: '#appNewCategory',
    methods: {
        created: function () {
            app.newProducts = app.newProducts.splice(0, 0);
            productAPI.get().then(result =>
                result.json().then(data =>
                    data.forEach(newProduct => app.newProducts.push(newProduct))
                )
            );
        }
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
        '<table style="width:270px; height: 60px; position: relative; top: 10px" >' +
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
        '<table style="width:270px; height: 60px; position: relative; top: 10px" >' +
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
})

let appLogoutButtons = new Vue({
    el: '#appLogoutButtons',
    methods: {
        logoutUserAction: function () {
            localStorage.removeItem('CustomHeader');
            let auth = localStorage.getItem('CustomHeader')
            if (auth == null){
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

