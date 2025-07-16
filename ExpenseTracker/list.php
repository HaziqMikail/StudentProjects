<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $item = ($_POST['item'] === 'add_new') ? htmlspecialchars($_POST['custom_item']) : htmlspecialchars($_POST['item']);
    $amount = htmlspecialchars($_POST['amount']);
    $date = date("Y-m-d H:i");

    $entry = "$date | $item | RM$amount\n";
    file_put_contents("expenses.txt", $entry, FILE_APPEND);
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
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                text-align: center;
                max-width: 400px;
                width: 100%;
            }
            h2 {
                color: #28a745;
                margin-bottom: 20px;
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
            <h2>✅ Expense Saved!</h2>
            <a href="main.html">➕ Add Another</a> |
            <a href="history.php">📜 View History</a>
        </div>
    </body>
    </html>
    <?php
}
?>
