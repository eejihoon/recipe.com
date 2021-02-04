const eventHandler = {
    init : function () {
        document.getElementById('like').addEventListener('click', (e) => {
            e.preventDefault();
            this.likeEvent();
            this.likeCount();
        });

        document.getElementById('cancel-like').addEventListener('click', (e) => {
            e.preventDefault();
            this.cancelLike();
            this.likeCount();
        });
    },

    likeEvent : function () {
        const xhr = new XMLHttpRequest();

        let recipeId = document.getElementById('id').innerText;

        xhr.open('POST', '/like/'+recipeId);
        xhr.send();

        const _this = this;

        xhr.onload = function () {
            if (xhr.status === 200 || xhr.status === 201 ) {
                //화면 처리
                _this.likeCount();
                return;
            }
            console.log(xhr.responseText);
        }
    }, //likeEvent()

    cancelLike: function () {
        const xhr = new XMLHttpRequest();

        let recipeId = document.getElementById('id').innerText;

        xhr.open('DELETE', '/like/' + recipeId);
        xhr.send();

        const _this = this;

        xhr.onload = function () {
            if (xhr.status === 200 || xhr.status === 201) {
                //화면 처리
                document.getElementById('is-already-like').style.display = 'none';
                document.getElementById('is-not-already-like').style.display = '';
                _this.likeCount();
                return;
            }
            console.log('likecount error')
            console.log(xhr.responseText);
        }
    },

    likeCount : function () {
        const xhr = new XMLHttpRequest();

        let recipeId = document.getElementById('id').innerText;

        xhr.open('GET', '/like/'+recipeId);
        xhr.send();

        xhr.onload = function () {
            if (xhr.status === 200 || xhr.status === 201 ) {
                //화면 처리
                let likeCount = JSON.parse(xhr.responseText);
                document.getElementsByClassName('like-count')[0].innerText = likeCount[0];
                /*
                *   이미 '좋아요'를 눌렀다면 false가 반환된다.
                *   내가 좋아요를 누른 게시물이라는 것을 쉽게 확인할 수 있도록 노란색으로 바꾼다.
                *   또한 '좋아요'버튼을 다시 누르면 취소 이벤트가 발생하도록 '좋아요'버튼의 id값을 'cancel-like'으로 변경한다.
                * */
                if (likeCount[1] === 'false') {
                    // document.getElementById('like-logo').className='bi bi-bookmark-heart-fill';
                    // document.getElementById('like').setAttribute('id', 'cancel-like');
                    // document.getElementById('cancel-like').classList.add('text-warning');
                    // document.getElementById('cancel-like').classList.remove('text-secondary');
                    document.getElementById('is-not-already-like').style.display = 'none';
                    document.getElementById('is-already-like').style.display = '';
                    document.getElementsByClassName('like-count')[1].innerText = likeCount[0];
                }
                return;
            }
            console.log('likecount error')
            console.log(xhr.responseText);
        }
    }
}
eventHandler.init();
eventHandler.likeCount();