<?php
require_once 'config.php';
$conn = getDBConnection();

// Handle delete request
if (isset($_GET['delete'])) {
    $deleteId = intval($_GET['delete']);
    $stmt = $conn->prepare("DELETE FROM categories WHERE id = ?");
    $stmt->bind_param("i", $deleteId);
    $stmt->execute();
    $stmt->close();
    header("Location: manage_categories.php");
    exit;
}

// Handle add category
if ($_SERVER["REQUEST_METHOD"] == "POST" && !empty($_POST['new_category'])) {
    $newCategory = trim($_POST['new_category']);
    $stmt = $conn->prepare("INSERT IGNORE INTO categories (name) VALUES (?)");
    $stmt->bind_param("s", $newCategory);
    $stmt->execute();
    $stmt->close();
    header("Location: manage_categories.php");
    exit;
}

// Get all categories with expense count
$query = "SELECT c.id, c.name, COUNT(e.id) as expense_count
          FROM categories c
          LEFT JOIN expenses e ON c.name = e.category
          GROUP BY c.id, c.name
          ORDER BY c.name ASC";
$result = $conn->query($query);
$categories = [];
while ($row = $result->fetch_assoc()) {
    $categories[] = $row;
}

$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Categories</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f7f8;
      padding: 40px 20px;
      max-width: 800px;
      margin: auto;
    }

    h1 {
      text-align: center;
      color: #333;
      margin-bottom: 40px;
    }

    .add-section {
      background: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.05);
      margin-bottom: 30px;
    }

    .add-section h2 {
      margin-top: 0;
      color: #007bff;
    }

    .form-group {
      display: flex;
      gap: 10px;
    }

    input[type="text"] {
      flex: 1;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 16px;
    }

    button {
      padding: 10px 20px;
      background-color: #28a745;
      border: none;
      border-radius: 6px;
      color: white;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    button:hover {
      background-color: #218838;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      background: white;
      box-shadow: 0 2px 6px rgba(0,0,0,0.05);
      border-radius: 8px;
      overflow: hidden;
    }

    th, td {
      padding: 15px;
      text-align: left;
    }

    th {
      background-color: #007bff;
      color: white;
      font-weight: 600;
    }

    tr:nth-child(even) {
      background-color: #f9f9f9;
    }

    .delete-btn {
      background-color: #dc3545;
      color: white;
      padding: 6px 12px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      text-decoration: none;
      display: inline-block;
    }

    .delete-btn:hover {
      background-color: #c82333;
    }

    .delete-btn.disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }

    .count-badge {
      background-color: #e9ecef;
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 14px;
      color: #666;
    }

    .back-button {
      display: block;
      margin: 30px auto 0;
      background-color: #007bff;
      color: #fff;
      padding: 12px 24px;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      cursor: pointer;
      text-align: center;
      text-decoration: none;
      transition: background-color 0.3s;
      width: fit-content;
    }

    .back-button:hover {
      background-color: #0056b3;
    }

    .warning {
      font-size: 14px;
      color: #856404;
      background-color: #fff3cd;
      padding: 10px;
      border-radius: 4px;
      margin-top: 10px;
    }
  </style>

  <script>
    function confirmDelete(name, count) {
      if (count > 0) {
        return confirm(`Are you sure you want to delete "${name}"? This category has ${count} expense(s) recorded.`);
      }
      return confirm(`Delete category "${name}"?`);
    }
  </script>
</head>
<body>

<h1>Manage Categories</h1>

<div class="add-section">
  <h2>Add New Category</h2>
  <form method="POST">
    <div class="form-group">
      <input type="text" name="new_category" placeholder="Enter category name" required>
      <button type="submit">Add Category</button>
    </div>
  </form>
</div>

<table>
  <tr>
    <th>Category Name</th>
    <th style="text-align: center;">Expenses Count</th>
    <th style="text-align: center;">Action</th>
  </tr>
  <?php foreach ($categories as $cat): ?>
    <tr>
      <td><strong><?= htmlspecialchars($cat['name']) ?></strong></td>
      <td style="text-align: center;">
        <span class="count-badge"><?= $cat['expense_count'] ?></span>
      </td>
      <td style="text-align: center;">
        <?php if ($cat['expense_count'] == 0): ?>
          <a href="?delete=<?= $cat['id'] ?>" 
             class="delete-btn" 
             onclick="return confirmDelete('<?= htmlspecialchars($cat['name']) ?>', 0)">
            Delete
          </a>
        <?php else: ?>
          <span class="delete-btn disabled" title="Cannot delete - category has expenses">
            Delete
          </span>
        <?php endif; ?>
      </td>
    </tr>
  <?php endforeach; ?>
</table>

<div class="warning">
  ⚠️ <strong>Note:</strong> You can only delete categories that have no expenses recorded. 
  To delete a category with expenses, you must first delete all related expenses.
</div>

<a class="back-button" href="main.php">← Back to Main</a>

</body>
</html>