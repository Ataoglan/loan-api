# Loan API Project

This project is a Loan API application that manages customer-based loan processes. The API includes user and admin authorization mechanisms, customer management, and loan operations.

# Setup

Prerequisites
•	Docker: Ensure that Docker is installed on your machine before running the project.
•	Docker Compose: Included with Docker.

Running the Project
1.	Start MySQL with Docker:
In the project directory, run the following command to start MySQL:

    "docker compose up"
This will start a MySQL server on port 3306.

2. Initial Setup:
	•	When the project is initialized, a default admin user will be created:
	•	TCKN: 11111111111
	•	Password: admin
You can use this admin user to access all endpoints.


# **API Usage**

Swagger API Documentation
•	This project includes Swagger for API documentation.
•	After starting the project, you can access the Swagger UI at:
http://localhost:8080/swagger-ui/index.html#/

**Authorization with JWT Tokens**

•	JWT Required for Most Endpoints:

•	All APIs (except AuthenticationController) require a valid JWT token for access.

•	Ensure that you log in using the signin endpoint to obtain the token.

•	**Authorize in Swagger:**
•	Use the token from the signin endpoint and enter it in the “Authorize” button at the top right of the Swagger UI.


Authorization

•	Admin-Only Endpoints:

	/admin/**: For administrative operations.

	/customer/update-credit-limit: To update a customer’s credit limit.

•	For General Use:

	All other endpoints are accessible to general users. To create a new customer account, use the signup endpoint.

Example Endpoints
1.	Create Customer:
•	Endpoint: POST /signup
•	Description: Registers a new customer account.
•	Authorization: Not required (general users can access this endpoint).


2.	Update Credit Limit:
•	Endpoint: PATCH /customer/update-credit-limit
•	Description: Updates a customer’s credit limit.
•	Authorization: Admin-only.

# Important Notes

•	MySQL is used as the database, so ensure the Dockerized MySQL instance is running successfully.

•	Before interacting with the endpoints, log in using the default admin credentials (11111111111 TCKN and admin password).

•	Authorization tokens are required for endpoints that need admin privileges.

# Contact

If you encounter any issues or have suggestions, please reach out to me.

mail : ozcanataoglan@gmail.com

