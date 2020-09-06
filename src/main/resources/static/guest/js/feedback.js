const feedbackAPILeaveFeedBack = Vue.resource('/guest/feedback/create');
const feedbackAPIGetFeedBacks = Vue.resource('/guest/feedback/all');

let wrapperFooter = new Vue({
    el: '#wrapperFooter',
    methods: {
        anotherAction: function () {
            alert('Просим извинения, но эта функция сайта еще в разработке')
        }
    }
});

Vue.component('newFeedback-row', {
    props: ['newFeedback'],
    data: function () {
        return {
            recommendation: ''
        }
    },
    template:
        '<div>' +
        '<table id="feedBackTracker">' +
        '<tr>' +
        '<td hidden>{{this.recommendation=newFeedback.recommendation}}</td>' +
        '</tr>' +
        '    <tr>' +
        '        <td rowspan="4" style="width: 200px">' +
        '            <img src="css/images/noAvatar.png" style="height: 160px; border-radius: 10px" alt="photo">' +
        '        </td>' +
        '        <td style="height: 50px; width: 200px">{{newFeedback.userName}} {{newFeedback.userLastName}}</td>' +
        '        <td rowspan="4" style="width: 900px; height: 200px; text-align: left">' +
        '            <p style="margin: 10px">{{newFeedback.comment}}</p>' +
        '        </td>' +
        '    </tr>' +
        '    <tr>' +
        '        <td style="width: 200px; height: 40px">' +
        '         {{newFeedback.dateOfComment}}' +
        '        </td>' +
        '    </tr>' +
        '    <tr>' +
        '        <td style="height: 70px; width: 200px; text-align: left">' +
        '            <div style="position: relative; height: 70px">' +
        '                <table v-if="(this.recommendation===true)" style="width: 200px"><tr>' +
        '                    <td><img src="css/images/like.png" style="height: 70px" alt="photo"></td>' +
        '                    <td style="text-align: left">Рекомендую</td>' +
        '                </tr></table>' +
        '                <table v-if="(this.recommendation===false)" style="width: 200px"><tr>' +
        '                    <td><img src="css/images/disLike.png" style="height: 70px" alt="photo"></td>' +
        '                    <td style="text-align: left">Не рекомендую</td>' +
        '                </tr></table>' +
        '            </div>' +
        '        </td>' +
        '    </tr>' +
        '    <tr>' +
        '        <td style="width: 200px; height: 40px">Оценка {{newFeedback.mark}} из 10</td>' +
        '    </tr>' +
        '</table>' +
        '</div>'
});

Vue.component('newFeedbacks-list', {
    props: ['newFeedbacks'],
    template: '<div>' +
        '<newFeedback-row v-for="newFeedback in newFeedbacks" :key="newFeedback.id" :newFeedback="newFeedback"/>' +
        '</div>',
    created: function () {
        feedbackAPIGetFeedBacks.get().then(result =>
            result.json().then(data =>
                data.forEach(newFeedback => this.newFeedbacks.push(newFeedback))
            )
        );
    }
});

let appFeedback = new Vue({
    el: '#appFeedback',
    template: '<newFeedbacks-list :newFeedbacks="newFeedbacks" />',
    data: {
        newFeedbacks: []
    }
});

let appLeaveFeedback = new Vue({
    el: '#appLeaveFeedback',
    data: {
        userName: '',
        userLastName: '',
        comment: '',
        recommendation: '',
        mark: '',
        dataLoadStatus: ''
    },
    methods: {
        leaveFeedback: function () {
            let array = this.recommendation.toString().split(' ');
            let feedbackDto = {
                userName: this.userName,
                userLastName: this.userLastName,
                comment: this.comment,
                recommendation: '',
                mark: this.mark
            };
            if (array.length > 1) {
                feedbackDto.recommendation = false;
            } else {
                feedbackDto.recommendation = true;
            }
            if ((feedbackDto.userName !== '') && (feedbackDto.userLastName !== '') && (feedbackDto.comment !== '')
                && (feedbackDto.recommendation !== '') && (feedbackDto.mark !== '')) {
                if (feedbackDto.comment.toString().length < 1000) {
                    feedbackAPILeaveFeedBack.save({}, feedbackDto).then(response => (this.dataLoadStatus = response.data.status));
                    setTimeout(function () {
                        appFeedback.newFeedbacks = appFeedback.newFeedbacks.splice(0, 0);
                        feedbackAPIGetFeedBacks.get().then(result =>
                            result.json().then(data =>
                                data.forEach(newFeedback => appFeedback.newFeedbacks.push(newFeedback))
                            ));
                    }, 400);
                    this.userName = ''
                    this.userLastName = ''
                    this.comment = ''
                    this.recommendation = ''
                    this.mark = ''
                    this.dataLoadStatus = ''
                } else {
                    alert('Ваш коментарий имеет болше 1000 символов (включительно с пробелами)');
                }
            } else {
                alert('Все поля обязательны к заполнению');
            }
        }
    }
});

