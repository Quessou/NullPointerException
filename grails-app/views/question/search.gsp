<!doctype html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Homepage</title>
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

<g:if test="${questions != null}">
  <g:each var="question" in="${questions.sort{it.date}}">
			<a href="<g:createLink  controller="Question" action="display" id="${question.id}"/>" ><p> ${question.title} </p> </a>

      ${question.text}


			<p> Posted by <a href="<g:createLink controller="SecureUser" action="displayUser" params="[userId : question.secureUser.id]" />">    <b> ${question.secureUser.username} </b>  </a> on
       <b> <g:formatDate format="MM/dd HH:mm" date="${question.date}" /> </b> </p>


	</g:each>
</g:if>
<g:else>
	No result for ${value}

</g:else>


</body>
</html>
