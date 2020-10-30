
let userInfo;
let stack=[];
let everyThingUpdated=false;
let inlogged= false;

function getAllUsers() {
    fetch('http://noticeboardapplication.herokuapp.com/api/fetch/useraccount/all')
        .then(response => response.json())
        .then(data => console.log(data));
}


function createPosts() {
    fetch('http://noticeboardapplication.herokuapp.com/api/create/post', {
        method: 'Post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: 4, message: "Everyone disagree with you. ", postId: 2, userAccountId: 3})
    })
        .then(response => {return response.json()})
        .catch(error => error= console.log('The Post was not added'));
}

function getUserById(id) {
    return fetch('http://noticeboardapplication.herokuapp.com/api/fetch/useraccount/' + id)
        .then(response => response.json())
        .then(data => data);
}

//TODO:This method don't work yet.
function updateUser(id) {
    fetch('http://noticeboardapplication.herokuapp.com/api/update/useraccount/' + id, {
        method: 'Patch',

        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({message: "This is not my upinions"})
    })
        .then(response => {return response.json()})
        .catch(error => console.log('Error to update User'));
}

//commentId ska läggas till
// function createComment(userId,commentText,postId) {
//     fetch('http://localhost:8080/api/create/comment/'+userId+'/'+postId, {
//         method: 'Post',
//         headers: {
//             'Accept': 'application/json',
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({message: commentText})
//     })
//     .then(response => {return response.json()})
//     .catch(error => error= console.log('The Post was not added'));
//     }

function createComment(userId,commentText,postId) {
    (async () => {
        console.log(userId,postId)
        const rawResponse = await fetch('http://noticeboardapplication.herokuapp.com/api/create/comment/'+userId+'/'+postId, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({"message": commentText})
        });
        const content = await rawResponse.json();

        console.log(content);
    })();
}

function getAllPosts() {
    fetch('http://noticeboardapplication.herokuapp.com/api/fetch/post/all').then(response =>
        response.json().then(data => ({
                data: data,
                status: response.status
            })
        ).then(res => {
            if(res.status=200){
                postWriter = res.data[0].userAccount.userName;
                postText =  res.data[0].message;
                postId = "postId-"+  res.data[0].id.toString();



                loadAllPosts(postWriter,postText,postId);
                if(res.data.length>1){

                    for(i=1; i<res.data.length; i++){
                        console.log(res.data[i])
                        getPostById(res.data[i])

                    }
                }
            }
        }));
}

function getAllComments() {
    fetch('http://noticeboardapplication.herokuapp.com/api/fetch/comment/all').then(response =>
        response.json().then(data => ({
                data: data,
                status: response.status
            })
        ).then(res => {
            if(res.status=200){
                if(res.data.length>0){

                    console.log("All comments",res.data)
                    commentWriterId = res.data[0].userAccount;
                    commentText =  res.data[0].message;
                    commentId = "commentId-"+  res.data[0].id.toString();
                    postId =    "postId-"+  res.data[0].post.id.toString();

                    addUserInfoToComment(commentWriterId,commentText,commentId,postId);

                    if(res.data.length>1){
                        for(i=1; i<res.data.length; i++){
                            getCommentById(res.data[i])

                        }
                    }
                }
            }
        }));
}

function addUserInfoToComment(userId,commentText,commentId,postId) {
    return fetch('http://noticeboardapplication.herokuapp.com/api/fetch/useraccount/' + userId)
        .then(response =>
            response.json().then(data => ({
                    data: data,
                    status: response.status
                })
            ).then(res => {
                if(res.status=200){
                    loadAllComments(res.data.userName,commentText,commentId,postId)
                }
            }));
}


function getCommentById(id, commentText,commentId,postId) {
    fetch('http://noticeboardapplication.herokuapp.com/api/fetch/comment/' + id).then(response =>
        response.json().then(data => ({
                data: data,
                status: response.status
            })
        ).then(res => {
            if(res.status=200){
                commentWriterId = res.data.userAccount;
                commentText =  res.data.message;
                commentId = "commentId-"+  res.data.id.toString();
                postId =    "postId-"+  res.data.post.id.toString();

                addUserInfoToComment(commentWriterId,commentText,commentId,postId);
            }
        }));
}


function getPostById(id) {

    fetch('http://noticeboardapplication.herokuapp.com/api/fetch/post/' + id).then(response =>
        response.json().then(data => ({
                data: data,
                status: response.status
            })
        ).then(res => {
            if(res.status=200){
                postWriter = res.data.userAccount.userName;
                postText =  res.data.message;
                postId = "postId-"+  res.data.id.toString();

                console.log(postWriter,postText,postId)
                loadAllPosts(postWriter,postText,postId);
            }
        }));
}

