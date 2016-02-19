<%@ page import="grails.plugin.springsecurity.SpringSecurityService" %>
<%@ page import="grails.plugin.springsecurity.SecurityTagLib" %>


<!doctype html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Welcome to Grails</title>
      <style type="text/css" media="screen">
          #status {
              background-color: #eee;
              border: .2em solid #fff;
              margin: 2em 2em 1em;
              padding: 1em;
              width: 12em;
              float: left;
              -moz-box-shadow: 0px 0px 1.25em #ccc;
              -webkit-box-shadow: 0px 0px 1.25em #ccc;
              box-shadow: 0px 0px 1.25em #ccc;
              -moz-border-radius: 0.6em;
              -webkit-border-radius: 0.6em;
              border-radius: 0.6em;
          }

          #status ul {
              font-size: 0.9em;
              list-style-type: none;
              margin-bottom: 0.6em;
              padding: 0;
          }

          #status li {
              line-height: 1.3;
          }

          #status h1 {
              text-transform: uppercase;
              font-size: 1.1em;
              margin: 0 0 0.3em;
          }

          #page-body {
              margin: 2em 1em 1.25em 18em;
          }

          h2 {
              margin-top: 1em;
              margin-bottom: 0.3em;
              font-size: 1em;
          }

          p {
              line-height: 1.5;
              margin: 0.25em 0;
          }

          #controller-list ul {
              list-style-position: inside;
          }

          #controller-list li {
              line-height: 1.3;
              list-style-position: inside;
              margin: 0.25em 0;
          }

          @media screen and (max-width: 480px) {
              #status {
                  display: none;
              }

              #page-body {
                  margin: 0 1em 1em;
              }

              #page-body h1 {
                  margin-top: 0;
              }
          }
      </style>
    </head>
<body>

<g:render template="/home/header" />

<g:set var="service" value="${new SpringSecurityService()}" />

	<h1> ${question.title} </h1> </a>
		<p> ${question.text} </p>
	<a href="<g:createLink controller="User" action="displayUser" params="[userId : question.secureUser.id]" />">  <p> <b> ${question.secureUser.username} </b> </p> </a>
  <g:if test="${service.getCurrentUserId() == question.secureUser.id}">
  <a href="<g:createLink controller="Question" action="editQuestion" params="[questionId : question.id]" />"> <p> <b> Edit </b> </p> </a>

</g:if>
<p> <b> <g:formatDate format="MM/dd HH:mm" date="${question.date}" /> </b> </p>

  <g:each var="answer" in="${question.answers.sort{-it.mark}}">

			<p> ${answer.text} </p>
			<a href="<g:createLink controller="User" action="displayUser" params="[userId : question.secureUser.id]" />"> <b> ${answer.secureUser.username} </b> </a>
      <p> <b> <g:formatDate format="MM/dd HH:mm" date="${answer.date}" /> </b> </p>
      <sec:ifLoggedIn>
      <a href="<g:createLink controller="Answer" action="upvote" params="[userId : service.getCurrentUserId(), answerId : answer.id]" />"> <b> <g:img dir="images" file="upvote.png" width="40" height="40"/> </b> </a>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <b> <g:img dir="images" file="upvote.png" width="40" height="40"/> </b>
    </sec:ifNotLoggedIn>
      <p> ${answer.mark} </p>
      <sec:ifLoggedIn>
      <a href="<g:createLink controller="Answer" action="downvote" params="[userId : service.getCurrentUserId(), answerId : answer.id]" />"> <b> <g:img dir="images" file="downvote.png" width="40" height="40"/> </b> </a>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <b> <g:img dir="images" file="downvote.png" width="40" height="40"/> </b>
    </sec:ifNotLoggedIn>
      <g:if test="${service.getCurrentUserId() == answer.secureUser.id}">
  <a href="<g:createLink controller="Answer" action="editAnswer" params="[answerId : answer.id]" />"> <p> <b> Edit </b> </p> </a>

</g:if>
	</g:each>
  <sec:ifLoggedIn>

      <g:if test="${question.isLocked == false}">

        <g:form name="answeringForm" controller="Question" action="addAnswer" >
          <g:hiddenField name="questionId" value="${question.id}" />
          <g:hiddenField name="userId" value="${question.secureUser.id}" />
        <g:textArea name="answerField" rows="10" cols="80" />
        <g:actionSubmit value="Post" action="addAnswer" />
      </g:form>
      </g:if>

      <sec:ifAllGranted roles='ROLE_ADMIN'>

          <g:if test="${question.isLocked == false}">
                <g:form name="lockForm" controller="Question" action="lock" params="[questionId : question.id]" >
                    <g:actionSubmit value="Lock" action="lock" params="[questionId : question.id]" />
                </g:form>
          </g:if>
          <g:else>
                <g:form name="lockForm" controller="Question" action="unlock" params="[questionId : question.id]" >
                    <g:actionSubmit value="Unlock" action="unlock" params="[questionId : question.id]" />
                </g:form>
          </g:else>
      </sec:ifAllGranted>
</sec:ifLoggedIn>
</body>
</html>
