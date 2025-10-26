<?php
require_once 'config.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $conn = getDBConnection();
    
    // Get and sanitize inputs
    $category = ($_POST['item'] === 'add_new') ? trim($_POST['custom_item']) : $_POST['item'];
    $amount = floatval($_POST['amount']);
    $date = date("Y-m-d H:i:s");
    
    // Validate inputs
    if (empty($category) || $amount <= 0) {
        die("Invalid input. Please go back and try again.");
    }
    
    // If new category, add it to categories table
    if ($_POST['item'] === 'add_new') {
        $stmt = $conn->prepare("INSERT IGNORE INTO categories (name) VALUES (?)");
        $stmt->bind_param("s", $category);
        $stmt->execute();
        $stmt->close();
    }
    
    // Insert expense
    $stmt = $conn->prepare("INSERT INTO expenses (category, amount, created_at) VALUES (?, ?, ?)");
    $stmt->bind_param("sds", $category, $amount, $date);
    
    if ($stmt->execute()) {
        $success = true;
    } else {
        $success = false;
        $error = $conn->error;
    }
    
    $stmt->close();
    $conn->close();
    ?>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Expense Saved</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f7f8;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }
            .container {
                background-color: #fff;
                padding: 40px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                text-align: center;
                max-width: 400px;
                width: 100%;
            }
            h2 {
                margin-bottom: 20px;
            }
            .success {
                color: #28a745;
            }
            .error {
                color: #dc3545;
            }
            .details {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin: 20px 0;
                text-align: left;
            }
            .details p {
                margin: 5px 0;
            }
            .links {
                margin-top: 20px;
            }
            a {
                text-decoration: none;
                color: #007bff;
                margin: 0 10px;
                font-weight: bold;
            }
            a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <?php if ($success): ?>
                <h2 class="success">✅ Expense Saved!</h2>
                <div class="details">
                    <p><strong>Category:</strong> <?= htmlspecialchars($category) ?></p>
                    <p><strong>Amount:</strong> RM <?= number_format($amount, 2) ?></p>
                    <p><strong>Date:</strong> <?= date("d M Y, g:i A", strtotime($date)) ?></p>
                </div>
            <?php else: ?>
                <h2 class="error">Error Saving Expense</h2>
                <p><?= htmlspecialchars($error ?? 'Unknown error occurred') ?></p>
            <?php endif; ?>
            
            <div class="links">
                <a href="main.php">Add Another</a> |
                <a href="history.php">View History</a>
            </div>
        </div>
    </body>
    </html>
    <?php
}
?>