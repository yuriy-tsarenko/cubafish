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

let basketImage = new Vue({
    el: '#basketImage',
    methods: {
        activeImageAction: function () {
            window.location = 'http://91.235.128.12:8081/admin/admin_booking_manager.html';
        }
    }
});

Vue.component('newProduct-row', {
    props: ['newProduct'],
    data: function () {
        return {
            show: false,
            showCreateProduct: false,
            showBigPhoto: false,
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
            productSubCategoryForRequest: '',
            productBrandForRequest: '',
            totalAmount: '',
            description: '',
            specification: '',
            typeOfPurpose: '',
            productPrice: '',
            productImageName: '',
            productImageRightName: '',
            productImageLeftName: '',
            productImageBackName: '',
            file: null,
            fileRightSide: null,
            fileLeftSide: null,
            fileBackSide: null,
            fileForCreate: null,
            fileRightSideForCreate: null,
            fileLeftSideForCreate: null,
            fileBackSideForCreate: null,
            editStatus: '',
            productCategoryForCreate: '',
            productSubCategoryForCreate: '',
            productBrandForCreate: '',
            totalAmountForCreate: '',
            descriptionForCreate: '',
            specificationForCreate: '',
            typeOfPurposeForCreate: '',
            productPriceForCreate: '',
            dataLoadStatus: ''
        }
    },
    template:
        '<div>' +
        '<table id="fromDb" style="width:1000px; height: 270px" >' +
        '<tr>' +
        '<td id="cellStyle" hidden><p>{{this.id=newProduct.id}}</p></td>' +
        '<td id="cellStyle" hidden><p>{{this.productImageName=newProduct.productImageName}}</p></td>' +
        '<td id="cellStyle" hidden><p>{{this.productImageRightName=newProduct.productImageRightName}}</p></td>' +
        '<td id="cellStyle" hidden><p>{{this.productImageLeftName=newProduct.productImageLeftName}}</p></td>' +
        '<td id="cellStyle" hidden><p>{{this.productImageBackName=newProduct.productImageBackName}}</p></td>' +
        '<td id="cellStyle" hidden><p>{{this.productSubCategoryForRequest=newProduct.productSubCategory}}</p></td>' +
        '<td colspan="3" id="cellStyle" hidden><p>{{this.productBrandForRequest=newProduct.productBrand}}</p></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="height: 30px"><p>Основное изображение</p></td>' +
        '<td style="height: 30px"><p>Полное имя товара</p></td>' +
        '<td style="height: 30px"><p>Тип ловли</p></td>' +
        '<td style="height: 30px"><p>Бренд</p></td>' +
        '<td rowspan="5" style="width:70px;height:auto">' +
        '<input id="superAdminButtonEdit" type="button" value="Изменить" v-on:click="hiddenFlag">' +
        '<input id="superAdminButtonDelete" type="button" value="Создать" v-on:click="hiddenFlagForCreate">' +
        '<input id="superAdminButtonCreate" type="button" value="Удалить" v-on:click="deleteProduct">' +
        '</td>'+
        '</tr>' +

        '<tr>' +
        '<td rowspan="3" style="alignment:center;vertical-align:center; width=250px; height: 200px">' +
        '<img v-on:click="hiddenFlagBigPhoto" style="width: 195px; height: 250px; border-radius:30%" :src="newProduct.productImageName" alt="photo"/>' +
        '</td>' +
        '<td id="cellStyleDescription" style="width:250px; height: auto">' +
        '<div class="productValue" id="productValue1"><p>{{newProduct.description}}</p></div>' +
        '</td>' +
        '<td id="cellStyle" style="width:220px; height: auto">' +
        '<div class="productValue" id="productValue3"><p>{{newProduct.typeOfPurpose}}</p></div>' +
        '</td>' +
        '<td  id="cellStyle" style="width:220px; height: auto">' +
        '<div class="productValue" id="productValue3"><p>{{newProduct.productBrand}}</p></div>' +
        '</td>' +
        '</tr>'+

        // '<tr>' +
        // '<td rowspan="2" style="width:50px;height: auto">' +
        // '<input id="superAdminButtonProductTemplate" type="button" value="Создать" v-on:click="hiddenFlagForCreate">' +
        // '</td>'+
        // '</tr>'+

        '<tr>' +
        '<td style="height: 30px"><p>Категория</p></td>' +
        '<td style="height: 30px"><p>Подкатегория</p></td>' +
        '<td style="height: 30px"><p>Наличие и Цена</p></td>' +
        '<td style="height: 30px"></td>' +
        '</tr>' +

        '<tr>'+
        '<td rowspan="2"  id="cellStyle" style="width:250px; height: auto">' +
        '<div class="productValue" id="productValue1"><p>{{newProduct.productCategory}}</p></div>' +
        '</td>' +
        '<td rowspan="2" id="cellStyle" style="width:220px; height: auto">' +
        '<div class="productValue" id="productValue3"><p>{{newProduct.productSubCategory}}</p></div>' +
        '</td>' +
        '<td rowspan="2" id="cellStyle" style="width:220px; height: auto">' +
        '<div class="productValue" id="productValue3"><p>В наличии: {{newProduct.totalAmount}} ед.</p>' +
        '</br><p>{{newProduct.productPrice}} грн</p></div>' +
        '</td>' +
        '</tr>'+

        '<tr>' +
        '<td id="cellStyle" style="width:250px; height: 65px">' +
        '<img v-if="productImageRightName!=null" v-on:click="hiddenFlagRightSide" style="width: 65px;height: 65px; border-radius:5%" :src="newProduct.productImageRightName" alt="photo"/>' +
        '<img v-if="productImageLeftName!=null" v-on:click="hiddenFlagLeftSide" style="width: 65px;height: 65px; border-radius:5%" :src="newProduct.productImageLeftName" alt="photo"/>' +
        '<img v-if="productImageBackName!=null" v-on:click="hiddenFlagBackSide" style="width: 65px;height: 65px; border-radius:5%" :src="newProduct.productImageBackName" alt="photo"/>' +
        '</td>' +
        '</tr>'+
        '<tr>' +
        '<td colspan="5" id="cellStyle" style="width:1000px; height: 25px" >' +
        '<div class="detailsBtnColor" v-if="showDetailsButton" v-on:click="hiddenFlagDetails"><input class="btn" type="button"></div>' +
        '</td>' +
        '</tr>' +
        '</table>' +

        '<transition name="fade">' +
        '<table v-if="show" style="width:1000px; height: auto; background: rgb(208,208,208); border-right: black">' +
        '<tr>' +
        '<td style="height: 30px"><h4>Основное изображение</h4></td>' +
        '<td style="height: 30px"><h4>Вид справа</h4></td>' +
        '<td style="height: 30px"><h4>Вид слева</h4></td>' +
        '<td colspan="2" style="height: 30px"><h4>Вид сзади</h4></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="width: 225px"><input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/></td>' +
        '<td style="width: 225px"><input type="file" id="file" ref="fileRightSide" v-on:change="handleSecondFileUpload()"/></td>' +
        '<td style="width: 225px"><input type="file" id="file" ref="fileLeftSide" v-on:change="handleThirdFileUpload()"/></td>' +
        '<td colspan="2" style="width: 225px"><input type="file" id="file" ref="fileBackSide" v-on:change="handleFourthFileUpload()"/></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="height: 30px"><h4>Введите полное имя товара</h4></td>' +
        '<td style="height: 30px"><h4>Введите описание товара</h4></td>' +
        '<td style="height: 30px"><h4>Введите тип ловли</h4></td>' +
        '<td colspan="2" style="height: 30px"><h4>Введите имя бренда</h4></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="width:225px;height: auto"><textarea  placeholder="Введите полное имя товара" v-model="description"></textarea></td>' +
        '<td style="width:225px;height: auto"><textarea  placeholder="Введите описание товара" v-model="specification"></textarea></td>' +
        '<td style="width:225px;height: auto"><textarea  placeholder="Введите тип ловли" v-model="typeOfPurpose"></textarea></td>' +
        '<td colspan="2" style="width:225px;height: auto"><textarea placeholder="Введите имя бренда" v-model="productBrand"></textarea></td>' +

        '</tr>' +
        '<tr>' +
        '<td style="height: 30px"><h4>Введите категорию</h4></td>' +
        '<td style="height: 30px"><h4>Введите подкатегорию</h4></td>' +
        '<td style="height: 30px"><h4>Количество товара в наличии</h4></td>' +
        '<td colspan="2" style="height: 30px"><h4>Цена</h4></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Введите категорию" v-model="productCategory"></textarea></td>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Введите подкатегорию" v-model="productSubCategory"></textarea></td>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Введите количество единиц" v-model="totalAmount"></textarea></td>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Цена" v-model="productPrice"></textarea></td>' +
        '<td style="width:100px; height: auto"><input id="superAdminButton" type="button"  value="Сохранить" @click="editProduct"></td>' +
        '</tr>' +

        '<tr>' +
        '<td colspan="5" height="30px"><h2>Status: {{editStatus}}</h2></td>' +
        '</tr>' +
        '</table>' +
        '</transition>' +

        '<transition name="fade">' +
        '<div v-if="showBigPhoto" id="window" v-on:click="hiddenFlagBigPhoto">' +
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
        '<td id="cellStyle" style="height: 30px; width: 900px"><p>Описание</p>' +
        '</td>' +
        '</tr>'+
        '<tr>'+
        '<td>' +
        '<div id="productValue2"><br/><p>{{newProduct.specification}}</p></div>' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td style="height: 30px">' +
        '<div class="detailsBtnColor" v-on:click="hiddenFlagDetails"><input class="btnUp" type="button"></div>' +
        '</td>' +
        '</tr>' +
        '</table>' +
        '</transition>' +

        '<transition name="fade">' +
        '<table v-if="showCreateProduct" class="tableCreate">' +
        '<tr>' +
        '<td style="height: 30px"><h4>Основное изображение</h4></td>' +
        '<td style="height: 30px"><h4>Вид справа</h4></td>' +
        '<td style="height: 30px"><h4>Вид слева</h4></td>' +
        '<td colspan="2" style="height: 30px"><h4>Вид сзади</h4></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="width: 225px"><input type="file" id="file" ref="fileForCreate" v-on:change="handleFileUploadForCreate()"/></td>' +
        '<td style="width: 225px"><input type="file" id="file" ref="fileRightSideForCreate" v-on:change="handleSecondFileUploadForCreate()"/></td>' +
        '<td style="width: 225px"><input type="file" id="file" ref="fileLeftSideForCreate" v-on:change="handleThirdFileUploadForCreate()"/></td>' +
        '<td colspan="2" style="width: 225px"><input type="file" id="file" ref="fileBackSideForCreate" v-on:change="handleFourthFileUploadForCreate()"/></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="height: 30px"><h4>Введите полное имя товара</h4></td>' +
        '<td style="height: 30px"><h4>Введите описание товара</h4></td>' +
        '<td style="height: 30px"><h4>Введите тип ловли</h4></td>' +
        '<td colspan="2" style="height: 30px"><h4>Введите имя бренда</h4></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="width:225px;height: auto"><textarea  placeholder="Введите полное имя товара" v-model="descriptionForCreate"></textarea></td>' +
        '<td style="width:225px;height: auto"><textarea  placeholder="Введите описание товара" v-model="specificationForCreate"></textarea></td>' +
        '<td style="width:225px;height: auto"><textarea  placeholder="Введите тип ловли" v-model="typeOfPurposeForCreate"></textarea></td>' +
        '<td colspan="2" style="width:225px;height: auto"><textarea placeholder="Введите имя бренда" v-model="productBrandForCreate"></textarea></td>' +

        '</tr>' +
        '<tr>' +
        '<td style="height: 30px"><h4>Введите категорию</h4></td>' +
        '<td style="height: 30px"><h4>Введите подкатегорию</h4></td>' +
        '<td style="height: 30px"><h4>Количество товара в наличии</h4></td>' +
        '<td colspan="2" style="height: 30px"><h4>Цена</h4></td>' +
        '</tr>' +

        '<tr>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Введите категорию" v-model="productCategoryForCreate"></textarea></td>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Введите подкатегорию" v-model="productSubCategoryForCreate"></textarea></td>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Введите количество единиц" v-model="totalAmountForCreate"></textarea></td>' +
        '<td style="width:200px; height: auto"><textarea placeholder="Цена" v-model="productPriceForCreate"></textarea></td>' +
        '<td style="width:100px; height: auto"><input id="superAdminButton" type="button"  value="Сохранить" @click="saveProduct"></td>' +
        '</tr>' +

        '<tr>' +
        '<td colspan="5" height="30px"><h2>Status: {{dataLoadStatus}}</h2></td>' +
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
        hiddenFlagForCreate: function () {
            if (this.showCreateProduct === false) {
                this.showCreateProduct = true
            } else {
                this.showCreateProduct = false
            }
        },
        hiddenFlagBigPhoto: function () {
            if (!(this.showBigPhoto === true) && !(this.showImage === true)) {
                this.showBigPhoto = true;
                this.showImage = true;
            } else {
                this.showBigPhoto = false;
                this.showImage = false;
                this.showRightSide = false;
                this.showLeftSide = false;
                this.showBackSide = false;
            }
        },
        hiddenFlagRightSide: function () {
            if (!(this.showBigPhoto === true) && !(this.showRightSide === true)) {
                this.showBigPhoto = true;
                this.showRightSide = true;
            } else {
                this.showBigPhoto = false;
                this.showRightSide = false;
            }
        },
        hiddenFlagLeftSide: function () {
            if (!(this.showBigPhoto === true) && !(this.showLeftSide === true)) {
                this.showBigPhoto = true;
                this.showLeftSide = true;
            } else {
                this.showBigPhoto = false;
                this.showLeftSide = false;
            }
        },
        hiddenFlagBackSide: function () {
            if (!(this.showBigPhoto === true) && !(this.showBackSide === true)) {
                this.showBigPhoto = true;
                this.showBackSide = true;
            } else {
                this.showBigPhoto = false;
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
        handleFileUpload() {
            this.file = this.$refs.file.files[0];
        },
        handleSecondFileUpload() {
            this.fileRightSide = this.$refs.fileRightSide.files[0];
        },
        handleThirdFileUpload() {
            this.fileLeftSide = this.$refs.fileLeftSide.files[0];
        },
        handleFourthFileUpload() {
            this.fileBackSide = this.$refs.fileBackSide.files[0];
        },
        handleFileUploadForCreate() {
            this.fileForCreate = this.$refs.fileForCreate.files[0];
        },
        handleSecondFileUploadForCreate() {
            this.fileRightSideForCreate = this.$refs.fileRightSideForCreate.files[0];
        },
        handleThirdFileUploadForCreate() {
            this.fileLeftSideForCreate = this.$refs.fileLeftSideForCreate.files[0];
        },
        handleFourthFileUploadForCreate() {
            this.fileBackSideForCreate = this.$refs.fileBackSideForCreate.files[0];
        },
        saveProduct: function () {
            let formData = new FormData();
            formData.append('productCategory', this.productCategoryForCreate);
            formData.append('productSubCategory', this.productSubCategoryForCreate);
            formData.append('productBrand', this.productBrandForCreate);
            formData.append('typeOfPurpose', this.typeOfPurposeForCreate);
            formData.append('description', this.descriptionForCreate);
            formData.append('specification', this.specificationForCreate);
            formData.append('totalAmount', this.totalAmountForCreate);
            formData.append('productPrice', this.productPriceForCreate);
            formData.append('file', this.fileForCreate);
            formData.append('fileRightSide', this.fileRightSideForCreate);
            formData.append('fileLeftSide', this.fileLeftSideForCreate);
            formData.append('fileBackSide', this.fileBackSideForCreate);
            if (this.fileForCreate === null) {
                formData.delete('file');
                this.fileForCreate = new Blob([], {type: 'image/png'})
                formData.append('file', this.fileForCreate, 'no_image')
            }
            if (this.fileRightSideForCreate === null) {
                formData.delete('fileRightSide');
                this.fileRightSideForCreate = new Blob([], {type: 'image/png'})
                formData.append('fileRightSide', this.fileRightSideForCreate, 'no_image')
            }
            if (this.fileLeftSideForCreate === null) {
                formData.delete('fileLeftSide');
                this.fileLeftSideForCreate = new Blob([], {type: 'image/png'})
                formData.append('fileLeftSide', this.fileLeftSideForCreate, 'no_image')
            }
            if (this.fileBackSideForCreate === null) {
                formData.delete('fileBackSide');
                this.fileBackSideForCreate = new Blob([], {type: 'image/png'})
                formData.append('fileBackSide', this.fileBackSideForCreate, 'no_image')
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
            this.file = null
            this.fileRightSide = null
            this.fileLeftSide = null
            this.fileBackSide = null

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
            formData.append('file', this.file);
            formData.append('fileRightSide', this.fileRightSide);
            formData.append('fileLeftSide', this.fileLeftSide);
            formData.append('fileBackSide', this.fileBackSide);

            if (this.file === null) {
                formData.delete('file');
                this.file = new Blob([], {type: 'image/png'})
                formData.append('file', this.file, 'no_image')
            }
            if (this.fileRightSide === null) {
                formData.delete('fileRightSide');
                this.fileRightSide = new Blob([], {type: 'image/png'})
                formData.append('fileRightSide', this.fileRightSide, 'no_image')
            }
            if (this.fileLeftSide === null) {
                formData.delete('fileLeftSide');
                this.fileLeftSide = new Blob([], {type: 'image/png'})
                formData.append('fileLeftSide', this.fileLeftSide, 'no_image')
            }
            if (this.fileBackSide === null) {
                formData.delete('fileBackSide');
                this.fileBackSide = new Blob([], {type: 'image/png'})
                formData.append('fileBackSide', this.fileBackSide, 'no_image')
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
            this.file = null
            this.fileRightSide = null
            this.fileLeftSide = null
            this.fileBackSide = null

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
            formDataPath.append('pathImage', this.productImageName);
            formDataPath.append('pathImageRight', this.productImageRightName);
            formDataPath.append('pathImageLeft', this.productImageLeftName);
            formDataPath.append('pathImageBack', this.productImageBackName);

            if (this.productImageName === null) {
                formDataPath.delete('pathImage');
                this.productImageName = 'no_path';
                formDataPath.append('pathImage', this.productImageName);
            }
            if (this.productImageRightName === null) {
                formDataPath.delete('pathImageRight');
                this.productImageRightName = 'no_path';
                formDataPath.append('pathImageRight', this.productImageRightName);
            }
            if (this.productImageLeftName === null) {
                formDataPath.delete('pathImageLeft');
                this.productImageLeftName = 'no_path';
                formDataPath.append('pathImageLeft', this.productImageLeftName);
            }
            if (this.productImageBackName === null) {
                formDataPath.delete('pathImageBack');
                this.productImageBackName = 'no_path';
                formDataPath.append('pathImageBack', this.productImageBackName);
            }
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

// let appNewCategory = new Vue({
//     props: ['newProducts'],
//     el: '#appNewCategory',
//     methods: {
//         created: function () {
//             app.newProducts = app.newProducts.splice(0, 0);
//             productAPI.get().then(result =>
//                 result.json().then(data =>
//                     data.forEach(newProduct => app.newProducts.push(newProduct))
//                 )
//             );
//         }
//     }
// });

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

