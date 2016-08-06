<!DOCTYPE html>
<html>

<head>
	<title>Update User</title>

	
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>DimitriT's list of Users</h2>
		</div>
	</div>
	
	<div id="container">
		<h3>Update User</h3>
		
		<form action="UserControllerServlet" method="GET">
		
			<input type="hidden" name="command" value="UPDATE" />

			<input type="hidden" name="userId" value="${THE_USER.id}" />
			
			<table>
				<tbody>
					<tr>
						<td><label>First name:</label></td>
						<td><input type="text" name="firstName" 
								   value="${THE_USER.firstName}" /></td>
					</tr>

					<tr>
						<td><label>Last name:</label></td>
						<td><input type="text" name="lastName" 
								   value="${THE_USER.lastName}" /></td>
					</tr>

					<tr>
						<td><label>Age:</label></td>
						<td><input type="number" name="age" 
								   value="${THE_USER.age}" /></td>
					</tr>
					
					<tr>
						<td><label></label></td>
						<td><input type="submit" value="Save" class="save" /></td>
					</tr>
					
				</tbody>
			</table>
		</form>
		
		<div style="clear: both;"></div>
		
		<p>
			<a href="UserControllerServlet">Back to List</a>
		</p>
	</div>
</body>

</html>











