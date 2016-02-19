<!doctype html>
<html>
    <head>
    </head>
<body>


    <g:form name="answerForm" controller="Answer" action="editOk" >
    <g:hiddenField name="answerId" value="${answer.id}" /> <br/>
          <g:textArea name="answerField" value="${answer.text}" rows="10" cols="80" />
          <g:actionSubmit value="Post" action="editOk" />
    </g:form>


</body>
