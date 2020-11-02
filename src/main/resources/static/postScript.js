
        function createPost() {
            (async () => {
                var txtField= document.getElementById("newPostText").value;
                if(txtField.length>1){
                    const rawResponse = await fetch('http://localhost:8080/api/create/post/1', {
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({"message": txtField})
                    });
                    const content = await rawResponse.json();
                    console.log(content)

                }
                else{
                    alert("Please add text to your post!")
                }
            })();
            reloadPage();
        }

        


        //TODO:This method don't work yet.
        function updateUser(id) {
            fetch('http://localhost:8080/api/update/useraccount/' + id, {
                method: 'Patch',

                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({message: "This is not my upinions"})
            })
                .then(response => {return response.json()})
                .catch(error => console.log('Error to update User'));
        }

        function createComment(userId,commentText,postId) {
            (async () => {

                const rawResponse = await fetch('http://localhost:8080/api/create/comment/'+userId+'/'+postId, {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({"message": commentText})
                }); 
            })();
            reloadPage();
        }

  
        function updatePost(postId,newPostText) {
            (async () => {

                const rawResponse = await fetch('http://localhost:8080/api/update/post/'+postId, {
                    method: 'Patch',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({"message": newPostText})
                });
            })();
            reloadPage();
        }


        function getAllPosts() {
            
            (async () => {

                const rawResponse = await fetch('http://localhost:8080/api/fetch/post/all', {
                    method: 'Get',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },

                });
                const content = await rawResponse.json();
               
              //I need to add the key of all comments in a list out side the loop
                if(content.length>0){ 
                        for(i=0; i<content.length; i++){             
                            if (isObject(content[i])==true){                      
                                postWriter = content[i].userAccount.userName;
                                postText =  content[i].message;
                                postId = "postId-"+  content[i].id.toString();
                                loadAllPosts(postWriter,postText,postId)                                 
                                if(content[i].comments.length>0){
                                    comments = removeDuplicates(content[i].comments);
                                    console.log("There is "+ content[i].comments.length + " comments on this post number " + postId )
                                    
                                    for(j=0; j<comments.length; j++){
                                            getCommentById(comments[j],postId)
                                          
                                    }      
                                }     
                             }
                            else{
                              getPostById(content[i])
                            }
                        }
                    }
            })();
        }

        function removeDuplicates(array) {
            return array.filter((a, b) => array.indexOf(a) === b)
          };

        
        function getCommentById(id,postDivId) {
           
            (async () => {

                const rawResponse = await fetch('http://localhost:8080/api/fetch/comment/' + id, {
                    method: 'Get',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }

                });
                const content = await rawResponse.json();
                
                useraccountId = content.userAccount;
                commentText =  content.message;
                commentId = "commentId-"+  content.id.toString();
                postId =   postDivId

                console.log(useraccountId,commentText,commentId,postId)  
                addUserInfoToComment(useraccountId,commentText,commentId,postId)

            })();
        }

        
        function addUserInfoToComment(useraccountId,commentText,commentId,postId) {
           
            (async () => {

                const rawResponse = await fetch('http://localhost:8080/api/fetch/useraccount/' + useraccountId, {
                    method: 'Get',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }

                });
                const content = await rawResponse.json();
                
                commentWriterName = content.userName;       
                loadAllComments(commentWriterName,commentText,commentId,postId)
            })();
        }
        function isObject(val) {
            if (val === null) { return false;}
            return ( (typeof val === 'function') || (typeof val === 'object') );
        }

        function getPostById(id) {
         
            (async () => {

                const rawResponse = await fetch('http://localhost:8080/api/fetch/post/' + id, {
                    method: 'Get',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                });
                const content = await rawResponse.json();
                if (isObject(content)==true){     
                          
                    postWriter = content.userAccount.userName;
                    postText =  content.message;
                    postId = "postId-"+  content.id.toString();
                    loadAllPosts(postWriter,postText,postId)                                 
                    if(content.comments.length>0){
                        comments = removeDuplicates(content.comments);
                        console.log("There is "+ comments.length + " comments on this post number " + postId , content )
                        
                        for(j=0; j<comments.length; j++){
                            if(isObject(comments[j])==true){
                                commentId = "commentId-"+  content.toString();
                                getCommentById(comments[j],postId)
                            }
                            else{
                                getCommentById(comments[j],postId)
                            }
                         
                        }      
                    }     
                 }
               
            })();
        }


        function deletePost(id) {
            fetch('http://localhost:8080/api/delete/post/' + id, {
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
                    reloadPage();
                };

                editIcon.onclick = function() {
                    var postToEdit= splitId(post_id);
                    var txtArea = document.createElement("TEXTAREA");
                    txtArea.innerHTML= text;
                    txtArea.style.width = "500px";
                    txtArea.id = "editedPostText"   
                    var editPostBtn = document.createElement("button");
                    editPostBtn.classList.add("btn", "btn-info","m-3");
                    editPostBtn.innerHTML="Edit";

                    editIcon.remove();
                    trashIcon.remove();
                    postText.remove();              
                    $(txtArea).insertAfter( $(postAuthor ));
                    $(editPostBtn).insertAfter( $("#editedPostText"));
                   
                    $(editPostBtn).button().click(function(){
                        updatePost(postToEdit,txtArea.innerHTML);
                       
                    }); 
               
              
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

           
            $( postText ).insertAfter( $(".allFeed" ) );

            var posts = document.getElementById(postId)
            posts.append(addCommentInputField(true,postId))
        };

        function loadAllComments(author,text,commentId,postId) {
            var commentText = addCommentElement(author,text,commentId,postId);
            var posts = document.getElementById(postId)
            
            posts.appendChild(commentText);

        };
        //Need an ajax call here
        function addComment(postId){
            var post = splitId(postId);
            inputElementId = postId + "-comment-input"
            var commentTxt = document.getElementById(inputElementId).value;
            if(commentTxt.length>1){
                createComment(1,commentTxt,post);

            }
            else{
                alert("You need to add some text!")
            }
        }


        function runLoadingSpinner(){

        }
        function reloadPage(){
            location.reload(true);
        }

        window.onload = function() {
            getAllPosts()
        };

        