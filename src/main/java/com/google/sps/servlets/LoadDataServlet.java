// Copyright 2019 Google LLC
//  
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.sps.servlets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.google.appengine.api.datastore.*;
import com.google.sps.data.User;
import com.google.sps.data.Users;
import com.google.sps.data.EditComment;
import com.google.gson.Gson;
import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data */
/* TO DO (DEUS):   Use the actual EditComment Class as soon as David pushes some code*/
@WebServlet("/load-data")
public class LoadDataServlet extends HttpServlet {
  String USER_PROFILE_DATASTORE_ENTITY_NAME = "userProfileEntity";


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get request json
    StringBuffer stringBuffer = new StringBuffer();
    String line = null;
    try {
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null){
            stringBuffer.append(line);
        } 
    } catch (Exception e) { System.out.println("EXCEPTION: \t" + e);}

    String json = stringBuffer.toString();
    System.out.println(json);

    // TO DO (Maybe David): parse json to add EditComments and store them


    // TO DO (Deus): parse json and use David's work to add and edit UserProfiles and store them
    loadUserToDatastore();

    // Redirect back to the HTML page.
    response.sendRedirect("index.html");
  }

  private void loadUserToDatastore() {

    Collection<EditComment> listEditComments = new ArrayList<EditComment>();
    EditComment ec1 = new EditComment("82141", "Tom", "Your comment is pretty ignorant.","66.55", "September, 5 2019, 12:40","incivility", "pending");
    EditComment ec2 = new EditComment("13513", "Tom", "Your are the worst!","83.79", "September, 6 2019, 21:09","incivility", "pending");
    listEditComments.add(ec1);
    listEditComments.add(ec2);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Entity userProfileEntity = new Entity("UserProfile", "Tom");
    userProfileEntity.setProperty("userName", "Tom");
    ArrayList<EmbeddedEntity> listEditCommentsEntity = new ArrayList<EmbeddedEntity>();
    for (EditComment ec : listEditComments) {
      EmbeddedEntity editCommentEntity = createEmbeddedEntity(ec);
      listEditCommentsEntity.add(editCommentEntity);
    }
    userProfileEntity.setProperty("listEditComments", listEditCommentsEntity);
    datastore.put(userProfileEntity);
  }

  private EmbeddedEntity createEmbeddedEntity(EditComment editComment) {
    EmbeddedEntity editCommentEntity = new EmbeddedEntity();

    editCommentEntity.setProperty("userName", editComment.getUserName());
    editCommentEntity.setProperty("comment", editComment.getComment());
    editCommentEntity.setProperty("date", editComment.getDate());
    editCommentEntity.setProperty("parentArticle", editComment.getParentArticle());
    editCommentEntity.setProperty("status", editComment.getStatus());
    editCommentEntity.setProperty("revisionID", editComment.getRevisionId());
    editCommentEntity.setProperty("toxicityObject", editComment.getToxicityObject());

    return editCommentEntity;
  }
}