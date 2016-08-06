<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	<title>DimitriT's Users App</title>
	
</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>DimitriT's list of Users</h2>
		</div>
	</div>

	<div id="container">
	
		<div id="content">
		
			<!-- put new button: Add User -->
			
			<input type="button" value="Add User" 
				   onclick="window.location.href='add-user-form.jsp'; return false;"
				   class="add-user-button"
			/>
			
			<table>
			
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Age</th>
					<th>Action</th>
				</tr>
				
				<c:forEach var="tempUser" items="${USER_LIST}">
					
					<!-- set up a link for each user -->
					<c:url var="tempLink" value="UserControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="userId" value="${tempUser.id}" />
					</c:url>

					<!--  set up a link to delete a user -->
					<c:url var="deleteLink" value="UserControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="userId" value="${tempUser.id}" />
					</c:url>
																		
					<tr>
						<td> ${tempUser.firstName} </td>
						<td> ${tempUser.lastName} </td>
						<td> ${tempUser.age} </td>
						<td> 
							<a href="${tempLink}">Update</a> 
							 | 
							<a href="${deleteLink}"
							onclick="if (!(confirm('Please confirm to delete this user.'))) return false">
							Delete</a>	
						</td>
					</tr>
				
				</c:forEach>
				
			</table>
		
		</div>
	
	</div>
</body>


</html>








