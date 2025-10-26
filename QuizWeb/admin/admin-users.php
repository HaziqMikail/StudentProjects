<?php
session_start();
require_once '../database/db.php';

// ✅ Check if admin is logged in
if (!isset($_SESSION['admin'])) {
    header("Location: ../login/admin-login.php");
    exit();
}

// ✅ Fetch all users
$sql = "SELECT user_id, username, email, created_at FROM users ORDER BY user_id DESC";
$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Users | Quiz System</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="admin-container">
  <aside class="sidebar">
    <div class="logo">
      <h2>Quiz Admin</h2>
      <p>Welcome, <?php echo htmlspecialchars($_SESSION['admin']); ?></p>
    </div>
    <ul>
      <li><a href="dashboard.php">📊 Dashboard</a></li>
      <li><a href="manage_questions.php">📘 Manage Questions</a></li>
      <li class="active"><a href="admin-users.php">👤 Manage Users</a></li>
      <li><a href="logout.php">🚪 Logout</a></li>
    </ul>
  </aside>

  <main class="dashboard">
    <header>
      <h1>Manage Users</h1>
    </header>

    <table class="styled-table">
      <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>Created At</th>
      </tr>
      <?php if ($result && $result->num_rows > 0): ?>
        <?php while ($row = $result->fetch_assoc()): ?>
        <tr>
          <td><?php echo $row['user_id']; ?></td>
          <td><?php echo htmlspecialchars($row['username']); ?></td>
          <td><?php echo htmlspecialchars($row['email']); ?></td>
          <td><?php echo $row['created_at'] ?? "-"; ?></td>
        </tr>
        <?php endwhile; ?>
      <?php else: ?>
        <tr><td colspan="4">No users found.</td></tr>
      <?php endif; ?>
    </table>
  </main>
</div>

</body>
</html>
