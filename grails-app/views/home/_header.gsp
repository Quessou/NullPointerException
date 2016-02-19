<%@ page import="grails.plugin.springsecurity.SecurityTagLib" %>


<a href="<g:createLink controller="Home" action="index" />"> Home </a>

<sec:ifNotLoggedIn>
<a href="<g:createLink controller="Register"  />"> <h1> <b> Register </b> </h1> </a>
<a href="<g:createLink controller="login"  />"> <h1> <b> Log in </b> </h1> </a>


</sec:ifNotLoggedIn>
<sec:ifLoggedIn>

  <g:set var="userId" value="${sec.loggedInUserInfo(field:'id')}" />
  <a href="<g:createLink controller='User' action='displayUser'
    params='[userId : userId ]' />" >


  <p> <b> My profile <sec:loggedInUserInfo field='id' /> </b> </p> </a>
<!--
<a href="<g:createLink controller="logout"  />"> <h1> <b> Se deconnecter </b> </h1> </a>
-->
<g:link controller="logout" params="[currentController: params.controller, currentAction: params.action]">Logout</g:link><br/>


 </sec:ifLoggedIn>

 Search a question :
 <g:form name="searchForm" controller="Question" action="search" >
 <g:textField name="searchValue" />
 </g:form>
 <br/>

 Search by tag :
 <g:form name="searchForm" controller="Question" action="searchByTag" >
 <g:textField name="searchValue" />
 </g:form>
 <br/>
