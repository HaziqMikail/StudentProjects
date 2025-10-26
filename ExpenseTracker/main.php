<?php
require_once 'config.php';
$conn = getDBConnection();

// Fetch categories from database
$categories = [];
$result = $conn->query("SELECT name FROM categories ORDER BY name ASC");
if ($result) {
    while ($row = $result->fetch_assoc()) {
        $categories[] = $row['name'];
    }
}
$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Daily Expense Tracker</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f7f8;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      margin: 0;
      padding: 20px;
    }

    .container {
      background-color: #ffffff;
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 400px;
    }

    h1 {
      text-align: center;
      color: #333;
      margin-bottom: 20px;
    }

    label {
      font-weight: bold;
      color: #555;
      display: block;
      margin-top: 10px;
    }

    .form-control {
      width: 100%;
      padding: 10px;
      margin-top: 5px;
      margin-bottom: 15px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 16px;
      box-sizing: border-box;
    }

    button {
      width: 100%;
      padding: 12px;
      background-color: #007bff;
      border: none;
      border-radius: 6px;
      color: white;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s;
      margin-top: 10px;
    }

    button:hover {
      background-color: #0056b3;
    }

    .links {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
    }

    a {
      text-decoration: none;
      color: #007bff;
      font-size: 14px;
    }

    a:hover {
      text-decoration: underline;
    }

    .hidden {
      display: none;
    }
  </style>

  <script>
    function handleCategoryChange(select) {
      const customInput = document.getElementById('customCategory');
      if (select.value === 'add_new') {
        customInput.classList.remove('hidden');
        customInput.required = true;
        customInput.focus();
      } else {
        customInput.classList.add('hidden');
        customInput.required = false;
      }
    }
  </script>
</head>
<body>
  <div class="container">
    <h1>Daily Expense Tracker</h1>
    <form action="save_expense.php" method="POST">
      <label>Category:</label>
      <select name="item" onchange="handleCategoryChange(this)" required class="form-control">
        <option value="">-- Select Category --</option>
        <?php foreach ($categories as $cat): ?>
          <option value="<?= htmlspecialchars($cat) ?>"><?= htmlspecialchars($cat) ?></option>
        <?php endforeach; ?>
        <option value="add_new">Add New Category</option>
      </select>

      <input type="text" name="custom_item" id="customCategory" class="form-control hidden" 
             placeholder="Enter new category name">

      <label>Amount (RM):</label>
      <input type="number" name="amount" step="0.01" required class="form-control" 
             placeholder="0.00" min="0.01">

      <button type="submit">Add Expense</button>
    </form>

    <div class="links">
      <a href="history.php">View History</a>
      <a href="manage_categories.php">Manage Categories</a>
    </div>
  </div>
</body>
</html>