//TODO:This method don't work yet.
function updatePost(id) {
    fetch('http://noticeboardapplication.herokuapp.com/api/update/post/' + id, {
        method: 'Patch',

        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({message: "This is not my upinions"})
    })
        .then(response => {return response.json()})
        .catch(error => console.log('Error to update Post'));
}

function deletePost(id) {
    fetch('http://noticeboardapplication.herokuapp.com/api/delete/post/' + id, {
        method: 'Delete',
    })

        .catch(error => console.log('The Post was not deleted'));
}

function addPostElement (author,text,post_id,autor_inlogged) {

    var newPost = document.createElement("div");
    newPost.classList.add("ui","card","container","m-4");
    newPost.style.width = "800px";
    newPost.id= post_id;

    var newPostContent = document.createElement("div");
    newPostContent.classList.add("content");
    newPostContent.id = "post-text"
    newPostContent.style.backgroundColor= "#efeff5"

    var postAuthor = document.createElement("h3");
    postAuthor.classList.add("post-author");
    postAuthor.innerHTML = author;

    var postText = document.createElement("h5");
    postText.classList.add("post-text");
    postText.innerHTML = text;

    newPost.appendChild(newPostContent);
    newPostContent.appendChild(postAuthor);
    newPostContent.appendChild(postText);

    if(autor_inlogged==true){
        var trashIcon = document.createElement("i");
        trashIcon.classList.add("trash","icon","ml-3");
        trashIcon.style.cursor = 'pointer';

        var editIcon = document.createElement("i");
        editIcon.classList.add("edit","icon","ml-2");
        editIcon.style.cursor = 'pointer';

        newPostContent.appendChild(editIcon)
        newPostContent.appendChild(trashIcon)

        trashIcon.onclick = function() {
            var postToDeleteId= splitId(post_id);
            deletePost(postToDeleteId);
            getAllPosts();
        };
    }
    return newPost;
}

function splitId(postId){
    var splitedPostId= postId.split('-');
    var postId = parseInt(splitedPostId[1]);

    return postId;
}

function addCommentInputField(inlogged,postId){
    var extraContent = document.createElement("div");
    extraContent.classList.add("extra","content");

    var commentInput = document.createElement("div");
    commentInput.classList.add("ui","larg","transparent","left","icon","input","fluid");

    var textField = document.createElement("input");
    textField.id=postId+"-comment-input"
    textField.placeholder = "Add comment here..";

    var commentButton = document.createElement("button");
    commentButton.classList.add("btn","btn-primary");
    commentButton.textContent = 'Add comment';

    commentButton.onclick = function() {
        addComment(postId);
    };

    if(inlogged==true){
        extraContent.appendChild(commentInput)
        commentInput.appendChild(textField)
        commentInput.appendChild(commentButton)
    }

    return extraContent;
}

function addCommentElement (commentWriter, commentText,comment_Id,post_Id) {
    var formatedPostCommentId = "formated-"+post_Id;

    var postCommentId = document.createElement("div");
    postCommentId.classList.add("content", "m-2");
    postCommentId.id= formatedPostCommentId;
    postCommentId.style.backgroundColor= "#f0f5f5";

    var commentAuthor = document.createElement("h5");
    commentAuthor.classList.add("comment-author");
    commentAuthor.id=comment_Id;
    commentAuthor.innerHTML= commentWriter;

    var newCommentText = document.createElement("p");
    newCommentText.classList.add("comment-text");
    newCommentText.innerText = commentText;

    var extraContent = document.createElement("div");
    extraContent.classList.add("extra","content");

    postCommentId.appendChild(commentAuthor);
    postCommentId.appendChild(newCommentText);


    return postCommentId;
}

function loadingSpinner(start=false){

    var div = document.createElement("div");
    div.style.marginTop="20%";
    div.style.marginLeft= "50%"
    div.style.width ="100%";
    div.style.height="100%";
    var spinner = document.createElement("div")
    spinner.classList.add("spinner-border","text-info")
    var span = document.createElement("span");

    div.appendChild(spinner);
    spinner.appendChild(span);
    $(".main").append(div);
}

function loadAllPosts(author,text,postId) {
    var postText = addPostElement(author,text,postId,true);
    stack.push(postText)

    $(".allFeed").append(postText);

    var posts = document.getElementById(postId)
    posts.append(addCommentInputField(true,postId))
};

function loadAllComments(author,text,commentId,postId,username) {
    var commentText = addCommentElement(author,text,commentId,postId,username);
    var posts = document.getElementById(postId)

    posts.appendChild(commentText);

};
//Need an ajax call here
function addComment(postId){
    var post = splitId(postId);
    inputElementId = postId + "-comment-input"
    var commentTxt = document.getElementById(inputElementId).value;
    if(commentTxt.length>1){
        createComment(12,commentTxt,post);

    }
    else{
        alert("You need to add some text!")
    }
}


function runLoadingSpinner(){

}

window.onload = function() {
    getAllPosts()
    getAllComments();

};