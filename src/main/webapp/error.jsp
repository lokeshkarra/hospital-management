<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Hospital Management</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-body text-center">
                        <i class="bi bi-exclamation-triangle text-warning" style="font-size: 4rem;"></i>
                        <h2 class="mt-3">Oops! Something went wrong</h2>
                        <p class="text-muted">
                            <% 
                                String errorCode = String.valueOf(response.getStatus());
                                String errorMessage = "";
                                
                                switch(errorCode) {
                                    case "404":
                                        errorMessage = "The page you're looking for could not be found.";
                                        break;
                                    case "500":
                                        errorMessage = "Internal server error occurred.";
                                        break;
                                    default:
                                        errorMessage = "An unexpected error occurred.";
                                }
                            %>
                            <%= errorMessage %>
                        </p>
                        <div class="mt-4">
                            <a href="<%= request.getContextPath() %>/login" class="btn btn-primary">
                                <i class="bi bi-house"></i> Go to Login
                            </a>
                            <button onclick="history.back()" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Go Back
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>